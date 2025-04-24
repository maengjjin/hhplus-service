package kr.hhplus.be.server.domain.point;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository {

    void savePointHistory(PointHistory entity);

    boolean existsByUserIdAndAmount(long userId, long amount);

    List<PointHistory> findAllByUserId(long userId);

}
