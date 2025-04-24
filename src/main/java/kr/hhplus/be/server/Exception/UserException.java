package kr.hhplus.be.server.Exception;

public class UserException {

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("사용자가 존재하지 않습니다");
        }
    }

}
