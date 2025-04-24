package kr.hhplus.be.server.domain.product;

import static kr.hhplus.be.server.domain.product.ProductStatus.INACTIVE;
import static kr.hhplus.be.server.domain.product.ProductStatus.SOLD_OUT;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import kr.hhplus.be.server.Exception.ProductException.ProductInactiveException;
import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import kr.hhplus.be.server.Exception.ProductException.SoldoutStockException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private long productId;

    private String name;

    private long price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPreUpdate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    private Product(long productId, String name, long price, ProductStatus status){
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public static Product of(long productId, String name, long price, ProductStatus status){
        return Product.builder()
            .productId(productId)
            .name(name)
            .price(price)
            .status(status)
            .build();
    }


    public static void productValidation(ProductStatus status){

        if (status == INACTIVE) {
            throw new ProductInactiveException();
        }

        if (status == SOLD_OUT) {
            throw new SoldoutStockException();
        }

    }



}
