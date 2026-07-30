import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Aluno } from '../../models/edubase.models';
import { AlunoService } from '../../services/aluno.service';
import { mensagemErroApi } from '../../core/api-error';
import { ToastService } from '../../core/toast.service';

@Component({
  selector: 'app-alunos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './alunos.component.html'
})
export class AlunosComponent implements OnInit {
  alunos: Aluno[] = [];
  editandoId: number | null = null;
  form = { nome: '', email: '' };

  constructor(
    private readonly alunoService: AlunoService,
    private readonly toast: ToastService
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.alunoService.listar().subscribe({
      next: (lista) => (this.alunos = lista),
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao listar alunos'))
    });
  }

  iniciarNovo(): void {
    this.editandoId = null;
    this.form = { nome: '', email: '' };
  }

  editar(aluno: Aluno): void {
    this.editandoId = aluno.id;
    this.form = { nome: aluno.nome, email: aluno.email };
  }

  salvar(): void {
    const req = { nome: this.form.nome.trim(), email: this.form.email.trim() };
    const obs =
      this.editandoId == null
        ? this.alunoService.criar(req)
        : this.alunoService.atualizar(this.editandoId, req);

    obs.subscribe({
      next: () => {
        this.toast.success(this.editandoId == null ? 'Aluno criado.' : 'Aluno atualizado.');
        this.iniciarNovo();
        this.carregar();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao salvar aluno'))
    });
  }

  excluir(aluno: Aluno): void {
    if (!confirm(`Excluir aluno "${aluno.nome}"?`)) {
      return;
    }
    this.alunoService.excluir(aluno.id).subscribe({
      next: () => {
        this.toast.success('Aluno excluído.');
        if (this.editandoId === aluno.id) {
          this.iniciarNovo();
        }
        this.carregar();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao excluir aluno'))
    });
  }
}
