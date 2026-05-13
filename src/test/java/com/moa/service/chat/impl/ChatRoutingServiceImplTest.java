package com.moa.service.chat.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.moa.domain.ChatRoute;

class ChatRoutingServiceImplTest {

	private final ChatRoutingServiceImpl service = new ChatRoutingServiceImpl();

	@Test
	void routesPasswordResetQuestionToPasswordCategory() {
		ChatRoute route = service.route("\uBE44\uBC00\uBC88\uD638\uB97C \uC783\uC5B4\uBC84\uB838\uC5B4\uC694");

		assertThat(route.category()).isEqualTo("PASSWORD");
		assertThat(route.keyword()).isEqualTo("\uBE44\uBC00\uBC88\uD638 \uC7AC\uC124\uC815");
	}

	@Test
	void routesSettlementAccountQuestionToSettlementCategory() {
		ChatRoute route = service.route("\uC815\uC0B0 \uACC4\uC88C\uB294 \uC5B4\uB514\uC11C \uB4F1\uB85D\uD574?");

		assertThat(route.category()).isEqualTo("SETTLEMENT");
		assertThat(route.keyword()).isEqualTo("\uC815\uC0B0 \uACC4\uC88C");
	}

	@Test
	void routesNoticeQuestionToSupportCategory() {
		ChatRoute route = service.route("\uACF5\uC9C0\uC0AC\uD56D\uC740 \uC5B4\uB514\uC5D0 \uC788\uC5B4?");

		assertThat(route.category()).isEqualTo("SUPPORT");
		assertThat(route.keyword()).isEqualTo("\uACF5\uC9C0\uC0AC\uD56D FAQ \uACE0\uAC1D\uC13C\uD130");
	}
}
