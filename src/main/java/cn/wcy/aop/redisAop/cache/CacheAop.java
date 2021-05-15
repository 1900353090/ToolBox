package cn.wcy.aop.redisAop.cache;

import cn.wcy.aop.redisAop.cache.annotation.RedisCache;
import cn.wcy.aop.redisAop.cache.annotation.RedisCacheParaKey;
import cn.wcy.aop.redisAop.lock.annotation.Lock;
import cn.wcy.encryption.MD5;
import cn.wcy.redis.RedisService;
import cn.wcy.util.BeanUtil;
import cn.wcy.util.FileUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Title : CacheAop.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/7/15 10:54
 * @version : 0.0.1
 */
@Aspect
@Component
public class CacheAop {

    @Autowired
    private RedisService redisService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheAop.class);

    /**
     * @Description: 获取方法注解
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-14:32
    */
    public static <T> T getAnnotation(ProceedingJoinPoint joinPoint, Class<T> tClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return BeanUtil.get(tClass,method.getAnnotation(RedisCache.class));
    }

    /**
     * @Description: 获取入参
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-15:56
    */
    public static Map<String, Object> getKeyAndValue(ProceedingJoinPoint joinPoint) {
        Map<String, Object> map = new HashMap<>(joinPoint.getArgs().length);
        // 得到类对象
        if (null == joinPoint.getArgs()) {
            return null;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        for(int i=0;i<joinPoint.getArgs().length;i++) {
            map.put(signature.getParameterNames()[i],joinPoint.getArgs()[i]);
        }
        return map;
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
                    if(RedisCacheParaKey.class.equals(annotation.annotationType())) {
                        String value = para.toString();
                        if(Objects.nonNull(value) && value.length() > 0) {
                            key.append("-").append(value);
                        }
                        continue A;
                    }
                }
            }
            Class<?> c = para.getClass();
            //获取c的当前成员变量的属性描述器
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(c);
            for(PropertyDescriptor propertyDescriptor:propertyDescriptors) {
                RedisCacheParaKey esColumn = propertyDescriptor.getPropertyType().getAnnotation(RedisCacheParaKey.class);
                if(Objects.isNull(esColumn)) {
                    continue;
                }
                //获取当前属性描述的读取方法
                Object value = propertyDescriptor.getReadMethod();
                if(Objects.nonNull(value) && value.toString().length() > 0) {
                    key.append("-").append(value);
                }
            }
        }
        if(key.length() == 0) {
            key.append("all");
        }
        return key.toString();
    }

    /**
     * @Description: 环绕
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/15-14:15
    */
    @Around(value = "@annotation(cn.wcy.aop.redisAop.cache.annotation.RedisCache)")
    public Object doAroundForService(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        RedisCache redisCache = getAnnotation(joinPoint, RedisCache.class);
        boolean cache = Objects.nonNull(redisCache);
        Map<Object, Object> map = null;
        StringBuilder key = new StringBuilder();
        String item = null;
        if(cache) {
            //获取类全路径
            String clazzName = Class.forName(joinPoint.getTarget().getClass().getName()).getName();
            //获取方法名称
            String methodName = joinPoint.getSignature().getName();
            //拼接缓存key
            key.append(clazzName).append(methodName);
            item = MD5.valueOf(jointKey(joinPoint));
            LOGGER.info("【缓存】item："+item);
            map = redisService.hmGet(key.toString());
            if(Objects.nonNull(map)) {
                if(Objects.nonNull(item)) {
                    Object obj = map.get(item);
                    if(Objects.nonNull(obj)) {
                        if(redisCache.extensionTimeRefresh() && redisCache.expirationTime() > 0L) {
                            redisService.hmset(key.toString(), map, redisCache.expirationTime(), redisCache.timeFormat());
                        }
                        return obj;
                    }
                }
            }
        }
        Object result = null;
        try {
            redisService.setIfAbsentEXDoWhile(key.toString(), item, 60);
            result = joinPoint.proceed();
        }catch (Exception e) {
            LOGGER.error("缓存aop的锁异常:{}", e.getMessage());
            return null;
        }finally {
            redisService.releaseLock(item, key.toString());
        }
        if(cache) {
            if(Objects.isNull(map)) {
                map = new HashMap<>(1);
            }
            map.put(item, result);
            redisService.hmset(key.toString(), map, redisCache.expirationTime(), redisCache.timeFormat());
        }
        return result;
    }

    public static void main(String[] args) {
        ArrayList<String> read = FileUtil.getRead("C:\\Users\\王晨阳\\Desktop\\a.txt");
        System.out.println(read.size());
        Map<String,String> map = new HashMap<>(read.size());
        for(String key:read) {
            map.put(key,"");
        }
        for(String key:map.keySet()) {
            System.out.println(key);
        }
    }

}
