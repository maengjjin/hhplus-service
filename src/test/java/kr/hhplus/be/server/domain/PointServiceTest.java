package kr.hhplus.be.server.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.TransactionType;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @Mock
    PointRepository pointRepository;

    @InjectMocks
    PointService pointService;

    User user;
    // 충전성공, 충전 실패, 충전 금액 검증

    @BeforeEach
    void beforeEach(){
        user = User.of(1L, 3000L);
    }


    @Test
    void 사용자_충전_성공(){

        // given 사용자가 충전할 금액 2,000원
        long amount = 2000L;


        // then 포인트 충전 로직 실행 확인
        PointHistory history = PointHistory.builder()
            .userId(user.getUserId())
            .amount(amount)
            .beforeAmount(user.getPoint())
            .afterAmount(amount + user.getPoint())
            .type(TransactionType.CHARGE)
            .build();


        // when 포인트 충전
        pointService.charge(user, amount);

        // then 히스토리 저장 확인 및 인자 전달 검증

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(pointRepository, times(1)).savePointHistory(captor.capture());

        // 캡처한 실제 인자
        PointHistory actual = captor.getValue();

        // 필드 값 검증
        assertEquals(history.getUserId(), actual.getUserId());
        assertEquals(history.getAmount(), actual.getAmount());
        assertEquals(history.getBeforeAmount(), actual.getBeforeAmount());
        assertEquals(history.getAfterAmount(), actual.getAfterAmount());
        assertEquals(history.getType(), actual.getType());

    }



    @Test
    void 포인트_사용_성공(){

        // given 사용자가 충전할 금액 2,000원  히스토리에 저장 될 데이터
        long amount = 2000L;

        PointHistory history = PointHistory.builder()
            .userId(user.getUserId())
            .amount(amount)
            .beforeAmount(user.getPoint())
            .afterAmount(user.getPoint() - amount)
            .type(TransactionType.USE)
            .build();


        // when 포인트 사용
        pointService.usePoint(user, amount);

        // then 히스토리 저장 확인 및 인자 전달 검증

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(pointRepository, times(1)).savePointHistory(captor.capture());

        // 캡처한 실제 인자
        PointHistory actual = captor.getValue();

        // 필드 값 검증
        assertEquals(history.getUserId(), actual.getUserId());
        assertEquals(history.getAmount(), actual.getAmount());
        assertEquals(history.getBeforeAmount(), actual.getBeforeAmount());
        assertEquals(history.getAfterAmount(), actual.getAfterAmount());
        assertEquals(history.getType(), actual.getType());

    }



}
