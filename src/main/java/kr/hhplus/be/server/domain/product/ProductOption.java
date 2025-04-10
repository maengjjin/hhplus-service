package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    private long optionId;

    private long productId;

    private String optionName;

    private long price;

    private long stockQty;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate(){
        this.updateAt = LocalDateTime.now();
    }

    @Builder
    private ProductOption(long optionId, long productId, String optionName, long price, long stockQty) {
        this.optionId = optionId;
        this.productId = productId;
        this.optionName = optionName;
        this.price = price;
        this.stockQty = stockQty;
    }

    public static ProductOption of(long optionId, long productId, String optionName, long price, long stockQty) {
        return ProductOption.builder()
            .optionId(optionId)
            .productId(productId)
            .optionName(optionName)
            .price(price)
            .stockQty(stockQty)
            .build();
    }

}
