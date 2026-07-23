import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Disciplina, StatusTurma, Turma } from '../../models/edubase.models';
import { DisciplinaService } from '../../services/disciplina.service';
import { TurmaService } from '../../services/turma.service';
import { mensagemErroApi } from '../../services/api-error';

@Component({
  selector: 'app-turmas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './turmas.component.html',
  styleUrl: './turmas.component.css'
})
export class TurmasComponent implements OnInit {
  turmas: Turma[] = [];
  disciplinas: Disciplina[] = [];
  editandoId: number | null = null;
  form = {
    nome: '',
    vagas: 30,
    status: 'ABERTA' as StatusTurma,
    disciplinaId: null as number | null
  };
  erro = '';
  sucesso = '';
  readonly statusOpcoes: StatusTurma[] = ['ABERTA', 'FECHADA'];

  constructor(
    private readonly turmaService: TurmaService,
    private readonly disciplinaService: DisciplinaService
  ) {}

  ngOnInit(): void {
    this.disciplinaService.listar().subscribe({
      next: (lista) => (this.disciplinas = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar disciplinas'))
    });
    this.carregar();
  }

  carregar(): void {
    this.turmaService.listar().subscribe({
      next: (lista) => (this.turmas = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar turmas'))
    });
  }

  iniciarNovo(): void {
    this.editandoId = null;
    this.form = { nome: '', vagas: 30, status: 'ABERTA', disciplinaId: null };
    this.limparAvisos();
  }

  editar(turma: Turma): void {
    this.editandoId = turma.id;
    this.form = {
      nome: turma.nome,
      vagas: turma.vagas,
      status: turma.status,
      disciplinaId: turma.disciplinaId
    };
    this.limparAvisos();
  }

  salvar(): void {
    this.limparAvisos();
    if (this.form.disciplinaId == null) {
      this.erro = 'Selecione uma disciplina.';
      return;
    }
    const req = {
      nome: this.form.nome.trim(),
      vagas: Number(this.form.vagas),
      status: this.form.status,
      disciplinaId: this.form.disciplinaId
    };
    const obs =
      this.editandoId == null
        ? this.turmaService.criar(req)
        : this.turmaService.atualizar(this.editandoId, req);

    obs.subscribe({
      next: () => {
        this.sucesso = this.editandoId == null ? 'Turma criada.' : 'Turma atualizada.';
        this.iniciarNovo();
        this.carregar();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao salvar turma'))
    });
  }

  excluir(turma: Turma): void {
    if (!confirm(`Excluir turma "${turma.nome}"?`)) {
      return;
    }
    this.limparAvisos();
    this.turmaService.excluir(turma.id).subscribe({
      next: () => {
        this.sucesso = 'Turma excluída.';
        if (this.editandoId === turma.id) {
          this.iniciarNovo();
        }
        this.carregar();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao excluir turma'))
    });
  }

  private limparAvisos(): void {
    this.erro = '';
    this.sucesso = '';
  }
}
