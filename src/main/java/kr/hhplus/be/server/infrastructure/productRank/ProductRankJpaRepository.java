package kr.hhplus.be.server.infrastructure.productRank;

import kr.hhplus.be.server.domain.productRank.ProductRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRankJpaRepository extends JpaRepository<ProductRank, Long> {

}
