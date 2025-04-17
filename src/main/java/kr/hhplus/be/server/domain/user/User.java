package kr.hhplus.be.server.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private long userId;

    private long point;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @Builder
    private User(long userId, long point){
        this.userId = userId;
        this.point = point;
    }

    public static User of(long userId, long point) {
        return new User(userId, point);
    }

}
