package kr.hhplus.be.server.point.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerPointRequest {

    @Schema(description = "사용자 ID", example = "1")
    private long userId;

    @Schema(description = "충전 포인트 금액", example = "5000")
    private long point;

}
