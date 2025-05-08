package kr.hhplus.be.server.domain.productRank;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                return new ProductRank(product.getProductId(), product.getName(), rank.getAndIncrement());
            })
            .toList();

        productRankRepository.saveAll(ranks);


    }


}
