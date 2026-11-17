import ExpoModulesCore
import CoreMotion

public class ShikhshaPedometerModule: Module {
  private let pedometer = CMPedometer()
  private let defaults = UserDefaults.standard
  private var liveDay: String?
  private var liveGeneration = 0
  private var sessionGeneration = 0
  private let enabledKey = "shikhsha.enabled.v1"
  private let daysKey = "shikhsha.days.v1"
  private let updatedKey = "shikhsha.updated.v1"
  private let anchorKey = "shikhsha.session.start.v2"
  private let baselineKey = "shikhsha.session.base.v2"
  private let endKey = "shikhsha.session.end.v2"
  private var lastError: String?

  private var anchor: Date? {
    let value = defaults.double(forKey: anchorKey)
    return value > 0 ? Date(timeIntervalSince1970: value) : nil
  }
  private var sessionEnd: Date? {
    let value = defaults.double(forKey: endKey)
    return value > 0 ? Date(timeIntervalSince1970: value) : nil
  }
  // A persisted end is also a pause marker if the process died between writes.
  private var enabled: Bool { defaults.bool(forKey: enabledKey) && sessionEnd == nil }
  private func dayKey(_ date: Date) -> String {
    let f = DateFormatter()
    f.calendar = Calendar(identifier: .gregorian)
    f.locale = Locale(identifier: "en_US_POSIX")
    f.timeZone = .current
    f.dateFormat = "yyyy-MM-dd"
    return f.string(from: date)
  }
  private var days: [String: Int] { defaults.dictionary(forKey: daysKey) as? [String: Int] ?? [:] }
  private func record(_ count: Int, on date: Date) {
    var values = days
    let key = dayKey(date)
    values[key] = max(values[key] ?? 0, count)
    let retained = values.keys.sorted().suffix(90)
    defaults.set(Dictionary(uniqueKeysWithValues: retained.map { ($0, values[$0]!) }), forKey: daysKey)
    defaults.set(Date().timeIntervalSince1970 * 1000, forKey: updatedKey)
  }
  private func snapshot() -> [String: Any] {
    let status = CMPedometer.authorizationStatus()
    var result: [String: Any] = [
      "available": CMPedometer.isStepCountingAvailable(),
      "permission": status == .authorized ? "granted" : (status == .notDetermined ? "undetermined" : "denied"),
      "enabled": enabled, "running": liveDay != nil && status == .authorized && enabled,
      "days": days, "updatedAt": defaults.double(forKey: updatedKey)
    ]
    if let error = lastError { result["error"] = error }
    return result
  }
  private func openSession() {
    let now = Date()
    sessionGeneration += 1
    defaults.set(days[dayKey(now)] ?? 0, forKey: baselineKey)
    defaults.set(now.timeIntervalSince1970, forKey: anchorKey)
    defaults.removeObject(forKey: endKey)
    defaults.set(true, forKey: enabledKey)
  }
  private func migrateAnchorIfNeeded() {
    // Keep old totals, then count only new enabled intervals after upgrading.
    if enabled && anchor == nil { openSession() }
  }
  private func base(for day: Date, from start: Date) -> Int {
    return dayKey(day) == dayKey(start) ? defaults.integer(forKey: baselineKey) : 0
  }
  private func startLive() {
    guard enabled, let start = anchor else { return }
    let today = Calendar.current.startOfDay(for: Date())
    let from = max(start, today)
    let baseline = base(for: today, from: start)
    liveGeneration += 1
    let generation = liveGeneration
    liveDay = dayKey(today)
    pedometer.stopUpdates()
    pedometer.startUpdates(from: from) { [weak self] data, error in
      DispatchQueue.main.async {
        guard let self, self.enabled, self.liveGeneration == generation else { return }
        if let error { self.lastError = error.localizedDescription; self.liveDay = nil; return }
        if self.liveDay != self.dayKey(Date()) {
          self.synchronize(until: Date()) { _ in }
          self.startLive()
          return
        }
        if let data { self.record(baseline + data.numberOfSteps.intValue, on: today) }
      }
    }
  }
  private func synchronize(until: Date, completion: @escaping (Error?) -> Void) {
    guard let start = anchor else { completion(nil); return }
    let generation = sessionGeneration
    let today = Calendar.current.startOfDay(for: Date())
    let earliest = Date().addingTimeInterval(-7 * 24 * 3600)
    var requests: [(Date, Date, Int)] = []
    for offset in (-6)...0 {
      guard let day = Calendar.current.date(byAdding: .day, value: offset, to: today),
            let next = Calendar.current.date(byAdding: .day, value: 1, to: day) else { continue }
      let from = max(max(day, start), earliest)
      let to = min(next, until)
      if from < to { requests.append((from, to, base(for: day, from: start))) }
    }
    guard !requests.isEmpty else { completion(nil); return }
    var remaining = requests.count
    var queryError: Error?
    for (from, to, baseline) in requests {
      pedometer.queryPedometerData(from: from, to: to) { [weak self] data, error in
        DispatchQueue.main.async {
          guard let self else { return }
          // Queries from an earlier start/resume must not replace the new session.
          if self.sessionGeneration == generation {
            if let data { self.record(baseline + data.numberOfSteps.intValue, on: from) }
            if let error { queryError = error }
          }
          remaining -= 1
          if remaining == 0 { completion(queryError) }
        }
      }
    }
  }
  public func definition() -> ModuleDefinition {
    Name("ShikhshaPedometer")
    AsyncFunction("getSnapshot") { (promise: Promise) in
      self.migrateAnchorIfNeeded()
      guard CMPedometer.authorizationStatus() == .authorized, CMPedometer.isStepCountingAvailable() else {
        promise.resolve(self.snapshot()); return
      }
      if self.enabled { self.startLive() }
      let until = self.sessionEnd ?? Date()
      // A paused session is closed at its stored end, never at the current time.
      if self.enabled || self.sessionEnd != nil {
        self.synchronize(until: until) { error in
          self.lastError = error?.localizedDescription
          promise.resolve(self.snapshot())
        }
      } else { promise.resolve(self.snapshot()) }
    }.runOnQueue(.main)

    AsyncFunction("getLiveSnapshot") { self.snapshot() }.runOnQueue(.main)
    AsyncFunction("getStepsInInterval") { (from: Double, to: Double, promise: Promise) in
      let start = Date(timeIntervalSince1970: from / 1000)
      let end = min(Date(timeIntervalSince1970: to / 1000), Date())
      guard CMPedometer.isStepCountingAvailable(), CMPedometer.authorizationStatus() == .authorized,
            start <= end, start >= Date().addingTimeInterval(-7 * 24 * 3600) else {
        promise.resolve(nil as Int?); return
      }
      self.pedometer.queryPedometerData(from: start, to: end) { data, error in
        if error != nil { promise.resolve(nil as Int?) } else { promise.resolve(data?.numberOfSteps.intValue) }
      }
    }.runOnQueue(.main)

    AsyncFunction("start") { (promise: Promise) in
      guard CMPedometer.isStepCountingAvailable() else {
        promise.reject("ERR_UNAVAILABLE", "На устройстве нет доступного шагомера."); return
      }
      // This query requests motion consent; its daily count is not imported.
      let today = Calendar.current.startOfDay(for: Date())
      self.pedometer.queryPedometerData(from: today, to: Date()) { [weak self] _, error in
        DispatchQueue.main.async {
          guard let self else { return }
          if let error { promise.reject("ERR_MOTION", "Проверьте доступ к движению и фитнесу.", error); return }
          guard CMPedometer.authorizationStatus() == .authorized else {
            promise.reject("ERR_PERMISSION", "Разрешите движение и фитнес в настройках телефона."); return
          }
          self.migrateAnchorIfNeeded()
          if self.enabled {
            self.startLive()
            self.synchronize(until: Date()) { error in
              self.lastError = error?.localizedDescription; promise.resolve(self.snapshot())
            }
            return
          }
          let resume = { [weak self] in
            guard let self else { return }
            self.openSession(); self.lastError = nil; self.startLive()
            promise.resolve(self.snapshot())
          }
          if let end = self.sessionEnd {
            self.synchronize(until: end) { _ in resume() }
          } else { resume() }
        }
      }
    }.runOnQueue(.main)

    AsyncFunction("stop") { (promise: Promise) in
      guard self.enabled else { promise.resolve(self.snapshot()); return }
      self.migrateAnchorIfNeeded()
      let until = Date()
      self.defaults.set(until.timeIntervalSince1970, forKey: self.endKey)
      self.defaults.set(false, forKey: self.enabledKey)
      self.liveGeneration += 1
      self.pedometer.stopUpdates(); self.liveDay = nil
      self.synchronize(until: until) { error in
        self.lastError = error?.localizedDescription; promise.resolve(self.snapshot())
      }
    }.runOnQueue(.main)
    OnDestroy { self.pedometer.stopUpdates() }
  }
}
