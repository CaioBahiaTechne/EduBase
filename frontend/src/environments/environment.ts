/**
 * Ambiente de desenvolvimento.
 * apiUrl relativo usa o proxy do `ng serve` (proxy.conf.json → http://localhost:8080),
 * evitando CORS no browser.
 */
export const environment = {
  production: false,
  /** Prefixo dos endpoints REST (mesmo contrato do backend `/api`). */
  apiUrl: '/api'
};
