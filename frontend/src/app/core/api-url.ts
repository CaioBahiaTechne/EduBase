import { environment } from '../../environments/environment';

/** Monta URL absoluta da API a partir do prefixo em `environment.apiUrl`. */
export function apiUrl(...segments: (string | number)[]): string {
  const base = environment.apiUrl.replace(/\/+$/, '');
  const path = segments
    .map((s) => String(s).replace(/^\/+|\/+$/g, ''))
    .filter((s) => s.length > 0)
    .join('/');
  return path ? `${base}/${path}` : base;
}
