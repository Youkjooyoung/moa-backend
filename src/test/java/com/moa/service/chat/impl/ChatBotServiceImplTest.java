package com.moa.service.chat.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.moa.dao.chat.ChatKnowledgeDao;
import com.moa.domain.ChatKnowledge;
import com.moa.dto.chat.request.ChatRequest;
import com.moa.dto.chat.response.ChatResponse;
import com.moa.service.chat.ChatAnswerGenerator;

class ChatBotServiceImplTest {

	private final ChatKnowledgeDao chatKnowledgeDao = mock(ChatKnowledgeDao.class);
	private final ChatAnswerGenerator generator = (question, route, knowledge, fallback) -> Optional.empty();
	private final ChatBotServiceImpl service = new ChatBotServiceImpl(chatKnowledgeDao, new ChatRoutingServiceImpl(),
			generator);

	@Test
	void usesProjectSpecificPasswordFallbackWhenKnowledgeIsMissing() {
		when(chatKnowledgeDao.searchByKeyword(anyString())).thenReturn(List.of());

		ChatResponse response = service.chat(request("\uBE44\uBC00\uBC88\uD638\uB97C \uC783\uC5B4\uBC84\uB838\uC5B4\uC694"));

		assertThat(response.getCategory()).isEqualTo("PASSWORD");
		assertThat(response.getReply()).contains("\uBE44\uBC00\uBC88\uD638");
		assertThat(response.isFromKnowledge()).isFalse();
	}

	@Test
	void prefersKnowledgeAnswerBeforeFallback() {
		ChatKnowledge knowledge = new ChatKnowledge();
		knowledge.setId(7L);
		knowledge.setCategory("PASSWORD");
		knowledge.setTitle("\uBE44\uBC00\uBC88\uD638 \uC7AC\uC124\uC815");
		knowledge.setQuestion("\uBE44\uBC00\uBC88\uD638\uB97C \uC78A\uC5C8\uC744 \uB54C");
		knowledge.setAnswer("\uB85C\uADF8\uC778 \uD654\uBA74\uC5D0\uC11C \uBE44\uBC00\uBC88\uD638 \uC7AC\uC124\uC815\uC744 \uC120\uD0DD\uD574 \uC0C8 \uBE44\uBC00\uBC88\uD638\uB97C \uC124\uC815\uD558\uC138\uC694.");
		knowledge.setKeywords("\uBE44\uBC00\uBC88\uD638,\uC7AC\uC124\uC815,\uC7A0\uAE08\uACC4\uC815");
		when(chatKnowledgeDao.searchByKeyword(anyString())).thenReturn(List.of(knowledge));

		ChatResponse response = service.chat(request("\uBE44\uBC00\uBC88\uD638\uB97C \uC783\uC5B4\uBC84\uB838\uC5B4\uC694"));

		assertThat(response.isFromKnowledge()).isTrue();
		assertThat(response.getKnowledgeId()).isEqualTo(7L);
		assertThat(response.getReply()).contains("\uBE44\uBC00\uBC88\uD638 \uC7AC\uC124\uC815");
	}

	private ChatRequest request(String message) {
		ChatRequest request = new ChatRequest();
		request.setMessage(message);
		return request;
	}
}
