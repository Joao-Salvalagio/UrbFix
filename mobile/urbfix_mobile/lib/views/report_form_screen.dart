import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:provider/provider.dart';
import '../core/app_theme.dart';
import '../models/categoria.dart';
import '../viewmodels/report_form_viewmodel.dart';

class ReportFormScreen extends StatelessWidget {
  const ReportFormScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => ReportFormViewModel(),
      child: const _ReportFormScaffold(),
    );
  }
}

class _ReportFormScaffold extends StatefulWidget {
  const _ReportFormScaffold();

  @override
  State<_ReportFormScaffold> createState() => _ReportFormScaffoldState();
}

class _ReportFormScaffoldState extends State<_ReportFormScaffold> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ReportFormViewModel>().loadCategories();
      _acquireLocation();
    });
  }

  Future<void> _acquireLocation() async {
    if (!mounted) return;
    final result = await context.read<ReportFormViewModel>().acquireLocation();
    if (!mounted) return;
    if (result == LocationResult.permissionDenied) {
      _showPermissionDeniedDialog();
    }
  }

  void _showPermissionDeniedDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => AlertDialog(
        title: const Text('Localização Necessária'),
        content: const Text(
          'O UrbFix precisa da sua localização para direcionar a equipe de zeladoria ao local exato do problema.\n\nPor favor, habilite a permissão de localização para este aplicativo nas configurações do dispositivo.',
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(ctx);
              Navigator.pop(context);
            },
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () async {
              Navigator.pop(ctx);
              await Geolocator.openLocationSettings();
            },
            style: ElevatedButton.styleFrom(
              backgroundColor: AppTheme.primary,
              foregroundColor: Colors.white,
            ),
            child: const Text('Abrir Configurações'),
          ),
        ],
      ),
    );
  }

  void _showImageSourceSheet() {
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (ctx) {
        final vm = context.read<ReportFormViewModel>();
        return SafeArea(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const SizedBox(height: 8),
              Container(
                width: 40,
                height: 4,
                decoration: BoxDecoration(
                  color: AppTheme.borderColor,
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
              const SizedBox(height: 16),
              const Text(
                'Adicionar Foto',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w600,
                  color: AppTheme.textPrimary,
                ),
              ),
              const SizedBox(height: 8),
              _SourceOption(
                icon: Icons.camera_alt_rounded,
                label: 'Usar Câmera',
                onTap: () {
                  Navigator.pop(ctx);
                  vm.captureImage();
                },
              ),
              _SourceOption(
                icon: Icons.photo_library_rounded,
                label: 'Escolher da Galeria',
                onTap: () {
                  Navigator.pop(ctx);
                  vm.pickFromGallery();
                },
              ),
              const SizedBox(height: 8),
            ],
          ),
        );
      },
    );
  }

  Future<void> _handleSubmit() async {
    final vm = context.read<ReportFormViewModel>();
    final success = await vm.submitReport();
    if (!mounted) return;
    if (success) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Chamado registrado com sucesso!'),
          backgroundColor: AppTheme.successColor,
          behavior: SnackBarBehavior.floating,
        ),
      );
      await Future.delayed(const Duration(milliseconds: 800));
      if (mounted) Navigator.pop(context);
    } else {
      final error = vm.error;
      if (error != null) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(error),
            backgroundColor: AppTheme.errorColor,
            behavior: SnackBarBehavior.floating,
          ),
        );
        vm.clearError();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final vm = context.watch<ReportFormViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Relatar Problema'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.fromLTRB(20, 20, 20, 32),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _SectionLabel('1. Foto do Problema'),
            const SizedBox(height: 10),
            _ImageCapture(
              imageBase64: vm.imageBase64,
              onTap: _showImageSourceSheet,
            ),
            const SizedBox(height: 28),
            _SectionLabel('2. Localização GPS'),
            const SizedBox(height: 10),
            _LocationStatus(
              vm: vm,
              onRetry: () => _acquireLocation(),
            ),
            const SizedBox(height: 28),
            _SectionLabel('3. Categoria do Problema'),
            const SizedBox(height: 10),
            _CategoryPicker(vm: vm),
            const SizedBox(height: 36),
            _SubmitButton(vm: vm, onSubmit: _handleSubmit),
          ],
        ),
      ),
    );
  }
}

