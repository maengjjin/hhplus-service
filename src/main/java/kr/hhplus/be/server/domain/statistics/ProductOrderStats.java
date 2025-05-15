package kr.hhplus.be.server.domain.statistics;


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
@Table(name = "product_order_stats")
public class ProductOrderStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long productId;

    private String productName;

    private long totalQty;

    private LocalDate statDate;

    @PrePersist
    public void prePersist() {
        if (statDate == null) {
            this.statDate = LocalDate.now();
        }
    }

    public ProductOrderStats(long productId, String productName, long totalQty,
        LocalDate statDate) {
        this.productId = productId;
        this.productName = productName;
        this.totalQty = totalQty;
        this.statDate = statDate;
    }
}
