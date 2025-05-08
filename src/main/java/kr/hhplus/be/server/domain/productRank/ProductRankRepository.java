package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRankRepository {

    void save(ProductRank option);

    void saveAll(List<ProductRank> ranks);

    List<ProductRank> findByStatDate(LocalDate statDate, Limit limit, Sort sort);
}
