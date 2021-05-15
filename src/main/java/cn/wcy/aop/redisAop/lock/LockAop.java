package cn.wcy.aop.redisAop.lock;

import cn.wcy.aop.redisAop.lock.annotation.*;
import cn.wcy.encryption.MD5;
import cn.wcy.redis.RedisService;
import cn.wcy.util.BeanUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title : LockAop.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/11/11 10:53
 * @version : 0.0.1
 */
@Aspect
@Component
public class LockAop {

    @Autowired
    private RedisService redisService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LockAop.class);

    /**
     * @Description: 获取方法注解
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-14:32
     */
    public static <T> T getAnnotation(ProceedingJoinPoint joinPoint, Class<T> tClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return BeanUtil.get(tClass,method.getAnnotation(Lock.class));
    }

    /**
     * @Description: 获取入参类型
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-15:56
     */
    public static Map<Class<?>, Integer> getKeyAndValue(JoinPoint joinPoint) {
        Map<Class<?>, Integer> map = new ConcurrentHashMap<>(joinPoint.getArgs().length);
        // 得到类对象
        if (Objects.isNull(joinPoint.getArgs())) {
            return null;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        System.out.println(signature.getParameterTypes());
        for(int i=0;i<joinPoint.getArgs().length;i++) {
            map.put(signature.getParameterTypes()[i], i);
        }
        return map;
    }

    /**
     * @Description: 拼接参数key
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/11/18-11:44
    */
    public String jointKey(Object para) {
        if(Objects.isNull(para)) {
            return "";
        }
        Class<?> c = para.getClass();
        //获取c的当前成员变量的属性描述器
        StringBuilder key = new StringBuilder();
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(c);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors) {
            LockParaKey esColumn = propertyDescriptor.getPropertyType().getAnnotation(LockParaKey.class);
            if(Objects.isNull(esColumn)) {
                continue;
            }
            //获取当前属性描述的读取方法
            Object value = propertyDescriptor.getReadMethod();
            if(Objects.nonNull(value) && value.toString().length() > 0) {
                key.append("-").append(value);
            }
        }
        return key.toString();
    }

    /**
     * @Description: 获取参数key
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/11/18-11:45
    */
    public String getParaKey(ProceedingJoinPoint joinPoint) {
        StringBuilder key = new StringBuilder();
        if(joinPoint.getArgs().length > 0) {
            for(Object arg:joinPoint.getArgs()) {
                if(arg instanceof Collection) {
                    Collection<Object> collection = (Collection<Object>) arg;
                    for(Object obj:collection) {
                        String jointKey = jointKey(obj);
                        if(Strings.isNotBlank(jointKey)) {
                            key.append(jointKey);
                        }
                    }
                }else {
                    String jointKey = jointKey(arg);
                    if(Strings.isNotBlank(jointKey)) {
                        key.append(jointKey);
                    }
                }
            }
        }
        return key.toString();
    }

    /**
     * @Description: 拼接参数key
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/11/18-11:44
     */
    public String jointKey(JoinPoint joinPoint) {
        Object[] paras = joinPoint.getArgs();
        if(ArrayUtils.isEmpty(paras)) {
            return "all";
        }
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature )joinPoint.getSignature();
        Method method = signature.getMethod();
        //参数注解，1维是参数位置，2维是注解数量
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        StringBuilder key = new StringBuilder();
        A: for(int i = 0;i<parameterAnnotations.length;i++) {
            Object para = paras[i];
            Annotation[] annotations = parameterAnnotations[i];
            if(Objects.nonNull(para) && annotations.length > 0) {
                for(Annotation annotation:annotations) {
                    //验证是否为redis缓存key注解
                    if(LockParaKey.class.equals(annotation.annotationType())) {
                        String value = para.toString();
                        if(Objects.nonNull(value) && value.length() > 0) {
                            key.append("-").append(value);
                        }
                        continue A;
                    }
                }
            }
            String value = jointKey(para);
            if(Strings.isNotBlank(value)) {
                key.append("-").append(value);
            }
        }
        if(key.length() == 0) {
            key.append("all");
        }
        return MD5.valueOf(key.toString());
    }

    /**
     * @Description: 环绕
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-14:15
     */
    @Around(value = "@annotation(cn.wcy.aop.redisAop.lock.annotation.Lock)")
    public Object lockScan(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Lock lock = getAnnotation(joinPoint, Lock.class);
        //获取类全路径
        String prefix = Class.forName(joinPoint.getTarget().getClass().getName()).getName();
        //获取方法名称
        StringBuilder key = new StringBuilder();
        key.append(joinPoint.getSignature().getName()).append("-").append(jointKey(joinPoint));
        try {
            redisService.setIfAbsentEXDoWhile(prefix, key.toString(), lock.expireSeconds());
            return joinPoint.proceed();
        }catch (Exception e) {
            LOGGER.error("锁环绕异常:{}", e.getMessage());
            throw e;
        }finally {
            redisService.releaseLock(key.toString(), prefix);
        }
    }

}
