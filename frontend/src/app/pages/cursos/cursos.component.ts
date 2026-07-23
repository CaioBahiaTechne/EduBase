import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Curso } from '../../models/edubase.models';
import { CursoService } from '../../services/curso.service';
import { mensagemErroApi } from '../../services/api-error';

@Component({
  selector: 'app-cursos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cursos.component.html',
  styleUrl: './cursos.component.css'
})
export class CursosComponent implements OnInit {
  cursos: Curso[] = [];
  editandoId: number | null = null;
  form = { nome: '' };
  erro = '';
  sucesso = '';

  constructor(private readonly cursoService: CursoService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.cursoService.listar().subscribe({
      next: (lista) => (this.cursos = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar cursos'))
    });
  }

  iniciarNovo(): void {
    this.editandoId = null;
    this.form = { nome: '' };
    this.limparAvisos();
  }

  editar(curso: Curso): void {
    this.editandoId = curso.id;
    this.form = { nome: curso.nome };
    this.limparAvisos();
  }

  salvar(): void {
    this.limparAvisos();
    const req = { nome: this.form.nome.trim() };
    const obs =
      this.editandoId == null
        ? this.cursoService.criar(req)
        : this.cursoService.atualizar(this.editandoId, req);

    obs.subscribe({
      next: () => {
        this.sucesso = this.editandoId == null ? 'Curso criado.' : 'Curso atualizado.';
        this.iniciarNovo();
        this.carregar();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao salvar curso'))
    });
  }

  excluir(curso: Curso): void {
    if (!confirm(`Excluir curso "${curso.nome}"?`)) {
      return;
    }
    this.limparAvisos();
    this.cursoService.excluir(curso.id).subscribe({
      next: () => {
        this.sucesso = 'Curso excluído.';
        if (this.editandoId === curso.id) {
          this.iniciarNovo();
        }
        this.carregar();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao excluir curso'))
    });
  }

  private limparAvisos(): void {
    this.erro = '';
    this.sucesso = '';
  }
}
