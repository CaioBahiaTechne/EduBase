import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Curso, CursoRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class CursoService {
  private readonly url = `${environment.apiUrl}/cursos`;

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.url);
  }

  criar(body: CursoRequest): Observable<Curso> {
    return this.http.post<Curso>(this.url, body);
  }

  atualizar(id: number, body: CursoRequest): Observable<Curso> {
    return this.http.put<Curso>(`${this.url}/${id}`, body);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
