package kr.hhplus.be.server.domain.order;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private long detailId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "option_id")
    private long optionId;

    @Column(name = "option_price")
    private long optionPrice;

    @Column(name = "order_qty")
    private long orderQty;

    @Column(name = "create_at")
    private LocalDateTime createAt;


    public static OrderDetail of(OrderCommand.OrderItem orderItem, long orderId) {
        return new OrderDetail(
            orderId,
            orderItem.getProductId(),
            orderItem.getOptionId(),
            orderItem.getPrice(),
            orderItem.getQty()
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
