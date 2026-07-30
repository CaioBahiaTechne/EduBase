import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Curso } from '../../models/edubase.models';
import { CursoService } from '../../services/curso.service';
import { mensagemErroApi } from '../../core/api-error';
import { ToastService } from '../../core/toast.service';

@Component({
  selector: 'app-cursos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cursos.component.html'
})
export class CursosComponent implements OnInit {
  cursos: Curso[] = [];
  editandoId: number | null = null;
  form = { nome: '' };

  constructor(
    private readonly cursoService: CursoService,
    private readonly toast: ToastService
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.cursoService.listar().subscribe({
      next: (lista) => (this.cursos = lista),
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao listar cursos'))
    });
  }

  iniciarNovo(): void {
    this.editandoId = null;
    this.form = { nome: '' };
  }

  editar(curso: Curso): void {
    this.editandoId = curso.id;
    this.form = { nome: curso.nome };
  }

  salvar(): void {
    const req = { nome: this.form.nome.trim() };
    const obs =
      this.editandoId == null
        ? this.cursoService.criar(req)
        : this.cursoService.atualizar(this.editandoId, req);

    obs.subscribe({
      next: () => {
        this.toast.success(this.editandoId == null ? 'Curso criado.' : 'Curso atualizado.');
        this.iniciarNovo();
        this.carregar();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao salvar curso'))
    });
  }

  excluir(curso: Curso): void {
    if (!confirm(`Excluir curso "${curso.nome}"?`)) {
      return;
    }
    this.cursoService.excluir(curso.id).subscribe({
      next: () => {
        this.toast.success('Curso excluído.');
        if (this.editandoId === curso.id) {
          this.iniciarNovo();
        }
        this.carregar();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao excluir curso'))
    });
  }
}
