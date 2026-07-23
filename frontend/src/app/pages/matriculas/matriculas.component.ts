import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Aluno, Matricula, Turma } from '../../models/edubase.models';
import { AlunoService } from '../../services/aluno.service';
import { MatriculaService } from '../../services/matricula.service';
import { TurmaService } from '../../services/turma.service';
import { mensagemErroApi } from '../../services/api-error';

@Component({
  selector: 'app-matriculas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './matriculas.component.html',
  styleUrl: './matriculas.component.css'
})
export class MatriculasComponent implements OnInit {
  matriculas: Matricula[] = [];
  alunos: Aluno[] = [];
  turmas: Turma[] = [];

  alunoId: number | null = null;
  turmaId: number | null = null;
  filtroAlunoId: number | null = null;
  filtroTurmaId: number | null = null;

  erro = '';
  sucesso = '';

  constructor(
    private readonly matriculaService: MatriculaService,
    private readonly alunoService: AlunoService,
    private readonly turmaService: TurmaService
  ) {}

  ngOnInit(): void {
    this.alunoService.listar().subscribe({
      next: (lista) => (this.alunos = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar alunos'))
    });
    this.turmaService.listar().subscribe({
      next: (lista) => (this.turmas = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar turmas'))
    });
    this.carregar();
  }

  get turmasDisponiveis(): Turma[] {
    return this.turmas.filter((t) => t.status === 'ABERTA' && t.vagas > 0);
  }

  carregar(): void {
    this.limparAvisos();
    const aluno = this.filtroAlunoId ?? undefined;
    const turma = this.filtroTurmaId ?? undefined;
    this.matriculaService.listar(aluno, turma).subscribe({
      next: (lista) => (this.matriculas = lista),
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao listar matrículas'))
    });
  }

  limparFiltros(): void {
    this.filtroAlunoId = null;
    this.filtroTurmaId = null;
    this.carregar();
  }

  matricular(): void {
    this.limparAvisos();
    if (this.alunoId == null || this.turmaId == null) {
      this.erro = 'Selecione aluno e turma.';
      return;
    }
    this.matriculaService.criar({ alunoId: this.alunoId, turmaId: this.turmaId }).subscribe({
      next: () => {
        this.sucesso = 'Matrícula criada com status PENDENTE.';
        this.alunoId = null;
        this.turmaId = null;
        this.recarregarTurmasELista();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao matricular'))
    });
  }

  confirmar(m: Matricula): void {
    this.limparAvisos();
    this.matriculaService.confirmar(m.id).subscribe({
      next: () => {
        this.sucesso = `Matrícula #${m.id} confirmada.`;
        this.recarregarTurmasELista();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao confirmar'))
    });
  }

  cancelar(m: Matricula): void {
    if (!confirm(`Cancelar matrícula #${m.id}?`)) {
      return;
    }
    this.limparAvisos();
    this.matriculaService.cancelar(m.id).subscribe({
      next: () => {
        this.sucesso = `Matrícula #${m.id} cancelada.`;
        this.recarregarTurmasELista();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao cancelar'))
    });
  }

  excluir(m: Matricula): void {
    if (!confirm(`Excluir matrícula #${m.id}?`)) {
      return;
    }
    this.limparAvisos();
    this.matriculaService.excluir(m.id).subscribe({
      next: () => {
        this.sucesso = 'Matrícula excluída.';
        this.recarregarTurmasELista();
      },
      error: (err) => (this.erro = mensagemErroApi(err, 'Erro ao excluir'))
    });
  }

  private recarregarTurmasELista(): void {
    this.turmaService.listar().subscribe({
      next: (lista) => (this.turmas = lista)
    });
    this.carregar();
  }

  private limparAvisos(): void {
    this.erro = '';
    this.sucesso = '';
  }
}
