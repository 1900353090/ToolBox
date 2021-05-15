package cn.wcy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>Title : CmdUtil.java</p>
 * <p>Description : cmd运行命令工具对象</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/6/10 18:14
 * @version : 0.0.1
 */
public class CmdUtil {

    /**
    * @Description: 运行cmd命令的方法
    * @Param:
    * @return:
    * @Author: 王晨阳
    * @Date: 2019/6/10-18:14
    */
    public static void runTime(String command,String tml) {
        try {
            Process exec = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream(),"GBK"));
            String line = null;
            String t  = "    所有用户配置文件 : ";
            String p  = "    关键内容            : ";
            while((line = br.readLine())!=null) {
                int i = line.indexOf(t);
                int x = line.indexOf(p);
                if(i == 0) {
                    String name = line.substring(t.length());
                    runTime("netsh wlan show profiles name="+name+" key=clear",name);
                }
                if(x == 0) {
                    String password = line.substring(p.length());
                    System.out.println("账户: "+tml+" "+"密码: "+password);
                }
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("链接过的WiFi：");
        runTime("netsh wlan show profiles",null);
    }

}
