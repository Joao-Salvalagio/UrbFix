import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-painel-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './painel-layout.component.html',
  styleUrl: './painel-layout.component.scss'
})
export class PainelLayoutComponent {}
