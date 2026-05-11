package com.moa.common.util;

public final class ProductImageUrlResolver {

    private ProductImageUrlResolver() {
    }

    public static String logoUrl(String imageUrl) {
        return imageUrl;
    }

    public static String thumbnailUrl(String imageUrl) {
        return imageUrl;
    }

    public static String iconUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return imageUrl;
        }
        return imageUrl
                .replaceAll("(?i)_logo\\.", "_icon.")
                .replaceAll("(?i)logo\\.", "icon.");
    }
}
