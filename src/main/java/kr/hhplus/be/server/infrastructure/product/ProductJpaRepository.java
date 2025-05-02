package kr.hhplus.be.server.infrastructure.product;

import java.util.Optional;
import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT DISTINCT p
            FROM Product p
            JOIN FETCH p.options po
            WHERE p.productId = :productId
          """)
    Optional<Product> findProductWithAllOptions(@Param("productId") long productId);

}
