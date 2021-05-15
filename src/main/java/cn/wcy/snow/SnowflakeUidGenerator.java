package cn.wcy.snow;

import java.util.Random;

/**
 * <p>Title : SnowflakeUidGenerator.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/11/5 14:59
 * @version : 0.0.1
 */
public class SnowflakeUidGenerator implements UidGenerator {
    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(getWorkerId());

    @Override
    public long nextId() {
        return snowflakeIdWorker.nextId();
    }

    @Override
    public String nextIdStr() {
        return String.valueOf(nextId());
    }

    protected long getWorkerId() {
        long workerId = IpUtil.getLastIpNum();
        if (workerId == -1L) {
            /**
             * 本系统SnowflakeIdWorker的workerId范围为0-1023，ip最后一段数字最大为255
             * 一旦获取本机ip失败，就取300-1023之间的随机数做为workerId
             */
            Random random = new Random();
            workerId = random.nextInt(1024) % (1024 - 300 + 1) + 300;
        }
        return workerId;
    }
}
