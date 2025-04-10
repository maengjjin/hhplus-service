package kr.hhplus.be.server.domain;

import static kr.hhplus.be.server.domain.product.ProductStatus.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.Exception.ProductException.OutOfStockException;
import kr.hhplus.be.server.Exception.ProductException.ProductInactiveException;
import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductOption;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;


    Product product;

    List<ProductOption> options;

    ProductInfo productInfo;



    @BeforeEach
    void beforeEach(){
         product = Product.of(100L, "신발", 30_000L, ACTIVE);

        options = List.of(
            ProductOption.of(100L, 3000L, "250", 30_000L, 5L),
            ProductOption.of(100L, 3001L, "240", 30_000L, 7L),
            ProductOption.of(100L, 3002L, "230", 30_000L, 3L)
        );

        productInfo = ProductInfo.builder()
            .product(product)
            .options(options)
            .build();
    }




    @Test
    void 상품_정보_조회_성공(){

        // given 존재하는 상품 ID와 상품 옵션 설정
        long productId = 100L;

        Mockito.when(productRepository.findProductById(productId)).thenReturn(product);
        Mockito.when(productRepository.findProductOptionById(productId)).thenReturn(options);


        // when 상품 조회
        ProductInfo result = productService.findProductInfo(productId);

        // then 상품 검증
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(productInfo);
        verify(productRepository, times(1)).findProductById(productId);
        verify(productRepository, times(1)).findProductOptionById(productId);


    }

    @Test
    void 상품_조회시_상품이_없을때_예외발생(){

        // given 존재하지 않는 상품 ID에 대해 productRepository가 null을 반환하도록 설정
        long productId = 200L;

        Mockito.when(productRepository.findProductById(productId)).thenReturn(null);

        // when then 예외 발생
        assertThrows(ProductNotFoundException.class, () ->
            productService.findProductInfo(productId)
            );

    }

    @Test
    void 상품_조회시_중단된_상품일때_예외발생() {

        // given 상태가 중단된 상품을 반환하도록 설정
        product = Product.of(100L, "신발", 30_000L, INACTIVE);

        Mockito.when(productRepository.findProductById(100L)).thenReturn(product);

        // when: 예외 발생
        assertThrows(ProductInactiveException.class, () ->
            productService.findProductInfo(100L));

    }

    @Test
    void 상품_재고_충분할때_조회_성공(){

        // given 주문한 수량보다 재고가 충분한 상품옵션들이 존재함을 mock으로 설정
        List<OrderItem> orderItems = List.of(
            new OrderItem(100L, 101L, 2),
            new OrderItem(100L, 102L, 2)
        );

        when(productRepository.fetchOptionsByProductId(100L, 101L)).thenReturn(3L);
        when(productRepository.fetchOptionsByProductId(100L, 102L)).thenReturn(3L);

        // when 상품 재고 조회
        productService.checkStockAvailability(orderItems);

        // then 예외가 발생하지 않고, 메서드가 옵션 수만큼 호출되었는지 검증
        verify(productRepository, times(orderItems.size())).fetchOptionsByProductId(Mockito.anyLong(), Mockito.anyLong());
        assertDoesNotThrow(() ->
            productService.checkStockAvailability(orderItems)
            );

    }

    @Test
    void 상품_재고가_요청수량보다_적을때_예외발생(){

        // given 한 상품옵션의 재고가 부족하도록 설정 (요청 수량 > 재고)
        List<OrderItem> orderItems = List.of(
            new OrderItem(100L, 101L, 5)
        );

        // when 재고 확인 요청
        when(productRepository.fetchOptionsByProductId(100L, 101L)).thenReturn(3L);

        // then 예외 검증
        assertThrows(OutOfStockException.class, () ->
            productService.checkStockAvailability(orderItems)
        );

    }



}
