package kr.hhplus.be.server.web.product.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.web.ApiResult;
import kr.hhplus.be.server.web.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@Tag(name = "상품 API")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ApiResult<ProductResponse> getProduct(@Parameter(hidden = true) @PathVariable long productId) {

        ProductInfo info = productService.findProductInfo(productId); // 도메인에서 가져옴

        return ApiResult.success(ProductResponse.from(info));

    }


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
