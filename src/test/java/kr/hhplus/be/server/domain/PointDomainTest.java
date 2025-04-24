package kr.hhplus.be.server.domain;

import kr.hhplus.be.server.Exception.PointException.InvalidPointAmountException;
import kr.hhplus.be.server.domain.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointDomainTest {


    // 충전성공, 충전 실패, 충전 금액 검증

    Point point = new Point(3000L);

    @Test
    void 충전금액이_최소값보다_작으면_예외가_발생한다(){

        // given 포인트 3,000원을 보유한 사용자와 충전할 금액 500원
        long amount = 500L;


        // then 최소 금액 호출 되었는지 검증
        Assertions.assertThrows(InvalidPointAmountException.class, () ->{
            point.validateChargeAmount(amount);
        });

    }


    @Test
    void 충전금액이_최대값보다_크면_예외가_발생한다(){

        // given 포인트 충전할 금액 1,000,001원
        long amount = 1_000_001L;


        // then 포인트 충전 로직 실행 확인
        Assertions.assertThrows(InvalidPointAmountException.class, () ->{
            point.validateChargeAmount(amount);
        });

    }

    @Test
    void 기존금액과_충전금액의_합이_최대값보다_크면_예외가_발생한다(){

        // given 포인트 충전할 금액 999,999원
        long amount = 999_999L;


        // then 포인트 충전 로직 실행 확인
        Assertions.assertThrows(InvalidPointAmountException.class, () ->{
            point.validateChargeAmount(amount);
        });

    }


    @Test
    void 정상_충전금액이면_예외없이_검증된다(){

        // given 포인트 충전할 금액 2,000원
        long amount = 2000L;

        // then 포인트 충전 로직 실행 확인
        Assertions.assertDoesNotThrow(() -> {
           point.validateChargeAmount(amount);
        });

    }

}
