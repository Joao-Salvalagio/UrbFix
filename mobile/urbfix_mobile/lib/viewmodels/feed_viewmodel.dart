import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../core/constants.dart';
import '../models/chamado.dart';
import '../services/api_service.dart';

enum FeedStatus { idle, loading, success, error }

class FeedViewModel extends ChangeNotifier {
  final _api = ApiService();

  FeedStatus _status = FeedStatus.idle;
  String? _error;
  List<Chamado> _chamados = [];
  int? _cidadaoId;
  bool _temHistorico = false;

  FeedStatus get status => _status;
  String? get error => _error;
  List<Chamado> get chamados => _chamados;
  bool get temHistorico => _temHistorico;

  Future<void> loadFeed() async {
    _status = FeedStatus.loading;
    _error = null;
    notifyListeners();

    try {
      final prefs = await SharedPreferences.getInstance();
      _cidadaoId = prefs.getInt(kCidadaoIdKey);

      if (_cidadaoId != null) {
        _chamados = await _api.getMeusChamados(_cidadaoId!);
        _temHistorico = true;
      } else {
        _chamados = await _api.getFilaPrioridade();
        _temHistorico = false;
      }
      _status = FeedStatus.success;
    } on SocketException {
      _status = FeedStatus.error;
      _error = 'Sem conexão. Verifique sua internet e tente novamente.';
    } catch (_) {
      _status = FeedStatus.error;
      _error = 'Erro ao carregar os chamados.';
    }
    notifyListeners();
  }
}
