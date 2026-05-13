package com.moa.service.chat.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.moa.common.prompt.ChatFallbackReplies;
import com.moa.dao.chat.ChatKnowledgeDao;
import com.moa.domain.ChatKnowledge;
import com.moa.domain.ChatRoute;
import com.moa.dto.chat.request.ChatRequest;
import com.moa.dto.chat.response.ChatResponse;
import com.moa.service.chat.ChatAnswerGenerator;
import com.moa.service.chat.ChatBotService;
import com.moa.service.chat.ChatRoutingService;

@Service
public class ChatBotServiceImpl implements ChatBotService {

	private final ChatKnowledgeDao chatKnowledgeDao;
	private final ChatRoutingService chatRoutingService;
	private final ChatAnswerGenerator chatAnswerGenerator;

	public ChatBotServiceImpl(ChatKnowledgeDao chatKnowledgeDao, ChatRoutingService chatRoutingService,
			ChatAnswerGenerator chatAnswerGenerator) {
		this.chatKnowledgeDao = chatKnowledgeDao;
		this.chatRoutingService = chatRoutingService;
		this.chatAnswerGenerator = chatAnswerGenerator;
	}

	@Override
	public ChatResponse chat(ChatRequest request) {
		String text = request.getMessage() == null ? "" : request.getMessage().trim();
		ChatRoute route = chatRoutingService.route(text);
		List<ChatKnowledge> candidates = findKnowledgeCandidates(text, route);
		ChatKnowledge top = findBestKnowledge(candidates, text, route);
		String fallback = ChatFallbackReplies.fallback(route.category());

		return chatAnswerGenerator.generate(text, route, candidates, fallback)
				.map(reply -> ChatResponse.builder().reply(reply).fromKnowledge(!candidates.isEmpty())
						.category(route.category()).knowledgeId(top == null ? null : top.getId()).build())
				.orElseGet(() -> fallbackResponse(route, top, fallback));
	}

	private ChatResponse fallbackResponse(ChatRoute route, ChatKnowledge top, String fallback) {
		if (top != null && isUsableAnswer(top.getAnswer())) {
			return ChatResponse.builder().reply(top.getAnswer().trim()).fromKnowledge(true).category(top.getCategory())
					.knowledgeId(top.getId()).build();
		}

		return ChatResponse.builder().reply(fallback).fromKnowledge(false).category(route.category())
				.knowledgeId(top == null ? null : top.getId()).build();
	}

	private List<ChatKnowledge> findKnowledgeCandidates(String text, ChatRoute route) {
		Map<Long, ChatKnowledge> candidates = new LinkedHashMap<>();
		for (String keyword : searchKeywords(text, route)) {
			List<ChatKnowledge> hits = chatKnowledgeDao.searchByKeyword(keyword);
			if (hits == null) {
				continue;
			}
			for (ChatKnowledge hit : hits) {
				if (hit != null && hit.getId() != null) {
					candidates.putIfAbsent(hit.getId(), hit);
				}
			}
		}

		return new ArrayList<>(candidates.values());
	}

	private ChatKnowledge findBestKnowledge(List<ChatKnowledge> candidates, String text, ChatRoute route) {
		return candidates.stream().filter(hit -> isUsableAnswer(hit.getAnswer()))
				.max((left, right) -> Integer.compare(score(left, text, route), score(right, text, route)))
				.orElse(null);
	}

	private List<String> searchKeywords(String text, ChatRoute route) {
		List<String> keywords = new ArrayList<>();
		addKeyword(keywords, route.keyword());
		addKeyword(keywords, text);

		switch (route.category()) {
		case "PASSWORD" -> {
			addKeyword(keywords, "비밀번호");
			addKeyword(keywords, "비밀번호 재설정");
			addKeyword(keywords, "잠금 계정");
		}
		case "SETTLEMENT" -> {
			addKeyword(keywords, "정산 계좌");
			addKeyword(keywords, "계좌 등록");
			addKeyword(keywords, "내 지갑");
		}
		case "SUBSCRIPTION" -> {
			addKeyword(keywords, "구독 상품");
			addKeyword(keywords, "내 구독");
		}
		case "PARTY" -> {
			addKeyword(keywords, "파티 찾기");
			addKeyword(keywords, "파티 참여");
		}
		case "SUPPORT" -> {
			addKeyword(keywords, "공지사항");
			addKeyword(keywords, "FAQ");
			addKeyword(keywords, "고객센터");
			addKeyword(keywords, "1:1 문의");
		}
		default -> {
			// The route keyword and raw user text are enough for broad categories.
		}
		}

		return keywords;
	}

	private void addKeyword(List<String> keywords, String value) {
		if (value == null || value.isBlank()) {
			return;
		}
		String keyword = value.trim();
		if (!keywords.contains(keyword)) {
			keywords.add(keyword);
		}
	}

	private int score(ChatKnowledge knowledge, String text, ChatRoute route) {
		String haystack = normalize(String.join(" ", safe(knowledge.getCategory()), safe(knowledge.getTitle()),
				safe(knowledge.getQuestion()), safe(knowledge.getKeywords()), safe(knowledge.getAnswer())));
		int score = 0;
		if (safe(knowledge.getCategory()).equalsIgnoreCase(route.category())) {
			score += 20;
		}
		for (String token : importantTokens(text, route.keyword())) {
			if (haystack.contains(token)) {
				score += token.length() >= 4 ? 6 : 3;
			}
		}
		return score;
	}

	private List<String> importantTokens(String text, String keyword) {
		String combined = (safe(text) + " " + safe(keyword)).toLowerCase(Locale.ROOT);
		List<String> tokens = new ArrayList<>();
		for (String token : combined.split("[\\s,./?]+")) {
			String normalized = normalize(token);
			if (normalized.length() >= 2 && !tokens.contains(normalized)) {
				tokens.add(normalized);
			}
		}
		return tokens;
	}

	private String normalize(String value) {
		return safe(value).toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
	}

	private String safe(String value) {
		return value == null ? "" : value;
	}

	private boolean isUsableAnswer(String answer) {
		if (answer == null || answer.isBlank()) {
			return false;
		}

		String value = answer.trim();
		if (value.length() < 8) {
			return false;
		}

		return !(value.contains("확인 중") || value.contains("준비 중") || value.contains("개발자"));
	}
}
