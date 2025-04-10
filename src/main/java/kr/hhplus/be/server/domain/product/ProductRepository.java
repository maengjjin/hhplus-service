package kr.hhplus.be.server.domain.product;

import java.util.List;
import kr.hhplus.be.server.domain.point.PointHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {


    Product findProductById(long productId);

    List<ProductOption> findProductOptionById(long productId);

    long fetchOptionsByProductId(long productId, long optionId);
}
