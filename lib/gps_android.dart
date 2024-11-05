import 'dart:io';

import 'package:flutter/services.dart';

class GpsAndroid {
  static const MethodChannel _channel = MethodChannel('gps_android');

  /// Android, метод отправляет запрос на включение GPS и возвращает
  /// `true`, если операция была успешна, и `false` в противном случае.
  /// Возвращает `null`, если метод вызван не на Android или произошла ошибка.
  static Future<bool?> enableGPS() async {
    if (Platform.isAndroid) {
      try {
        final bool result = await _channel.invokeMethod('enableGPS');
        return result;
      } catch (_) {}
    }
    return null;
  }
}
