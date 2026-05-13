package com.moa.service.chat.impl;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.moa.domain.ChatRoute;
import com.moa.service.chat.ChatRoutingService;

@Service
public class ChatRoutingServiceImpl implements ChatRoutingService {

	private static final String CATEGORY_SIGNUP = "SIGNUP";
	private static final String CATEGORY_LOGIN = "LOGIN";
	private static final String CATEGORY_PASSWORD = "PASSWORD";
	private static final String CATEGORY_SOCIAL = "SOCIAL";
	private static final String CATEGORY_SUBSCRIPTION = "SUBSCRIPTION";
	private static final String CATEGORY_PAYMENT = "PAYMENT";
	private static final String CATEGORY_PARTY = "PARTY";
	private static final String CATEGORY_ACCOUNT = "ACCOUNT";
	private static final String CATEGORY_SETTLEMENT = "SETTLEMENT";
	private static final String CATEGORY_NOTIFICATION = "NOTIFICATION";
	private static final String CATEGORY_SUPPORT = "SUPPORT";
	private static final String CATEGORY_GENERAL = "GENERAL";

	@Override
	public ChatRoute route(String text) {
		if (text == null || text.isBlank()) {
			return new ChatRoute(CATEGORY_GENERAL, "");
		}

		String normalized = normalize(text);

		if (contains(normalized, "공지", "공지사항", "faq", "고객센터", "문의", "1:1", "도움말", "지원")) {
			return new ChatRoute(CATEGORY_SUPPORT, "공지사항 FAQ 고객센터");
		}

		if (contains(normalized, "비밀번호", "비번", "패스워드", "password", "pwd", "잠금", "재설정", "잊어", "찾기")) {
			return new ChatRoute(CATEGORY_PASSWORD, "비밀번호 재설정");
		}

		if (contains(normalized, "회원가입", "가입", "계정생성", "이메일인증", "인증메일")) {
			return new ChatRoute(CATEGORY_SIGNUP, "회원가입");
		}

		if (contains(normalized, "로그인", "로그아웃", "접속", "세션")) {
			return new ChatRoute(CATEGORY_LOGIN, "로그인");
		}

		if (contains(normalized, "소셜", "간편", "구글", "카카오", "google", "kakao", "oauth", "연결", "해제")) {
			return new ChatRoute(CATEGORY_SOCIAL, "소셜 계정 연결");
		}

		if (contains(normalized, "구독", "상품", "ott", "chatgpt", "googleai", "넷플릭스", "디즈니", "티빙",
				"웨이브", "네이버플러스", "신청")) {
			return new ChatRoute(CATEGORY_SUBSCRIPTION, "구독 상품");
		}

		if (contains(normalized, "결제", "카드", "빌링", "자동결제", "portone", "결제수단", "실패", "환불")) {
			return new ChatRoute(CATEGORY_PAYMENT, "결제 수단");
		}

		if (contains(normalized, "정산", "계좌", "은행", "예금주", "보증금", "입금", "출금", "지갑")) {
			return new ChatRoute(CATEGORY_SETTLEMENT, "정산 계좌");
		}

		if (contains(normalized, "파티", "모집", "참여", "파티장", "가입", "인원", "시작일")) {
			return new ChatRoute(CATEGORY_PARTY, "파티 찾기");
		}

		if (contains(normalized, "마이페이지", "내정보", "닉네임", "연락처", "휴대폰", "otp", "보안", "탈퇴",
				"마케팅", "알림수신")) {
			return new ChatRoute(CATEGORY_ACCOUNT, "계정 정보");
		}

		if (contains(normalized, "알림", "푸시", "혜택", "수신동의", "마케팅동의")) {
			return new ChatRoute(CATEGORY_NOTIFICATION, "알림 설정");
		}

		return new ChatRoute(CATEGORY_GENERAL, text.trim());
	}

	private String normalize(String text) {
		return text.toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
	}

	private boolean contains(String text, String... keywords) {
		for (String keyword : keywords) {
			if (text.contains(normalize(keyword))) {
				return true;
			}
		}
		return false;
	}
}
