package kr.hhplus.be.server.Exception;

public class PointException {

    public static class InvalidPointAmountException extends IllegalArgumentException {
        public InvalidPointAmountException() {
            super("충전 금액이 유효하지 않습니다");
        }
    }

}
