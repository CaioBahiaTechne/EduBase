import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'EduBase';
  apiStatus = signal('verificando...');

  constructor(private readonly http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<{ status: string }>(`${environment.apiUrl}/health`).subscribe({
      next: (res) => this.apiStatus.set(res.status),
      error: () => this.apiStatus.set('API indisponível (subir o backend em :8080)')
    });
  }
}
