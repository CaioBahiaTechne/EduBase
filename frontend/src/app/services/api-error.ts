import { HttpErrorResponse } from '@angular/common/http';
import { ApiErrorBody } from '../models/edubase.models';

export function mensagemErroApi(err: unknown, fallback = 'Falha na requisição'): string {
  if (!(err instanceof HttpErrorResponse)) {
    return fallback;
  }
  const body = err.error as ApiErrorBody | string | null;
  if (body && typeof body === 'object' && body.message) {
    return body.message;
  }
  if (typeof body === 'string' && body.trim()) {
    return body;
  }
  return fallback;
}
