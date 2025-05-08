-- user table
CREATE TABLE user (
                      user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      point INT,
                      create_at DATETIME,
                      update_at DATETIME
);

-- point_history table
CREATE TABLE point_history (
                               history_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id INT,
                               history_type VARCHAR(255),
                               amount INT,
                               before_point INT,
                               after_point INT,
                               create_at DATETIME
);

-- product table
CREATE TABLE product (
                         product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255),
                         price INT,
                         status TINYINT(1),
                         create_at DATETIME
);

-- product_option table
CREATE TABLE product_option (
                                option_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                product_id INT,
                                option_name VARCHAR(255),
                                price INT,
                                stock_quantity INT,
                                create_at DATETIME,
                                update_at DATETIME
);

-- coupon table
CREATE TABLE coupon (
                        coupon_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255),
                        coupon_type VARCHAR(255),
                        discount_rate INT,
                        discount_amount INT,
                        min_purchase_amount INT,
                        max_discount_amount INT,
                        issued_qty INT,
                        left_qty INT,
                        expires_at DATETIME,
                        create_at DATETIME
);

-- user_coupon table
CREATE TABLE user_coupon (
                             user_coupon_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             coupon_id INT,
                             user_id INT,
                             coupon_yn CHAR(1),
                             create_at DATETIME,
                             update_at DATETIME
);

-- order table
CREATE TABLE `order` (
                        order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT,
                        total_price INT,
                        status VARCHAR(255),
                        create_at DATETIME,
                        update_at DATETIME
);

-- payment table
CREATE TABLE payment (
                         pay_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         order_id INT,
                         coupon_id INT,
                         user_id INT,
                         paid_pay INT,
                         coupon_price INT,
                         create_at DATETIME
);

-- order_detail table
CREATE TABLE order_detail (
                              detail_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              order_id INT,
                              product_id INT,
                              option_id INT,
                              option_price INT,
                              order_quantity INT,
                              create_at DATETIME
);

-- productrank table
CREATE TABLE productrank (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             product_id INT,
                             product_name VARCHAR(255),
                             order_rank INT,
                             state_date DATETIME
);
