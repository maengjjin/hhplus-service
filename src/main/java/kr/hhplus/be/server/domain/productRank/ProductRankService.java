package kr.hhplus.be.server.domain.productRank;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRankService {

    private final ProductRepository productRepository;

    private final ProductRankRepository  productRankRepository;




    public void cacheProductRankingByDate(ProductRankCommand command) {

        if (command.getOrderStats().isEmpty()) {
            return; // 데이터가 없으면 바로 종료
        }

        ProductRankEntry rankEntry = ProductRankEntry.fromCommand(command);
        productRankRepository.saveRanking(rankEntry);

    }

    public void cacheTopProductRankingByDate(ProductRankCommand command) {

        if (command.getOrderStats().isEmpty()) {
            return; // 통계 데이터가 없으면 바로 종료
        }

        LocalDate date = command.getStatDate();

        List<Long> productIds = extractProductIds(command);

        Map<Long, Product> productMap = fetchProductMapById(productIds);

        boolean saved = productRankRepository.saveTopRanking(date);

        if (!saved) {
            return; // 저장할 통합 데이터가 없으면 종료
        }


        // 랭킹 상세 정보 캐싱
        productRankRepository.saveTopRankingDetail(date, productMap);


    }





    public List<Long> extractProductIds(ProductRankCommand command) {
        return  command.getOrderStats().stream()
            .map(ProductRankCommand.OrderStats::getProductId)
            .toList();
    }


    public Map<Long, Product> fetchProductMapById(List<Long> productIds) {
        if (productIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return productRepository.findByIdIn(productIds)
            .stream()
            .collect(Collectors.toMap(Product::getProductId, Function.identity()));
    }



}
