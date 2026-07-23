import { Routes } from '@angular/router';
import { AlunosComponent } from './pages/alunos/alunos.component';
import { CursosComponent } from './pages/cursos/cursos.component';
import { DisciplinasComponent } from './pages/disciplinas/disciplinas.component';
import { TurmasComponent } from './pages/turmas/turmas.component';
import { MatriculasComponent } from './pages/matriculas/matriculas.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'matriculas' },
  { path: 'alunos', component: AlunosComponent },
  { path: 'cursos', component: CursosComponent },
  { path: 'disciplinas', component: DisciplinasComponent },
  { path: 'turmas', component: TurmasComponent },
  { path: 'matriculas', component: MatriculasComponent },
  { path: '**', redirectTo: 'matriculas' }
];
