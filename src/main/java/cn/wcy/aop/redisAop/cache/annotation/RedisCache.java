package cn.wcy.aop.redisAop.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title : Cache.java</p>
 * <p>Description : aop切面缓存</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/7/15 10:35
 * @version : 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {

    /**
     * @Description: 过期时间
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-10:41
    */
    long expirationTime() default  -1L;

    /**
     * @Description: 过期时间类型
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-13:37
    */
    TimeUnit timeFormat() default TimeUnit.SECONDS;

    /**
     * @Description: 是否每次访问后都刷新缓存时间
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-10:42
    */
    boolean extensionTimeRefresh() default false;
    
    /** 
     * 是否加锁（防止缓存击穿）
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/27-16:28
     * @version 0.0.1
    */
    boolean lock() default false;

}
