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
import kr.hhplus.be.server.domain.order.OrderCommand;
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
import org.mockito.ArgumentCaptor;
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


    // 상품 조회 성공
    // 상품 조회 실패 (상품이 있거나, 상품상태 유효)
    //


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
    void 상품_조회_성공(){

        // given 상품 조회를 성공 하기 위해 상품과 상품 옵션 세팅
        long productId = 100L;

        Mockito.when(productRepository.findProductById(productId)).thenReturn(product);
        Mockito.when(productRepository.findProductOptionById(productId)).thenReturn(options);


        // 상품 조회
        ProductInfo result = productService.findProductInfo(productId);

        // 상품 검증
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(productInfo);
        verify(productRepository, times(1)).findProductById(productId);
        verify(productRepository, times(1)).findProductOptionById(productId);


    }

    @Test
    void 잘못된_상품을_조회_했을때(){

        // given 상품 조회를 성공 하기 위해 상품과 상품 옵션 세팅
        long productId = 200L;

        Mockito.when(productRepository.findProductById(productId)).thenReturn(null);


        assertThrows(ProductNotFoundException.class, () ->
            productService.findProductInfo(productId)
            );

    }

    @Test
    void 상품_중단이_됐을때_예외() {

        // given
        product = Product.of(100L, "신발", 30_000L, INACTIVE);

        Mockito.when(productRepository.findProductById(100L)).thenReturn(product);

        assertThrows(ProductInactiveException.class, () ->
            productService.findProductInfo(100L));

    }

    @Test
    void 상품_재고_조회_성공(){

        // given 상품, 상품옵션번호
        List<OrderItem> orderItems = List.of(
            new OrderItem(100L, 101L, 2),
            new OrderItem(100L, 102L, 2)
        );

        when(productRepository.fetchOptionsByProductId(100L, 101L)).thenReturn(3L);
        when(productRepository.fetchOptionsByProductId(100L, 102L)).thenReturn(3L);

        // when 상품 재고 조회
        productService.checkStockAvailability(orderItems);

        // then 검증
        verify(productRepository, times(orderItems.size())).fetchOptionsByProductId(Mockito.anyLong(), Mockito.anyLong());
        assertDoesNotThrow(() ->
            productService.checkStockAvailability(orderItems)
            );

    }

    @Test
    void 상품_재고가_없을때_예외_발생(){

        // given 상품, 상품옵션번호
        List<OrderItem> orderItems = List.of(
            new OrderItem(100L, 101L, 5),
            new OrderItem(100L, 102L, 2)
        );

        when(productRepository.fetchOptionsByProductId(100L, 101L)).thenReturn(3L);

        assertThrows(OutOfStockException.class, () ->
            productService.checkStockAvailability(orderItems)
        );

    }



}
