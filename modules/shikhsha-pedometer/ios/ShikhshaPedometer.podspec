Pod::Spec.new do |s|
  s.name           = 'ShikhshaPedometer'
  s.version        = '1.0.0'
  s.summary        = 'On-device step counting for Shikhsha'
  s.description    = 'Core Motion history and live steps, stored locally.'
  s.author         = 'Shikhsha'
  s.swift_version  = '5.9'
  s.homepage       = 'https://docs.expo.dev/modules/'
  s.platforms      = {
    :ios => '16.4'
  }
  s.source         = { git: '' }
  s.static_framework = true

  s.dependency 'ExpoModulesCore'

  # Swift/Objective-C compatibility
  s.pod_target_xcconfig = {
    'DEFINES_MODULE' => 'YES',
  }

  s.source_files = "**/*.{h,m,mm,swift,hpp,cpp}"
end
