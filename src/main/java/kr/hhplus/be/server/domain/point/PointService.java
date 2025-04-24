package kr.hhplus.be.server.domain.point;


import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Point charge(User user, long amount) {

        long beforePoint = user.getPoint();

        Point point = new Point(beforePoint);

        // 충전
        point.add(amount);

        user.updatePoint(point.getPoint());

        PointHistory pointHistory = PointHistory.builder()
            .userId(user.getUserId())
            .amount(amount)
            .beforeAmount(beforePoint)
            .afterAmount(user.getPoint())
            .type(TransactionType.CHARGE)
            .build();

        // 히스토리 저장
        pointRepository.savePointHistory(pointHistory);

        return  point;
    }


    public void usePoint(User user, long amount) {

        long beforePoint = user.getPoint();

        Point point = new Point(user.getPoint());

        point.use(amount);

        user.updatePoint(point.getPoint());


        PointHistory pointHistory = PointHistory.builder()
            .userId(user.getUserId())
            .amount(amount)
            .beforeAmount(beforePoint)
            .afterAmount(user.getPoint())
            .type(TransactionType.USE)
            .build();

        pointRepository.savePointHistory(pointHistory);
    }


}
