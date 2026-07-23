import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Aluno, AlunoRequest } from '../models/edubase.models';

@Injectable({ providedIn: 'root' })
export class AlunoService {
  private readonly url = `${environment.apiUrl}/alunos`;

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
    return this.http.get<Aluno>(`${this.url}/${id}`);
  }

  criar(body: AlunoRequest): Observable<Aluno> {
    return this.http.post<Aluno>(this.url, body);
  }

  atualizar(id: number, body: AlunoRequest): Observable<Aluno> {
    return this.http.put<Aluno>(`${this.url}/${id}`, body);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
