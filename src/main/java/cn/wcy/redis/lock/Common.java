package cn.wcy.redis.lock;

/**
 * <p>Title : Common.java</p>
 * <p>Description : 默认锁设置</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/12/30 11:25
 * @version : 0.0.1
 */
public class Common implements LockSet {

    @Override
    public int getExpireSeconds() {
        return 60;
    }

    @Override
    public String getPrefix() {
        return "common";
    }
}
