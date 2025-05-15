package kr.hhplus.be.server.domain.statistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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
public class ProductOrderService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final ProductOrderRepository productOrderRepository;


    public List<ProductOrderVolume> findAggregateTopOrders(LocalDate date){

        LocalDateTime start = date.minusDays(1).atStartOfDay();
        LocalDateTime end = date.atStartOfDay();

        return orderRepository.findAggregateTopOrders(start, end);

    }


    public void createAggregateTopOrders(List<ProductOrderVolume> productOrder, LocalDate date) {

        List<Long> productIds = extractProductIds(productOrder);

        Map<Long, Product> productMap = fetchProductMapById(productIds);

        List<ProductOrderStats> productOrderStats = productOrder.stream()
            .map(orderStats -> {
                Product product = productMap.get(orderStats.getProductId());

                return new ProductOrderStats(
                    product.getProductId(),
                    product.getName(),
                    orderStats.getTotalQty(),
                    date
                );
            })
            .toList();

        productOrderRepository.saveAll(productOrderStats);

    }



    public List<Long> extractProductIds(List<ProductOrderVolume> productOrders) {
        return  productOrders.stream()
            .map(ProductOrderVolume::getProductId)
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
