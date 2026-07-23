export interface Aluno {
  id: number;
  nome: string;
  email: string;
}

export interface AlunoRequest {
  nome: string;
  email: string;
}

export interface Curso {
  id: number;
  nome: string;
}

export interface CursoRequest {
  nome: string;
}

export interface Disciplina {
  id: number;
  nome: string;
  cursoId: number;
  cursoNome: string;
}

export interface DisciplinaRequest {
  nome: string;
  cursoId: number;
}

export type StatusTurma = 'ABERTA' | 'FECHADA';

export interface Turma {
  id: number;
  nome: string;
  vagas: number;
  status: StatusTurma;
  disciplinaId: number;
  disciplinaNome: string;
}

export interface TurmaRequest {
  nome: string;
  vagas: number;
  status: StatusTurma;
  disciplinaId: number;
}

export type StatusMatricula = 'PENDENTE' | 'CONFIRMADA' | 'CANCELADA';

export interface Matricula {
  id: number;
  alunoId: number;
  alunoNome: string;
  turmaId: number;
  turmaNome: string;
  status: StatusMatricula;
}

export interface MatriculaRequest {
  alunoId: number;
  turmaId: number;
}

export interface ApiErrorBody {
  message?: string;
  fields?: Record<string, string>;
}
