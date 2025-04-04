package kr.hhplus.be.server.point.controller;


import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.point.request.PointRequest;
import kr.hhplus.be.server.point.response.PointResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {

    @PatchMapping("{/charge")
    public ApiResponse<PointResponse> getUserPoint(@RequestBody PointRequest pointRequest) {
        return ApiResponse.success(PointResponse.of(10000L));
    }

}
