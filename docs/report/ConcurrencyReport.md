### 📝 쿠폰 발급 동시성 이슈 분석 및 해결 보고서

##### 문제 배경

사용자의 요청에 따라 쿠폰을 1회만 발급하는 기능을 제공하며,
프로모션 기간 중에는 대량의 동시 요청이 몰리는 구조입니다.
특히, 쿠폰 수량이 제한된 '선착순 발급' 기능에서는 요청 충돌이 빈번하게 발생할 수 있으며,
동시성 제어가 없을 경우 중복 발급 또는 재고 수량을 초과하는 쿠폰 발급이 발생할 수 있는 경쟁 상태(Race Condition)가 존재합니다.

##### 문제 식별 (AS-IS 상태)

현재 쿠폰 발급은 아래의 흐름으로 동작합니다:

```java
@Transactional
public void createCoupon(CouponCommand command) {
    userService.getUserInfo(command.getUserId());
    couponService.validateCouponIssue(command); // 수량 차감
    couponService.createUserCoupon(command);    // 유저-쿠폰 저장
}
```

핵심 로직은 Coupon.validateAndDecreaseLeftQty()에서
쿠폰 수량을 차감하는 방식으로 구현되어 있으며, JPA 영속성 컨텍스트를 통해 leftQty-- 연산이 반영됩니다.

```java
public void validateAndDecreaseLeftQty() {
    if (leftQty <= 0) throw new CouponOutOfStockException();
    leftQty--; // 영속 상태 객체이므로 flush 시점에 DB 반영
}
```

문제점:

- 동시 요청 시 여러 트랜잭션이 leftQty를 동시에 읽고 차감 시도
- 조건 검증(leftQty > 0)이 통과되어도 실제 DB 반영 이전에 여러 쓰레드가 진입 가능
- 결과적으로 쿠폰이 중복 발급되거나, leftQty가 음수로 저장되는 현상 발생

##### 🔐 해결 방안 (TO-BE 개선)

###### 🔸적용 전략: 비관적 락 (Pessimistic Lock)

선착순 쿠폰 발급은 "먼저 도착한 요청"이 발급되어야 하므로,
꼭 처리되어야 하며 동시에 여러 요청이 충돌할 가능성이 높습니다.
따라서 락으로 자원을 선점하는 비관적 락 전략이 적합합니다.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId")
Optional<Coupon> findByIdWithLock(Long couponId);
```

→ 해당 쿼리는 조회 시점에 DB에서 FOR UPDATE 락을 걸고,
다른 트랜잭션이 같은 쿠폰을 동시에 수정하지 못하도록 방지합니다.

**개선 후 기대 효과**

- leftQty는 락이 선점된 트랜잭션만 수정할 수 있음
- 중복 발급, 초과 발급 방지
- 순차적 처리가 가능해 "선착순" 의미 유지 가능



##### 대안 비교 (낙관적 락 vs 비관적 락)


| 항목            | 낙관적 락           | 비관적 락               |
| --------------- | ------------------- | ----------------------- |
| **락 방식**     | `@Version` 사용     | `FOR UPDATE` 방식       |
| **충돌 처리**   | 예외 발생 → 재시도 | 요청 자체를 대기시킴    |
| **성능**        | 낮은 충돌 시 유리   | 높은 충돌 시 안정적     |
| **적합한 상황** | 충돌이 드문 경우    | 꼭 성공해야 할 트랜잭션 |



##### 실제 적용 예시

```java
@Transactional
public void createCoupon(CouponCommand command) {
    userService.getUserInfo(command.getUserId());

    // 락 걸린 상태로 쿠폰 조회
    Coupon coupon = couponRepository.findByIdWithLock(command.getCouponId())
                                    .orElseThrow(...);

    coupon.validateAndDecreaseLeftQty(); // 수량 차감
    couponService.createUserCoupon(command);
}
```

###### 보완점 및 한계

- 비관적 락은 처리 순서를 보장하진 않음 (락 선점 순서 = 요청 순서 X)
- 트래픽이 몰릴 경우 데드락이나 대기 시간 증가 가능
- 비공정 락이기 때문에 "선착순"을 완벽하게 보장하려면 Kafka 등의 메시지 큐 기반 직렬 처리 방식 고려 필요

###### 결론 요약

- 문제: 쿠폰 중복 발급, 수량 초과 발생
- AS-IS: 단순 수량 차감(leftQty--) → 충돌 제어 없음
- TO-BE: @Lock(PESSIMISTIC_WRITE) 적용하여 충돌 차단
- 효과: 중복 방지, 정확한 수량 관리
- 보완: 완전한 순차성 보장은 메시지 큐 기반 처리 필요
