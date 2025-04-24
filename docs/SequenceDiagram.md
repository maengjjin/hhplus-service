
### 사용자 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant UserFacadeService
    participant UserService
    participant PointService
    participant DB

    User ->> Controller: 사용자 정보 조회 요청
    activate Controller

    Controller ->> UserFacadeService: 사용자 정보 조회 요청
    activate UserFacadeService

    UserFacadeService ->> UserService: 사용자 정보 조회 요청
    activate UserService

    UserService ->> DB: 사용자 정보 조회
    activate DB
    DB -->> UserService: 사용자 정보 반환
    deactivate DB
    UserService -->> UserFacadeService: 사용자 정보 or null
    deactivate UserService

    alt 사용자 정보가 존재하지 않음
        UserFacadeService -->> Controller: 사용자 정보 없음
        Controller -->> User: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보가 존재함
        UserFacadeService ->> PointService: 포인트 조회 요청
        activate PointService

        PointService ->> DB: 사용자 포인트 정보 조회
        activate DB
        DB -->> PointService: 포인트 정보 반환
        deactivate DB

        PointService -->> UserFacadeService: 포인트 정보 전달
        deactivate PointService

        UserFacadeService -->> Controller: 사용자 정보 + 포인트 정보 전달
        Controller -->> User: "200 OK"<br>사용자 정보 및 포인트 응답
    end

    deactivate UserFacadeService
    deactivate Controller
```

---

### 포인트 충전 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant PointFacadeService
    participant UserService
    participant PointService
    participant DB


    User ->> Controller: 사용자 정보 조회 요청
    activate Controller

    Controller ->> PointFacadeService: 사용자 정보 조회 요청
    activate PointFacadeService

    PointFacadeService ->> UserService: 사용자 정보 조회 요청
    activate UserService

    UserService ->> DB: 사용자 정보 조회
    activate DB
    DB -->> UserService: 사용자 정보 반환
    deactivate DB

    UserService -->> PointFacadeService: 사용자 정보 or null
    deactivate UserService

    alt 사용자 정보가 존재하지 않음
        PointFacadeService -->> Controller: 사용자 정보 없음
        Controller -->> User: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보가 존재함
        PointFacadeService ->> PointService: 포인트 충전 요청
        activate PointService
        alt 충전 금액이 유효하지 않음
            Note left of PointService: 충전 금액 누락<br>충전 최소/최대 기준 미달<br>충전 후 잔액이 1,000,000원 초과
            PointService -->> PointFacadeService: 금액 검증 실패
            PointFacadeService -->> Controller: 금액 검증 실패
            Controller -->> User: "400 Bad Request"<br>충전 금액이 잘못되었습니다
        else 충전 금액이 유효함
            Note left of DB: 포인트 잔액 업데이트<br>포인트 히스토리 저장
            PointService ->> DB: 포인트 잔액 업데이트
            activate DB
            DB -->> PointService: 충전 후 포인트 반환
            deactivate DB
            deactivate PointService
            
            PointService -->> PointFacadeService: 

            PointFacadeService -->> Controller: 사용자 ID 및 포인트 정보 전달
            Controller -->> User: "200 OK"<br>충전 완료 + 사용자 포인트 응답
        end
    end

    deactivate PointFacadeService
    deactivate Controller
```

---



### 상품 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant ProductFacadeService
    participant ProductService
    participant DB

    User ->> Controller: 상품 정보 조회 요청
    activate Controller

    Controller ->> ProductFacadeService: 상품 정보 조회 요청
    activate ProductFacadeService

    ProductFacadeService ->> ProductService: 상품 정보 조회 요청
    activate ProductService

    ProductService ->> DB: 상품 정보 조회
    activate DB
    DB -->> ProductService: 상품 정보 반환
    deactivate DB

    alt 상품 정보 없음 or 상태가 중단, 품절
        ProductService -->> ProductFacadeService: 상품 정보 없음 or 상태가 중단, 품절
        ProductFacadeService -->> Controller: 상품 검증 실패
        Controller -->> User: "400 or 404"<br>구매할 수 없는 상품입니다
    else 상품 정보와 상태가 정상
        ProductService -->> ProductFacadeService: 상품 정보 전달
        deactivate ProductService

        ProductFacadeService -->> Controller: 상품 정보 전달
        Controller -->> User: "200 OK"<br>상품 정보 응답
    end

    deactivate ProductFacadeService
    deactivate Controller