class _SectionLabel extends StatelessWidget {
  final String text;
  const _SectionLabel(this.text);

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      style: const TextStyle(
        fontSize: 14,
        fontWeight: FontWeight.w600,
        color: AppTheme.textSecondary,
        letterSpacing: 0.3,
      ),
    );
  }
}

class _ImageCapture extends StatelessWidget {
  final String? imageBase64;
  final VoidCallback onTap;

  const _ImageCapture({required this.imageBase64, required this.onTap});

  @override
  Widget build(BuildContext context) {
    final hasImage = imageBase64 != null;

    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: double.infinity,
        height: 200,
        decoration: BoxDecoration(
          color: const Color(0xFFF1F5F9),
          borderRadius: BorderRadius.circular(16),
          border: Border.all(
            color: hasImage ? AppTheme.primary : AppTheme.borderColor,
            width: hasImage ? 2 : 1,
          ),
        ),
        child: hasImage
            ? ClipRRect(
                borderRadius: BorderRadius.circular(15),
                child: Image.memory(
                  base64Decode(imageBase64!.split(',').last),
                  fit: BoxFit.cover,
                  gaplessPlayback: true,
                ),
              )
            : Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(
                    Icons.add_a_photo_rounded,
                    size: 48,
                    color: Color(0xFF94A3B8),
                  ),
                  const SizedBox(height: 10),
                  const Text(
                    'Toque para fotografar o problema',
                    style: TextStyle(
                      color: Color(0xFF94A3B8),
                      fontSize: 14,
                    ),
                  ),
                ],
              ),
      ),
    );
  }
}

class _LocationStatus extends StatelessWidget {
  final ReportFormViewModel vm;
  final VoidCallback onRetry;

  const _LocationStatus({required this.vm, required this.onRetry});

  @override
  Widget build(BuildContext context) {
    final isLoading = vm.status == FormStatus.loadingLocation;
    final hasPosition = vm.position != null;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      decoration: BoxDecoration(
        color: hasPosition
            ? const Color(0xFFF0FDF4)
            : isLoading
                ? const Color(0xFFF8FAFC)
                : const Color(0xFFFFFBEB),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: hasPosition
              ? const Color(0xFFBBF7D0)
              : isLoading
                  ? AppTheme.borderColor
                  : const Color(0xFFFDE68A),
        ),
      ),
      child: Row(
        children: [
          Icon(
            hasPosition
                ? Icons.location_on_rounded
                : isLoading
                    ? Icons.gps_fixed_rounded
                    : Icons.location_off_rounded,
            color: hasPosition
                ? AppTheme.successColor
                : isLoading
                    ? AppTheme.textSecondary
                    : AppTheme.warningColor,
            size: 22,
          ),
          const SizedBox(width: 12),
          Expanded(
            child: isLoading
                ? const Text(
                    'Obtendo localização...',
                    style: TextStyle(
                      color: AppTheme.textSecondary,
                      fontSize: 13,
                    ),
                  )
                : hasPosition
                    ? Text(
                        '${vm.position!.latitude.toStringAsFixed(6)}, ${vm.position!.longitude.toStringAsFixed(6)}',
                        style: const TextStyle(
                          fontFamily: 'monospace',
                          fontSize: 12,
                          color: AppTheme.textPrimary,
                        ),
                      )
                    : const Text(
                        'Localização não obtida',
                        style: TextStyle(
                          color: AppTheme.warningColor,
                          fontSize: 13,
                        ),
                      ),
          ),
          if (isLoading)
            const SizedBox(
              width: 18,
              height: 18,
              child: CircularProgressIndicator(strokeWidth: 2),
            )
          else if (!hasPosition)
            TextButton(
              onPressed: onRetry,
              style: TextButton.styleFrom(
                padding: EdgeInsets.zero,
                minimumSize: const Size(60, 32),
              ),
              child: const Text(
                'Tentar novamente',
                style: TextStyle(fontSize: 12),
              ),
            ),
        ],
      ),
    );
  }
}

