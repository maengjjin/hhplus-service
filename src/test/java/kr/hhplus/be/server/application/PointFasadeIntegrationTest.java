package kr.hhplus.be.server.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.List;
import kr.hhplus.be.server.Exception.PointException.InvalidPointAmountException;
import kr.hhplus.be.server.Exception.UserException.UserNotFoundException;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.point.TransactionType;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.web.point.request.PointRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PointFasadeIntegrationTest  extends DatabaseConnectionTest  {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PointFacade pointFacade;

    @Autowired
    PointRepository pointRepository;


    @Test
    void 포인트_충전_성공_통합테스트() {
        // given 초기 포인트 3000원인 사용자와 충전 요청 생성
        User user = userRepository.save(User.builder().point(3000L).build());
        long chargeAmount = 2000L;
        PointRequest request = new PointRequest(user.getUserId(), chargeAmount);

        // when 포인트 충전 실행
        Point chargedUser = pointFacade.userCharge(request);

        // then 1. 사용자 포인트 증가 확인
        User updatedUser = userRepository.findById(user.getUserId()).orElseThrow();
        assertThat(updatedUser.getPoint()).isEqualTo(5000L);

        // then 2. 포인트 히스토리가 존재하는지 확인
        boolean exists = pointRepository.existsByUserIdAndAmount(user.getUserId(), chargeAmount);
        assertThat(exists).isTrue();

    }

    @Test
    void 충전금액이_최대한도를_초과하면_예외발생() {
        // given 포인트가 3000원인 사용자와 과도한 충전 요청
        User user = userRepository.save(User.builder().point(3000L).build());
        PointRequest request = new PointRequest(user.getUserId(), 1_000_000L);

        // when & then 예외 발생 검증
        assertThatThrownBy(() -> pointFacade.userCharge(request))
            .isInstanceOf(InvalidPointAmountException.class);
    }

    @Test
    void 충전금액이_0원이면_예외발생하고_포인트_변경되지_않음() {
        // given 포인트가 3000원인 사용자와 잘못된 금액 요청
        User user = userRepository.save(User.builder().point(3000L).build());
        long invalidAmount = 0L;
        PointRequest request = new PointRequest(user.getUserId(), invalidAmount);

        // when & then 예외 발생 검증
        assertThatThrownBy(() -> pointFacade.userCharge(request))
            .isInstanceOf(InvalidPointAmountException.class);

        // then 실제 사용자 포인트는 변경되지 않아야 함
        User updatedUser = userRepository.findById(user.getUserId()).orElseThrow();
        assertThat(updatedUser.getPoint()).isEqualTo(3000L);
    }

}
