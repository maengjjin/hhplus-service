package kr.hhplus.be.server.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import kr.hhplus.be.server.common.ApiResult;
import kr.hhplus.be.server.product.response.ProductOptionResponse;
import kr.hhplus.be.server.product.response.ProductResponse;
import kr.hhplus.be.server.product.response.SwaggerProductOptionResponse;
import kr.hhplus.be.server.product.response.SwaggerProductResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Operation(summary = "상품 단건 조회 API", description = "상품 단건 조회",
        responses = @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = SwaggerProductOptionResponse.class))))
    @Parameters({ @Parameter(name = "productId", description = "상품 ID", required = true)})
    @GetMapping("/{productId}")
    public ApiResult<ProductResponse> getProduct(@Parameter(hidden = true) @PathVariable long productId) {
        return ApiResult.success(ProductResponse.of(101L, "신발", 58000L, "상품 판매",
            List.of(
                new ProductOptionResponse(1L, "흰색", 30000L, 20L),
                new ProductOptionResponse(2L, "검정색", 20000L, 30L)
            )
        ));
    }

    @Operation(summary = "상품 단건 조회 API", description = "상품 단건 조회",
        responses = @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SwaggerProductResponse.class))))
    @GetMapping("/ranking")
    public ApiResult<List<ProductResponse>> getProductRanking() {
        return ApiResult.success(
            List.of(
                new ProductResponse(1L, "후드티", 30000L, "상품 판매", null),
                new ProductResponse(2L, "맨투맨", 35000L, "상품 판매", null),
                new ProductResponse(3L, "블라우스", 28000L, "상품 판매", null),
                new ProductResponse(4L, "신발", 32000L, "상품 판매", null),
                new ProductResponse(5L, "슬리퍼", 27000L, "상품 판매", null)
            )
        );
    }
}
