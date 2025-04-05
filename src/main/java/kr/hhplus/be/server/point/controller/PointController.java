package kr.hhplus.be.server.point.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.hhplus.be.server.common.ApiResult;
import kr.hhplus.be.server.point.request.PointRequest;
import kr.hhplus.be.server.point.response.PointResponse;
import kr.hhplus.be.server.point.response.SwaggerPointResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {

    @Operation(summary = "포인트 충전 API", description = "포인트 충전",
        responses = @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = SwaggerPointResponse.class))))
    @PatchMapping("/charge")
    public ApiResult<PointResponse> getUserPoint(@Parameter(hidden = true) @RequestBody PointRequest pointRequest) {
        return ApiResult.success(PointResponse.of(10000L));
    }

}
