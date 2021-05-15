package cn.wcy.util;

import java.net.*;
import java.util.Enumeration;
import java.util.Objects;

/**
 * <p>Title : WindowsUtil.java</p>
 * <p>Description : </p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/11/27 16:40
 * @version : 0.0.1
 */
public class WindowsUtil {

    /**
    * @Description: 按照"XX-XX-XX-XX-XX-XX"格式，获取本机MAC地址
    * @Author: 王晨阳
    * @Date: 2019/11/27-17:11
    */
    public static String getMacAddress() throws Exception{
        Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
        while(ni.hasMoreElements()){
            NetworkInterface netI = ni.nextElement();

            byte[] bytes = netI.getHardwareAddress();
            if(netI.isUp() && Objects.nonNull(bytes) && bytes.length == 6){
                StringBuilder sb = new StringBuilder();
                for(byte b:bytes){
                    //与11110000作按位与运算以便读取当前字节高4位
                    sb.append(Integer.toHexString((b&240)>>4));
                    //与00001111作按位与运算以便读取当前字节低4位
                    sb.append(Integer.toHexString(b&15));
                    sb.append("-");
                }
                sb.deleteCharAt(sb.length()-1);
                return sb.toString().toUpperCase();
            }
        }
        return null;
    }

    /**
    * @Description: 获取本机ipv4
    * @Author: 王晨阳
    * @Date: 2019/11/27-17:11
    */
    public static String getIsIpv4Addr() throws Exception {
        Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while(ni.hasMoreElements()){
            NetworkInterface netI = ni.nextElement();
            Enumeration<InetAddress> addresses = netI.getInetAddresses();
            if(netI.isUp()){
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        return null;
    }

    /**
     * @Description: 获取本机ipv6
     * @Author: 王晨阳
     * @Date: 2019/11/27-17:11
     */
    public static String getIsIpv6Addr() throws Exception {
        Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while(ni.hasMoreElements()){
            NetworkInterface netI = ni.nextElement();
            Enumeration<InetAddress> addresses = netI.getInetAddresses();
            if(netI.isUp()){
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ip.isLoopbackAddress() && ip instanceof Inet6Address) {
                        String ipv6 = ip.getHostAddress();
                        return ipv6.substring(0, ipv6.indexOf("%"));
                    }
                }
            }
        }
        return null;
    }

    /**
    * @Description: 获取本机名
    * @Author: 王晨阳
    * @Date: 2019/11/27-17:14
    */
    public static String getHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}
