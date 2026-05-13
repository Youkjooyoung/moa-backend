package com.moa.service.chat.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.moa.domain.ChatKnowledge;
import com.moa.domain.ChatRoute;
import com.moa.service.chat.ChatAnswerGenerator;

@Service
public class OpenAiChatAnswerGenerator implements ChatAnswerGenerator {

	private static final Logger log = LoggerFactory.getLogger(OpenAiChatAnswerGenerator.class);

	private final WebClient webClient;
	private final String apiKey;
	private final String model;
	private final boolean enabled;
	private final int maxOutputTokens;
	private final Duration timeout;

	public OpenAiChatAnswerGenerator(WebClient.Builder webClientBuilder,
			@Value("${openai.api-key:${OPENAI_API_KEY:}}") String apiKey,
			@Value("${openai.api-base-url:https://api.openai.com/v1}") String baseUrl,
			@Value("${openai.chat.model:gpt-4o-mini}") String model,
			@Value("${openai.chat.enabled:true}") boolean enabled,
			@Value("${openai.chat.max-output-tokens:500}") int maxOutputTokens,
			@Value("${openai.chat.timeout-seconds:5}") long timeoutSeconds) {
		this.webClient = webClientBuilder.baseUrl(baseUrl).build();
		this.apiKey = apiKey == null ? "" : apiKey.trim().replace("\r", "").replace("\n", "");
		this.model = model;
		this.enabled = enabled;
		this.maxOutputTokens = maxOutputTokens;
		this.timeout = Duration.ofSeconds(timeoutSeconds);
	}

	@Override
	public Optional<String> generate(String question, ChatRoute route, List<ChatKnowledge> knowledge, String fallback) {
		if (!enabled || apiKey.isBlank()) {
			return Optional.empty();
		}

		Map<String, Object> request = Map.of(
				"model", model,
				"instructions", instructions(),
				"input", buildInput(question, route, knowledge, fallback),
				"temperature", 0.2,
				"max_output_tokens", maxOutputTokens,
				"store", false);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> response = webClient.post()
					.uri("/responses")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(request)
					.retrieve()
					.bodyToMono(Map.class)
					.block(timeout);

			return Optional.ofNullable(extractText(response))
					.map(String::trim)
					.filter(value -> !value.isBlank());
		} catch (RuntimeException ex) {
			log.warn("[chatbot] OpenAI answer generation failed. category={}, reason={}", route.category(),
					ex.getMessage());
			return Optional.empty();
		}
	}

	private String instructions() {
		return """
				You are MOA's in-product support assistant.
				Answer in the same language as the user's question.
				Use only the MOA context provided in the input. If the context is insufficient, say what can be checked in MOA instead of inventing facts.
				Never ask for passwords, card numbers, OTP codes, access tokens, API keys, or resident registration numbers.
				Keep answers concise, practical, and written like a real customer support assistant.
				For account, payment, settlement, and party flows, give the exact MOA menu path when available.
				""";
	}

	private String buildInput(String question, ChatRoute route, List<ChatKnowledge> knowledge, String fallback) {
		StringBuilder builder = new StringBuilder();
		builder.append("User question:\n").append(question).append("\n\n");
		builder.append("Detected MOA category: ").append(route.category()).append("\n");
		builder.append("Search keyword: ").append(route.keyword()).append("\n\n");
		builder.append("MOA verified context:\n");

		if (knowledge == null || knowledge.isEmpty()) {
			builder.append("- ").append(fallback).append("\n");
		} else {
			int count = 0;
			for (ChatKnowledge item : knowledge) {
				if (item == null || item.getAnswer() == null || item.getAnswer().isBlank()) {
					continue;
				}
				builder.append("- [").append(nullToBlank(item.getCategory())).append("] ")
						.append(nullToBlank(item.getTitle())).append("\n")
						.append("  Q: ").append(nullToBlank(item.getQuestion())).append("\n")
						.append("  A: ").append(item.getAnswer().trim()).append("\n");
				count++;
				if (count >= 5) {
					break;
				}
			}
			builder.append("- Safe fallback: ").append(fallback).append("\n");
		}

		builder.append("\nWrite the final answer for the user.");
		return builder.toString();
	}

	private String extractText(Map<String, Object> response) {
		if (response == null) {
			return null;
		}
		Object outputText = response.get("output_text");
		if (outputText instanceof String text && !text.isBlank()) {
			return text;
		}

		Object output = response.get("output");
		if (!(output instanceof List<?> outputItems)) {
			return null;
		}

		List<String> parts = new ArrayList<>();
		for (Object outputItem : outputItems) {
			if (!(outputItem instanceof Map<?, ?> outputMap)) {
				continue;
			}
			Object content = outputMap.get("content");
			if (!(content instanceof List<?> contentItems)) {
				continue;
			}
			for (Object contentItem : contentItems) {
				if (!(contentItem instanceof Map<?, ?> contentMap)) {
					continue;
				}
				Object text = contentMap.get("text");
				if (text instanceof String value && !value.isBlank()) {
					parts.add(value);
				}
			}
		}
		return parts.isEmpty() ? null : String.join("\n", parts);
	}

	private String nullToBlank(String value) {
		return value == null ? "" : value;
	}
}
