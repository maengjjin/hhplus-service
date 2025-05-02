package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
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


        // 해당 날자로 주문내역 조회
        List<Long> bestProductIds = orderRepository.findTopSellingProductsBetween(start, end);

        int rank = 1;

        for(Long productId : bestProductIds){

            Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

            productRankRepository.save(new ProductRank(product.getProductId(), product.getName(), rank++));

        }


    }


}
