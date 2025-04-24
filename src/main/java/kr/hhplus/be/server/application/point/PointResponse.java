package kr.hhplus.be.server.application.point;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointResponse {

    private long point;


    public static PointResponse of(Point point) {
        return new PointResponse(point.getPoint());
    }


}
