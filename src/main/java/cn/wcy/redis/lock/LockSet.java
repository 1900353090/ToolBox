package cn.wcy.redis.lock;

/**
 * <p>Title : LockSet.java</p>
 * <p>Description : redis锁默认接口</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/12/30 11:23
 * @version : 0.0.1
 */
public interface LockSet {

    /**
     * 获取key的 有效时间
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2020/12/30-11:26
     * @version 0.0.1
    */
    int getExpireSeconds();

    /**
     * 获取 锁前缀
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2020/12/30-11:26
     * @version 0.0.1
    */
    String getPrefix();

}
