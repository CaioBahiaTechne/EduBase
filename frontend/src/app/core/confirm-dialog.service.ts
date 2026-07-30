import { Injectable, signal } from '@angular/core';

export interface ConfirmOptions {
  title: string;
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
}

@Injectable({ providedIn: 'root' })
export class ConfirmDialogService {
  private resolver: ((value: boolean) => void) | null = null;
  readonly options = signal<ConfirmOptions | null>(null);

  confirm(options: ConfirmOptions): Promise<boolean> {
    if (this.resolver) {
      this.resolver(false);
      this.resolver = null;
    }
    this.options.set({
      confirmLabel: 'Confirmar',
      cancelLabel: 'Cancelar',
      ...options
    });
    return new Promise<boolean>((resolve) => {
      this.resolver = resolve;
    });
  }

  respond(accepted: boolean): void {
    const resolve = this.resolver;
    this.resolver = null;
    this.options.set(null);
    resolve?.(accepted);
  }
}
