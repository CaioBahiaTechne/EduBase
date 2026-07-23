import { HttpErrorResponse } from '@angular/common/http';
import { ApiErrorBody } from '../models/edubase.models';

/** Extrai mensagem amigável do corpo de erro padronizado do backend (GlobalExceptionHandler). */
export function mensagemErroApi(err: unknown, fallback = 'Falha na requisição'): string {
  if (!(err instanceof HttpErrorResponse)) {
    return fallback;
  }

  if (err.status === 0) {
    return 'Não foi possível conectar à API. Verifique se o backend está em http://localhost:8080 e se o proxy `/api` está ativo (`npm start`).';
  }

  const body = err.error as ApiErrorBody | string | null;
  if (body && typeof body === 'object' && body.message) {
    return body.message;
  }
  if (typeof body === 'string' && body.trim()) {
    return body;
  }
  if (err.status === 404) {
    return 'Recurso não encontrado';
  }
  if (err.status === 422) {
    return 'Regra de negócio impediu a operação';
  }
  return fallback;
}
