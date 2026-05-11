package com.moa.dto.product;

import com.moa.domain.Product;
import com.moa.common.util.ProductImageUrlResolver;
import lombok.Data;

@Data
public class ProductDTO {
    private int productId;
    private int categoryId;
    private String productName;
    private String productStatus;
    private int price;
    private String image;
    private String categoryName;

    public String getLogoUrl() {
        return ProductImageUrlResolver.logoUrl(image);
    }

    public String getThumbnailUrl() {
        return ProductImageUrlResolver.thumbnailUrl(image);
    }

    public String getIconUrl() {
        return ProductImageUrlResolver.iconUrl(image);
    }

    public Product toEntity() {
        Product product = new Product();
        product.setProductId(this.productId);
        product.setCategoryId(this.categoryId);
        product.setProductName(this.productName);
        product.setProductStatus(this.productStatus);
        product.setPrice(this.price);
        product.setImage(this.image);
        product.setCategoryName(this.categoryName);
        return product;
    }

    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setCategoryId(product.getCategoryId());
        dto.setProductName(product.getProductName());
        dto.setProductStatus(product.getProductStatus());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());
        dto.setCategoryName(product.getCategoryName());
        return dto;
    }
}
