export type EstadoChamado = 'ABERTO' | 'EM_ANALISE' | 'EM_EXECUCAO' | 'RESOLVIDO';

export interface Chamado {
  id: number;
  cidadaoId: number | null;
  cidadaoNome: string | null;
  categoriaId: number;
  categoriaNome: string;
  pesoCategoria: number;
  latitude: number;
  longitude: number;
  scorePrioridade: number;
  estado: EstadoChamado;
  createdAt: string;
}

export const ESTADOS_CHAMADO: EstadoChamado[] = ['ABERTO', 'EM_ANALISE', 'EM_EXECUCAO', 'RESOLVIDO'];

export const PROXIMOS_ESTADOS: Record<EstadoChamado, EstadoChamado[]> = {
  ABERTO: ['EM_ANALISE'],
  EM_ANALISE: ['EM_EXECUCAO', 'ABERTO'],
  EM_EXECUCAO: ['RESOLVIDO', 'EM_ANALISE'],
  RESOLVIDO: []
};
