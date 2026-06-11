import { Component, input } from '@angular/core';
import { EstadoChamado } from '../../core/models/chamado.model';

const LABEL_MAP: Record<EstadoChamado, string> = {
  ABERTO: 'Aberto',
  EM_ANALISE: 'Em Análise',
  EM_EXECUCAO: 'Em Execução',
  RESOLVIDO: 'Resolvido'
};

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [],
  templateUrl: './status-badge.component.html',
  styleUrl: './status-badge.component.scss'
})
export class StatusBadgeComponent {
  readonly status = input.required<EstadoChamado>();

  get label(): string {
    return LABEL_MAP[this.status()];
  }

  get modifier(): string {
    return this.status().toLowerCase().replace('_', '-');
  }
}
