package kr.hhplus.be.server.web.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private long userId;

    private long point;

    public static UserResponse of(long userid, long point){
        return new UserResponse(userid, point);
    }
}
