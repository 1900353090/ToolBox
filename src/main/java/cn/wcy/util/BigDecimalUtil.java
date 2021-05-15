package cn.wcy.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * <p>Title : BigDecimalUtil.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/3/17 11:35
 * @version : 0.0.1
 */
public class BigDecimalUtil {

    /**
     * @Description: 转换小数位数
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/14-11:04
     */
    public static BigDecimal setScale(BigDecimal value, Integer scale) {
        if(Objects.nonNull(value)) {
            return value.setScale(scale,BigDecimal.ROUND_DOWN);
        }else {
            return new BigDecimal(0);
        }
    }

    /**
     * @Description: 转换小数位数
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/14-11:04
     */
    public static BigDecimal setScale(String value, Integer scale) {
        if(StringUtils.isNotEmpty(value)) {
            return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_DOWN);
        }else {
            return new BigDecimal(0);
        }
    }

    /**
     * @Description: 转换小数位数并自动重新改变传入参数的值
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/14-11:04
     */
    public static void setScaleAndAssign(Object obj, Integer scale) {
        try {
            if(Objects.nonNull(obj)) {
                Class<?> c = obj.getClass();
                Field[] fields = c.getDeclaredFields();
                for(Field field : fields) {
                    if(field.getType().isAssignableFrom(BigDecimal.class)) {
                        String name = field.getName();
                        name = StringUtil.toUpperCaseFirstOne(name);
                        Method get = c.getMethod("get" + name);
                        Object invoke = get.invoke(obj);
                        BigDecimal value = new BigDecimal(0);
                        if(Objects.nonNull(invoke)) {
                            value = (BigDecimal)invoke;
                        }
                        value = value.setScale(scale, BigDecimal.ROUND_DOWN);
                        Method set = c.getMethod("set" + name, BigDecimal.class);
                        set.invoke(obj,value);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 转换小数位数并自动重新改变传入参数的值
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/14-11:04
     */
    public static void setScaleAndAssign(String value, Integer scale) {
        try {
            if(Objects.nonNull(value)) {
                Field value1 = value.getClass().getDeclaredField("value");
                value1.setAccessible(true);
                if(StringUtils.isNotEmpty(value)) {
                    String string = new BigDecimal(value).setScale(scale, BigDecimal.ROUND_DOWN).toString();
                    value1.set(value,string.toCharArray());
                }else {
                    value1.set(value,new char[]{'0'});
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
