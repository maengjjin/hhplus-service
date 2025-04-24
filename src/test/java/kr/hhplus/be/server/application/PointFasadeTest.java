package kr.hhplus.be.server.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.web.point.request.PointRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointFasadeTest {


    @InjectMocks
    PointFacade pointFacade;

    @Mock
    PointService pointService;

    @Mock
    UserService userService;


    @Test
    void 포인트_충전_성공(){

        // given 포인트 충전 요청이 들어왔을 때
        PointRequest pointRequest = new PointRequest(1L, 3000L);

        // when 충전을 위한 mock 동작 설정
        pointFacade.userCharge(pointRequest);

        // then 포인트 충전 흐름이 순서대로 정상 호출되었는지 검증
        InOrder inOrder = inOrder(userService, pointService);
        inOrder.verify(userService).getUserInfo(pointRequest.getUserId());
        inOrder.verify(pointService).charge(any(), anyLong());

    }

}
