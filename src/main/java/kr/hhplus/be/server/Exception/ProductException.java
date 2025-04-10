package kr.hhplus.be.server.Exception;

public class ProductException {

    public static class ProductNotFoundException extends IllegalArgumentException {
        public ProductNotFoundException() {
            super("상품이 존재 하지 않습니다");
        }
    }

    public static class ProductInactiveException extends IllegalArgumentException {
        public ProductInactiveException() {
            super("상품이 중단 됐습니다");
        }
    }


    public static class OutOfStockException extends IllegalArgumentException {
        public OutOfStockException() {
            super("재고가 부족합니다");
        }

    }

    public static class SoldoutStockException extends IllegalArgumentException {
        public SoldoutStockException() {
            super("재고가 없습니다");
        }

    }

}
