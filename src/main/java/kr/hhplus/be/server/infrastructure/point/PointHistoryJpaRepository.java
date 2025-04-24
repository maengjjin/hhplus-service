package kr.hhplus.be.server.infrastructure.point;


import java.util.List;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {

    boolean existsByUserIdAndAmount(long userId, long amount);

    List<PointHistory> findAllByUserId(long userId);
}
