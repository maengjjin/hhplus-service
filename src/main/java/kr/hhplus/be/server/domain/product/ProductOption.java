package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@Table(name = "product_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // 외래 키로 productId와 연결
    private Product product;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "price")
    private long price;

    @Column(name = "stock_qty")
    private long stockQty;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updateAt = LocalDateTime.now();
    }



    public ProductOption(String optionName, long price, long stockQty, Product product) {
        this.optionName = optionName;
        this.price = price;
        this.stockQty = stockQty;
        this.product = product;
    }






}
