package kr.hhplus.be.server.domain.order;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemInfo {

    private long productId;

    private long optionId;

    private long qty;

    private long price;

}
