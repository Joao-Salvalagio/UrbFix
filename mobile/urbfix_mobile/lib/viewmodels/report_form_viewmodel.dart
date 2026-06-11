import 'dart:io';
import 'dart:math';
import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../core/constants.dart';
import '../models/categoria.dart';
import '../services/api_service.dart';
import '../services/camera_service.dart';
import '../services/location_service.dart';

enum FormStatus { idle, loadingCategories, loadingLocation, submitting, success, error }

enum LocationResult { success, permissionDenied, error }

class ReportFormViewModel extends ChangeNotifier {
  final _api = ApiService();
  final _camera = CameraService();
  final _location = LocationService();

  FormStatus _status = FormStatus.idle;
  String? _error;
  String? _imageBase64;
  Position? _position;
  List<Categoria> _categorias = [];
  Categoria? _selectedCategoria;

  FormStatus get status => _status;
  String? get error => _error;
  String? get imageBase64 => _imageBase64;
  Position? get position => _position;
  List<Categoria> get categorias => _categorias;
  Categoria? get selectedCategoria => _selectedCategoria;

  bool get canSubmit =>
      _imageBase64 != null &&
      _position != null &&
      _selectedCategoria != null &&
      (_status == FormStatus.idle || _status == FormStatus.error);

  Future<void> loadCategories() async {
    _status = FormStatus.loadingCategories;
    notifyListeners();
    try {
      _categorias = await _api.getCategorias();
      _selectedCategoria = _categorias.isNotEmpty ? _categorias.first : null;
      _status = FormStatus.idle;
    } on SocketException {
      _status = FormStatus.error;
      _error = 'Sem conexão. Não foi possível carregar as categorias.';
    } catch (_) {
      _status = FormStatus.error;
      _error = 'Erro ao carregar categorias.';
    }
    notifyListeners();
  }

  Future<LocationResult> acquireLocation() async {
    final permitted = await _location.checkAndRequestPermission();
    if (!permitted) return LocationResult.permissionDenied;

    _status = FormStatus.loadingLocation;
    notifyListeners();
    try {
      _position = await _location.getCurrentPosition();
      _status = FormStatus.idle;
      notifyListeners();
      return LocationResult.success;
    } catch (_) {
      _status = FormStatus.idle;
      notifyListeners();
      return LocationResult.error;
    }
  }

  Future<void> captureImage() async {
    try {
      final base64 = await _camera.captureImage();
      if (base64 != null) {
        _imageBase64 = base64;
        notifyListeners();
      }
    } catch (_) {
      _error = 'Não foi possível acessar a câmera.';
      notifyListeners();
    }
  }

  Future<void> pickFromGallery() async {
    try {
      final base64 = await _camera.pickFromGallery();
      if (base64 != null) {
        _imageBase64 = base64;
        notifyListeners();
      }
    } catch (_) {
      _error = 'Não foi possível acessar a galeria.';
      notifyListeners();
    }
  }

  void selectCategoria(Categoria categoria) {
    _selectedCategoria = categoria;
    notifyListeners();
  }

  Future<bool> submitReport() async {
    if (!canSubmit) return false;

    final prefs = await SharedPreferences.getInstance();
    var deviceId = prefs.getString(kDeviceIdKey);
    if (deviceId == null) {
      deviceId = _generateUuid();
      await prefs.setString(kDeviceIdKey, deviceId);
    }

    _status = FormStatus.submitting;
    _error = null;
    notifyListeners();

    try {
      final criado = await _api.postChamado(
        latitude: _position!.latitude,
        longitude: _position!.longitude,
        categoriaId: _selectedCategoria!.id,
        imagemBase64: _imageBase64!,
        dispositivoId: deviceId,
      );
      if (criado.cidadaoId != null) {
        await prefs.setInt(kCidadaoIdKey, criado.cidadaoId!);
      }
      _status = FormStatus.success;
      notifyListeners();
      return true;
    } on SocketException {
      _status = FormStatus.error;
      _error = 'Sem conexão com o servidor. Tente novamente.';
      notifyListeners();
      return false;
    } catch (_) {
      _status = FormStatus.error;
      _error = 'Erro ao enviar relato. Tente novamente.';
      notifyListeners();
      return false;
    }
  }

  void clearError() {
    _error = null;
    if (_status == FormStatus.error) _status = FormStatus.idle;
    notifyListeners();
  }

  String _generateUuid() {
    final r = Random.secure();
    const hex = '0123456789abcdef';
    String s(int n) => List.generate(n, (_) => hex[r.nextInt(16)]).join();
    final v = ['8', '9', 'a', 'b'][r.nextInt(4)];
    return '${s(8)}-${s(4)}-4${s(3)}-$v${s(3)}-${s(12)}';
  }
}
