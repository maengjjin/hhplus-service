package kr.hhplus.be.server.domain.product;

import static kr.hhplus.be.server.domain.product.Product.productValidation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductInfo findProductInfo(long productId) {

        Product product = productRepository.findProductWithOptions(productId);

        productValidation(product.getStatus());

        return new ProductInfo(product, product.getOptions());
    }


    public ProductValidation checkProductAvailability(ProductCommand product) {

        ProductOption option = productRepository.findOptionWithProduct(product.getProductId(), product.getOptionId());

        productValidation(option.getProduct().getStatus());

        ProductValidation validation = new ProductValidation(option, product.getQty());

        validation.stockValidation(product.getQty());

        return validation;

    }


    public void decreaseStock(ProductValidation stockOrder) {

        ProductValidation decreaseStock = stockOrder.decreaseStock();

        productRepository.updateStockQuantity(decreaseStock.getProductId(), decreaseStock.getOptionId(), decreaseStock.getStockQty());
    }
}
