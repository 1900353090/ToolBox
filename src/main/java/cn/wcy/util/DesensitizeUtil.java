package cn.wcy.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 脱敏类
 * @Author: 王晨阳
 * @Date: 2021/6/1-9:12
*/
public class DesensitizeUtil {

    /** 
     * @Description: 姓名脱敏
     * @Param: name姓名
     * @Author: 王晨阳
     * @Date: 2021/6/1-9:20
    */
    public static String name(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        return "**"+name.substring(name.length()-1, name.length());
    }

    /**
     * @Description: 手机号码前三后四脱敏
     * @Param: phone手机号
     * @Author: 王晨阳
     * @Date: 2021/6/1-9:12
     */
    public static String phone(String phone) {
        if (StringUtils.isEmpty(phone) || (phone.length() != 11)) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * @Description: 银行账户：显示前六后四
     * @Param: 银行卡号
     * @Author: 王晨阳
     * @Date: 2021/6/1-9:16
     */
    public static String card(String cardno) {
        if (StringUtils.isEmpty(cardno) || (cardno.length() < 8)) {
            return cardno;
        }
        return replaceBetween(cardno, 6, cardno.length() - 4, null);
    }

    /**
     * 将字符串开始位置到结束位置之间的字符用指定字符替换
     * @param sourceStr 待处理字符串
     * @param begin	开始位置
     * @param end	结束位置
     * @param replacement 替换字符
     * @return
     */
    private static String replaceBetween(String sourceStr, int begin, int end, String replacement) {
        if (sourceStr == null) {
            return "";
        }
        if (replacement == null) {
            replacement = "*";
        }
        int replaceLength = end - begin;
        if (StringUtils.isNotBlank(sourceStr) && replaceLength > 0) {
            StringBuilder sb = new StringBuilder(sourceStr);
            sb.replace(begin, end, StringUtils.repeat(replacement, replaceLength));
            return sb.toString();
        } else {
            return sourceStr;
        }
    }

    /**
     * @Description: 身份证前三后四脱敏
     * @Param: id身份证
     * @Author: 王晨阳
     * @Date: 2021/6/1-9:16
    */
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    /**
     * @Description: 护照前2后3位脱敏，护照一般为8或9位
     * @Param: 护照id
     * @Author: 王晨阳
     * @Date: 2021/6/1-9:16
    */
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

}
