package kr.hhplus.be.server.infrastructure.statistics;



import java.util.List;
import kr.hhplus.be.server.domain.statistics.ProductOrderRepository;
import kr.hhplus.be.server.domain.statistics.ProductOrderStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductOrderRepositoryImpl implements ProductOrderRepository {

    private final ProductOrderJpaRepository productOrderJpaRepository;



    @Override
    public void saveAll(List<ProductOrderStats> productOrderStats) {
        productOrderJpaRepository.saveAll(productOrderStats);
    }
}
