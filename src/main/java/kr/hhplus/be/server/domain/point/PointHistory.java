package kr.hhplus.be.server.domain.point;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가

    private long historyId;

    private long userId;

    private long amount;

    private long beforeAmount;

    private long afterAmount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPreUpdate() {
        this.createdAt = LocalDateTime.now();
    }


    @Builder
    public PointHistory(long userId, long amount, long beforeAmount, long afterAmount, TransactionType type) {
        this.userId = userId;
        this.amount = amount;
        this.beforeAmount = beforeAmount;
        this.afterAmount = afterAmount;
        this.type = type;
    }


}
