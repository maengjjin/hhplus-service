
### 사용자 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant Service
    participant DB

    User ->> Controller: 사용자 금액 조회 요청 (userId)
    Controller ->> Service: 사용자 금액 조회 요청 (userId)
    Service ->> DB: userId로 사용자 정보 조회
    DB -->> Service: 사용자 정보 or null

    alt 사용자 정보 없음
        Service -->> Controller: `400 Bad Request`
        Controller -->> User: 존재하지 않는 사용자입니다
    else 사용자 id가 있음
        Service -->> Controller: 해당 사용자 정보 전달
        Controller -->> User: 200 OK + id, 포인트 응답
    end
```

---

### 포인트 충전 API

```mermaid
sequenceDiagram
    actor 사용자
    participant Controller
    participant Service
    participant DB

    사용자 ->> Controller: 사용자 금액 충전 요청 (userId, amount)
    Controller ->> Service: 사용자 금액 요청 전달 (userId, amount)
    Service ->> DB: userId로 사용자 정보 조회
    DB -->> Service: 사용자 정보 or null

    alt 사용자 정보 없음
        Service -->> Controller: '유저 검증 예외 처리'
        Controller -->> 사용자: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보 있음
        alt 충전 금액 검증 실패
            Service -->> Controller: '금액 처리 검증 예외 처리'
            Controller -->> 사용자: "400 Bad Request"<br>충전금액이 잘못 되었습니다
        else 충전 금액 검증 성공
            Service -->> DB: 포인트 잔액 업데이트
            Service -->> DB: 포인트 충전 히스토리 저장
            Service -->> Controller: id + 포인트 반환
            Controller -->> 사용자: "200 OK" + id, 포인트 응답
        end
    end
```

---



### 상품 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant Service
    participant DB

    User ->> Controller: 상품 정보 조회 요청 (productId)
    Controller ->> Service: productId 전달
    Service ->> DB: productId로 상품 조회
    DB -->> Service: 상품 정보 반환

    alt 상품 존재하지 않음
        Service -->> Controller: 상품 검증 예외 처리 
        Controller -->> User: '404 Not Found' <br>존재 하지 않는 상품입니다 응답
    else 상품 존재함
        Service -->> Controller: 상품 정보 반환
        Controller -->> User: 200 OK + 상품 정보 응답
    end
```

---

### 선착순 쿠폰 발급 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant Service
    participant DB

    User ->> Controller: 쿠폰 발급 요청
    Controller ->> Service: 전달
    Service ->> DB: 사용자 조회
    DB -->> Service: 사용자 정보 or null

    alt 사용자 없음
        Service -->> Controller: '유저 검증 예외의 처리'
        Controller -->> User: '400 Bad Request' <br>존재하지 않는 사용자입니다
    else 사용자 정보 있음
        alt 쿠폰 발급 검증 실패
            Note left of Service: 쿠폰 발급 검증 예외 발생 <br>409: 이미 발급받음 <br>400: 수량 없음, 잘못 된 쿠폰 id
            Service -->> Controller: 쿠폰 발급 검증 예외 처리
            Controller -->> User: 400 or 409 쿠폰 발급 실패 응답
        else 쿠폰 발급 검증 성공
            Service ->> DB: 발급 쿠폰 저장
            Service -->> Controller: 발급 된 쿠폰 정보 반환
            Controller -->> User: 200 OK + 발급 된 쿠폰 정보 응답
        end
    end
```

---

### 쿠폰 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant Service
    participant DB

    User ->> Controller: 사용자 쿠폰 리스트 조회 요청 (userId)
    Controller ->> Service: 사용자 쿠폰 리스트 조회 (userId)
    Service ->> DB: userId로 사용자 정보 조회
    DB -->> Service: 사용자 정보 or null

    alt 사용자 정보 없음
        Service -->> Controller: '유저 검증 예외 처리'
        Controller -->> User: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보 있음
        Service -->> Controller: 사용자 쿠폰 리스트 정보 반환
        Controller -->> User: 200 OK + 사용자 쿠폰 리스트 정보 응답
    end
```

