package kr.hhplus.be.server.infrastructure.statistics;

import kr.hhplus.be.server.domain.statistics.ProductOrderStats;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductOrderJpaRepository extends JpaRepository<ProductOrderStats, Long> {


}
