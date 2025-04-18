package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository{

    private final PointJpaRepository pointJpaRepository;

    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public void updatePoint(long userId, long amount) {
        pointJpaRepository.updatePoint(userId, amount);
    }

    @Override
    public void savePointHistory(PointHistory entity) {
        pointHistoryJpaRepository.save(entity);

    }
}
