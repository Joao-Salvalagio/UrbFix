import { Routes } from '@angular/router';
import { PainelLayoutComponent } from './features/dashboard/painel-layout/painel-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard/dashboard.component';

export const routes: Routes = [
  {
    path: '',
    component: PainelLayoutComponent,
    children: [
      { path: '', component: DashboardComponent }
    ]
  }
];
