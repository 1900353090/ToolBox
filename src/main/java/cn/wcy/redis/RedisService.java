package cn.wcy.redis;

import cn.wcy.encryption.MD5;
import cn.wcy.util.StringUtil;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title : Redis.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2020/6/24 16:18
 * @version : 0.0.1
 */
@Component("wcy-redis-service")
public class RedisService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private RedisTemplate<String, Object> getRedisTemplate() {
        if (Objects.isNull(this.redisTemplate)) {
            throw new SecurityException("RedisTemplate 未定义");
        }
        return this.redisTemplate;
    }

    /**
     * @Description: 如果执行的是读操作，由于连接对象不是代理对象，读操作并不会重新创建一个连接，
     * 而是使用当前连接，并且放在事务中运行，因此读操作并不会立即执行而是等到事务提交时才能执行，导致读操作读取的结果为null
     * 需要调用此方法释放链接
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/23-11:21
     */
    public void release() {
        //执行释放资源
        TransactionSynchronizationManager.unbindResource(this.getRedisTemplate().getConnectionFactory());
    }

    /**
     * @Description: 获取对象的值key
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/23-11:21
     */
    public static String getObjectKey(Object obj) {
        try {
            if(Objects.nonNull(obj)) {
                Class<?> c = obj.getClass();
                PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(c);
                StringBuffer sb = new StringBuffer();
                for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    String name = propertyDescriptor.getName();
                    name = StringUtil.toUpperCaseFirstOne(name);
                    Method get = c.getMethod("get" + name);
                    Object invoke = get.invoke(obj);
                    sb.append(name).append(":").append(invoke).append(",");
                }
                return MD5.valueOf(sb.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 指定缓存失效时间
     * @Param: key缓存主键，timeout失效时间，unit时间类型
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-10:53
    */
    public boolean expireSeconds(String key, final long timeout, final TimeUnit unit) {
        try {
            if (timeout > 0) {
                this.getRedisTemplate().expire(key, timeout, unit);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }

    /**
     * @Description: 根据key 获取过期时间
     * @Param: key缓存主键,unit时间类型
     * @return: 过期时间，返回0代表永久有效
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-10:53
    */
    public Long getExpire(final String key, final TimeUnit unit) {
        return this.getRedisTemplate().getExpire(key, unit);
    }

    /**
     * @Description: 判断key是否存在
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-10:58
    */
    public Boolean hasKey(String key) {
        try {
            return this.getRedisTemplate().hasKey(key);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }

    /**
     * @Description: 删除缓存key
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-10:59
    */
    @SuppressWarnings("unchecked")
    public Long del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                Boolean delete = this.getRedisTemplate().delete(key[0]);
                return Objects.nonNull(delete) && delete?1L:0L;
            } else {
                return this.getRedisTemplate().delete(CollectionUtils.arrayToList(key));
            }
        }
        return 0L;
    }
    public void delAsyn(final String... key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                del(key);
            }
        }).start();
    }

    /**
     * @Description: 普通缓存获取
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:03
    */
    public Object get(String key) {
        return key == null ? null : this.getRedisTemplate().opsForValue().get(key);
    }

    /** 
     * @Description: 普通缓存存入
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:04
    */
    public boolean set(String key, Object value) {
        try {
            this.getRedisTemplate().opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void setAsyn(String key, Object value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                set(key, value);
            }
        }).start();
    }

    /**
     * @Description: 普通缓存存入并设置时间
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:05
    */
    public boolean set(String key, Object value, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                System.out.println(key+"-"+value+"-"+time+"-"+unit);
                System.out.println(this.getRedisTemplate().opsForValue());
                this.getRedisTemplate().opsForValue().set(key, value, time, unit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void setAsyn(String key, Object value, long time, TimeUnit unit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                set(key, value, time, unit);
            }
        });
    }

    /**
     * @Description: hash get获取key下单个项
     * @Param: key缓存主键,item项
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:07
    */
    public Object hGet(String key, String item) {
        return this.getRedisTemplate().opsForHash().get(key, item);
    }

    /** 
     * @Description: hash set
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:19
    */
    public boolean hset(String key, String item, Object value) {
        try {
            this.getRedisTemplate().opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void hsetAsyn(String key, String item, Object value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hset(key, item, value);
            }
        });
    }

    /**
     * @Description: hash set
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:19
     */
    public boolean hset(String key, String item, Object value, long time, TimeUnit unit) {
        try {
            this.getRedisTemplate().opsForHash().put(key, item, value);
            if (time > 0) {
                expireSeconds(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void hsetAsyn(String key, String item, Object value, long time, TimeUnit unit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hset(key, item, value, time, unit);
            }
        });
    }

    /**
     * @Description: hash get获取key下所有项
     * @Param: key缓存主键
     * @return: key下所有项
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:15
    */
    public Map<Object, Object> hmGet(String key) {
        return this.getRedisTemplate().opsForHash().entries(key);
    }

    /**
     * @Description: 给对应key添加多个项
     * @Param: key缓存主键，map值
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:13
    */
    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            this.getRedisTemplate().opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void hmSetAsyn(String key, Map<String, Object> map) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hmSet(key, map);
            }
        });
    }

    /**
     * @Description: HashSet 并设置时间
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:17
    */
    public boolean hmset(String key, Map<Object, Object> map, final long time, final TimeUnit unit) {
        try {
            this.getRedisTemplate().opsForHash().putAll(key, map);
            if (time > 0) {
                expireSeconds(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void hmsetAsyn(String key, Map<Object, Object> map, final long time, final TimeUnit unit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hmset(key, map, time, unit);
            }
        });
    }

    /**
     * @Description: 删除hash表内多个项
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:21
    */
    public void hdel(String key, Object... item) {
        this.getRedisTemplate().opsForHash().delete(key, item);
    }
    public void hdelAsyn(String key, Object... item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hdel(key, item);
            }
        });
    }

    /**
     * @Description: 判断hash表内是否有该项
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:22
    */
    public boolean hHasKey(String key, String item) {
        return this.getRedisTemplate().opsForHash().hasKey(key, item);
    }

    /**
     * @Description: 根据key获取Set中的所有值
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:23
    */
    public Set<Object> sGet(String key) {
        try {
            return this.getRedisTemplate().opsForSet().members(key);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }

    /**
     * @Description: 根据value从一个set中查询,是否存在
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:23
    */
    public Boolean sHasKey(String key, Object value) {
        try {
            return this.getRedisTemplate().opsForSet().isMember(key, value);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }

    /**
     * @Description: 将数据放入set缓存
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Long sSet(String key, Object... values) {
        try {
            return this.getRedisTemplate().opsForSet().add(key, values);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return 0L;
        }
    }
    public void sSetAsyn(String key, Object... values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sSet(key, values);
            }
        });
    }

    /**
     * @Description: 将set数据放入缓存
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Long sSetAndTime(String key, long time, TimeUnit unit, Object... values) {
        try {
            Long count = this.getRedisTemplate().opsForSet().add(key, values);
            if (time > 0) {
                expireSeconds(key, time, unit);
            }
            return count;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return 0L;
        }
    }
    public void sSetAndTimeAsyn(String key, long time, TimeUnit unit, Object... values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sSetAndTime(key, time, unit, values);
            }
        });
    }

    /**
     * @Description: 获取set缓存的长度
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Long sGetSetSize(String key) {
        try {
            return this.getRedisTemplate().opsForSet().size(key);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return 0L;
        }
    }

    /**
     * @Description: 移除值为value的
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Long remove(String key, Object... values) {
        try {
            return this.getRedisTemplate().opsForSet().remove(key, values);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return 0L;
        }
    }
    public void setRemoveAsyn(String key, Object... values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                remove(key, values);
            }
        });
    }
    // ===============================list=================================

    /**
     * @Description: 获取list缓存的内容
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return this.getRedisTemplate().opsForList().range(key, start, end);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }

    /**
     * @Description: 获取list缓存的长度
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Long lGetListSize(String key) {
        try {
            return this.getRedisTemplate().opsForList().size(key);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return 0L;
        }
    }

    /**
     * @Description: 通过索引 获取list中的值
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Object lGetIndex(String key, long index) {
        try {
            return this.getRedisTemplate().opsForList().index(key, index);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }

    /**
     * @Description: 将list放入缓存
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public boolean lSet(String key, Object value) {
        try {
            this.getRedisTemplate().opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void lSetAsyn(String key, Object value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lSet(key, value);
            }
        });
    }

    /**
     * @Description: 将list放入缓存
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public boolean lSet(String key, Object value, long time, TimeUnit unit) {
        try {
            this.getRedisTemplate().opsForList().rightPush(key, value);
            if (time > 0)
                expireSeconds(key, time, unit);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void lSetAsyn(String key, Object value, long time, TimeUnit unit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lSet(key, value, time, unit);
            }
        });
    }

    /**
     * @Description: 将list放入缓存
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            this.getRedisTemplate().opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void lSetAsyn(String key, List<Object> value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lSet(key, value);
            }
        });
    }

    /**
     * @Description: 将list放入缓存
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public boolean lSet(String key, List<Object> value, long time, TimeUnit unit) {
        try {
            this.getRedisTemplate().opsForList().rightPushAll(key, value);
            if (time > 0) {
                expireSeconds(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void lSetAsyn(String key, List<Object> value, long time, TimeUnit unit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lSet(key, value, time, unit);
            }
        });
    }

    /**
     * @Description: 根据索引修改list中的某条数据
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            this.getRedisTemplate().opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }

    /**
     * @Description: 移除N个值为value
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Long lRemove(String key, long count, Object value) {
        try {
            return this.getRedisTemplate().opsForList().remove(key, count, value);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return 0L;
        }
    }
    public void lRemoveAsyn(String key, long count, Object value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lRemove(key, count, value);
            }
        });
    }

    // ===============================sorted set=================================

    /**
     * @Description: 向有序集合添加一个成员的
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public boolean zadd(String key, Object member, double score, long time, TimeUnit unit) {
        try {
            this.getRedisTemplate().opsForZSet().add(key, member, score);
            if (time > 0)
                expireSeconds(key, time, unit);
            return true;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return false;
        }
    }
    public void zaddAsyn(String key, Object member, double score, long time, TimeUnit unit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                zadd(key, member, score, time, unit);
            }
        });
    }

    /**
     * @Description: ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     *      通过分数返回有序集合指定区间内的成员
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Set<Object> zRangeByScore(String key, double minScore, double maxScore) {
        try {
            return this.getRedisTemplate().opsForZSet().rangeByScore(key, minScore, maxScore);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }

    /**
     * @Description: 获取指定区间的数据
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Set<Object> zRange(String key, long page, long limit) {
        try {
            return this.getRedisTemplate().opsForZSet().range(key, page, limit);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }


    /**
     * @Description: ZSCORE key member
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Double zscore(String key, Object member) {
        try {
            return this.getRedisTemplate().opsForZSet().score(key, member);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }

    /**
     * @Description: ZRANK key member 返回有序集合中指定成员的索引
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
     */
    public Long zrank(String key, Object member) {
        try {
            return this.getRedisTemplate().opsForZSet().rank(key, member);
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }

    /**
     * @Description: Zscan 迭代有序集合中的元素（包括元素成员和元素分值）
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/28-11:26
    */
    public Cursor<ZSetOperations.TypedTuple<Object>> zscan(String key) {
        try {
            Cursor<ZSetOperations.TypedTuple<Object>> cursor = this.getRedisTemplate().opsForZSet().scan(key, ScanOptions.NONE);
            return cursor;
        } catch (Exception e) {
            LOGGER.error("redis error: ", e);
            return null;
        }
    }

    /************************Lock*************************/

    // NX,XX
    //NX-key不存在则保存，XX-key存在则保存
    private static final String NX= "NX";
    private static final String XX= "XX";

    //EX,PX
    //EX-秒，PX-毫秒
    private static final String PX = "PX";
    private static final String EX = "EX";

    /**
     *  循环设置分布式锁，直到成功。单位秒. 【如果key 不存在的话】
     * @param key
     * @param prefix
     * @return
     */
    public boolean setIfAbsentEXDoWhile(final String prefix, final String key, final int expireSeconds) {
        boolean isOk = false;
        int i=0;
        int timeOut = expireSeconds*10+100;
        do {
            isOk = setIfAbsentEX(key, "0", prefix, expireSeconds);
            ++i;
//            try {
//                Thread.sleep(100L);
//            }catch (Exception e) {}
        }while (!isOk && i <= timeOut);
        LOGGER.info("线程{}，设置分布式锁，总共循环次数： {}", Thread.currentThread().getId(), i);
        return isOk;
    }

    /**
     * 设置分布式锁，单位秒 【如果key 不存在的话】
     * @param key
     * @param value
     * @param prefix
     * @return
     */
    private boolean setIfAbsentEX(final String key, final String value, final String prefix, final int expireSeconds) {
        return setIfAbsent(new StringBuilder().append(prefix)
                .append(":").append(key).toString(),
                value, expireSeconds==0?60:expireSeconds, NX, EX);
    }

    /**
     * 设置分布式锁
     * @param key
     * @param value
     * @param EXPX   "EX" or "PX"   EX：秒  PX:毫秒
     * @return  boolean
     */
    private boolean setIfAbsent(final String key, final String value, final long expireTime,final String NXXX,final String EXPX) {
        Boolean resultBoolean = null;
        try {
            resultBoolean = this.getRedisTemplate().execute((RedisCallback<Boolean>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                String redisResult = "";
                RedisSerializer<String> keySerializer = (RedisSerializer<String>) this.getRedisTemplate().getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) this.getRedisTemplate().getValueSerializer();
                //lettuce连接包下序列化键值，否知无法用默认的ByteArrayCodec解析
                byte[] keyByte = keySerializer.serialize(key);
                byte[] valueByte = valueSerializer.serialize(value);
                // lettuce连接包下 redis 单机模式setnx
                if (nativeConnection instanceof RedisAsyncCommands) {
                    RedisAsyncCommands commands = (RedisAsyncCommands)nativeConnection;
                    //同步方法执行、setnx禁止异步
                    redisResult = commands
                            .getStatefulConnection()
                            .sync()
                            .set(keyByte, valueByte, SetArgs.Builder.nx().ex(10));
                }
                // lettuce连接包下 redis 集群模式setnx
                if (nativeConnection instanceof RedisAdvancedClusterAsyncCommands) {
                    RedisAdvancedClusterAsyncCommands clusterAsyncCommands = (RedisAdvancedClusterAsyncCommands) nativeConnection;
                    redisResult = clusterAsyncCommands
                            .getStatefulConnection()
                            .sync()
                            .set(keyByte, keyByte, SetArgs.Builder.nx().ex(10));
                }
                //返回加锁结果
                return "OK".equalsIgnoreCase(redisResult);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBoolean != null && resultBoolean;
    }

    /**
     * 删除对应的 key
     * @param key
     * @param prefix key的前缀
     */
    public void releaseLock(final String key,final String prefix) {
        String mergeKey = new StringBuilder()
                .append(prefix)
                .append(":").append(key).toString();
        try {
            if (hasKey(mergeKey)) {
                this.getRedisTemplate().delete(mergeKey);
            }
        } catch (RedisConnectionFailureException re) {
            LOGGER.error("RedisService [remove] method  produces exceptionally, exception message is {}",re.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
