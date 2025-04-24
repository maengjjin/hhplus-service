package kr.hhplus.be.server.domain.point;


import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public User charge(User user, long amount) {

        Point point = new Point(user.getPoint());

        long addAmount = point.add(amount);

        // 충전
        pointRepository.updatePoint(user.getUserId(), addAmount);

        PointHistory historyEntity = PointHistory.builder()
            .userId(user.getUserId())
            .amount(amount)
            .beforeAmount(user.getPoint())
            .afterAmount(addAmount)
            .type(TransactionType.CHARGE)
            .build();

        // 히스토리 저장
        pointRepository.savePointHistory(historyEntity);

        return  user;
    }



}
