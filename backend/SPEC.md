# ⚙️ Especificação: Back-end (Spring Boot) - UrbFix

## 1. Contexto e Objetivo da Sprint
**Objetivo:** Configurar o core business da aplicação, focado na implementação do modelo relacional (JPA), na estrutura de dados de priorização em memória (Max-Heap) e na exposição da API RESTful para integração com os clientes (Web e Mobile).

## 2. Modelo de Domínio (JPA Entities)
- [ ] **Cidadao:** Entidade responsável pelos dados do usuário reportador. Deve possuir relacionamento 1:N com a entidade Chamado.
- [ ] **CategoriaProblema:** Tabela de domínio paramétrica. Deve armazenar o `nome` da categoria e o seu `pesoBase` (multiplicador utilizado no cálculo de risco).
- [ ] **Chamado:** Entidade transacional central. Deve mapear as coordenadas (`latitude`, `longitude`), o `scorePrioridade` calculado, o carimbo de data/hora (timestamp) e a referência ao estado atual (`EstadoChamado`).

## 3. Estrutura de Dados e Padrões de Projeto (Core AEP)
- **Fila de Prioridade (Max-Heap):**
  - Estrutura de dados operando em memória RAM, atuando de forma síncrona com o repositório do banco de dados.
  - Ao persistir um chamado com status `ABERTO` no PostgreSQL, o sistema deve inseri-lo simultaneamente na Max-Heap.
  - O algoritmo deve garantir complexidade matemática de O(log n) para a inserção e extração do nó raiz (chamado mais crítico).
- **Design Pattern - State:** Utilizado para controlar rigorosamente o ciclo de vida do chamado (ex: transições entre `AbertoState`, `EmAnaliseState`, `EmExecucaoState`, `ResolvidoState`). Deve impedir programaticamente transições inválidas (ex: regressão de `Resolvido` para `Em Analise`).
- **Design Pattern - Factory Method:** Encapsular a lógica de instanciação dos objetos `Chamado`, aplicando dinamicamente as regras de pontuação inicial com base na `CategoriaProblema` recebida.

## 4. Contratos REST (Controllers)
- **Criar Chamado:** `POST /api/v1/chamados`
  - Recebe o payload do Mobile, executa o motor de criticidade geográfica, persiste no SGBD e enfileira o objeto na Max-Heap.
- **Consultar Fila:** `GET /api/v1/chamados/fila-prioridade`
  - Extrai e retorna a lista de chamados presentes na Max-Heap, rigorosamente ordenada de forma decrescente pelo `scorePrioridade`.
- **Atualizar Status:** `PUT /api/v1/chamados/{id}/status`
  - Altera o state do chamado. Caso o status transite para fora de "Aberto", o chamado deve ser imediatamente removido/desconsiderado da Fila de Prioridade em memória.

## 5. Testes Unitários e Qualidade (QA)
- **TDD (Test-Driven Development):** Implementação obrigatória de cobertura de testes utilizando o framework **JUnit 5** em conjunto com **Mockito**, focado na classe `CalculadoraCriticidadeService`.
- **Cenários de Validação:**
  1. Validar se a fórmula matemática atribui corretamente pesos maiores dependendo da categoria do incidente.
  2. Provar, via assertions, que um chamado classificado como "Risco Alto", mesmo inserido cronologicamente após um de "Risco Baixo", é posicionado e extraído primeiro no topo da Max-Heap.