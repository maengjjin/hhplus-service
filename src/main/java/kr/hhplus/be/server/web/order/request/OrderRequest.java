package kr.hhplus.be.server.web.order.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequest {

    private long userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long couponId;

    List<OrderCriteria.OrderItem> items;

    public static OrderRequest of(long userId ,long couponId, List<OrderCriteria.OrderItem> items) {
        return new OrderRequest(userId, couponId, items);
    }

}
