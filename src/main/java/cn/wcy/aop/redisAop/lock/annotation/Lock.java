package cn.wcy.aop.redisAop.lock.annotation;

import java.lang.annotation.*;

/**
 * <p>Title : Lock.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/11/11 10:47
 * @version : 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    //锁超时时间
    int expireSeconds() default 60;

    //模块类型
    String projectName() default "${spring.application.name}";
    
}
