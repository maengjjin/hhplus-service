package kr.hhplus.be.server.domain.order;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailId;

    private long orderId;

    private long productId;

    private long optionId;

    private long optionPrice;

    private long orderQty;

    private LocalDateTime createAt;


    public static OrderDetail of(OrderCommand.OrderItemDetail detail, long orderId) {
        return new OrderDetail(
            orderId,
            detail.getProductId(),
            detail.getOptionId(),
            detail.getPrice(),
            detail.getQty()
        );
    }

    public OrderDetail(long orderId, long productId, long optionId, long optionPrice,
        long orderQty) {
        this.orderId = orderId;
        this.productId = productId;
        this.optionId = optionId;
        this.optionPrice = optionPrice;
        this.orderQty = orderQty;
    }
}
