package cn.wcy.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>Title : BeanUtil.java</p>
 * <p>Description : 对象操作工具</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/5/20 14:07
 * @version : 0.0.1
 */
public class BeanUtil {

    /**
     * @Description: 普通对象转json字符串
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/20-15:48
    */
    public static String toJSONStr(Object obj) {
        try {
            if(Objects.nonNull(obj)) {
                //获取对象类型
                Class<?> c = obj.getClass();
                //获取对象的属性描述器数组
                PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(c);
                final StringBuilder sb = new StringBuilder("{");
                for(int i=0;i<propertyDescriptors.length;i++) {
                    //获取属性名
                    String name = propertyDescriptors[i].getName();
                    //将属性名首字母转成大写
                    String methodName = StringUtil.toUpperCaseFirstOne(name);
                    //获取对象对应名称的方法对象
                    Method get = c.getMethod("get" + methodName);
                    //get.invoke(obj) 传入对应对象执行对象内的这个方法
                    if(i == propertyDescriptors.length-1) {
                        sb.append("\"").append(name).append("\"")
                                .append(":").append("\"").append(get.invoke(obj)).append("\"");
                    }else {
                        sb.append("\"").append(name).append("\"")
                                .append(":").append("\"").append(get.invoke(obj)).append("\"")
                                .append(",");
                    }
                }
                sb.append("}");
                return sb.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 支持对象间值拷贝（不同类型则以属性名相同时映射）
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/20-14:07
    */
    public static <T> T copy(Object obj, final Class<T> clz) {
        try {
            if(Objects.nonNull(obj)) {
                Class<?> c = obj.getClass();
                Object newObj = c.newInstance();
                PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(c);
                for(PropertyDescriptor propertyDescriptor:propertyDescriptors) {
                    String name = propertyDescriptor.getName();
                    name = StringUtil.toUpperCaseFirstOne(name);
                    Method get = c.getMethod("get" + name);
                    Object invoke = get.invoke(obj);
                    Method set = c.getMethod("set" + name, propertyDescriptor.getPropertyType());
                    set.invoke(newObj,invoke);
                }
                //String jsonStr = toJSONStr(newObj);
                //if(StringUtils.isEmpty(jsonStr)) {
                //    return null;
                //}else {
                //    return (T)jsonStr.toCharArray();
                //}
                return get(clz, newObj);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: object转对应class
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/20-14:20
    */
    public static <T> T get(Class<T> clz,Object o) {
        if(clz.isInstance(o)){
            return clz.cast(o);
        }
        return null;
    }

    /**
     * @Description: List值拷贝
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/20-14:07
    */
    public static <T> List<T> copy(List<T> origList, final Class<T> c) {
        try {
            if(CollectionUtils.isEmpty(origList)) {
                return null;
            }
            List<T> destList = new ArrayList<>();
            for (int i = 0; i < origList.size(); i++) {
                destList.add(copy(origList.get(i), c));
            }
            return destList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] quicksort(int[] array) {
        if(Objects.isNull(array) || array.length == 0) {
            return new int[0];
        }
        int x = array[0];
        int index = 0;
        int[] diff = new int[array.length-1];
        for(int i=1;i<array.length;i++) {
            if(x > array[i]) {
                diff[index] = x;
                index++;
                x = array[i];
            }else {
                diff[index] = array[i];
                index++;
            }
        }
        int[] ret = quicksort(diff);
        int[] a = new int[ret.length+1];
        for(int i=0;i<ret.length;i++) {
            a[i] = ret[i];
        }
        a[ret.length] = x;
        return a;
    }

    public static void main(String[] args) {
        int[] array = new int[]{133,32112,522,411,4,612321,7,8,1033,9};
        long s = System.currentTimeMillis();
        int[] quicksort = quicksort(array);
        System.out.println(System.currentTimeMillis()-s);
        for(int i:quicksort) {
            System.out.print(i+",");
        }
    }

}