import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Chamado, EstadoChamado } from '../models/chamado.model';

export interface Estatisticas {
  total: number;
  aberto: number;
  emAnalise: number;
  emExecucao: number;
  resolvido: number;
}

@Injectable({ providedIn: 'root' })
export class ChamadoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `http://${typeof location !== 'undefined' ? location.hostname : 'localhost'}:8080/api/v1/chamados`;

  getFilaPrioridade(): Observable<Chamado[]> {
    return this.http.get<Chamado[]>(`${this.apiUrl}/fila-prioridade`);
  }

  listar(estado?: EstadoChamado): Observable<Chamado[]> {
    const url = estado ? `${this.apiUrl}?estado=${estado}` : this.apiUrl;
    return this.http.get<Chamado[]>(url);
  }

  getEstatisticas(): Observable<Estatisticas> {
    return this.http.get<Estatisticas>(`${this.apiUrl}/estatisticas`);
  }

  atualizarStatus(id: number, estado: EstadoChamado): Observable<Chamado> {
    return this.http.put<Chamado>(`${this.apiUrl}/${id}/status`, { estado });
  }
}
