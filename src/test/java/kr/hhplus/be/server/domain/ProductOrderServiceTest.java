package kr.hhplus.be.server.domain;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.statistics.ProductOrderRepository;
import kr.hhplus.be.server.domain.statistics.ProductOrderService;
import kr.hhplus.be.server.domain.statistics.ProductOrderStats;
import kr.hhplus.be.server.domain.statistics.ProductOrderVolume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductOrderServiceTest {

    @Mock
    ProductOrderRepository productOrderRepository;

    @Mock
    ProductRepository productRepository;



    @InjectMocks
    ProductOrderService productOrderService;

    List<ProductOrderStats> list;

    LocalDate date;
    LocalDateTime end;
    LocalDateTime start;

    @BeforeEach
    void beforeEach(){
        // top5 생성
        date = LocalDate.now();
        start = date.minusDays(1).atStartOfDay();
        end = date.atStartOfDay();

        list = List.of(
            new ProductOrderStats(1L, "나이키운동화", 1L, date),
            new ProductOrderStats(2L, "퓨마운동화", 2L, date),
            new ProductOrderStats(3L, "아디다스운동화", 3L, date),
            new ProductOrderStats(4L, "샌들", 4L, date),
            new ProductOrderStats(5L, "슬리퍼", 5L, date)
        );

    }




    @Test
    void 통계데이터가_생성되면_저장_성공(){
        // given top5 만들어 놓기

        List<ProductOrderVolume> list = List.of(
            new ProductOrderVolume(1L, 30),
            new ProductOrderVolume(2L, 20),
            new ProductOrderVolume(5L, 6),
            new ProductOrderVolume(3L, 10),
            new ProductOrderVolume(4L, 8)
        );


        List<Product> products = List.of(
            new Product(1L, "나이키운동화", 30_000L, ProductStatus.INACTIVE),
            new Product(2L, "퓨마운동화", 30_000L, ProductStatus.INACTIVE),
            new Product(3L, "아디다스운동화", 30_000L, ProductStatus.INACTIVE),
            new Product(4L, "샌들", 30_000L, ProductStatus.INACTIVE),
            new Product(5L, "슬리퍼", 30_000L, ProductStatus.INACTIVE)
        );


        when(productRepository.findByIdIn(List.of(1L, 2L, 5L, 3L, 4L))).thenReturn(products);

        doNothing().when(productOrderRepository).saveAll(anyList());


        productOrderService.createAggregateTopOrders(list, date);


        ArgumentCaptor<List<kr.hhplus.be.server.domain.statistics.ProductOrderStats>> captor = ArgumentCaptor.forClass(List.class);
        verify(productOrderRepository, times(1)).saveAll(captor.capture());


    }



}
