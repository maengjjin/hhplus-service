package kr.hhplus.be.server.domain.order;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @Column(name = "order_no")
    private String orderNo;


    @Column(name = "user_id")
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrderStatus type;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;



    public Order(User user, OrderStatus type) {
        this.orderNo = createOrderNumber();
        this.userId = user.getUserId();
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
