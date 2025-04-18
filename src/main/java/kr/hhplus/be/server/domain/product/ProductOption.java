package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId") // ProductOption 테이블의 FK 컬럼
    private Product product;

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



    public ProductOption(long optionId, String optionName, long price, long stockQty, Product product) {
        this.optionId = optionId;
        this.optionName = optionName;
        this.price = price;
        this.stockQty = stockQty;
        this.product = product;
    }






}
