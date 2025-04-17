package kr.hhplus.be.server.domain.product;

import static kr.hhplus.be.server.domain.product.Product.productValidation;

import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductInfo findProductInfo(long productId) {

        ProductInfo productInfo = productRepository.findProductWithOptions(productId);

        if(productInfo == null){
            throw new ProductNotFoundException();
        }

        Product.productValidation(productInfo.getStatus());

        return productInfo;
    }


    public ProductValidation checkProductAvailability(ProductCommand product) {

        ProductValidation validation = productRepository.fetchOptionByProductId(product);

        if(validation == null){
            throw new ProductNotFoundException();
        }

        productValidation(validation.getStatus());

        validation.stockValidation(product.getQty());

        return validation;

    }


    public void decreaseStock(ProductValidation stockOrder) {

        ProductValidation decreaseStock = stockOrder.decreaseStock();

        productRepository.updateStockQuantity(decreaseStock);
    }
}
