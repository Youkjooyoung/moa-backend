package com.moa.service.chat.impl;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.moa.domain.ChatRoute;
import com.moa.service.chat.ChatRoutingService;

@Service
public class ChatRoutingServiceImpl implements ChatRoutingService {

	private static final String CATEGORY_SUBSCRIPTION = "SUBSCRIPTION";
	private static final String CATEGORY_PAYMENT = "PAYMENT";
	private static final String CATEGORY_PARTY = "PARTY";
	private static final String CATEGORY_ACCOUNT = "ACCOUNT";
	private static final String CATEGORY_NOTIFICATION = "NOTIFICATION";
	private static final String CATEGORY_SUPPORT = "SUPPORT";
	private static final String CATEGORY_GENERAL = "GENERAL";

	@Override
	public ChatRoute route(String text) {
		if (text == null || text.isBlank()) {
			return new ChatRoute(CATEGORY_GENERAL, "");
		}

		String normalized = normalize(text);

		if (contains(normalized, "구독", "상품", "ott", "넷플릭스", "디즈니", "유튜브", "티빙", "웨이브", "챗gpt", "chatgpt",
				"취소", "해지", "구독목록", "구독관리", "만료", "갱신")) {
			return new ChatRoute(CATEGORY_SUBSCRIPTION, "구독 상품 관리");
		}

		if (contains(normalized, "결제", "카드", "포트원", "portone", "입금", "정산", "환불", "요금", "금액", "청구",
				"자동결제", "결제일", "실패")) {
			return new ChatRoute(CATEGORY_PAYMENT, "결제 정산 환불");
		}

		if (contains(normalized, "파티", "모집", "참여", "파티장", "파티원", "나가기", "초대", "인원", "정원", "공유",
				"멤버", "모집중", "마감")) {
			return new ChatRoute(CATEGORY_PARTY, "파티 모집 참여");
		}

		if (contains(normalized, "로그인", "회원가입", "계정", "비밀번호", "이메일", "휴대폰", "전화번호", "프로필",
				"닉네임", "소셜", "카카오", "구글", "otp", "탈퇴", "마이페이지")) {
			return new ChatRoute(CATEGORY_ACCOUNT, "계정 로그인 회원정보");
		}

		if (contains(normalized, "알림", "푸시", "메일", "이메일인증", "인증메일", "읽지않은", "공지")) {
			return new ChatRoute(CATEGORY_NOTIFICATION, "알림 이메일 인증");
		}

		if (contains(normalized, "오류", "에러", "안돼", "안되", "문의", "고객센터", "버그", "장애", "문제", "도움",
				"신고", "관리자")) {
			return new ChatRoute(CATEGORY_SUPPORT, "오류 문의 고객지원");
		}

		return new ChatRoute(CATEGORY_GENERAL, text.trim());
	}

	private String normalize(String text) {
		return text.toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
	}

	private boolean contains(String text, String... keywords) {
		for (String keyword : keywords) {
			if (text.contains(keyword.toLowerCase(Locale.ROOT).replaceAll("\\s+", ""))) {
				return true;
			}
		}
		return false;
	}
}
