package kr.hhplus.be.server.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.web.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "사용자 API")
public class UserController {


    @Operation(summary = "사용자 포인트 조회 API", description = "사용자의 보유 쿠폰을 조회합니다",
        responses = @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SwaggerUserResponseResult.class))))
    @Parameters({ @Parameter(name = "userId", description = "사용자 id", required = true) })
    @GetMapping("/point/{userId}")
    public ApiResult<UserResponse> getUserPoint(@Parameter(hidden = true) @PathVariable long userId) {
        return ApiResult.success(UserResponse.of(userId, 10000L));
    }


}
