package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.Exception.PointException.InvalidPointAmountException;
import lombok.Getter;

@Getter
public class Point {

    // 충전 전 금액
    private long point;

    private final long MIN_AMOUNT = 1_000L;

    private final long MAX_AMOUNT = 1_000_000L;

    public Point( long point) {
        this.point = point;
    }

    public void validateChargeAmount(long amount){

        if(MIN_AMOUNT > amount){
           throw new InvalidPointAmountException();
        }

        if(MAX_AMOUNT < amount ){
            throw new InvalidPointAmountException();
        }

        if(MAX_AMOUNT < amount + point){
            throw new InvalidPointAmountException();
        }
    }

    // 충전 금액
    public long add(long amount){
        validateChargeAmount(amount);
       return point += amount;
    }

}
