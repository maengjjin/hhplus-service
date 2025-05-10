package kr.hhplus.be.server.domain;

import java.time.LocalDate;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.productRank.ProductRank;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankRepository;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class ProductRankServiceTest {

    @Mock
    ProductRankRepository productRankRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductRankService productRankService;

    List<ProductRank> list;

    LocalDate date;

    @BeforeEach
    void beforeEach(){
        // top5 생성
        date = LocalDate.of(2025, 5, 15);

        list = List.of(
            new ProductRank(1L, "나이키운동화", 1L, date),
            new ProductRank(2L, "퓨마운동화", 2L, date),
            new ProductRank(3L, "아디다스운동화", 3L, date),
            new ProductRank(4L, "샌들", 4L, date),
            new ProductRank(5L, "슬리퍼", 5L, date)
        );

    }

    @Test
    void 정해진_날짜와_개수로_정상_조회(){
        // given top5 만들어 놓기
        Limit limit = Limit.of(5);  // 제한 수
        Sort sort = Sort.by("order_rank").ascending();  // 정렬 기준

        Mockito.when(productRankRepository.findByStatDate(date, limit, sort)).thenReturn(list);

        // when 인기상품 리스트 가져오기
        List<ProductRank> result = productRankService.getProductRank(date, 5);

        // then
        verify(productRankRepository, times(1)).findByStatDate(date, limit, sort); // 3개니까
        assertThat(result).hasSize(5);

    }


    @Test
    void 집계_데이터_정상_저장_확인(){
        // given top5 만들어 놓기
        List<ProductRankCommand> command = List.of(
            new ProductRankCommand(1L, 30, 1, date),
            new ProductRankCommand(2L, 20, 2, date),
            new ProductRankCommand(5L, 6, 5, date),
            new ProductRankCommand(3L, 10, 3, date),
            new ProductRankCommand(4L, 8, 4, date)

        );

        List<Product> products = List.of(
            new Product(1L, "나이키운동화", 30_000L, ProductStatus.INACTIVE),
            new Product(2L, "퓨마운동화", 30_000L, ProductStatus.INACTIVE),
            new Product(3L, "아디다스운동화", 30_000L, ProductStatus.INACTIVE),
            new Product(4L, "샌들", 30_000L, ProductStatus.INACTIVE),
            new Product(5L, "슬리퍼", 30_000L, ProductStatus.INACTIVE)
        );

        when(productRepository.findByIdIn(List.of(1L, 2L, 5L, 3L, 4L))).thenReturn(products);

        doNothing().when(productRankRepository).saveAll(anyList());


        productRankService.saveProductSalesRanking(command);

        ArgumentCaptor<List<ProductRank>> captor = ArgumentCaptor.forClass(List.class);
        verify(productRankRepository, times(1)).saveAll(captor.capture());

        List<ProductRank> savedRanks = captor.getValue();

        assertThat(savedRanks).hasSize(command.size());
        assertThat(savedRanks)
            .extracting(ProductRank::getOrderRank)
            .isSorted(); // 정렬 순서 확인

    }

}
