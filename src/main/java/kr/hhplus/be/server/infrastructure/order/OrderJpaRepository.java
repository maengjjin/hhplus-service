package kr.hhplus.be.server.infrastructure.order;


import java.util.Optional;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNoAndUserId(String orderNo, long userId);

}
