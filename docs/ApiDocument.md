##### 📌 API Specification

###### 1️⃣ 사용자 잔액 조회 API
##### GET `/user/{userId}`
사용자 ID를 통해 현재 잔액을 조회합니다.

###### 🔸 Path Variable
| 이름 | 타입 | 설명 |
|------|------|------|
| userId | Long | 사용자 고유 ID |

###### 🔸 Response Body
```json
{
  "user_id": 1,
  "amount": 10000
}
```

###### 2️⃣ 포인트 충전 API
##### PATCH `/point/charge`
사용자 포인트를 충전합니다.

###### 🔸 Request Body
```json
{  "code": 200,
  "message": "성공",
  "data": {
    "user_id": 1,
    "amount": 10000
  }
}
```

###### 🔸 Response Body
```json
{  "code": 200,
  "message": "성공",
  "data": {
    "user_id": 1,
    "amount": 10000
  }
}
```

###### 3️⃣ 상품 조회 API
##### GET `/product/{productId}`
상품 ID를 기반으로 상품 정보를 조회합니다.

###### 🔸 Path Variable
| 이름 | 타입 | 설명 |
|-------------|------|----------------|
| productId | Long | 상품 고유 ID |

###### 🔸 Response Body
```json
{
  "code": 200,
  "message": "성공",
  "data": {
    "productId": 101,
    "name": "신발",
    "price": 58000,
    "status": "상품 판매",
    "options": [
      {
        "optionId": 1,
        "name": "흰색",
        "price": 30000,
        "stockQty": 20
      },
      {
        "optionId": 2,
        "name": "검정색",
        "price": 20000,
        "stockQty": 30
      }
    ]
  }
}
```

###### 4️⃣ 쿠폰 발급 API
##### POST `/coupon/send/{couponId}`
특정 쿠폰을 사용자에게 발급합니다.

###### 🔸 Path Variable
| 이름 | 타입 | 설명 |
|-------------|------|--------------|
| couponId | Long | 쿠폰 고유 ID |

###### 🔸 Response Body
```json
{
  "code": 200,
  "message": "성공",
  "data": {
    "couponId": 1,
    "name": "5,000원 할인쿠폰",
    "discountAmount": 5000,
    "expiresAt": "20250501"
  }
}
```

###### 5️⃣ 사용자 쿠폰조회 API
##### GET `/user/coupon/{userId}`
사용자의 쿠폰 리스트를 조회합니다.

###### 🔸 Path Variable
| 이름 | 타입 | 설명 |
|-------------|------|--------------|
| couponId | Long | 쿠폰 고유 ID |

###### 🔸 Response Body
```json
{
  "code": 200,
  "message": "성공",
  "data": [
    {
      "couponId": 1,
      "name": "5,000원 할인쿠폰",
      "discountAmount": 5000,
      "expireAt": "20250501"
    },
    {
      "couponId": 2,
      "name": "1,000원 할인쿠폰",
      "discountAmount": 1000,
      "expireAt": "20251001"
    }
  ]
}
```

###### 6️⃣ 결제 API
##### POST `/order/payment`
사용자가 상품 결제를 합니다

###### 🔸 Request Body
```json
{
  "userId": 1,
  "couponId": 1,
  "items": [
    {
      "productId": 101,
      "optionId": 201,
      "productPrice": 5000,
      "orderQty": 2
    },
    {
      "productId": 102,
      "optionId": 202,
      "productPrice": 19000,
      "orderQty": 1
    }
  ]
}
```


###### 🔸 Response Body
```json
{
  "code": 200,
  "message": "성공",
  "data": {
    "userId": 1,
    "totalAmount": 27000,
    "orderAt": 20250404091000
  }
}
```



###### 7️⃣ 상위 상품 조회 API
##### GET `/product/ranking`
최근 3일간 많이 팔린 상위 5개 상품을 조회합니다.

###### 🔸 Response Body
```json
{
  "code": 200,
  "message": "성공",
  "data": [
    {
      "productId": 1,
      "name": "후드티",
      "price": 30000,
      "status": "상품 판매"
    },
    {
      "productId": 2,
      "name": "맨투맨",
      "price": 35000,
      "status": "상품 판매"
    },
    {
      "productId": 3,
      "name": "블라우스",
      "price": 28000,
      "status": "상품 판매"
    },
    {
      "productId": 4,
      "name": "신발",
      "price": 32000,
      "status": "상품 판매"
    },
    {
      "productId": 5,
      "name": "슬리퍼",
      "price": 27000,
      "status": "상품 판매"
    }
  ]
}
```
