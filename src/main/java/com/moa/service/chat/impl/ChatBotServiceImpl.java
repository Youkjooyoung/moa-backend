package com.moa.service.chat.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moa.common.prompt.ChatFallbackReplies;
import com.moa.dao.chat.ChatKnowledgeDao;
import com.moa.domain.ChatKnowledge;
import com.moa.domain.ChatRoute;
import com.moa.dto.chat.request.ChatRequest;
import com.moa.dto.chat.response.ChatResponse;
import com.moa.service.chat.ChatBotService;
import com.moa.service.chat.ChatRoutingService;

@Service
public class ChatBotServiceImpl implements ChatBotService {

	private final ChatKnowledgeDao chatKnowledgeDao;
	private final ChatRoutingService chatRoutingService;

	public ChatBotServiceImpl(ChatKnowledgeDao chatKnowledgeDao, ChatRoutingService chatRoutingService) {
		this.chatKnowledgeDao = chatKnowledgeDao;
		this.chatRoutingService = chatRoutingService;
	}

	@Override
	public ChatResponse chat(ChatRequest request) {
		String text = request.getMessage() == null ? "" : request.getMessage().trim();

		ChatRoute route = chatRoutingService.route(text);

		String keyword = route.keyword();
		if (keyword == null || keyword.isBlank()) {
			keyword = text;
		}

		List<ChatKnowledge> hits = chatKnowledgeDao.searchByKeyword(keyword);

		if (hits == null || hits.isEmpty()) {
			String fallback = ChatFallbackReplies.fallback(route.category());
			return ChatResponse.builder().reply(fallback).fromKnowledge(false).category(route.category())
					.knowledgeId(null).build();
		}

		ChatKnowledge top = hits.get(0);
		String answer = top.getAnswer();
		if (!isUsableAnswer(answer)) {
			String fallback = ChatFallbackReplies.fallback(route.category());
			return ChatResponse.builder().reply(fallback).fromKnowledge(false).category(route.category())
					.knowledgeId(top.getId()).build();
		}

		return ChatResponse.builder().reply(answer.trim()).fromKnowledge(true).category(top.getCategory())
				.knowledgeId(top.getId()).build();
	}

	private boolean isUsableAnswer(String answer) {
		if (answer == null || answer.isBlank()) {
			return false;
		}

		String value = answer.trim();
		if (value.length() < 8) {
			return false;
		}

		return !(value.contains("�") || value.contains("æ") || value.contains("ì") || value.contains("ë")
				|| value.contains("Â") || value.contains("Ñ") || value.contains("å¯"));
	}
}
