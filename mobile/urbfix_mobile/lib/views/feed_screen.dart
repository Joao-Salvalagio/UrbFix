import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/app_theme.dart';
import '../models/chamado.dart';
import '../viewmodels/feed_viewmodel.dart';

class FeedScreen extends StatelessWidget {
  const FeedScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => FeedViewModel()..loadFeed(),
      child: const _FeedView(),
    );
  }
}

class _FeedView extends StatelessWidget {
  const _FeedView();

  @override
  Widget build(BuildContext context) {
    final vm = context.watch<FeedViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: Text(vm.temHistorico ? 'Meus Chamados' : 'Fila Pública'),
        centerTitle: true,
        actions: [
          IconButton(
            onPressed: vm.loadFeed,
            icon: const Icon(Icons.refresh_rounded),
            tooltip: 'Atualizar',
          ),
        ],
      ),
      body: _buildBody(context, vm),
    );
  }

  Widget _buildBody(BuildContext context, FeedViewModel vm) {
    return switch (vm.status) {
      FeedStatus.idle || FeedStatus.loading => const Center(
          child: CircularProgressIndicator(),
        ),
      FeedStatus.error => _ErrorState(
          message: vm.error ?? 'Erro desconhecido.',
          onRetry: vm.loadFeed,
        ),
      FeedStatus.success => vm.chamados.isEmpty
          ? const _EmptyState()
          : _ChamadoList(chamados: vm.chamados),
    };
  }
}

class _ErrorState extends StatelessWidget {
  final String message;
  final VoidCallback onRetry;

  const _ErrorState({required this.message, required this.onRetry});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(32),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.wifi_off_rounded,
              size: 64,
              color: Color(0xFF94A3B8),
            ),
            const SizedBox(height: 16),
            Text(
              message,
              textAlign: TextAlign.center,
              style: const TextStyle(
                color: AppTheme.textSecondary,
                fontSize: 15,
              ),
            ),
            const SizedBox(height: 24),
            SizedBox(
              width: double.infinity,
              height: 52,
              child: ElevatedButton.icon(
                onPressed: onRetry,
                icon: const Icon(Icons.refresh_rounded),
                label: const Text(
                  'Tentar novamente',
                  style: TextStyle(fontWeight: FontWeight.w600),
                ),
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppTheme.primary,
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _EmptyState extends StatelessWidget {
  const _EmptyState();

  @override
  Widget build(BuildContext context) {
    return const Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.inbox_rounded, size: 64, color: Color(0xFF94A3B8)),
          SizedBox(height: 12),
          Text(
            'Nenhum chamado registrado',
            style: TextStyle(color: AppTheme.textSecondary, fontSize: 15),
          ),
        ],
      ),
    );
  }
}

class _ChamadoList extends StatelessWidget {
  final List<Chamado> chamados;

  const _ChamadoList({required this.chamados});

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      padding: const EdgeInsets.all(16),
      itemCount: chamados.length,
      separatorBuilder: (_, _) => const SizedBox(height: 10),
      itemBuilder: (_, i) => _ChamadoCard(chamado: chamados[i], rank: i + 1),
    );
  }
}

class _ChamadoCard extends StatelessWidget {
  final Chamado chamado;
  final int rank;

  const _ChamadoCard({required this.chamado, required this.rank});

  Color get _scoreColor {
    if (chamado.scorePrioridade >= 7) return AppTheme.errorColor;
    if (chamado.scorePrioridade >= 4) return AppTheme.warningColor;
    return AppTheme.textSecondary;
  }

  ({Color bg, Color text, String label}) get _estadoBadge {
    return switch (chamado.estado) {
      'ABERTO' => (
          bg: const Color(0xFFFEF2F2),
          text: AppTheme.errorColor,
          label: 'Aberto',
        ),
      'EM_ANALISE' => (
          bg: const Color(0xFFFFFBEB),
          text: AppTheme.warningColor,
          label: 'Em Análise',
        ),
      'EM_EXECUCAO' => (
          bg: const Color(0xFFEFF6FF),
          text: AppTheme.primary,
          label: 'Em Execução',
        ),
      'RESOLVIDO' => (
          bg: const Color(0xFFF0FDF4),
          text: AppTheme.successColor,
          label: 'Resolvido',
        ),
      _ => (
          bg: const Color(0xFFF8FAFC),
          text: AppTheme.textSecondary,
          label: chamado.estado,
        ),
    };
  }

  @override
  Widget build(BuildContext context) {
    final badge = _estadoBadge;

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: AppTheme.surface,
        borderRadius: BorderRadius.circular(14),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            width: 40,
            height: 40,
            decoration: BoxDecoration(
              color: const Color(0xFFF1F5F9),
              borderRadius: BorderRadius.circular(10),
            ),
            child: Center(
              child: Text(
                '#$rank',
                style: const TextStyle(
                  fontWeight: FontWeight.w700,
                  fontSize: 13,
                  color: AppTheme.textPrimary,
                ),
              ),
            ),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  chamado.categoriaNome ?? 'Chamado #${chamado.id}',
                  style: const TextStyle(
                    fontWeight: FontWeight.w600,
                    fontSize: 14,
                    color: AppTheme.textPrimary,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  '#${chamado.id} · ${chamado.latitude.toStringAsFixed(4)}, ${chamado.longitude.toStringAsFixed(4)}',
                  style: const TextStyle(
                    fontSize: 11,
                    color: AppTheme.textSecondary,
                    fontFamily: 'monospace',
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(width: 12),
          Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                decoration: BoxDecoration(
                  color: badge.bg,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Text(
                  badge.label,
                  style: TextStyle(
                    color: badge.text,
                    fontSize: 11,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
              const SizedBox(height: 6),
              Text(
                'Score ${chamado.scorePrioridade.toStringAsFixed(1)}',
                style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.w700,
                  color: _scoreColor,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
