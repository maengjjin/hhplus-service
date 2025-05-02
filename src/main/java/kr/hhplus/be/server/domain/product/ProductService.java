package kr.hhplus.be.server.domain.product;

import static kr.hhplus.be.server.domain.product.Product.productValidation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOptionResult;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductOptionResult findProductInfo(long productId) {

        Product product = productRepository.findProductWithAllOptions(productId).orElseThrow(ProductNotFoundException::new);

        productValidation(product.getStatus());

        return new ProductOptionResult(product, product.getOptions());
    }




    public List<ProductOrderResult> prepareOrderItems(List<ProductCommand> item) {

        List<ProductOrderResult> productInfo = new ArrayList<>();

        List<ProductCommand> sorted = item.stream()
            .sorted(Comparator.comparing(ProductCommand::getProductId)
                .thenComparing(ProductCommand::getOptionId))
            .toList();


        for(ProductCommand order : sorted){
            // 상품 조회
            ProductOption option = productRepository.findOptionWithProduct(order.getProductId(), order.getOptionId()).orElseThrow(ProductNotFoundException::new);

            Product.productValidation(option.getProduct().getStatus());

            // 재고 검증
            option.stockValidation(order.getQty());

            // 재고 감소
            option.decreaseStock(order.getQty());

            long productId = option.getProduct().getProductId();
            long optionId = option.getOptionId();
            String productName = option.getProduct().getName();
            String optionName = option.getOptionName();
            long stockQty = option.getStockQty();
            long orderQty = order.getQty();
            ProductStatus status = option.getProduct().getStatus();
            long price = option.getPrice();


            productInfo.add(new ProductOrderResult(productId, optionId, stockQty, orderQty, status, price, productName, optionName));

        }


        return productInfo;

    }
}
