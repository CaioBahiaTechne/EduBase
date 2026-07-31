import { Injectable, signal } from '@angular/core';

export type ToastKind = 'error' | 'success';

export interface Toast {
  id: number;
  kind: ToastKind;
  message: string;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private nextId = 1;
  private readonly _toasts = signal<Toast[]>([]);
  readonly toasts = this._toasts.asReadonly();

  error(message: string, durationMs = 5200): void {
    this.push('error', message, durationMs);
  }

  success(message: string, durationMs = 3800): void {
    this.push('success', message, durationMs);
  }

  dismiss(id: number): void {
    this._toasts.update((list) => list.filter((t) => t.id !== id));
  }

  private push(kind: ToastKind, message: string, durationMs: number): void {
    const id = this.nextId++;
    this._toasts.update((list) => [...list, { id, kind, message }]);
    window.setTimeout(() => this.dismiss(id), durationMs);
  }
}
