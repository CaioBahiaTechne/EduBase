import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { StatusTurma, Turma, TurmaRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class TurmaService {
  private readonly url = `${environment.apiUrl}/turmas`;

  constructor(private readonly http: HttpClient) {}

  listar(disciplinaId?: number, status?: StatusTurma): Observable<Turma[]> {
    let params = new HttpParams();
    if (disciplinaId != null) {
      params = params.set('disciplinaId', disciplinaId);
    }
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<Turma[]>(this.url, { params });
  }

  criar(body: TurmaRequest): Observable<Turma> {
    return this.http.post<Turma>(this.url, body);
  }

  atualizar(id: number, body: TurmaRequest): Observable<Turma> {
    return this.http.put<Turma>(`${this.url}/${id}`, body);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
