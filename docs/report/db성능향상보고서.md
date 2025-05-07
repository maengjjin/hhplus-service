# DB 성능 개선 보고서

1. 쿠폰조회
2. 유저쿠폰 조회
3. 한상품으로 옵션들 조회
4. 단품옵션 조회
5. 상품찾기
6. 유저조회
7. 스케줄러 그룹바이 조회

시스템 내 주요 조회 쿼리 중 다음 세 가지가 성능 병목 가능성이 높은 쿼리로 분석되었습니다:

2. 유저 쿠폰 조회
3. 한 상품에 대한 옵션들 조회
4. 스케줄러 그룹 통계 조회
   이 중, 1번과 2번은 PK가 아닌 컬럼으로 조인 및 조건 필터링을 수행하고 있으며,
   3번은 날짜 기준으로 그룹 통계를 집계하는 쿼리로, 각각에 대해 인덱스를 적용하고 성능을 비교하였습니다.

---

#### ✅ 유저 쿠폰 조회

- 대상 테이블: `user_coupon`
- 총 데이터 건수: 359,765건
- 구조: 500종 쿠폰 × 쿠폰당 약 360건 발급

##### 공통 실행 쿼리

```sql
EXPLAIN ANALYZE
SELECT * 
FROM user_coupon uc 
INNER JOIN coupon c ON uc.coupon_id = c.coupon_id
where uc.user_id = 1
and uc.coupon_id = 500;
```

##### 🔹 인덱스 적용 전

```
-> Filter: ((uc.coupon_id = 500) and (uc.user_id = 1))  (cost=35762 rows=3547) (actual time=1.21..130 rows=1 loops=1)
    -> Table scan on uc  (cost=35762 rows=354656) (actual time=1.15..111 rows=359765 loops=1)
```

##### 🔹 인덱스 적용 후

```sql
CREATE INDEX idx_user_coupon_user_coupon ON user_coupon(user_id, coupon_id);
```

```
-> Index lookup on uc using idx_user_coupon_user_coupon 
(user_id=1, coupon_id=500) (cost=0.35 rows=1) (actual time=0.111..0.114 rows=1 loops=1)

```

- user_id와 coupon_id는 기본키는 아니지만, 고객이 보유한 쿠폰을 조회할 때 자주 사용되는 조건이므로 복합 인덱스를 적용함


##### 📊 성능 비교 요약


| 항목          | 인덱스 미적용    | 인덱스 적용  | 개선도    |
| ------------- | ---------------- | ------------ | --------- |
| 실행 시간     | 130ms            | 0.114ms      | -99.9%    |
| 비용 (cost)   | 35,762           | 0.35         | -99.99%   |
| 처리 행 수    | 359,765건 → 1건 | 1건          | ✅ 최소화 |
| 검색 방식     | Full Scan        | Index Lookup | ✅ 최적화 |
| 불필요한 읽기 | 359,764건        | 0건          | ✅ 제거됨 |

##### 🔍 성능 향상 계산 공식

- **실행 시간 감소율**
  `(130 - 0.114) / 130 × 100 ≈ 99.91% 감소`
- **비용 감소율**
  `(35762 - 0.35) / 35762 × 100 ≈ 99.99% 감소`

##### 📌결론

- `user_id`와 `coupon_id`를 함께 조회하는 조건에서는, **복합 인덱스(user_id, coupon_id)** 생성 시 성능이 극적으로 개선됨.
- 전체 테이블 스캔 대비 **응답 속도는 약 99.9% 빨라지고**, **불필요한 행 읽기는 제거**되어 DB 부하도 현저히 감소함.
- 해당 쿼리가 빈번할 경우, 복합 인덱스는 필수적임.

---

### ✅ 상품 옵션 조회 성능

- 대상 테이블: `product_option`
- 총 데이터 건수: 300,000건
- 구조: 500개의 상품 × 상품옵션 약 1000 ~ 140개

```sql
EXPLAIN ANALYZE
SELECT * 
FROM product_option po
INNER JOIN product p ON po.product_id = p.product_id
WHERE po.product_id = 10
  AND po.option_id = 9001;
```

##### 🔹 실행 계획 비교

| 항목 | 인덱스 미적용 (기존) | 인덱스 적용 후 (강제 사용 포함) |
|------|----------------------|----------------------------------|
| Fetched Info | `Rows fetched before execution` | 동일 |
| 예측 row 수 | rows=1 | rows=1 |
| 실행 시간 | `actual time=72e-6..152e-6` | `actual time=84e-6..148e-6` |
| loops        | 1회 | 1회 |
| 차이 | ❌ 거의 없음 | ❌ 거의 없음 |


##### ⚠️ 성능 개선이 없었던 이유

1. **조회 결과가 1건뿐**
    - 단일 레코드 조회로 이미 매우 빠름 (0.0001~0.00015초 수준)

