package com.edubase.academico.infrastructure.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS para o SPA Angular (:4200) e chamadas diretas à API (:8080).
 * Usa {@code allowedOriginPatterns} (não {@code allowedOrigins}) para aceitar
 * localhost / 127.0.0.1 / [::1] em qualquer porta e evitar 403 "Invalid CORS request"
 * com Origin inesperada (WSL, preview da IDE, ferramentas HTTP).
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Value("${edubase.cors.allowed-origin-patterns:*}")
	private String allowedOriginPatternsProperty;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOriginPatterns(parsePatterns(allowedOriginPatternsProperty))
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
				.allowedHeaders("*")
				.exposedHeaders("Location")
				.allowCredentials(false)
				.maxAge(3600);
	}

	private static String[] parsePatterns(String raw) {
		List<String> patterns = Arrays.stream(raw.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList();
		return patterns.toArray(String[]::new);
	}
}
