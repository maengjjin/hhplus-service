package kr.hhplus.be.server.domain;

import static kr.hhplus.be.server.domain.product.ProductStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.Exception.ProductException.OutOfStockException;
import kr.hhplus.be.server.Exception.ProductException.ProductInactiveException;
import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductOption;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductValidation;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
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

    ProductOption option;

    ProductInfo productInfo;

    ProductCommand command;

    ProductValidation productValidation;



    @BeforeEach
    void beforeEach(){
         product = new Product(100L, "신발", 30_000L, ACTIVE);

        options = List.of(
            new ProductOption(3000L, "250", 30_000L, 5L, product),
            new ProductOption(3001L, "240", 30_000L, 7L, product),
            new ProductOption(3002L, "230", 30_000L, 3L, product)
        );

        productInfo = ProductInfo.builder()
            .product(product)
            .options(options)
            .build();


        command = new ProductCommand(100L, 101L, 3L);

        productValidation = new ProductValidation(100L, 101L, 50L, 2L,ACTIVE, 1000L);
    }




    @Test
    void 상품_정보_조회_성공(){

        // given 존재하는 상품 ID와 상품 옵션 설정
        long productId = 100L;

        product = product.ofList(options, product.getProductId(), product.getName(), product.getPrice(), product.getStatus());

        Mockito.when(productRepository.findProductWithOptions(productId)).thenReturn(product);


        // when 상품 조회
        ProductInfo result = productService.findProductInfo(productId);

        // then 상품 검증
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(productInfo);
        verify(productRepository, times(1)).findProductWithOptions(productId);

    }

    @Test
    void 상품_조회시_상품이_없을때_예외발생(){

        // given 존재하지 않는 상품 ID에 대해 productRepository가 null을 반환하도록 설정
        long productId = 200L;

        Mockito.when(productRepository.findProductWithOptions(productId)).thenThrow(ProductNotFoundException.class);

        // when then 예외 발생
        assertThrows(ProductNotFoundException.class, () ->
            productService.findProductInfo(productId)
            );

    }

    @Test
    void 상품_조회시_중단된_상품일때_예외발생() {

        // given 상태가 중단된 상품을 반환하도록 설정
        product = new Product(100L, "신발", 30_000L, INACTIVE);

        options = List.of(
            new ProductOption(3000L, "250", 30_000L, 5L, product),
            new ProductOption(3001L, "240", 30_000L, 7L, product),
            new ProductOption(3002L, "230", 30_000L, 3L, product)
        );

        productInfo = ProductInfo.builder()
            .product(product)
            .options(options)
            .build();


        Mockito.when(productRepository.findProductWithOptions(100L)).thenReturn(product);



        // when: 예외 발생
        assertThrows(ProductInactiveException.class, () ->
            productService.findProductInfo(100L));

    }


    @Test
    void 상품_재고_충분할때_조회_성공(){

        // given 주문한 수량보다 재고가 충분한 상품옵션들이 존재함을 mock으로 설정


        productValidation = new ProductValidation(100L, 101L, 50L, 2L, ACTIVE, 1000L);

        option = new ProductOption(3000L, "250", 30_000L, 5L, product);


        when(productRepository.findOptionWithProduct(100L, 101L)).thenReturn(option);


        // when 상품 재고 조회
        productService.checkProductAvailability(command);


        // then 예외가 발생하지 않고, 호출 됐는지
        verify(productRepository, times(1)).findOptionWithProduct(100L, 101L);

    }

    @Test
    void 상품_재고가_요청수량보다_적을때_예외발생(){

        // given 한 상품옵션의 재고가 부족하도록 설정 (요청 수량 > 재고)

        option = new ProductOption(3000L, "250", 30_000L, 1L, product);


        when(productRepository.findOptionWithProduct(100L, 101L)).thenReturn(option);


        // when then 재고 예외 검증
        assertThrows(OutOfStockException.class, () -> {
            productService.checkProductAvailability(command);
        });

        verify(productRepository, times(1)).findOptionWithProduct(100L, 101L);

    }

    @Test
    void 상품_상태가_비활성일때_예외처리(){

        // given 한 상품옵션의 재고가 부족하도록 설정 (요청 수량 > 재고)
        product = new Product(100L, "신발", 30_000L, INACTIVE);

        option = new ProductOption(3000L, "250", 30_000L, 5L, product);

        when(productRepository.findOptionWithProduct(100L, 101L)).thenReturn(option);


        // when then 재고 예외 검증
        assertThrows(ProductInactiveException.class, () -> {
            productService.checkProductAvailability(command);
        });

        verify(productRepository, times(1)).findOptionWithProduct(100L, 101L);

    }


    @Test
    void 상품_재고_차감_성공(){

        // given 한 상품옵션의 재고가 부족하도록 설정 (요청 수량 > 재고)

        ProductValidation originalValidation = new ProductValidation(100L, 101L, 50L, 2L, ACTIVE, 1000L);
        ProductValidation expectedValidation = new ProductValidation(100L, 101L, 48L, 2L, ACTIVE, 1000L);



        // when then 재고 예외 검증
        productService.decreaseStock(originalValidation);

        ArgumentCaptor<ProductValidation> captor = ArgumentCaptor.forClass(ProductValidation.class);

        verify(productRepository).updateStockQuantity(100L, 101L, 48L);

        ProductValidation actual = captor.getValue();

        // 필드별로 확인
        assertThat(actual.getProductId()).isEqualTo(expectedValidation.getProductId());
        assertThat(actual.getOptionId()).isEqualTo(expectedValidation.getOptionId());
        assertThat(actual.getStockQty()).isEqualTo(expectedValidation.getStockQty());
        assertThat(actual.getOrderQty()).isEqualTo(expectedValidation.getOrderQty());
        assertThat(actual.getPrice()).isEqualTo(expectedValidation.getPrice());
        assertThat(actual.getStatus()).isEqualTo(expectedValidation.getStatus());

    }





}
