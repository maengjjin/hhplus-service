package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long productId;

    private Long userId;

    private Long amount;

    private Long beforeAmount;

    private Long afterAmount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPreUpdate() {
        this.createdAt = LocalDateTime.now();
    }


}
