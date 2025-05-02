package kr.hhplus.be.server.infrastructure.product;

import java.util.Optional;
import kr.hhplus.be.server.domain.product.ProductOption;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionJpaRepository extends JpaRepository<ProductOption, Long> {


    @Retryable(
        value = {
            CannotAcquireLockException.class,
            PessimisticLockException.class
        },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    @Query(""" 
             SELECT po
             FROM ProductOption po
             JOIN FETCH po.product p
             WHERE p.productId = :productId AND po.optionId = :optionId
             """)
    Optional<ProductOption> findOptionWithProduct(@Param("productId") long productId,@Param("optionId") long optionId);


    @Modifying
    @Query("""
            UPDATE ProductOption po
            SET po.stockQty = :stockQty
            WHERE po.optionId = :optionId AND po.product.productId = :productId
            """)
    void updateStockQuantity(@Param("productId") long productId, @Param("optionId") long optionId,@Param("stockQty") long stockQty);


    @Modifying
    @Query("""
            UPDATE ProductOption po
            SET po.stockQty = :stockQty
            WHERE po.optionId = :optionId AND po.product.productId = :productId
            """)
    int updateStockQuantity2(@Param("productId") long productId, @Param("optionId") long optionId,@Param("stockQty") long stockQty);



}
