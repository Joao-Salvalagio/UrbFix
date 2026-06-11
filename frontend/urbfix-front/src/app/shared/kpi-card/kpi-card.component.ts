import { Component, input } from '@angular/core';

export type KpiTone = 'neutral' | 'danger' | 'warning' | 'info' | 'success';

@Component({
  selector: 'app-kpi-card',
  standalone: true,
  templateUrl: './kpi-card.component.html',
  styleUrl: './kpi-card.component.scss'
})
export class KpiCardComponent {
  readonly label = input.required<string>();
  readonly value = input.required<number>();
  readonly tone = input<KpiTone>('neutral');
  readonly hint = input<string | null>(null);
}
