import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { apiUrl } from '../core/api-url';
import { Matricula, MatriculaRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class MatriculaService {
  private readonly url = apiUrl('matriculas');

  constructor(private readonly http: HttpClient) {}

  listar(alunoId?: number, turmaId?: number): Observable<Matricula[]> {
    let params = new HttpParams();
    if (alunoId != null) {
      params = params.set('alunoId', alunoId);
    }
    if (turmaId != null) {
      params = params.set('turmaId', turmaId);
    }
    return this.http.get<Matricula[]>(this.url, { params });
  }

  /** RN007 */
  listarPorAluno(alunoId: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(apiUrl('matriculas', 'aluno', alunoId));
  }

  /** RN008 */
  listarPorTurma(turmaId: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(apiUrl('matriculas', 'turma', turmaId));
  }

  buscar(id: number): Observable<Matricula> {
    return this.http.get<Matricula>(apiUrl('matriculas', id));
  }

  criar(body: MatriculaRequest): Observable<Matricula> {
    return this.http.post<Matricula>(this.url, body);
  }

  atualizar(id: number, body: MatriculaRequest): Observable<Matricula> {
    return this.http.put<Matricula>(apiUrl('matriculas', id), body);
  }

  /** RN005 */
  confirmar(id: number): Observable<Matricula> {
    return this.http.post<Matricula>(apiUrl('matriculas', id, 'confirmar'), {});
  }

  /** RN006 */
  cancelar(id: number): Observable<Matricula> {
    return this.http.post<Matricula>(apiUrl('matriculas', id, 'cancelar'), {});
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(apiUrl('matriculas', id));
  }
}
