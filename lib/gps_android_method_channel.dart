import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'gps_android_platform_interface.dart';

/// An implementation of [GpsAndroidPlatform] that uses method channels.
class MethodChannelGpsAndroid extends GpsAndroidPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('gps_android');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
