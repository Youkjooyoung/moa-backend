package com.moa.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductImageUrlResolverTest {

    @Test
    void resolvesLogoThumbnailAndIconUrlsFromStoredImageUrl() {
        String imageUrl = "/uploads/product-image/netflix_logo.png";

        assertThat(ProductImageUrlResolver.logoUrl(imageUrl)).isEqualTo(imageUrl);
        assertThat(ProductImageUrlResolver.thumbnailUrl(imageUrl)).isEqualTo(imageUrl);
        assertThat(ProductImageUrlResolver.iconUrl(imageUrl))
                .isEqualTo("/uploads/product-image/netflix_icon.png");
    }
}
