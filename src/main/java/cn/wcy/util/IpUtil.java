package cn.wcy.util;

import cn.wcy.interaction.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title : IpUtil</p>
 * <p>Description : 关于Ip的工具类</p>
 * <p>DevelopTools : Eclipse_x64_v4.8.0</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wangchenyang</p>
 * @author : wangChenYang
 * @date : 2018年11月25日 上午11:56:24
 * @version : 12.0.0
 */
public class IpUtil {

	/**
	 * 获取请求者的ip
	 * @param request 传入请求者对象
	 * @return
	 */
	public static String getRequestIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static void main(String[] args) {
		String post = HttpUtil.post(JSONObject.parseObject("{}"), "http://47.103.137.114:25672/api/queues/keda/keda-order%23order_channel/get");
		System.out.println(post);
	}
}
