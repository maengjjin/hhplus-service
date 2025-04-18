package kr.hhplus.be.server.infrastructure.product;

import java.util.Optional;
import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.options WHERE p.productId = :productId")
    Optional<Product> findProductWithOptions(@Param("productId") long productId);

}
