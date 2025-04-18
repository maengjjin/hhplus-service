package kr.hhplus.be.server.domain.product;

import static kr.hhplus.be.server.domain.product.ProductStatus.INACTIVE;
import static kr.hhplus.be.server.domain.product.ProductStatus.SOLD_OUT;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private long productId;

    private String name;

    private long price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductOption> options = new ArrayList<>();


    @PrePersist
    public void onPreUpdate() {
        this.createdAt = LocalDateTime.now();
    }



    public Product(long productId, String name, long price, ProductStatus status){
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public Product ofList(List<ProductOption> options, long productId, String name, long price, ProductStatus status) {
        Product product = new Product(productId, name, price, status);
        product.options = options;
        return product;
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
