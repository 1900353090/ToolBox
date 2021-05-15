package cn.wcy.aop.redisAop.lock.annotation;

import java.lang.annotation.*;

/**
 * <p>Title : LockParaKey.java</p>
 * <p>Description : 如果根据参数进行锁可以在参数上加上这个注解</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/11/18 11:23
 * @version : 0.0.1
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockParaKey {



}
