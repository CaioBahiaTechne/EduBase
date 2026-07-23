import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Disciplina, DisciplinaRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class DisciplinaService {
  private readonly url = `${environment.apiUrl}/disciplinas`;

  constructor(private readonly http: HttpClient) {}

  listar(cursoId?: number): Observable<Disciplina[]> {
    let params = new HttpParams();
    if (cursoId != null) {
      params = params.set('cursoId', cursoId);
    }
    return this.http.get<Disciplina[]>(this.url, { params });
  }

  criar(body: DisciplinaRequest): Observable<Disciplina> {
    return this.http.post<Disciplina>(this.url, body);
  }

  atualizar(id: number, body: DisciplinaRequest): Observable<Disciplina> {
    return this.http.put<Disciplina>(`${this.url}/${id}`, body);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
