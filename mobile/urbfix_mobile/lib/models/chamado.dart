class Chamado {
  final int id;
  final int? cidadaoId;
  final String? cidadaoNome;
  final int categoriaId;
  final String? categoriaNome;
  final double? pesoCategoria;
  final double latitude;
  final double longitude;
  final double scorePrioridade;
  final String estado;
  final String createdAt;

  const Chamado({
    required this.id,
    this.cidadaoId,
    this.cidadaoNome,
    required this.categoriaId,
    this.categoriaNome,
    this.pesoCategoria,
    required this.latitude,
    required this.longitude,
    required this.scorePrioridade,
    required this.estado,
    required this.createdAt,
  });

  factory Chamado.fromJson(Map<String, dynamic> json) {
    return Chamado(
      id: json['id'] as int,
      cidadaoId: json['cidadaoId'] as int?,
      cidadaoNome: json['cidadaoNome'] as String?,
      categoriaId: json['categoriaId'] as int,
      categoriaNome: json['categoriaNome'] as String?,
      pesoCategoria: (json['pesoCategoria'] as num?)?.toDouble(),
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      scorePrioridade: (json['scorePrioridade'] as num).toDouble(),
      estado: json['estado'] as String,
      createdAt: json['createdAt'] as String,
    );
  }
}
