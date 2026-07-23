import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { apiUrl } from '../core/api-url';
import { Disciplina, DisciplinaRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class DisciplinaService {
  private readonly url = apiUrl('disciplinas');

  constructor(private readonly http: HttpClient) {}

  listar(cursoId?: number): Observable<Disciplina[]> {
    let params = new HttpParams();
    if (cursoId != null) {
      params = params.set('cursoId', cursoId);
    }
    return this.http.get<Disciplina[]>(this.url, { params });
  }

  buscar(id: number): Observable<Disciplina> {
    return this.http.get<Disciplina>(apiUrl('disciplinas', id));
  }

  criar(body: DisciplinaRequest): Observable<Disciplina> {
    return this.http.post<Disciplina>(this.url, body);
  }

  atualizar(id: number, body: DisciplinaRequest): Observable<Disciplina> {
    return this.http.put<Disciplina>(apiUrl('disciplinas', id), body);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(apiUrl('disciplinas', id));
  }
}
