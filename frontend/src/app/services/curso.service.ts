import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { apiUrl } from '../core/api-url';
import { Curso, CursoRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class CursoService {
  private readonly url = apiUrl('cursos');

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.url);
  }

  buscar(id: number): Observable<Curso> {
    return this.http.get<Curso>(apiUrl('cursos', id));
  }

  criar(body: CursoRequest): Observable<Curso> {
    return this.http.post<Curso>(this.url, body);
  }

  atualizar(id: number, body: CursoRequest): Observable<Curso> {
    return this.http.put<Curso>(apiUrl('cursos', id), body);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(apiUrl('cursos', id));
  }
}
