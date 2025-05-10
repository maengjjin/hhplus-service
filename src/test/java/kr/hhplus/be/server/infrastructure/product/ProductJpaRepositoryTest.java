//package kr.hhplus.be.server.infrastructure.product;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.tuple;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import java.util.List;
//import java.util.Optional;
//import kr.hhplus.be.server.domain.product.Product;
//import kr.hhplus.be.server.domain.product.ProductOption;
//import kr.hhplus.be.server.domain.product.ProductStatus;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//
//@Transactional
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ProductJpaRepositoryTest {
//
//
//    @Autowired
//    private ProductOptionJpaRepository productOptionJpaRepository;
//
//    @Autowired
//    private ProductJpaRepository productJpaRepository;
//
//    @PersistenceContext // 영속성 컨텍스트 주입
//    private EntityManager em;
//
//
//    @Test
//    void 상품과_옵션_연관_조회_정상_수행() {
//        // given 상품과 옵션을 저장하고 양방향 연관관계 연결
//        Product product = productJpaRepository.save(new Product("후드티", 5000L, ProductStatus.ACTIVE));
//        ProductOption option1 = new ProductOption("검정색", 5000L, 10L, product);
//        ProductOption option2 = new ProductOption("흰색", 6000L, 5L, product);
//
//        productOptionJpaRepository.saveAll(List.of(option1, option2));
//
//        em.flush(); // DB 반영
//        em.clear(); // 영속성 컨텍스트 초기화 → 진짜 DB에서 조회됨
//
////        // when 상품 ID로 상품과 옵션 조인 조회
////        Optional<Product> result = productJpaRepository.findProductWithOptions(product.getProductId());
////
////        // then 상품과 옵션이 정상 조회되는지 확인
////        assertThat(result).isPresent();
////        Assertions.assertThat(result.get().getOptions()).hasSize(2);
//    }
//
//    @Test
//    @Rollback(false) // 확인을 위해 롤백 제거
//    void 상품_옵션_저장_후_조회_및_옵션_ID_일치_확인() {
//        // given 상품과 옵션을 저장하고 연관관계 수동 연결
//        Product product = productJpaRepository.save(new Product("후드티", 5000L, ProductStatus.ACTIVE));
//
//        List<ProductOption> options = productOptionJpaRepository.saveAll(List.of(
//            new ProductOption("검정색", 5000L, 10L, product),
//            new ProductOption("흰색", 6000L, 5L, product)
//        ));
//        product.getOptions().addAll(options); // 양방향 연관관계 수동 설정
//
//        // when 상품 ID로 상품과 옵션 조회, 특정 옵션 ID로도 조회
////        Optional<Product> result = productJpaRepository.findProductWithOptions(product.getProductId());
////        Optional<ProductOption> result2 = productOptionJpaRepository.findOptionWithProduct(product.getProductId(), options.get(0).getOptionId());
////
////        // then 옵션이 정상적으로 상품에 포함되어 있고, 옵션 ID가 일치하는지 확인
////        assertThat(result).isPresent();
////        Assertions.assertThat(result.get().getOptions()).hasSize(2);
////        assertThat(result2.get().getOptionId()).isEqualTo(options.get(0).getOptionId());
//    }
//
//
//}
