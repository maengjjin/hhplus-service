package kr.hhplus.be.server.domain.product;


import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.order.OrderCriteria;
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



    public static List<ProductCommand> toCommand(List<OrderCriteria.OrderItem> orderItems) {
        return orderItems.stream()
            .map(order -> new ProductCommand(order.getProductId(), order.getOptionId(), order.getQty()))
            .collect(Collectors.toList());
    }





}
