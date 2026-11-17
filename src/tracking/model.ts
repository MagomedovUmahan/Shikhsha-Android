export type Fix = {
  latitude: number; longitude: number; timestamp: number;
  accuracy: number | null; altitude?: number | null; mocked?: boolean;
};
export type RoutePoint = Fix & { part: string };
export type StepBaseline = { days: Record<string, number>; day: string; enabled: boolean };
export type Segment = {
  id: string; start: number; end: number | null; baseline: StepBaseline | null;
  steps: number | null; stepsPartial: boolean;
  lastSample: number | null;
};
export type Walk = {
  id: string; startedAt: number; endedAt: number | null;
  status: 'recording' | 'paused' | 'completed'; mode: 'background' | 'foreground';
  segments: Segment[]; distance: number; maxSpeed: number; pointCount: number;
  lastFix: number | null; lastSample: number | null; lastAccuracy: number | null;
  rejected: number; gaps: number; warning: string | null;
};
export type WalkView = { walk: Walk; points: RoutePoint[] };
const radians = (n: number) => n * Math.PI / 180;
export function meters(a: Fix, b: Fix): number {
  const dLat = radians(b.latitude - a.latitude), dLon = radians(b.longitude - a.longitude);
  const h = Math.sin(dLat / 2) ** 2 + Math.cos(radians(a.latitude)) * Math.cos(radians(b.latitude)) * Math.sin(dLon / 2) ** 2;
  return 6371008.8 * 2 * Math.atan2(Math.sqrt(h), Math.sqrt(Math.max(0, 1 - h)));
}
/** Reject poor fixes, jitter and impossible walking jumps. A gap is never bridged. */
export function acceptFix(fix: Fix, previous: RoutePoint | null, segment: Segment):
  { point: RoutePoint; distance: number; speed: number; gap: boolean } | null {
  if (!usableFix(fix, segment)) return null;
  if (previous && fix.timestamp <= previous.timestamp) return null;
  const seconds = previous ? (fix.timestamp - previous.timestamp) / 1000 : 0;
  const gap = !!previous && (fix.timestamp - (segment.lastSample ?? previous.timestamp)) > 30000;
  const part = previous && !gap ? previous.part : `${segment.id}:${fix.timestamp}`;
  if (!previous || gap) return { point: { ...fix, part }, distance: 0, speed: 0, gap };
  const distance = meters(previous, fix);
  if (distance < Math.max(4, Math.min(12, Math.max(fix.accuracy ?? 0, previous.accuracy ?? 0) * 0.5)) || distance / seconds > 8) return null;
  return { point: { ...fix, part }, distance, speed: distance / seconds, gap: false };
}
export function usableFix(fix: Fix, segment: Segment): boolean {
  return !(![fix.latitude, fix.longitude, fix.timestamp].every(Number.isFinite) ||
      Math.abs(fix.latitude) > 90 || Math.abs(fix.longitude) > 180 ||
      fix.timestamp < segment.start || (segment.end !== null && fix.timestamp > segment.end) ||
      fix.accuracy === null || !Number.isFinite(fix.accuracy) || fix.accuracy < 0 || fix.accuracy > 35 || !!fix.mocked);
}
export function activeMilliseconds(walk: Walk, now = Date.now()): number {
  return walk.segments.reduce((sum, s) => sum + Math.max(0, (s.end ?? now) - s.start), 0);
}
export function walkSteps(walk: Walk): { count: number | null; partial: boolean } {
  const known = walk.segments.filter(s => s.steps !== null);
  return { count: known.length ? known.reduce((sum, s) => sum + (s.steps ?? 0), 0) : null,
    partial: known.length !== walk.segments.length || walk.segments.some(s => s.stepsPartial) };
}
export function stepDelta(baseline: StepBaseline | null, days: Record<string, number>, enabled: boolean): { count: number | null; partial: boolean } {
  if (!baseline?.enabled) return { count: null, partial: true };
  let count = 0, partial = !enabled;
  for (const [day, value] of Object.entries(days)) {
    if (day < baseline.day) continue;
    const before = baseline.days[day] ?? 0;
    if (value < before) partial = true;
    count += Math.max(0, value - before);
  }
  if (Object.keys(baseline.days).some(day => day >= baseline.day && days[day] === undefined)) partial = true;
  return { count, partial };
}
export function duration(ms: number): string {
  const s = Math.floor(Math.max(0, ms) / 1000);
  return `${Math.floor(s / 3600).toString().padStart(2, '0')}:${Math.floor(s / 60 % 60).toString().padStart(2, '0')}:${(s % 60).toString().padStart(2, '0')}`;
}
export function pace(distance: number, ms: number): string {
  if (distance < 30 || ms <= 0) return '—';
  const seconds = Math.round(ms / distance);
  return `${Math.floor(seconds / 60)}:${(seconds % 60).toString().padStart(2, '0')}`;
}
export function routeLines(points: RoutePoint[]): number[][][] {
  const lines: number[][][] = [];
  let part: string | null = null;
  for (const point of points) {
    if (part !== point.part) { lines.push([]); part = point.part; }
    lines[lines.length - 1].push([point.longitude, point.latitude]);
  }
  return lines.filter(line => line.length > 1);
}
const escapeXml = (s: string) => s.replace(/[<>&"']/g, char => ({ '<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;', "'": '&apos;' })[char]!);
export function toGPX(view: WalkView, name = 'Прогулка — Шихша'): string {
  const segments: string[] = [];
  let part: string | null = null;
  for (const p of view.points) {
    if (part !== p.part) { if (part !== null) segments.push('</trkseg>'); segments.push('<trkseg>'); part = p.part; }
    segments.push(`<trkpt lat="${p.latitude}" lon="${p.longitude}">${Number.isFinite(p.altitude) ? `<ele>${p.altitude}</ele>` : ''}<time>${new Date(p.timestamp).toISOString()}</time></trkpt>`);
  }
  if (part !== null) segments.push('</trkseg>');
  return `<?xml version="1.0" encoding="UTF-8"?>\n<gpx version="1.1" creator="Shikhsha" xmlns="http://www.topografix.com/GPX/1/1"><metadata><name>${escapeXml(name)}</name><time>${new Date(view.walk.startedAt).toISOString()}</time></metadata><trk><name>${escapeXml(name)}</name>${segments.join('')}</trk></gpx>`;
}
