package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRankService {

    private final ProductRepository productRepository;

    private final ProductRankRepository productRankRepository;



    public void saveProductSalesRanking(List<ProductRankCommand> command) {

        List<Long> productIds = command.stream()
            .map(ProductRankCommand::getProductId)
            .toList();

        // 상품 정보만 list로 가져오기
        Map<Long, Product> productMap = productRepository.findByIdIn(productIds).stream()
            .collect(Collectors.toMap(Product::getProductId, Function.identity()));


        AtomicInteger rank = new AtomicInteger(1);

        List<ProductRank> ranks = command.stream()
            .sorted(Comparator.comparing(ProductRankCommand::getOrderQty).reversed()) // 많이 팔린 순
            .map(productRank -> {
                Product product = productMap.get(productRank.getProductId());
                return new ProductRank(product.getProductId(), product.getName(), rank.getAndIncrement(), command.get(0).getStatDate());
            })
            .toList();

        productRankRepository.saveAll(ranks);


    }

    @Transactional(readOnly = true)
    @Cacheable(value = "TopSellingProduct", key = "'days:' + #statDate + ':topRank:' + #topRank")
    public List<ProductRank> getProductRank(LocalDate statDate, long topRank) {

        Limit limit = Limit.of((int)topRank);  // 제한 수
        Sort sort = Sort.by("order_rank").ascending();  // 정렬 기준
        return productRankRepository.findByStatDate(statDate, limit, sort);
    }


}
