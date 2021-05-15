package cn.wcy.judge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title : RegExp.java</p>
 * <p>Description : 正则表达式的类</p>
 *
 * 描述：
 * find()方法是部分匹配，是查找输入串中与模式匹配的子串，如果该匹配的串有组还可以使用group()函数。
 * matches()是全部匹配，是将整个输入串与模式匹配，如果要验证一个输入的数据是否为数字类型或其他类型，一般要用matches()。
 *
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/5/28 9:51
 * @version : 0.0.1
 */
public class RegExp {

    /**
     * @Description: 判断是否是手机号码
     * @Param: 手机号码
     * @return: true false
     * @Author: 王晨阳
     * @Date: 2019/5/28-9:58
     */
    public static boolean isPhone(String phone) {
        //手机号码正则
        String regex = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
        //使用Pattern.compile将正则编译成正则模式类对象
        Pattern pattern = Pattern.compile(regex);
        //使用pattern.matcher获取匹配器
        Matcher matcher = pattern.matcher(phone);
        //使用匹配器的matches方法进行正则匹配
        return matcher.matches();
    }

    /**
    * @Description: 判断是否是邮箱
    * @Param: 邮箱
    * @return: true false
    * @Author: 王晨阳
    * @Date: 2019/5/28-9:58
    */
    public static boolean isMailbox(String mailbox) {
        //邮箱正则
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        //使用pattern.compile将正则编译成模式类对象
        Pattern pattern = Pattern.compile(regex);
        //使用pattern.matcher获取匹配器
        Matcher matcher = pattern.matcher(mailbox);
        //使用匹配器的matches方法进行正则匹配
        return matcher.matches();
    }

    /**
     * @Description: 判断身份证是否错误
     * @Param: 身份证
     * @return: true or false
     * @Author: 王晨阳
     * @Date: 2019/6/11-16:45
     */
    public static boolean isIdentity(String identity) {
        if (identity == null || "".equals(identity)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾
        boolean matches = identity.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (identity.length() == 18) {
                try {
                    char[] charArray = identity.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常:" + identity);
                    return false;
                }
            }

        }
        return matches;
    }

    /**
     * @Description: 判断字符内是否存在英文
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/13-9:25
    */
    public static boolean inEnglish(String str) {
        String regex = "[a-zA-z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * @Description: 自定义正则（全部匹配）
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/13-9:27
    */
    public static boolean customRegExpAll(String str, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @Description: 自定义正则（部分匹配）
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/13-9:27
     */
    public static boolean customRegExpPart(String str, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

}
