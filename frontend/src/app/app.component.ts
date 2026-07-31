import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';
import { ToastContainerComponent } from './shared/toast-container/toast-container.component';
import { ConfirmDialogComponent } from './shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    ToastContainerComponent,
    ConfirmDialogComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'EduBase';
  apiStatus = signal('...');

  readonly links = [
    { path: '/matriculas', label: 'Matrículas' },
    { path: '/alunos', label: 'Alunos' },
    { path: '/cursos', label: 'Cursos' },
    { path: '/disciplinas', label: 'Disciplinas' },
    { path: '/turmas', label: 'Turmas' }
  ];

  constructor(private readonly http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<{ status: string }>(`${environment.apiUrl}/health`).subscribe({
      next: (res) => this.apiStatus.set(res.status),
      error: () => this.apiStatus.set('offline')
    });
  }
}
