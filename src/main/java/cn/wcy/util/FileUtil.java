package cn.wcy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title : FileUtil.java</p>
 * <p>Description : 文件工具类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/5/28 10:16
 * @version : 0.0.1
 */
public class FileUtil {

    /** 
    * @Description: 根据文件路径读取byte[]数组
    * @Param: file需要读取的文件对象
    * @return: 读取到的byte数组
    * @Author: 王晨阳
    * @Date: 2019/6/20-18:08
    */
    public static byte[] readFileByBytes(java.io.File file) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                System.out.println("路径："+file.getAbsolutePath());
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }

    /**
     * @Description: 逐行读取文本框
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/2/27-16:42
    */
    public static ArrayList<String> getRead(String path) {
        ArrayList<String> data = new ArrayList<>();
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 读入TXT文件 */
            File filename = new File(path); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while ((line = br.readLine()) != null) {
                if(StringUtils.isNotEmpty(line)) {
                    data.add(line);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @Description: 写入TXT文本的方法
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/2/27-16:43
    */
    public static boolean write(String path, String content) {
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 写入Txt文件 */
            File writename = new File(path); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename,true));	//设置追加
            out.write(content);
            out.newLine();	//调用换行的方法
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //public static void main(String[] args) {
    //    ArrayList<String> arrayList = getRead("C:\\Users\\MI\\Desktop\\file.txt");
    //    for(String s:arrayList) {
    //        String[] s1 = s.split(" ");
    //        //System.out.println(s1[3]);
    //        String[] name = s1[3].split("\\|");
    //        String ss = "{\n" +
    //                "    \"head\":{\n" +
    //                "        \"msg\":\"541ced4887d1d1a1deb530c07bfb2acc\",\n" +
    //                "        \"reqId\":\"96zn16jrholkrrua\"\n" +
    //                "    },\n" +
    //                "    \"transDetails\":[\n" +
    //                "        {\n" +
    //                "            \"amount\":\""+s1[4]+"\",\n" +
    //                "            \"bankCode\":\""+s1[5]+"\",\n" +
    //                "            \"billId\":\""+s1[0]+"\",\n" +
    //                "            \"payAmount\":\""+s1[4]+"\",\n" +
    //                "            \"payId\":\"1\",\n" +
    //                "            \"payType\":\"刷卡\",\n" +
    //                "            \"payee\":\"1\",\n" +
    //                "            \"payer\":\"1\",\n" +
    //                "            \"providerName\":\"福州客达信息科技有限公司|057439\",\n" +
    //                "            \"psamId\":\""+s1[9]+"\",\n" +
    //                "            \"realname\":\""+name[1]+"\",\n" +
    //                "            \"status\":\"支付成功\",\n" +
    //                "            \"transTime\":\""+s1[1]+" "+s1[2]+"\",\n" +
    //                "            \"transType\":\""+s1[8]+"\"\n" +
    //                "        }\n" +
    //                "    ]\n" +
    //                "}";
    //        System.out.println(ss);
    //        String post = HttpUtil.post(JSONObject.parseObject(ss), "https://app6.kedaqianbao.com/channel/jiufu/notification");
    //        System.out.println(post);
    //        write("C:\\Users\\MI\\Desktop\\data.txt", ss);
    //        try {
    //            Thread.sleep(5000);
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    Map<String, String> map = readTxtToMap("C:\\Users\\MI\\Desktop\\365927877316763670\\pass.txt");
    //    Set<String> strings = map.keySet();
    //    for(String s:strings) {
    //        System.out.println(map.get(s));
    //    }
    //}

    //public static void main(String[] args) {
    //    String para = "{" +
    //            "\"topid\": \"0\"," +
    //            "\"p\": \"2\"" +
    //            "}";
    //    JSONObject classifyOne = HttpUtil.doPost(
    //            "https://api.fjkjt.cn/command.php?cmd=action&cn=wx_category&act=search",
    //            JSONObject.parseObject(para)
    //    );
    //    System.out.println(classifyOne);
    //    JSONArray classifyOneArray = classifyOne.getJSONObject("data").getJSONArray("list");
    //    for(int i=0;i<classifyOneArray.size();i++) {
    //        JSONObject obj = classifyOneArray.getJSONObject(i);
    //        System.out.println(obj);
    //        String getClassifyTwoPara = "{" +
    //                "\"topid\": \""+obj.getInteger("id")+"\"," +
    //                "\"p\": \"1\"" +
    //                "}";
    //        JSONObject post = HttpUtil.doPost(
    //                "https://api.fjkjt.cn/command.php?cmd=action&cn=wx_category&act=search",
    //                JSONObject.parseObject(getClassifyTwoPara)
    //        );
    //        JSONArray oneClassifyList = post.getJSONObject("data").getJSONArray("list");
    //        for(int j=0;j<oneClassifyList.size();j++) {
    //            JSONObject oneClassify = oneClassifyList.getJSONObject(j);
    //            JSONArray twoClassifys = oneClassify.getJSONArray("children");
    //            for(int x=0;x<twoClassifys.size();x++) {
    //                JSONObject twoClassify = twoClassifys.getJSONObject(x);
    //                String goodsListPara = "{" +
    //                        "\"cateid\": \""+twoClassify.get("id")+"\"," +
    //                        "\"goodstip\": \"true\"," +
    //                        "\"p\": \"1\"" +
    //                        "}";
    //                JSONArray goodsList = HttpUtil.doPost(
    //                        "https://api.fjkjt.cn/command.php?cmd=action&cn=wx_goods&act=search",
    //                        JSONObject.parseObject(goodsListPara)
    //                ).getJSONObject("data").getJSONArray("list");
    //                for(int z=0;z<goodsList.size();z++) {
    //                    JSONObject goods = goodsList.getJSONObject(z);
    //                    String goodsInfo = HttpUtil.sendGet("https://api.fjkjt.cn/command.php", "cmd=info&cn=goods&id=" + goods.getString("id"));
    //                    System.out.println(goodsInfo);
    //                    write("C:\\Users\\王晨阳\\Desktop\\data.txt", goodsInfo);
    //                }
    //            }
    //        }
    //    }
    //}

    /** 
    * @Description: 读取文件内的内容
    * @Param: path文件路径
    * @return: 文件内的内容
    * @Author: 王晨阳
    * @Date: 2019/6/20-18:07
    */
    public static String read(String path){

        if(StringUtils.isBlank(path)){
            return null;
        }

        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            return stringBuilder.toString();//得到JSONobject对象
        } catch (IOException e) {
            System.out.println("#######文件内容读取失败#######:"+ path);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @Description: 读取txt内格式为A=B的数据 A为keyB为值
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/20-9:40
    */
    public static Map<String, String> readTxtToMap(String path) {
        Map<String, String> map = new ConcurrentHashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
            Set<Object> set = properties.keySet();
            set.parallelStream().forEach(obj -> {
                String key = (String)obj;
                String value = properties.getProperty(key);
                map.put(key, value);
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    /**
    * @Description: 读取文件内的json
    * @Param: path文件路径
    * @return: 文件内的json
    * @Author: 王晨阳
    * @Date: 2019/6/20-18:06
    */
    public static JSONObject readJson(String path) {
        if(path == null || path.length() <= 0) {
            return null;
        }
        return JSON.parseObject(read(path));
    }

    /**
     * @Description: base64转File
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/15-16:17
    */
    public static boolean base64ToFile(String base64,File dest) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //由于base64在传输过程中，+和/和=会被替换所以在解码前需要将base64还原成可用的base64
            base64 = base64.replaceAll(" ","+");
            base64 = base64.replaceAll("%2F","/");
            base64 = base64.replaceAll("%3D","=");
            //当使用springMVC时无需使用以上方法进行还原

            byte[] bytes = Base64.decodeBase64(base64.replace("\r\n", ""));
            bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
            bos = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] bts = new byte[1024];
            int len = -1;
            while((len = bis.read(bts)) != -1) {
                bos.write(bts,0,len);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
