package kr.hhplus.be.server.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(kr.hhplus.be.server.lock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DistributedLock lock = signature.getMethod().getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + DistributedLockKeyGenerator.generate(signature.getParameterNames(), joinPoint.getArgs(), lock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            if (!rLock.tryLock(lock.waitTime(), lock.leaseTime(), lock.timeUnit())) {
                throw new InterruptedException();
            }
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            rLock.unlock();
        }

    }

}
