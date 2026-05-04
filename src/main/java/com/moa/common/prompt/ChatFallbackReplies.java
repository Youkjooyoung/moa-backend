package com.moa.common.prompt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class ChatFallbackReplies {

	private static final List<String> DEFAULT_REPLIES = List.of(
			"MOA에서 도와드릴 수 있는 주제는 구독 상품, 파티 찾기, 결제/정산, 계정 설정입니다. 예를 들어 '구독 취소는 어떻게 해?'처럼 물어보시면 더 정확히 안내할게요.",
			"질문을 조금만 더 구체적으로 적어주세요. 구독, 파티, 결제, 계정 중 어떤 부분인지 알려주시면 바로 이어서 설명드릴게요.",
			"현재 챗봇은 MOA 서비스 이용 안내를 중심으로 답변합니다. 상품 추천, 파티 참여 방법, 결제 오류, 회원정보 수정 같은 질문을 해보세요.");

	private static final Map<String, List<String>> CATEGORY_REPLIES = Map.of(
			"SUBSCRIPTION",
			List.of(
					"구독 상품은 상단의 '구독상품' 메뉴에서 확인할 수 있습니다. 상품 카드의 '상세보기'로 혜택과 금액을 확인한 뒤 '구독신청'을 누르면 내 구독 일정에 등록됩니다.",
					"이미 등록한 구독은 '내 구독 목록'에서 확인할 수 있습니다. 종료일을 비워두면 자동 갱신 상태로 관리되고, 필요 시 수정 또는 해지 흐름으로 관리하면 됩니다.",
					"상품 이미지나 가격이 이상하면 새로고침 후 다시 확인해 주세요. 계속 다르면 관리자에게 상품명과 화면 캡처를 전달하는 것이 가장 빠릅니다."),
			"PAYMENT",
			List.of(
					"결제는 등록된 구독 일정과 결제일 기준으로 처리됩니다. 결제 실패가 발생하면 카드 정보, 잔액, 인증 상태를 먼저 확인해 주세요.",
					"환불이나 정산 문제는 결제 내역과 파티 상태를 함께 확인해야 합니다. 문제가 된 상품명, 결제일, 금액을 알려주시면 확인 순서를 안내할게요.",
					"포트원 결제 오류는 API 키/시크릿, 고객사 식별코드, 결제 요청 금액, 리다이렉트 주소가 맞는지 확인해야 합니다."),
			"PARTY",
			List.of(
					"파티는 상단 '파티 찾기'에서 모집 중인 항목을 확인할 수 있습니다. 원하는 OTT와 가격, 모집 인원을 확인한 뒤 참여하면 됩니다.",
					"파티 참여 전에는 파티장, 모집 인원, 월 구독료, 시작일을 확인하세요. 이미 참여한 파티는 마이페이지의 '내 파티 목록'에서 확인할 수 있습니다.",
					"파티 생성이나 참여가 되지 않으면 로그인 상태, 모집 마감 여부, 이미 참여한 파티인지 먼저 확인해 주세요."),
			"ACCOUNT",
			List.of(
					"회원정보는 마이페이지에서 수정할 수 있습니다. 프로필 이미지, 닉네임, 휴대폰 번호, 마케팅 수신 동의 등을 관리할 수 있습니다.",
					"소셜 로그인은 구글과 카카오를 지원합니다. 계정 연결/해제는 마이페이지의 '소셜 연결' 영역에서 관리할 수 있습니다.",
					"로그인이 풀린 것처럼 보이면 새로고침 후 다시 확인해 주세요. 헤더에는 로그인 상태인데 보호 페이지가 로그인으로 이동하면 세션 갱신 문제일 수 있습니다."),
			"NOTIFICATION",
			List.of(
					"알림은 결제, 파티, 계정 관련 주요 이벤트를 알려주기 위한 기능입니다. 읽지 않은 알림 수는 상단 알림 아이콘에서 확인할 수 있습니다.",
					"이메일 인증 메일이 오지 않으면 스팸함을 확인하고, 입력한 이메일 주소가 정확한지 확인해 주세요.",
					"푸시 알림이 보이지 않으면 브라우저 권한과 로그인 상태를 먼저 확인하는 것이 좋습니다."),
			"SUPPORT",
			List.of(
					"오류가 발생했다면 어떤 메뉴에서, 어떤 버튼을 눌렀는지, 화면에 나온 메시지를 함께 확인해야 합니다. 가능하면 오류 화면과 시간을 같이 남겨주세요.",
					"500 오류는 서버 처리 중 문제가 발생했다는 뜻입니다. 같은 동작을 다시 시도해도 반복되면 관리자 확인이 필요합니다.",
					"요청이 실패하면 먼저 새로고침, 재로그인, 네트워크 상태를 확인해 주세요. 그래도 반복되면 오류가 난 API 주소와 화면을 알려주세요."),
			"GENERAL",
			DEFAULT_REPLIES);

	public static String fallback(String category) {
		List<String> list = CATEGORY_REPLIES.get(category);
		if (list == null || list.isEmpty()) {
			list = DEFAULT_REPLIES;
		}
		return random(list);
	}

	private static String random(List<String> list) {
		int index = ThreadLocalRandom.current().nextInt(list.size());
		return list.get(index);
	}

	private ChatFallbackReplies() {
	}
}
