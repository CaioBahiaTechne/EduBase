import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { apiUrl } from '../core/api-url';
import { Aluno, AlunoRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class AlunoService {
  private readonly url = apiUrl('alunos');

  constructor(private readonly http: HttpClient) {}

  listar(nome?: string, email?: string): Observable<Aluno[]> {
    let params = new HttpParams();
    if (nome?.trim()) {
      params = params.set('nome', nome.trim());
    }
    if (email?.trim()) {
      params = params.set('email', email.trim());
    }
    return this.http.get<Aluno[]>(this.url, { params });
  }

  buscar(id: number): Observable<Aluno> {
    return this.http.get<Aluno>(apiUrl('alunos', id));
  }

  criar(body: AlunoRequest): Observable<Aluno> {
    return this.http.post<Aluno>(this.url, body);
  }

  atualizar(id: number, body: AlunoRequest): Observable<Aluno> {
    return this.http.put<Aluno>(apiUrl('alunos', id), body);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(apiUrl('alunos', id));
  }
}