class _CategoryPicker extends StatelessWidget {
  final ReportFormViewModel vm;

  const _CategoryPicker({required this.vm});

  @override
  Widget build(BuildContext context) {
    if (vm.status == FormStatus.loadingCategories) {
      return const Center(
        child: Padding(
          padding: EdgeInsets.all(16),
          child: CircularProgressIndicator(),
        ),
      );
    }

    if (vm.categorias.isEmpty) {
      return Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: const Color(0xFFFEF2F2),
          borderRadius: BorderRadius.circular(12),
        ),
        child: const Row(
          children: [
            Icon(Icons.error_outline_rounded, color: AppTheme.errorColor),
            SizedBox(width: 10),
            Expanded(
              child: Text(
                'Sem categorias disponíveis. Verifique a conexão.',
                style: TextStyle(color: AppTheme.errorColor, fontSize: 13),
              ),
            ),
          ],
        ),
      );
    }

    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children: vm.categorias.map((cat) => _CategoryChip(
            categoria: cat,
            isSelected: vm.selectedCategoria?.id == cat.id,
            onTap: () => vm.selectCategoria(cat),
          )).toList(),
    );
  }
}

class _CategoryChip extends StatelessWidget {
  final Categoria categoria;
  final bool isSelected;
  final VoidCallback onTap;

  const _CategoryChip({
    required this.categoria,
    required this.isSelected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 12),
        decoration: BoxDecoration(
          color: isSelected ? AppTheme.primary : AppTheme.surface,
          borderRadius: BorderRadius.circular(10),
          border: Border.all(
            color: isSelected ? AppTheme.primary : AppTheme.borderColor,
            width: isSelected ? 2 : 1,
          ),
          boxShadow: isSelected
              ? [
                  BoxShadow(
                    color: AppTheme.primary.withValues(alpha: 0.25),
                    blurRadius: 8,
                    offset: const Offset(0, 2),
                  ),
                ]
              : null,
        ),
        child: Text(
          categoria.nome,
          style: TextStyle(
            color: isSelected ? Colors.white : AppTheme.textPrimary,
            fontWeight: isSelected ? FontWeight.w600 : FontWeight.w400,
            fontSize: 14,
          ),
        ),
      ),
    );
  }
}

class _SubmitButton extends StatelessWidget {
  final ReportFormViewModel vm;
  final VoidCallback onSubmit;

  const _SubmitButton({required this.vm, required this.onSubmit});

  @override
  Widget build(BuildContext context) {
    final isSubmitting = vm.status == FormStatus.submitting;

    return SizedBox(
      width: double.infinity,
      height: 58,
      child: ElevatedButton(
        onPressed: (vm.canSubmit && !isSubmitting) ? onSubmit : null,
        style: ElevatedButton.styleFrom(
          backgroundColor: AppTheme.primary,
          disabledBackgroundColor: const Color(0xFFE2E8F0),
          foregroundColor: Colors.white,
          disabledForegroundColor: AppTheme.textSecondary,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(14),
          ),
          elevation: 0,
        ),
        child: isSubmitting
            ? const SizedBox(
                width: 22,
                height: 22,
                child: CircularProgressIndicator(
                  color: Colors.white,
                  strokeWidth: 2.5,
                ),
              )
            : const Text(
                'Enviar Relato',
                style: TextStyle(
                  fontSize: 17,
                  fontWeight: FontWeight.w700,
                ),
              ),
      ),
    );
  }
}

class _SourceOption extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback onTap;

  const _SourceOption({
    required this.icon,
    required this.label,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        child: Row(
          children: [
            Container(
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: const Color(0xFFF1F5F9),
                borderRadius: BorderRadius.circular(10),
              ),
              child: Icon(icon, color: AppTheme.primary, size: 24),
            ),
            const SizedBox(width: 16),
            Text(
              label,
              style: const TextStyle(
                fontSize: 15,
                fontWeight: FontWeight.w500,
                color: AppTheme.textPrimary,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
