/**
 * Ambiente de produção.
 * Em deploy real, aponte `apiUrl` para a URL pública da API se o SPA e a API
 * não forem servidos no mesmo host (e mantenha CORS liberado no backend).
 */
export const environment = {
  production: true,
  apiUrl: '/api'
};
