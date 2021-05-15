package cn.wcy.interaction;

import cn.wcy.util.FileUtil;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title : Transmit.java</p>
 * <p>Description : 传输类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/5/28 10:00
 * @version : 0.0.1
 */
public class Transmit {

    /** 
    * @Description: 发送带参数和文件的方法
    * @Param: file文件,params参数,goalUrl目标地址,accessToken访问令牌
    * @return: 目标地址处理后的结果
    * @Author: 王晨阳
    * @Date: 2019/5/28-10:34
    */
    public static String sendFile(File file, Map<String,String> params, String goalUrl, String accessToken) {
        String result = "";
        // 通用识别url
        String otherHost = goalUrl;
        // 本地图片路径
        try {
            byte[] data = FileUtil.readFileByBytes(file);	//将文件转换为流
            String fileBase64 = Base64.encodeBase64String(data);	//获取流的base64编码
            String param = URLEncoder.encode("fileBase64", "UTF-8")+"=" + fileBase64;
            for(String key : params.keySet()) {
                param += "&"+URLEncoder.encode(key, "UTF-8")+"="+URLEncoder.encode(params.get(key), "UTF-8");
            }
            result = HttpUtil.post(otherHost, accessToken, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 
    * @Description: 发送文件不带参数
    * @Param: file文件,goalUrl目标地址，accessToken访问令牌
    * @return: 目标地址处理后的结果
    * @Author: 王晨阳
    * @Date: 2019/5/28-10:36
    */
    public static String sendFile(File file, String goalUrl, String accessToken) {
        String result = "";
        try {
            byte[] data = FileUtil.readFileByBytes(file);//将文件转换为流
            String fileBase64 = Base64.encodeBase64String(data);//获取流的base64编码
            String param = URLEncoder.encode("fileBase64","UTF-8")+"="+fileBase64;
            result = HttpUtil.post(goalUrl, accessToken, param);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /** 
    * @Description: 发送参数
    * @Param: params参数，goalUrl目标地址，accessToken访问令牌
    * @return: 目标地址返回的结果
    * @Author: 王晨阳
    * @Date: 2019/5/28-11:07
    */
    public static String sendParams(Map<String,String> params, String goalUrl, String accessToken) {
        String result = "";
        // 通用识别url
        String otherHost = goalUrl;
        // 本地图片路径
        try {
            String param = "";
            int i = 0;
            for(String key : params.keySet()) {
                if(i > 0) {
                    param += "&"+URLEncoder.encode(key, "UTF-8")+"="+URLEncoder.encode(params.get(key), "UTF-8");
                }else {
                    param += URLEncoder.encode(key, "UTF-8")+"="+URLEncoder.encode(params.get(key), "UTF-8");
                }
            }
            result = HttpUtil.post(otherHost, accessToken, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 获取request参数
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/21-9:48
    */
    public static Map<String, String> requestToMap(HttpServletRequest request) throws Exception {
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

}
