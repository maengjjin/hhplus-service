package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class PointJpaRepositoryImpl implements PointRepository{


    @Override
    public void updatePoint(long userId, long amount) {
    }



    @Override
    public void savePointHistory(PointHistory entity) {

    }
}