---

### 주문 내역 저장 API

```mermaid
sequenceDiagram
    actor 사용자
    participant Controller
    participant Service
    participant DB

    사용자 ->> Controller: 사용자 금액 충전 요청 (userId, amount)
    Controller ->> Service: 사용자 금액 요청 전달 (userId, amount)
    Service ->> DB: userId로 사용자 정보 조회
    DB -->> Service: 사용자 정보 or null

    alt 사용자 정보 없음
        Service -->> Controller: '유저 검증 예외 처리'
        Controller -->> 사용자: "400 Bad Request"<br>존재하지 않는 사용자입니다
    else 사용자 정보 있음
        alt 충전 금액 검증 실패
            Note left of Service: 400: 충전 금액 누락, 충전 최소 최대 기준 안됨,<br>충전 후 잔액이 1,000,000원 초과
            Service -->> Controller: '금액 처리 검증 예외 처리'
            Controller -->> 사용자: "400 Bad Request"<br>충전금액이 잘못 되었습니다
        else 충전 금액 검증 성공
            Service -->> DB: 포인트 잔액 업데이트
            Service -->> DB: 포인트 충전 히스토리 저장
            Service -->> Controller: id + 포인트 반환
            Controller -->> 사용자: "200 OK" + id, 포인트 응답
        end
    end
```

---

### 주문 결제 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant Service
    participant DB
    participant DataPlatform

    User ->> Controller: 사용자 결제 요청 (userId, orderId)
    Controller ->> Service: 사용자 결제 요청 (userId, orderId)
    Service ->> DB: userId로 사용자 정보 조회
    DB -->> Service: 사용자 정보 or null

    alt 사용자 정보 없음
        Service -->> Controller: '유저 검증 예외 처리'
        Controller -->> User: "400 Bad Request" <br>존재하지 않는 사용자입니다
    else 사용자 정보 있음 
        alt 결제 검증 실패
            Note left of Service: 404: 존재하지 않은 주문번호 <br>400: 쿠폰 유효기간 만료 <br>포인트 부족
            Service -->> Controller: 결제 검증 예외 처리
            Controller -->> User: 404 or 409 + 결제 실패 응답
        else 결제 검증 성공
            Note right of Service: 포인트 차감, 쿠폰 사용처리, <br>주문 상태 업데이트(결제 완료)
            Service -->> DB: 주문내역 저장
            DB -->> Service: 주문내역 반환
            Service -->> DataPlatform: 주문 정보 전송
            Service -->> Controller: 주문 정보 반환
            Controller -->> User: 200 OK + 주문 내역 응답
        end
    end
```

---

### 인기 상품 조회 API

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Controller
    participant Service
    participant Redis as 캐시
    participant DB

    User ->> Controller: 상위 상품 조회 요청
    Controller ->> Service: 요청 전달
    Service ->> Redis: 3일치 일별 통계 캐시 조회

    alt 캐시 있음
        Redis -->> Service: 각 날짜별 통계 데이터 반환
        Service ->> Service: 상품별 수량 합산 및 상위 5개 추출
        Service -->> Controller: 계산된 상품 리스트
        Controller -->> User: 200 OK + 상품 리스트
    else 캐시 없음
        Service ->> DB: 최근 3일치 판매 통계 조회
        DB -->> Service: 통계 데이터 반환
        Service -->> Controller: 상품 리스트
        Controller -->> User: 200 OK + 상품 리스트
    end
```

---

### 인기 상품 통계 스케줄러

```mermaid
sequenceDiagram
    participant Scheduler
    participant Service
    participant DB
    participant Redis

    Scheduler ->> Service: 통계 데이터 생성 요청
    Service ->> DB: 전날 판매 데이터 조회
    DB -->> Service: 판매 데이터 반환
    Service ->> Redis: 인기 상품 5개 캐시 저장(날짜)
    Redis -->> Service: 저장 완료
    Service -->> Scheduler: 캐시 저장 완료 응답
```

