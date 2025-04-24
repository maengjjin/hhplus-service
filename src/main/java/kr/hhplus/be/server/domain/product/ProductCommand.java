package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.domain.order.OrderCommand;
import lombok.Getter;


public class ProductCommand {


    @Getter
    public static class Product {

        long productId;

        long optionId;

        long qty;

        public Product(long productId, long optionId, long qty) {
            this.productId = productId;
            this.optionId = optionId;
            this.qty = qty;
        }

        public static Product toCommand(OrderCommand.OrderItem item){
            return new Product(item.getProductId(), item.getOptionId(), item.getQty());
        }

    }
}
