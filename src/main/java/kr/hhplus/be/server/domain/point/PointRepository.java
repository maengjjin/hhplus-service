package kr.hhplus.be.server.domain.point;

import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository {

    void updatePoint(long userId, long amount);

    void savePointHistory(PointHistory entity);

}
