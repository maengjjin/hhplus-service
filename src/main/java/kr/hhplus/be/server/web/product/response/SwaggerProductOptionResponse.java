package kr.hhplus.be.server.web.product.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerProductOptionResponse {

    @Schema(description = "code", example = "200")
    private int code;

    @Schema(description = "message", example = "success")
    private String message;

    @Schema(
        description = "상품",
        example = "{"
            + "\"productId\":101,"
            + "\"name\":\"신발\","
            + "\"price\":58000,"
            + "\"status\":\"상품 판매\","
            + "\"options\":["
            + "{\"optionId\":1,\"name\":\"흰색\",\"price\":30000,\"stockQty\":20},"
            + "{\"optionId\":2,\"name\":\"검정색\",\"price\":20000,\"stockQty\":30}"
            + "]"
            + "}"
    )
    private ProductResponse data;

}
