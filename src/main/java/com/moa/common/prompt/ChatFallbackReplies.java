package com.moa.common.prompt;

import java.util.List;
import java.util.Map;

public final class ChatFallbackReplies {

	private static final List<String> DEFAULT_REPLIES = List.of(
			"MOA 이용 중 궁금한 내용을 조금 더 구체적으로 입력해 주세요. 예: 비밀번호 재설정, 구독 상품 신청, 파티 참여, 결제수단 등록, 정산 계좌 등록, 공지사항 확인");

	private static final Map<String, List<String>> CATEGORY_REPLIES = Map.ofEntries(
			Map.entry("SIGNUP", List.of("회원가입은 이메일, 닉네임, 비밀번호를 입력한 뒤 이메일 인증을 완료하면 진행됩니다.")),
			Map.entry("LOGIN", List.of("로그인은 이메일/비밀번호 또는 연결된 소셜 계정으로 진행합니다. 로그인에 실패하면 비밀번호 재설정이나 잠금 계정 풀기를 먼저 확인해 주세요.")),
			Map.entry("PASSWORD", List.of("비밀번호를 잊었다면 로그인 화면에서 비밀번호 재설정 또는 잠금 계정 풀기를 이용해 주세요. 로그인한 상태라면 마이페이지에서 비밀번호를 변경할 수 있습니다. 비밀번호는 챗봇에 입력하지 마세요.")),
			Map.entry("SOCIAL", List.of("소셜 계정은 마이페이지의 보안 및 연결 영역에서 확인합니다. 연결된 Google 또는 Kakao 계정은 같은 화면에서 해제할 수 있습니다.")),
			Map.entry("SUBSCRIPTION", List.of("구독 상품은 구독 상품 화면에서 확인하고 신청할 수 있습니다. 이용 중인 구독은 마이페이지 또는 내 구독 목록에서 관리합니다.")),
			Map.entry("PAYMENT", List.of("결제수단은 마이페이지의 내 지갑 또는 구독/결제 관리에서 등록합니다. 결제가 실패하면 카드 상태, 한도, 자동결제 등록 여부를 확인해 주세요.")),
			Map.entry("SETTLEMENT", List.of("정산 계좌는 마이페이지의 내 지갑에서 등록합니다. 현재 계좌 확인은 mock 인증으로 처리되며, 입력한 은행/계좌번호/예금주 정보가 정산 계좌로 저장됩니다.")),
			Map.entry("PARTY", List.of("파티는 파티 찾기 화면에서 모집 중인 항목을 선택해 참여할 수 있습니다. 상세 화면에서 인원, 시작일, 월 부담액을 확인한 뒤 가입을 진행해 주세요.")),
			Map.entry("ACCOUNT", List.of("계정 정보는 마이페이지에서 관리합니다. 닉네임, 연락처, 마케팅 수신 설정, OTP, 소셜 계정 연결 상태를 확인할 수 있습니다.")),
			Map.entry("NOTIFICATION", List.of("알림과 마케팅 수신 여부는 마이페이지의 계정 정보 수정에서 변경할 수 있습니다.")),
			Map.entry("SUPPORT", List.of("공지사항과 FAQ는 고객센터에서 확인할 수 있습니다. 해결되지 않는 문제는 1:1 문의로 접수해 주세요.")),
			Map.entry("GENERAL", DEFAULT_REPLIES));

	public static String fallback(String category) {
		List<String> list = CATEGORY_REPLIES.get(category);
		if (list == null || list.isEmpty()) {
			list = DEFAULT_REPLIES;
		}
		return list.get(0);
	}

	private ChatFallbackReplies() {
	}
}
