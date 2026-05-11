package com.moa.dto.party.response;

import java.time.LocalDate;

import com.moa.common.util.ProductImageUrlResolver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyListResponse {

	private Integer partyId;
	private String partyStatus;
	private Integer maxMembers;
	private Integer currentMembers;
	private Integer monthlyFee;
	private LocalDate regDate;
	private LocalDate startDate;
	private LocalDate endDate;

	private Integer productId;
	private String productName;
	private String productImage;

	public String getProductLogoUrl() {
		return ProductImageUrlResolver.logoUrl(productImage);
	}

	public String getProductThumbnailUrl() {
		return ProductImageUrlResolver.thumbnailUrl(productImage);
	}

	public String getProductIconUrl() {
		return ProductImageUrlResolver.iconUrl(productImage);
	}

	private String partyLeaderId;
	private String leaderNickname;

	private Integer remainingSlots;
}
