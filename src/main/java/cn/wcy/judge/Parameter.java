package cn.wcy.judge;

import java.util.Objects;

/**
 * <p>Title : Parameter.java</p>
 * <p>Description : 参数判断类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/8/31 16:54
 * @version : 0.0.1
 */
public class Parameter {

    /**
    * @Description: 判断传入的参数是否为null
    * @Param: paras需要判断的参数
    * @return: true有null参，false无null参
    * @Author: 王晨阳
    * @Date: 2019/8/31-16:55
    */
    public static boolean isNull(Object... paras) {
        for(Object para : paras) {
            if(Objects.isNull(para)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 判断传入的参数是否为null
     * @Param: para需要判断的参数
     * @return: true有null，false无null
     * @Author: 王晨阳
     * @Date: 2019/8/31-16:55
     */
    public static boolean isNull(Object para) {
        return para == null;
    }

    /**
     * @Description: 判断多个形参内是否存在null，存在null时返回true否则返回false
     * @Param: objects需要判断的形参
     * @return: true存在null，false不存在null
     * @Author: 王晨阳
     * @Date: 2019/10/16-9:23
     */
    public static boolean isParaExistNull(Object... objects) {
        for(Object object : objects) {
            if(Objects.isNull(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 判断多个形参内是否全为null，全为null时返回true否则返回false
     * @Param: objects需要判断的形参
     * @return: true全为null，false不是全为null
     * @Author: 王晨阳
     * @Date: 2019/10/16-9:23
     */
    public static boolean isParaAllNull(Object... objects) {
        for(Object object : objects) {
            if(Objects.nonNull(object)) {
                return false;
            }
        }
        return true;
    }

}
