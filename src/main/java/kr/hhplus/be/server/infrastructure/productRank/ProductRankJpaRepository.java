package kr.hhplus.be.server.infrastructure.productRank;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.productRank.ProductRank;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRankJpaRepository extends JpaRepository<ProductRank, Long> {

    List<ProductRank> findByStatDate(LocalDate statDate, Limit limit, Sort sort);
}
