package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.domain.order.OrderCommand;
import lombok.Getter;


@Getter
public class ProductCommand {

    long productId;

    long optionId;

    long qty;

    public ProductCommand(long productId, long optionId, long qty) {
        this.productId = productId;
        this.optionId = optionId;
        this.qty = qty;
    }

    public static ProductCommand toCommand(OrderCommand item){
        return new ProductCommand(item.getProductId(), item.getOptionId(), item.getQty());
    }



}
