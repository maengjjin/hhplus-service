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

        point.add(amount);

        // 충전
        pointRepository.updatePoint(user.getUserId(), point.getPoint());

        PointHistory pointHistory = PointHistory.builder()
            .userId(user.getUserId())
            .amount(amount)
            .beforeAmount(user.getPoint())
            .afterAmount(point.getPoint())
            .type(TransactionType.CHARGE)
            .build();

        // 히스토리 저장
        pointRepository.savePointHistory(pointHistory);

        return  user;
    }


    public void usePoint(User user, long amount) {

        Point point = new Point(user.getPoint());

        point.use(amount);

        pointRepository.updatePoint(user.getUserId(), point.getPoint());

        PointHistory pointHistory = PointHistory.builder()
            .userId(user.getUserId())
            .amount(amount)
            .beforeAmount(user.getPoint())
            .afterAmount(point.getPoint())
            .type(TransactionType.USE)
            .build();

        pointRepository.savePointHistory(pointHistory);
    }


}
