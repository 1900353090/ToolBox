package cn.wcy.snow;

/**
 * <p>Title : UidGenerator.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/11/5 14:58
 * @version : 0.0.1
 */
public interface UidGenerator {
    /**
     * 获取下一个Id
     * @return
     */
    long nextId();

    String nextIdStr();
}
