package kr.hhplus.be.server.product.controller;

import java.util.List;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.product.response.ProductOptionResponse;
import kr.hhplus.be.server.product.response.ProductResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable long productId) {
        return ApiResponse.success(ProductResponse.of(101L, "신발", 58000L, "상품 판매",
            List.of(
                new ProductOptionResponse(1L, "흰색", 30000L, 20L),
                new ProductOptionResponse(2L, "검정색", 20000L, 30L)
            )
        ));
    }

    @GetMapping("/ranking")
    public ApiResponse<List<ProductResponse>> getProductRanking() {
        return ApiResponse.success(
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