```

---

### 선착순 쿠폰 발급 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant CouponFacadeService
    participant UserService
    participant CouponService
    participant DB

    User ->> Controller: 쿠폰 발급 요청
    activate Controller

    Controller ->> CouponFacadeService: 쿠폰 발급 처리 요청
    activate CouponFacadeService

    CouponFacadeService ->> UserService: 사용자 정보 조회 요청
    activate UserService

    UserService ->> DB: 사용자 정보 조회
    activate DB
    DB -->> UserService: 사용자 정보 반환
    deactivate DB

    UserService -->> CouponFacadeService: 사용자 정보 전달
    deactivate UserService

    alt 사용자 정보가 존재하지 않음
        CouponFacadeService -->> Controller: 사용자 정보 없음
        Controller -->> User: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보가 존재함
        CouponFacadeService ->> CouponService: 쿠폰 발급 요청

        alt 쿠폰 발급 조건을 만족하지 않음
            Note left of CouponService: 쿠폰 발급 불가 사유<br>409: 이미 발급받은 쿠폰<br>400: 수량 없음, 잘못된 쿠폰 ID
            CouponService -->> CouponFacadeService: 쿠폰 발급 실패
            CouponFacadeService -->> Controller: 쿠폰 발급 실패
            Controller -->> User: "400 or 409"<br>쿠폰 발급에 실패했습니다
        else 쿠폰 발급 조건을 만족함
            CouponService ->> DB: 쿠폰 발급 저장
            activate DB
            DB -->> CouponService: 쿠폰 발급 완료
            deactivate DB

            CouponService -->> CouponFacadeService: 발급된 쿠폰 정보 전달
            CouponFacadeService -->> Controller: 발급된 쿠폰 정보 전달
            Controller -->> User: "200 OK"<br>쿠폰 발급 성공
        end
    end

    deactivate CouponFacadeService
    deactivate Controller
```

---

### 쿠폰 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant CouponFacadeService
    participant UserService
    participant CouponService
    participant DB

    User ->> Controller: 사용자 쿠폰 조회 요청
    activate Controller

    Controller ->> CouponFacadeService: 사용자 쿠폰 조회 요청
    activate CouponFacadeService

    CouponFacadeService ->> UserService: 사용자 정보 조회 요청
    activate UserService

    UserService ->> DB: 사용자 정보 조회
    activate DB
    DB -->> UserService: 사용자 정보 반환
    deactivate DB

    UserService -->> CouponFacadeService: 사용자 정보 전달
    deactivate UserService

    alt 사용자 정보가 존재하지 않음
        CouponFacadeService -->> Controller: 사용자 정보 없음
        Controller -->> User: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보가 존재함
        CouponFacadeService ->> CouponService: 사용자 쿠폰 조회 요청
        CouponService -->> CouponFacadeService: 사용자 쿠폰 정보 전달
        CouponFacadeService -->> Controller: 사용자 쿠폰 정보 전달
        Controller -->> User: "200 OK"<br>사용자 쿠폰리스트
        
    end

    deactivate CouponFacadeService
    deactivate Controller
