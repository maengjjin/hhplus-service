package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointService;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.web.point.request.PointRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {

    private final UserService userService;
    private final PointService pointService;


    public User userCharge(PointRequest pointRequest){

        User userInfo = userService.getUserInfo(pointRequest.getUserId());

        return pointService.charge(userInfo, pointRequest.getPoint());
    }

}
