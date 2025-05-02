package kr.hhplus.be.server.infrastructure.productRank;


import kr.hhplus.be.server.domain.productRank.ProductRank;
import kr.hhplus.be.server.domain.productRank.ProductRankRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductOptionJpaRepository;
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
}
