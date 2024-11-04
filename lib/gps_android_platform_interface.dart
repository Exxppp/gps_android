import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'gps_android_method_channel.dart';

abstract class GpsAndroidPlatform extends PlatformInterface {
  /// Constructs a GpsAndroidPlatform.
  GpsAndroidPlatform() : super(token: _token);

  static final Object _token = Object();

  static GpsAndroidPlatform _instance = MethodChannelGpsAndroid();

  /// The default instance of [GpsAndroidPlatform] to use.
  ///
  /// Defaults to [MethodChannelGpsAndroid].
  static GpsAndroidPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [GpsAndroidPlatform] when
  /// they register themselves.
  static set instance(GpsAndroidPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
