package kr.hhplus.be.server.infrastructure.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductOption;
import kr.hhplus.be.server.domain.product.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductOptionJpaRepositoryTest {


    @Autowired
    private ProductOptionJpaRepository productOptionJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;


    @PersistenceContext // 영속성 컨텍스트 주입
    private EntityManager em;


    @Test
    @Rollback(false) // 변경된 값을 확인하기 위해 롤백 해제
    void 상품옵션과_상품정보_연관조회_성공() {
        // given: 상품과 상품 옵션 저장
        Product product = productJpaRepository.save(new Product("후드티", 5000L, ProductStatus.ACTIVE));
        ProductOption option = productOptionJpaRepository.save(new ProductOption("검정색", 5000L, 10L, product));

        // when: 옵션 ID와 상품 ID로 옵션 조회 (연관된 상품도 함께)
        Optional<ProductOption> result = productOptionJpaRepository.findOptionWithProduct(product.getProductId(), option.getOptionId());

        // then: 연관된 상품 정보가 정상적으로 조회되는지 확인
        assertThat(result).isPresent();
        assertThat(result.get().getProduct()).isNotNull();
        assertThat(result.get().getProduct().getProductId()).isEqualTo(product.getProductId());
        assertThat(result.get().getProduct().getStatus()).isEqualTo(product.getStatus());
        assertThat(result.get().getOptionId()).isEqualTo(option.getOptionId());
    }

    @Test
    @Rollback(false) // 변경된 값을 확인하기 위해 롤백 해제
    void 옵션_재고수량_업데이트_정상처리() {
        // given: 상품과 옵션 저장
        Product product = productJpaRepository.save(new Product("후드티", 5000L, ProductStatus.ACTIVE));
        ProductOption option = productOptionJpaRepository.save(new ProductOption("검정색", 5000L, 10L, product));

        // when: 옵션의 재고 수량을 3으로 업데이트
        productOptionJpaRepository.updateStockQuantity(product.getProductId(), option.getOptionId(), 3L);
        em.clear(); // 영속성 컨텍스트 초기화 → 실제 DB 상태 기준으로 조회

        // then: 옵션이 정상적으로 업데이트되었는지 확인
        Optional<ProductOption> result = productOptionJpaRepository.findOptionWithProduct(product.getProductId(), option.getOptionId());

        assertThat(result).isPresent();
        assertThat(result.get().getProduct().getProductId()).isEqualTo(product.getProductId());
        assertThat(result.get().getOptionId()).isEqualTo(option.getOptionId());
        assertThat(result.get().getStockQty()).isEqualTo(3L);
    }
}
