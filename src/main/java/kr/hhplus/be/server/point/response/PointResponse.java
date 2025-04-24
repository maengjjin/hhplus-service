package kr.hhplus.be.server.point.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointResponse {

    private long point;

    public static PointResponse of(long point){
        return new PointResponse(point);
    }
}
