package kr.hhplus.be.server.web.point.controller;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointResponse;
import kr.hhplus.be.server.web.ApiResult;
import kr.hhplus.be.server.web.point.request.PointRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
@Tag(name = "충전 API")
@RequiredArgsConstructor
public class PointController {

    private final PointFacade pointFacade;

    @PatchMapping("/charge")
    public ApiResult<PointResponse> getUserPoint(@Parameter(hidden = true) @RequestBody PointRequest pointRequest) {
        return ApiResult.success(PointResponse.of(pointFacade.userCharge(pointRequest)));
    }

}
