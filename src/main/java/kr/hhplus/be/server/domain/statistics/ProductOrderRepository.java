package kr.hhplus.be.server.domain.statistics;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository {

    void saveAll(List<ProductOrderStats> productOrderStats);
}
