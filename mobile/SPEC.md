# 📱 Especificação: Mobile (Flutter) - UrbFix

## 1. Contexto e Objetivo da Sprint
**Objetivo:** Desenvolver o fluxo do aplicativo do cidadão para captura georreferenciada de problemas urbanos e envio para a API por meio do método POST.

## 2. Arquitetura de UI (Telas e Widgets)
- [ ] **HomeScreen:** Tela principal em formato de mapa ocupando a tela cheia (recomenda-se o pacote `Maps_flutter` ou similar). Deve conter o FAB (Floating Action Button) de "Relatar Problema" em destaque.
- [ ] **ReportFormScreen:** Formulário exibido após a captura fotográfica. Deve conter o preview da imagem e componentes (Dropdown ou ChoiceChips) para a seleção da categoria do problema.
- [ ] **FeedScreen:** Lista cronológica contendo o histórico de chamados reportados pelo dispositivo local, consumindo o status atualizado do back-end.

## 3. Integrações Nativas
- **Geolocalização:** Necessário implementar plugin de geolocalização (ex: `geolocator`). É obrigatório validar e solicitar permissões de localização (Foreground) antes de habilitar a submissão do formulário.
- **Câmera/Mídia:** Necessário implementar plugin de captura (ex: `image_picker`). Deve-se realizar a compressão da imagem antes da conversão para Base64 para evitar estouro de limite de payload na API.

## 4. Integração (Contrato da API)
- **Endpoint Rest:** `POST /api/v1/chamados`
- **Payload de Envio (Body da Requisição):**

    {
      "latitude": -23.4205,
      "longitude": -51.9333,
      "categoriaId": 2,
      "imagemBase64": "data:image/jpeg;base64,/9j/4AAQSkZJRg...",
      "dispositivoId": "UUID-DO-APARELHO"
    }

## 5. Tratamento de Exceções e Edge Cases
1. **Falta de Conexão (Offline):** Interceptar falhas de rede (`SocketException`) e exibir uma `SnackBar` ou modal amigável ao usuário informando a indisponibilidade. Não é necessário implementar enfileiramento offline nesta versão do protótipo.
2. **Permissão Negada (GPS):** Bloquear o fluxo de reporte e exibir um modal explicando de forma clara que a localização é obrigatória para o direcionamento da zeladoria urbana.