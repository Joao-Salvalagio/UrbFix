import { Component, inject, signal, computed, OnInit, DestroyRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DatePipe, DecimalPipe } from '@angular/common';
import { forkJoin } from 'rxjs';
import { ChamadoService, Estatisticas } from '../../../core/services/chamado.service';
import { Chamado, EstadoChamado, ESTADOS_CHAMADO, PROXIMOS_ESTADOS } from '../../../core/models/chamado.model';
import { StatusBadgeComponent } from '../../../shared/status-badge/status-badge.component';
import { KpiCardComponent } from '../../../shared/kpi-card/kpi-card.component';

type Aba = 'fila' | 'todos';

const ESTADO_LABEL: Record<EstadoChamado, string> = {
  ABERTO: 'Aberto',
  EM_ANALISE: 'Em Análise',
  EM_EXECUCAO: 'Em Execução',
  RESOLVIDO: 'Resolvido'
};

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [StatusBadgeComponent, KpiCardComponent, DatePipe, DecimalPipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private readonly chamadoService = inject(ChamadoService);
  private readonly destroyRef = inject(DestroyRef);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly toast = signal<{ kind: 'success' | 'error'; message: string } | null>(null);
  readonly updatingId = signal<number | null>(null);

  readonly fila = signal<Chamado[]>([]);
  readonly todos = signal<Chamado[]>([]);
  readonly estatisticas = signal<Estatisticas>({
    total: 0, aberto: 0, emAnalise: 0, emExecucao: 0, resolvido: 0
  });

  readonly aba = signal<Aba>('fila');
  readonly filtroEstado = signal<EstadoChamado | 'TODOS'>('TODOS');
  readonly estadosDisponiveis = ESTADOS_CHAMADO;

  readonly skeletonRows = Array.from({ length: 6 });

  readonly listaVisivel = computed<Chamado[]>(() => {
    if (this.aba() === 'fila') {
      return this.fila();
    }
    const filtro = this.filtroEstado();
    if (filtro === 'TODOS') return this.todos();
    return this.todos().filter(c => c.estado === filtro);
  });

  ngOnInit(): void {
    this.carregar();
  }

  rotuloEstado(estado: EstadoChamado): string {
    return ESTADO_LABEL[estado];
  }

  proximosEstados(estado: EstadoChamado): EstadoChamado[] {
    return PROXIMOS_ESTADOS[estado];
  }

  mudarAba(nova: Aba): void {
    this.aba.set(nova);
  }

  mudarFiltro(estado: EstadoChamado | 'TODOS'): void {
    this.filtroEstado.set(estado);
  }

  mudarStatus(chamado: Chamado, novoEstado: EstadoChamado): void {
    if (this.updatingId() !== null) return;
    this.updatingId.set(chamado.id);
    this.chamadoService
      .atualizarStatus(chamado.id, novoEstado)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (atualizado) => {
          this.updatingId.set(null);
          this.mostrarToast('success', `Chamado #${atualizado.id}: ${ESTADO_LABEL[novoEstado]}`);
          this.carregar();
        },
        error: (err) => {
          this.updatingId.set(null);
          const msg = err?.error?.message ?? 'Falha ao atualizar status.';
          this.mostrarToast('error', msg);
        }
      });
  }

  carregar(): void {
    this.loading.set(true);
    forkJoin({
      fila: this.chamadoService.getFilaPrioridade(),
      todos: this.chamadoService.listar(),
      estatisticas: this.chamadoService.getEstatisticas()
    })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: ({ fila, todos, estatisticas }) => {
          this.fila.set(fila);
          this.todos.set(todos);
          this.estatisticas.set(estatisticas);
          this.error.set(null);
          this.loading.set(false);
        },
        error: () => {
          this.error.set('Não foi possível carregar os dados. Verifique se o servidor está disponível.');
          this.loading.set(false);
        }
      });
  }

  private mostrarToast(kind: 'success' | 'error', message: string): void {
    this.toast.set({ kind, message });
    setTimeout(() => this.toast.set(null), 3500);
  }
}
