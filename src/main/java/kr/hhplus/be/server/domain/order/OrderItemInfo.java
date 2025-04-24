package kr.hhplus.be.server.domain.order;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemInfo {

    private final long productId;

    private final long optionId;

    private final long qty;

    private final long price;

}
