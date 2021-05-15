package cn.wcy.forEach;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Title : Traversals.java</p>
 * <p>Description : 遍历工具类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/6/20 11:32
 * @version : 0.0.1
 */
public class Traversals {

    private static final Integer MAP_SIZE = 10;

    private static Map<String,Integer> map = new HashMap<>(MAP_SIZE);

    /** 
     * @Description: 多线程下初始化好指定线程数的方法
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/20-11:47
    */
    public static synchronized void sleepInitSize(Integer fNum, Long millis, Integer size) {
        sleep(fNum, millis, size);
    }

    /**
     * @Description: 多线程下默认线程数的方法
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/20-11:47
     */
    public static synchronized void sleep(Integer fNum, Long millis) {
        sleep(fNum, millis, null);
    }

    /**
     * @Description: 循环时指定循环次数并指定睡眠时间
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/20-11:46
    */
    private static synchronized void sleep(Integer fNum, Long millis, Integer size) {
        if(Objects.nonNull(size) && map.size() < size) {
            map = new HashMap<>(size);
        }
        String threadId = Thread.currentThread().getId()+"";
        Integer index = map.get(threadId);
        index = Objects.isNull(index)?1:index;
        //每循环fNum就休息millis秒，防止破服务器炸了 QAQ~
        if(index%fNum == 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        map.put(threadId,index+1);
    }

}
