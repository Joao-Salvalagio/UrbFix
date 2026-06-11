import 'dart:convert';
import 'package:http/http.dart' as http;
import '../core/constants.dart';
import '../models/categoria.dart';
import '../models/chamado.dart';

class ApiService {
  static const _timeout = Duration(seconds: 15);

  Future<List<Categoria>> getCategorias() async {
    final response = await http
        .get(Uri.parse('$kApiBaseUrl/categorias'))
        .timeout(_timeout);

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(utf8.decode(response.bodyBytes));
      return data.map((e) => Categoria.fromJson(e as Map<String, dynamic>)).toList();
    }
    throw Exception('Falha ao carregar categorias: ${response.statusCode}');
  }

  Future<Chamado> postChamado({
    required double latitude,
    required double longitude,
    required int categoriaId,
    required String imagemBase64,
    required String dispositivoId,
  }) async {
    final response = await http
        .post(
          Uri.parse('$kApiBaseUrl/chamados'),
          headers: {'Content-Type': 'application/json; charset=UTF-8'},
          body: jsonEncode({
            'latitude': latitude,
            'longitude': longitude,
            'categoriaId': categoriaId,
            'imagemBase64': imagemBase64,
            'dispositivoId': dispositivoId,
          }),
        )
        .timeout(_timeout);

    if (response.statusCode == 201) {
      return Chamado.fromJson(
        jsonDecode(utf8.decode(response.bodyBytes)) as Map<String, dynamic>,
      );
    }
    throw Exception('Falha ao registrar chamado: ${response.statusCode}');
  }

  Future<List<Chamado>> getFilaPrioridade() async {
    final response = await http
        .get(Uri.parse('$kApiBaseUrl/chamados/fila-prioridade'))
        .timeout(_timeout);

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(utf8.decode(response.bodyBytes));
      return data.map((e) => Chamado.fromJson(e as Map<String, dynamic>)).toList();
    }
    throw Exception('Falha ao carregar fila: ${response.statusCode}');
  }

  Future<List<Chamado>> getMeusChamados(int cidadaoId) async {
    final response = await http
        .get(Uri.parse('$kApiBaseUrl/chamados/cidadao/$cidadaoId'))
        .timeout(_timeout);

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(utf8.decode(response.bodyBytes));
      return data.map((e) => Chamado.fromJson(e as Map<String, dynamic>)).toList();
    }
    throw Exception('Falha ao carregar histórico: ${response.statusCode}');
  }
}
