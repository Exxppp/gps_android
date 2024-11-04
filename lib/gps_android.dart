import 'dart:io';

import 'package:flutter/services.dart';

class GpsAndroid {
  static const MethodChannel _channel = MethodChannel('gps_android');

  static Future<bool> enableGPS() async {
    if (Platform.isAndroid) {
      final bool result = await _channel.invokeMethod('enableGPS');
      return result;
    }
    return false;
  }
}
