import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Matricula, MatriculaRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class MatriculaService {
  private readonly url = `${environment.apiUrl}/matriculas`;

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

  listarPorAluno(alunoId: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.url}/aluno/${alunoId}`);
  }

  listarPorTurma(turmaId: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.url}/turma/${turmaId}`);
  }

  criar(body: MatriculaRequest): Observable<Matricula> {
    return this.http.post<Matricula>(this.url, body);
  }

  confirmar(id: number): Observable<Matricula> {
    return this.http.post<Matricula>(`${this.url}/${id}/confirmar`, {});
  }

  cancelar(id: number): Observable<Matricula> {
    return this.http.post<Matricula>(`${this.url}/${id}/cancelar`, {});
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
