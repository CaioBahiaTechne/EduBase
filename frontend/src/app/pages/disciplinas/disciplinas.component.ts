import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Curso, Disciplina } from '../../models/edubase.models';
import { CursoService } from '../../services/curso.service';
import { DisciplinaService } from '../../services/disciplina.service';
import { mensagemErroApi } from '../../core/api-error';
import { ToastService } from '../../core/toast.service';

@Component({
  selector: 'app-disciplinas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './disciplinas.component.html'
})
export class DisciplinasComponent implements OnInit {
  disciplinas: Disciplina[] = [];
  cursos: Curso[] = [];
  editandoId: number | null = null;
  form = { nome: '', cursoId: null as number | null };

  constructor(
    private readonly disciplinaService: DisciplinaService,
    private readonly cursoService: CursoService,
    private readonly toast: ToastService
  ) {}

  ngOnInit(): void {
    this.cursoService.listar().subscribe({
      next: (lista) => (this.cursos = lista),
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao listar cursos'))
    });
    this.carregar();
  }

  carregar(): void {
    this.disciplinaService.listar().subscribe({
      next: (lista) => (this.disciplinas = lista),
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao listar disciplinas'))
    });
  }

  iniciarNovo(): void {
    this.editandoId = null;
    this.form = { nome: '', cursoId: null };
  }

  editar(disciplina: Disciplina): void {
    this.editandoId = disciplina.id;
    this.form = { nome: disciplina.nome, cursoId: disciplina.cursoId };
  }

  salvar(): void {
    if (this.form.cursoId == null) {
      this.toast.error('Selecione um curso.');
      return;
    }
    const req = { nome: this.form.nome.trim(), cursoId: this.form.cursoId };
    const obs =
      this.editandoId == null
        ? this.disciplinaService.criar(req)
        : this.disciplinaService.atualizar(this.editandoId, req);

    obs.subscribe({
      next: () => {
        this.toast.success(
          this.editandoId == null ? 'Disciplina criada.' : 'Disciplina atualizada.'
        );
        this.iniciarNovo();
        this.carregar();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao salvar disciplina'))
    });
  }

  excluir(disciplina: Disciplina): void {
    if (!confirm(`Excluir disciplina "${disciplina.nome}"?`)) {
      return;
    }
    this.disciplinaService.excluir(disciplina.id).subscribe({
      next: () => {
        this.toast.success('Disciplina excluída.');
        if (this.editandoId === disciplina.id) {
          this.iniciarNovo();
        }
        this.carregar();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao excluir disciplina'))
    });
  }
}
