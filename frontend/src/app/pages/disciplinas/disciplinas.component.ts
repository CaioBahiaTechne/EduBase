import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Curso, Disciplina } from '../../models/edubase.models';
import { CursoService } from '../../services/curso.service';
import { DisciplinaService } from '../../services/disciplina.service';
import { mensagemErroApi } from '../../services/api-error';

@Component({
  selector: 'app-disciplinas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './disciplinas.component.html',
  styleUrl: './disciplinas.component.css'
})
export class DisciplinasComponent implements OnInit {
  disciplinas: Disciplina[] = [];
  cursos: Curso[] = [];
  editandoId: number | null = null;
  form = { nome: '', cursoId: null as number | null };
  erro = '';
  sucesso = '';

  constructor(
    private readonly disciplinaService: DisciplinaService,
    private readonly cursoService: CursoService
  ) {}

  ngOnInit(): void {
    this.cursoService.listar().subscribe({
      next: (lista) => (this.cursos = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar cursos'))
    });
    this.carregar();
  }

  carregar(): void {
    this.disciplinaService.listar().subscribe({
      next: (lista) => (this.disciplinas = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar disciplinas'))
    });
  }

  iniciarNovo(): void {
    this.editandoId = null;
    this.form = { nome: '', cursoId: null };
    this.limparAvisos();
  }

  editar(disciplina: Disciplina): void {
    this.editandoId = disciplina.id;
    this.form = { nome: disciplina.nome, cursoId: disciplina.cursoId };
    this.limparAvisos();
  }

  salvar(): void {
    this.limparAvisos();
    if (this.form.cursoId == null) {
      this.erro = 'Selecione um curso.';
      return;
    }
    const req = { nome: this.form.nome.trim(), cursoId: this.form.cursoId };
    const obs =
      this.editandoId == null
        ? this.disciplinaService.criar(req)
        : this.disciplinaService.atualizar(this.editandoId, req);

    obs.subscribe({
      next: () => {
        this.sucesso = this.editandoId == null ? 'Disciplina criada.' : 'Disciplina atualizada.';
        this.iniciarNovo();
        this.carregar();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao salvar disciplina'))
    });
  }

  excluir(disciplina: Disciplina): void {
    if (!confirm(`Excluir disciplina "${disciplina.nome}"?`)) {
      return;
    }
    this.limparAvisos();
    this.disciplinaService.excluir(disciplina.id).subscribe({
      next: () => {
        this.sucesso = 'Disciplina excluída.';
        if (this.editandoId === disciplina.id) {
          this.iniciarNovo();
        }
        this.carregar();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao excluir disciplina'))
    });
  }

  private limparAvisos(): void {
    this.erro = '';
    this.sucesso = '';
  }
}
