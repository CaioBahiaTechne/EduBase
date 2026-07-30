/**
 * Proxy do ng serve: /api → backend :8080.
 * Remove Origin para o Spring não tratar a chamada proxied como CORS
 * (evita 403 Invalid CORS request com Origin null / preview / WSL).
 */
module.exports = {
  '/api': {
    target: 'http://localhost:8080',
    secure: false,
    changeOrigin: true,
    logLevel: 'debug',
    onProxyReq: (proxyReq) => {
      proxyReq.removeHeader('origin');
    }
  }
};
