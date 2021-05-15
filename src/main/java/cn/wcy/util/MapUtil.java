package cn.wcy.util;

import java.util.*;

/**
 * <p>Title : MapUtil.java</p>
 * <p>Description : 操作map的工具类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/9/5 11:13
 * @version : 0.0.1
 */
public class MapUtil<K,V> extends HashMap<K,V> implements Map<K,V> {

    /**
    * @Description: map值转list
    * @return: value集合
    * @Author: 王晨阳
    * @Date: 2019/9/5-11:46
    */
    public List<V> valueToList() {
        List<V> list = new ArrayList<>();
        for(K key : this.keySet()) {
            list.add(this.get(key));
        }
        return list;
    }

    /**
    * @Description: map值转list
    * @Param: map需要value转list的集合
    * @return: value集合
    * @Author: 王晨阳
    * @Date: 2019/9/5-11:46
    */
    public List<V> valueToList(Map<K,V> map) {
        List<V> list = new ArrayList<>();
        for(K key : map.keySet()) {
            list.add(map.get(key));
        }
        return list;
    }

    /**
    * @Description: map转xml
    * @Param: map需要转换的map
    * @return: xml字符串
    * @Author: 王晨阳
    * @Date: 2019/11/5-15:07
    */
    public String mapToXML(Map<K,V> map) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        for(K key : map.keySet()) {
            String value = Objects.isNull(map.get(key))?"null":map.get(key).toString();
            xml.append("<").append(key.toString()).append(">").append(value).append("</").append(key.toString()).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }
}
