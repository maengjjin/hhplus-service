package kr.hhplus.be.server.infrastructure.point;

import java.util.List;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository{


    private final PointHistoryJpaRepository pointHistoryJpaRepository;



    @Override
    public void savePointHistory(PointHistory entity) {
        pointHistoryJpaRepository.save(entity);

    }

    @Override
    public boolean existsByUserIdAndAmount(long userId, long amount) {
        return pointHistoryJpaRepository.existsByUserIdAndAmount(userId, amount);
    }

    @Override
    public List<PointHistory> findAllByUserId(long userId) {
        return pointHistoryJpaRepository.findAllByUserId(userId);
    }
}
