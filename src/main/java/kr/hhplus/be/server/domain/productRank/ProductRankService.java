package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductRankService {


    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final ProductRankRepository productRankRepository;


    public void aggregateTopSellingProducts(LocalDate date){

        LocalDateTime end = date.atStartOfDay();
        LocalDateTime start =  end.minusDays(3);


        // 인기순위 5개 정렬
        List<ProductRank> bestProducts = orderRepository.findTopSellingProductsBetween(start, end).stream()
            .sorted(Comparator.comparing(ProductRank::getOrderQty).reversed()) // 주문 수량 기준 내림차순
            .limit(5)
            .toList();

        List<Long> productIds = bestProducts.stream()
            .map(ProductRank::getProductId)
            .toList();

        // 상품 정보만 list로 가져오기
        Map<Long, Product> productMap = productRepository.findByIdIn(productIds).stream()
            .collect(Collectors.toMap(Product::getProductId, Function.identity()));

        int rank = 1;

        for(ProductRank productRank : bestProducts){

            Product product = productMap.get(productRank.getProductId());

            productRankRepository.save(new ProductRank(product.getProductId(), product.getName(), rank++));

        }


    }


}
