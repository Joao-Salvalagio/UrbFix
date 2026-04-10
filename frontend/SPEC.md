# 🖥️ Especificação: Front-end (Angular) - UrbFix

## 1. Contexto e Objetivo da Sprint
**Objetivo:** Desenvolver o painel administrativo Web (Dashboard) para gestão e triagem de chamados urbanos, focado na visualização da Fila de Prioridade.

## 2. Arquitetura de Componentes
- [ ] **PainelLayoutComponent:** Estrutura base contendo a Sidebar de navegação e o Header.
- [ ] **DashboardComponent:** Tela principal. Deve conter o Grid/Tabela para exibição dos dados.
- [ ] **StatusBadgeComponent:** Componente "dumb/presentational" focado apenas em renderizar as tags visuais de status com base em enums (`ABERTO`, `EM_ANALISE`, `EM_EXECUCAO`, `RESOLVIDO`).

## 3. Gerenciamento de Estado e Serviços
- Criar `ChamadoService` utilizando `HttpClient` do Angular para as chamadas REST.
- Recomenda-se utilizar RxJS `BehaviorSubject` ou Signals (Angular 16+) para garantir a reatividade na atualização da tabela, evitando o reload completo da página ao consumir a fila.

## 4. Integração (Contrato da API)
- **Endpoint Rest:** `GET /api/v1/chamados/fila-prioridade`
- **Payload Esperado (Mock para desenvolvimento UI):**

    [
      {
        "id": 101,
        "categoria": "Buraco em Via Rápida",
        "scorePrioridade": 95,
        "status": "ABERTO",
        "dataReporte": "2026-04-10T14:30:00Z"
      }
    ]

## 5. Diretrizes de UI/UX (Heurísticas)
1. **Lei de Hick:** Reduzir opções de navegação na tela principal. O foco primário e único deve ser a tabela de prioridades.
2. **Acessibilidade:** Garantir alto contraste nos Badges de Status (evitar combinações de cores que dificultem a leitura por daltônicos, priorizando rótulos textuais claros).
3. **Feedback Visual:** Implementar Skeleton Loaders ou Spinners na tabela durante o carregamento do `GET` da API para indicar processamento assíncrono.