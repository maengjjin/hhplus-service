package kr.hhplus.be.server.order.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequest {

    private long userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long couponId;

    List<OrderProductRequest> items;

    public static OrderRequest of(long userId ,long couponId, List<OrderProductRequest> items) {
        return new OrderRequest(userId, couponId, items);
    }

}
