package kr.hhplus.be.server.domain.order;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    private String orderNo;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private OrderStatus type;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;



    public Order(User user, OrderStatus type) {
        this.orderNo = createOrderNumber();
        this.user = user;
        this.type = type;
    }

    public static Order of(User user, OrderStatus type) {
        return new Order(user, type);
    }


    public static String createOrderNumber() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimePart = LocalDateTime.now().format(formatter);

        int randomNum = new Random().nextInt(1000);
        String randomPart = String.format("%03d", randomNum);

        // 3. 두 부분을 합쳐서 17자리 주문번호 생성
        return dateTimePart + randomPart;

    }


}
