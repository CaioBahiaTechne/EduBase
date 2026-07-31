import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Aluno, Matricula, Turma } from '../../models/edubase.models';
import { AlunoService } from '../../services/aluno.service';
import { MatriculaService } from '../../services/matricula.service';
import { TurmaService } from '../../services/turma.service';
import { mensagemErroApi } from '../../core/api-error';
import { ToastService } from '../../core/toast.service';
import { ConfirmDialogService } from '../../core/confirm-dialog.service';

@Component({
  selector: 'app-matriculas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './matriculas.component.html'
})
export class MatriculasComponent implements OnInit {
  matriculas: Matricula[] = [];
  alunos: Aluno[] = [];
  turmas: Turma[] = [];

  alunoId: number | null = null;
  turmaId: number | null = null;
  filtroAlunoId: number | null = null;
  filtroTurmaId: number | null = null;

  constructor(
    private readonly matriculaService: MatriculaService,
    private readonly alunoService: AlunoService,
    private readonly turmaService: TurmaService,
    private readonly toast: ToastService,
    private readonly confirmDialog: ConfirmDialogService
  ) {}

  ngOnInit(): void {
    this.alunoService.listar().subscribe({
      next: (lista) => (this.alunos = lista),
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao listar alunos'))
    });
    this.turmaService.listar().subscribe({
      next: (lista) => (this.turmas = lista),
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao listar turmas'))
    });
    this.carregar();
  }

  get turmasDisponiveis(): Turma[] {
    return this.turmas.filter((t) => t.status === 'ABERTA' && t.vagas > 0);
  }

  carregar(): void {
    const aluno = this.filtroAlunoId ?? undefined;
    const turma = this.filtroTurmaId ?? undefined;
    this.matriculaService.listar(aluno, turma).subscribe({
      next: (lista) => (this.matriculas = lista),
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao listar matrículas'))
    });
  }

  limparFiltros(): void {
    this.filtroAlunoId = null;
    this.filtroTurmaId = null;
    this.carregar();
  }

  async matricular(): Promise<void> {
    if (this.alunoId == null || this.turmaId == null) {
      this.toast.error('Selecione aluno e turma.');
      return;
    }

    const aluno = this.alunos.find((a) => a.id === this.alunoId);
    const turma = this.turmas.find((t) => t.id === this.turmaId);
    const alunoLabel = aluno ? `${aluno.nome} (${aluno.email})` : `#${this.alunoId}`;
    const turmaLabel = turma
      ? `${turma.nome} — ${turma.disciplinaNome}`
      : `#${this.turmaId}`;

    const ok = await this.confirmDialog.confirm({
      title: 'Confirmar matrícula',
      message: `Matricular ${alunoLabel} na turma ${turmaLabel}? A matrícula será criada com status PENDENTE.`,
      confirmLabel: 'Matricular',
      cancelLabel: 'Voltar'
    });
    if (!ok) {
      return;
    }

    this.matriculaService.criar({ alunoId: this.alunoId, turmaId: this.turmaId }).subscribe({
      next: () => {
        this.toast.success('Matrícula criada com status PENDENTE.');
        this.alunoId = null;
        this.turmaId = null;
        this.recarregarTurmasELista();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao matricular'))
    });
  }

  confirmar(m: Matricula): void {
    this.matriculaService.confirmar(m.id).subscribe({
      next: () => {
        this.toast.success(`Matrícula #${m.id} confirmada.`);
        this.recarregarTurmasELista();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao confirmar'))
    });
  }

  cancelar(m: Matricula): void {
    if (!confirm(`Cancelar matrícula #${m.id}?`)) {
      return;
    }
    this.matriculaService.cancelar(m.id).subscribe({
      next: () => {
        this.toast.success(`Matrícula #${m.id} cancelada.`);
        this.recarregarTurmasELista();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao cancelar'))
    });
  }

  excluir(m: Matricula): void {
    if (!confirm(`Excluir matrícula #${m.id}?`)) {
      return;
    }
    this.matriculaService.excluir(m.id).subscribe({
      next: () => {
        this.toast.success('Matrícula excluída.');
        this.recarregarTurmasELista();
      },
      error: (err) => this.toast.error(mensagemErroApi(err, 'Erro ao excluir'))
    });
  }

  private recarregarTurmasELista(): void {
    this.turmaService.listar().subscribe({
      next: (lista) => (this.turmas = lista)
    });
    this.carregar();
  }
}
