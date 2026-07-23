package com.edubase.academico.infrastructure.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS para o SPA Angular em :4200 quando chama a API em :8080 diretamente.
 * Em desenvolvimento preferir o proxy do {@code ng serve} ({@code /api} → :8080),
 * que evita preflight; este filtro cobre acesso cross-origin e produção local.
 */
@Configuration
public class CorsConfig {

	@Value("${edubase.cors.allowed-origins:http://localhost:4200,http://127.0.0.1:4200}")
	private String allowedOriginsProperty;

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(parseOrigins(allowedOriginsProperty));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("Location"));
		// Sem cookies/JWT: credentials=false evita falhas com Origin + wildcard headers
		config.setAllowCredentials(false);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config);
		return new CorsFilter(source);
	}

	private static List<String> parseOrigins(String raw) {
		return Arrays.stream(raw.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList();
	}
}