2. **`option_id`가 PK**
    - 이미 인덱스 존재 → `option_id`로 탐색하는 순간 인덱스 효율 확보됨

3. **`SELECT *` 모든 컬럼 조회**
    - 인덱스는 `product_id`만 포함 → 나머지 컬럼 조회 위해 어차피 테이블 접근 발생 (Covering Index 아님)

4. **옵티마이저 판단상 인덱스 사용해도 이득 거의 없음**
    - 읽는 row 수, 비교 범위가 좁음 → Index Lookup vs Table Access 차이 거의 없음


##### 📌 결론

- `product_id + option_id` 조건이지만, `option_id` 자체가 PK이고 결과도 1건이라서 **인덱스를 강제로 써도 성능 향상이 발생하지 않음**
- **실행 시간이 이미 마이크로초 수준**이라 인덱스 개선 효과가 의미 없음
- 향후 최적화가 필요한 경우:
    - 조회 대상이 많은 `product_id` 조건만 사용할 경우
    - `ORDER BY`, `LIMIT`, `GROUP BY` 등 성능 영향을 주는 쿼리 형태일 때 고려

---

### ✅ 인기상품 조회 집계

##### 📌 조회 목적

- 최근 3일간 주문 데이터를 조회하여,
- `product_id`별 **총 주문 수량(SUM)** 기준으로 **가장 많이 팔린 5개 상품**을 정렬하여 조회

```sql
EXPLAIN ANALYZE
SELECT product_id
FROM order_detail
WHERE create_at >= CURDATE() - INTERVAL 3 DAY
  AND create_at < CURDATE()
GROUP BY product_id
ORDER BY SUM(order_quantity) DESC
LIMIT 5;
```

##### 🔹 인덱스 적용 전

```text
-> Table scan on <temporary>  (cost=2.96..2.96 rows=1) (actual time=491..492 rows=10000 loops=1)
    -> Temporary table with deduplication  (cost=0.45..0.45 rows=1) (actual time=491..491 rows=10000 loops=1)
        -> Filter: ((order_detail.create_at >= <cache>((curdate() - interval 3 day))) and (order_detail.create_at < <cache>(curdate())))  (cost=0.35 rows=1) (actual time=0.386..352 rows=400000 loops=1)
            -> Table scan on order_detail  (cost=0.35 rows=1) (actual time=0.126..297 rows=400000 loops=1)
```


##### 🔹 인덱스 적용 후

```sql
CREATE INDEX idx_order_detail_create_at_product_id ON order_detail(create_at, product_id);
```

```
-> Table scan on <temporary>  (cost=2.96..2.96 rows=1) (actual time=194..195 rows=10000 loops=1)
    -> Temporary table with deduplication  (cost=0.45..0.45 rows=1) (actual time=194..194 rows=10000 loops=1)
        -> Filter: ((order_detail.create_at >= <cache>((curdate() - interval 3 day))) and (order_detail.create_at < <cache>(curdate())))  (cost=0.35 rows=1) (actual time=0.0239..138 rows=400000 loops=1)
            -> Covering index scan on order_detail using idx_order_detail_create_at_product_id  (cost=0.35 rows=1) (actual time=0.0167..96.5 rows=400000 loops=1)

```

##### 📊 성능 비교 요약

| 항목             | 인덱스 미적용         | 인덱스 적용 후         | 개선 효과        |
|------------------|------------------------|--------------------------|------------------|
| 접근 방식        | Full Table Scan        | Covering Index Scan      | ✅ 빠른 범위 스캔 |
| 총 실행 시간     | 약 492ms               | 약 195ms                 | ⬇️ 약 60% 단축   |
| 필터링 처리 시간 | 0.386..352             | 0.0239..138              | ✅ 큰 폭 개선     |
| 내부 테이블 처리 | 필요                   | 동일                     | -                |


##### 🔍 성능 향상 계산 공식

- **실행 시간 감소율**  
  `(492 - 195) / 492 × 100 ≈ 60.37% 감소`

- **필터링 처리 시간 감소율**  
  `(352 - 138) / 352 × 100 ≈ 60.79% 감소`

- **데이터 접근 시간 감소율**  
  `(297 - 96.5) / 297 × 100 ≈ 67.51% 감소`


##### 📌 결론

- order_detail.create_at 조건이 자주 사용되고, product_id와 함께 조회/집계되는 패턴이므로  create_at, product_id 복합 인덱스를 적용한 결과 약 60% 이상의 성능 개선이 확인됨
- Full Table Scan → Covering Index Scan으로 변경되어 I/O 비용 절감 및 빠른 결과 응답이 가능해짐
- 정렬 및 집계 성능이 중요한 스케줄러/통계성 쿼리에 대해서는 적절한 복합 인덱스가 매우 효과적임