package cn.wcy.aop.redisAop.lock.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

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

    @Getter
    @AllArgsConstructor
    enum Type {
        MODULE(1, "模块自定义，同一个模块根据这个加锁"),
        CLASS(2, "类自定义，同一个类根据这个加锁"),
        GLOBAL(3, "全局自定义，所有模块都根据这个加锁");
        private Integer value;
        private String desc;
    }

    //模块类型
    @Value("${spring.application.name}")
    String projectName() default "";

    Type type() default Type.CLASS;

    String key() default "";
    
}
