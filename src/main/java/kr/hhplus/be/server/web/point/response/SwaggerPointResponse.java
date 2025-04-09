package kr.hhplus.be.server.web.point.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerPointResponse {

    @Schema(description = "code", example = "200")
    private int code;

    @Schema(description = "message", example = "success")
    private String message;

    @Schema(
        description = "잔액 정보",
        example = "{\"user_id\":1,\"amount\":10000}"
    )
    public PointResponse data;


}
