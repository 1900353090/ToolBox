package cn.wcy.util;

import java.util.Objects;

/**
 * <p>Title : StringUtil.java</p>
 * <p>Description : 字符串工具类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/2/13 13:10
 * @version : 0.0.1
 */
public class StringUtil {

    /**
    * @Description: 判断字符串是否为null或无意义的
    * @Param: value需要判断的参数
    * @return: true为null或无意义，false不为null有意义
    * @Author: 王晨阳
    * @Date: 2019/8/31-17:13
    */
    public static boolean isNullOrEmpty(String value){
        return Objects.isNull(value) || value.length() == 0;
    }

    /**
     * @Description: 判断是否有为null或无意义的字符串
     * @Param: values需要判断的参数
     * @return: true有为null或无意义，false没有为null有意义
     * @Author: 王晨阳
     * @Date: 2019/8/31-17:13
     */
    public static boolean isNullOrEmpty(String... values){
        for(String value : values) {
            if(Objects.isNull(value) || value.length() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 首字母转小写
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/14-12:35
     */
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))) {
            return s;
        }else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * @Description: 首字母转大写
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/14-12:35
     */
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        }else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * @Description: initialLower:false首字母大写。true首字母小写
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/14-12:35
     */
    public static StringBuffer underlineToHump(String name,boolean initialLower) {
        name = name.toLowerCase();
        if(initialLower) {
            name = toLowerCaseFirstOne(name);
        }else {
            name = toUpperCaseFirstOne(name);
        }
        char[] chars = name.toCharArray();
        boolean judge = false;
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<chars.length;i++) {
            if(chars[i]=='_') {
                judge = true;
            }else {
                String c = chars[i]+"";
                if(judge) {
                    c = c.toUpperCase();
                    judge = false;
                }
                sb.append(c);
            }
        }
        return sb;
    }

}