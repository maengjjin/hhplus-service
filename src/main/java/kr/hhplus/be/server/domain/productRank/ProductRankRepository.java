package kr.hhplus.be.server.domain.productRank;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRankRepository {

    void save(ProductRank option);

    void saveAll(List<ProductRank> ranks);
}
