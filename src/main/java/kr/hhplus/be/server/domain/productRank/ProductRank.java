package kr.hhplus.be.server.domain.productRank;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "product_rank")
public class ProductRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long productId;

    private String productName;

    private long orderRank;

    private LocalDate statDate;

    @PrePersist
    public void prePersist() {
        if (statDate == null) {
            this.statDate = LocalDate.now();
        }
    }

    public ProductRank(long productId, String productName, long orderRank) {
        this.productId = productId;
        this.productName = productName;
        this.orderRank = orderRank;
    }
}