```

---

### 주문 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant OrderFacadeService
    participant UserService
    participant OrderService
    participant PaymentService
    participant DataPlatform
    participant DB

    User ->> Controller: 주문 요청
    activate Controller

    Controller ->> OrderFacadeService: 주문 요청
    activate OrderFacadeService

    OrderFacadeService ->> UserService: 사용자 조회 요청
    activate UserService

    UserService ->> DB: 사용자 정보 조회
    activate DB
    DB -->> UserService: 사용자 정보 반환
    deactivate DB

    UserService -->> OrderFacadeService: 사용자 정보 전달
    deactivate UserService

    alt 사용자 정보가 존재하지 않음
        OrderFacadeService -->> Controller: 사용자 정보 없음
        Controller -->> User: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보가 존재함
        OrderFacadeService ->> OrderService: 주문 생성 요청
        activate OrderService

        OrderService ->> DB: 주문 상품 재고 조회
        activate DB
        DB -->> OrderService: 상품 정보 반환
        deactivate DB

        OrderService -->> OrderFacadeService: 주문 정보 전달 or 검증 실패 사유
        deactivate OrderService

        alt 주문 조건이 유효하지 않음
            Note left of OrderFacadeService: 400: 상품 품절<br>중복 주문<br>재고 부족
            OrderFacadeService -->> Controller: 주문 검증 실패
            Controller -->> User: "400 Bad Request"<br>주문 조건이 유효하지 않습니다
        else 주문 조건이 유효함
            OrderFacadeService ->> PaymentService: 결제 처리 요청
            activate PaymentService

            PaymentService ->> DB: 포인트, 재고, 쿠폰 정보 조회
            activate DB
            DB -->> PaymentService: 검증 대상 데이터 반환
            deactivate DB

            alt 결제 검증 조건을 만족하지 않음
                Note right of OrderFacadeService: 400: 포인트 부족, 쿠폰 만료, 재고 부족
                PaymentService -->> OrderFacadeService: 결제 검증 실패
                OrderFacadeService -->> Controller: 결제 실패
                Controller -->> User: "409 Conflict"<br>결제 실패 (재고 부족, 쿠폰 만료 등)
            else 결제 검증 조건을 만족함
                PaymentService ->> DB: 결제 정보 저장
                activate DB
                DB -->> PaymentService: 저장 완료
                deactivate DB

                PaymentService ->> DataPlatform: 주문 데이터 전송
                PaymentService -->> OrderFacadeService: 결제 완료 정보 전달
                deactivate PaymentService

                OrderFacadeService -->> Controller: 결제 완료 정보 전달
                Controller -->> User: "200 OK"<br>결제 완료
            end
        end
    end

    deactivate OrderFacadeService
    deactivate Controller

```

---

### 인기 상품 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant ProductFacadeService
    participant ProductService
    participant Redis as 캐시
    participant DB

    User ->> Controller: 상위 상품 조회 요청
    activate Controller

    Controller ->> ProductFacadeService: 상위 상품 조회 요청
    activate ProductFacadeService

    ProductFacadeService ->> ProductService: 상위 상품 조회 요청
    activate ProductService

    ProductService ->> Redis: 최근 3일치 일별 통계 캐시 조회
    activate Redis

    alt 캐시가 존재함
        Redis -->> ProductService: 일별 통계 데이터 반환
        deactivate Redis
        ProductService ->> ProductService: 상품별 수량 집계 및 상위 5개 추출
        ProductService -->> ProductFacadeService: 계산된 상품 리스트 전달
        ProductFacadeService -->> Controller: 계산된 상품 리스트 전달
        Controller -->> User: "200 OK"<br>상위 상품 리스트 응답
    else 캐시가 존재하지 않음
        ProductService ->> DB: 최근 3일치 판매 통계 조회
        activate DB
        DB -->> ProductService: 통계 데이터 반환
        deactivate DB

        ProductService ->> ProductService: 상품별 수량 집계 및 상위 5개 추출
        ProductService -->> ProductFacadeService: 계산된 상품 리스트 전달
        ProductFacadeService -->> Controller: 계산된 상품 리스트 전달
        Controller -->> User: "200 OK"<br>상위 상품 리스트 응답
    end

    deactivate ProductService
    deactivate ProductFacadeService
    deactivate Controller
```

---

### 인기 상품 통계 스케줄러

```mermaid
sequenceDiagram
    participant Scheduler
    participant ProductFacadeService
    participant ProductService
    participant Redis
    participant DB

    loop 매일 자정
        Scheduler ->> ProductFacadeService: 상위 상품 통계 갱신 요청
        activate ProductFacadeService

        ProductFacadeService ->> ProductService: 상위 상품 통계 생성 요청
        activate ProductService

        ProductService ->> DB: 전날 판매 데이터 조회
        activate DB
        DB -->> ProductService: 판매 데이터 반환
        deactivate DB

        ProductService ->> Redis: 상위 상품 캐시 저장 요청
        activate Redis
        Redis -->> ProductService: 캐시 저장 완료
        deactivate Redis

        ProductService -->> ProductFacadeService: 캐시 저장 완료 전달
        deactivate ProductService

        ProductFacadeService -->> Scheduler: 통계 갱신 완료 응답
        deactivate ProductFacadeService
    end    
    
```

