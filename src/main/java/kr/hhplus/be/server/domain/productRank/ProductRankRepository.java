package kr.hhplus.be.server.domain.productRank;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRankRepository {

    void save(ProductRank option);

}
