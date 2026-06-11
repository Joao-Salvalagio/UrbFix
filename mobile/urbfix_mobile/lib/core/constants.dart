import 'package:flutter/foundation.dart';

final String kApiBaseUrl = _resolveApiBaseUrl();

String _resolveApiBaseUrl() {
  if (!kIsWeb) return 'http://localhost:8080/api/v1';
  if (Uri.base.scheme == 'https') return '${Uri.base.origin}/api/v1';
  return 'http://${Uri.base.host}:8080/api/v1';
}
const String kDeviceIdKey = 'urbfix_device_id';
const String kCidadaoIdKey = 'urbfix_cidadao_id';
