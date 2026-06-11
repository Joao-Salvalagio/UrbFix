class Categoria {
  final int id;
  final String nome;

  const Categoria({required this.id, required this.nome});

  factory Categoria.fromJson(Map<String, dynamic> json) {
    return Categoria(
      id: json['id'] as int,
      nome: json['nome'] as String,
    );
  }
}
