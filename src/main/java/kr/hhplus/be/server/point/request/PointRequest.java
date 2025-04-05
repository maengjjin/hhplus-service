package kr.hhplus.be.server.point.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointRequest {

    private long userId;

    private long point;

    public static PointRequest of(long userid, long point){
        return new PointRequest(userid, point);
    }
}
