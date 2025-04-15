package kr.hhplus.be.server.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductStock {

    private long productId;

    private long optionId;

    private long stockQty;


}
