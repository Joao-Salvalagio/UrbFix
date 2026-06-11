import 'package:flutter/foundation.dart';
import '../services/location_service.dart';

class HomeViewModel extends ChangeNotifier {
  final _location = LocationService();

  Future<bool> checkLocationPermission() {
    return _location.checkAndRequestPermission();
  }
}
