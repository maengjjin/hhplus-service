package kr.hhplus.be.server.domain.product;

import static kr.hhplus.be.server.domain.product.ProductStatus.INACTIVE;
import static kr.hhplus.be.server.domain.product.ProductStatus.SOLD_OUT;

import java.util.List;
import kr.hhplus.be.server.Exception.ProductException.OutOfStockException;
import kr.hhplus.be.server.Exception.ProductException.ProductInactiveException;
import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import kr.hhplus.be.server.Exception.ProductException.SoldoutStockException;
import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductInfo findProductInfo(long productId) {

        // 상품 조회
        Product product = getValidProduct(productId);

        List<ProductOption> productOption = productRepository.findProductOptionById(productId);

        ProductInfo productInfo = ProductInfo.builder()
            .product(product)
            .options(productOption)
            .build();

        return productInfo;
    }


    public void checkStockAvailability(List<OrderItem> orderItems) {

        for (OrderItem item : orderItems) {

            Product product = getValidProduct(item.getProductId());

            if(SOLD_OUT.equals(product.getStatus())){
                throw new SoldoutStockException();
            }

            long stock = productRepository.fetchOptionsByProductId(item.getProductId(),
                item.getOptionId());

            if (stock < item.getQty()) {
                throw new OutOfStockException();
            }
        }

    }


    private Product getValidProduct(long productId) {
        Product product = productRepository.findProductById(productId);

        if (product == null) {
            throw new ProductNotFoundException();
        }

        if (product.getStatus().equals(INACTIVE)) {
            throw new ProductInactiveException();
        }

        return product;
    }
}
