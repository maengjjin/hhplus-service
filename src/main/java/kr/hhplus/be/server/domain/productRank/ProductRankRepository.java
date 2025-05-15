package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.Map;
import kr.hhplus.be.server.domain.product.Product;

public interface ProductRankRepository {

     // 특정 날짜의 상품 랭킹 데이터 저장
    void saveRanking(ProductRankEntry ranking);

     // 최근 3일간의 데이터를 통합하여 상위 랭킹 저장
    boolean saveTopRanking(LocalDate date);

    //통합 랭킹에 포함된 상품들의 상세 정보 저장
    void saveTopRankingDetail(LocalDate date, Map<Long, Product> details);


}
