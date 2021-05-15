package cn.wcy.snow;

import java.util.Objects;

/**
 * <p>Title : UidGeneratorUtil.java</p>
 * <p>Description : 雪花算法工具类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/11/5 14:58
 * @version : 0.0.1
 */
public class UidGeneratorUtil {

    private static UidGenerator UID_GENERATOR;

    private UidGeneratorUtil() {
    }

    private static UidGenerator getUidGenerator() {
        if(Objects.isNull(UID_GENERATOR)) {
            UID_GENERATOR = new SnowflakeUidGenerator();
        }
        return UID_GENERATOR;
    }

    public static long nextId(){
        return getUidGenerator().nextId();
    };

    public static String nextIdStr(){
        return getUidGenerator().nextIdStr();
    };

}