package kr.hhplus.be.server.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "사용자 API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/point/{userId}")
    public ApiResult<UserResponse> getUserPoint(@Parameter(hidden = true) @PathVariable long userId) {
        User user = userService.getUserInfo(userId);
        return ApiResult.success(UserResponse.of(user.getUserId(), user.getPoint()));
    }


}
