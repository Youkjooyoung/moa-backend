package com.moa.service.chat;

import java.util.List;
import java.util.Optional;

import com.moa.domain.ChatKnowledge;
import com.moa.domain.ChatRoute;

public interface ChatAnswerGenerator {

	Optional<String> generate(String question, ChatRoute route, List<ChatKnowledge> knowledge, String fallback);
}
