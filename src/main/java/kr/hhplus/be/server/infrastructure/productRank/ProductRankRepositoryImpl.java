package kr.hhplus.be.server.infrastructure.productRank;


import java.util.List;
import kr.hhplus.be.server.domain.productRank.ProductRank;
import kr.hhplus.be.server.domain.productRank.ProductRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRankRepositoryImpl implements ProductRankRepository {

    private final ProductRankJpaRepository productRankJpaRepository;


    @Override
    public void save(ProductRank option) {

        productRankJpaRepository.save(option);

    }

    @Override
    public void saveAll(List<ProductRank> ranks) {
        productRankJpaRepository.saveAll(ranks);

    }
}
