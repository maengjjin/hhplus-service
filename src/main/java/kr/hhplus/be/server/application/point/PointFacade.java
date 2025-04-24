package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.web.point.request.PointRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class PointFacade {

    private final UserService userService;
    private final PointService pointService;


    public Point userCharge(PointRequest pointRequest){

        User user = userService.getUserInfo(pointRequest.getUserId());

        return pointService.charge(user, pointRequest.getPoint());
    }

}
