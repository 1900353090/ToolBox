package cn.wcy.encryption;

import org.apache.logging.log4j.util.Strings;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>Title : AES.java</p>
 * <p>Description : AES加密解密</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2021/1/12 17:05
 * @version : 0.0.1
 */
public class AES {

    private static final String KEY_AES = "AES";

    private static final String KEY = "0000000000000000";

    /**
     * 加密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:08
     * @version 0.0.1
    */
    public static String encrypt(String src) throws Exception {
        byte[] raw = KEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return byte2hex(encrypted);
    }

    /**
     * 解密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:08
     * @version 0.0.1
    */
    public static String decrypt(String src) throws Exception {
        byte[] raw = KEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = hex2byte(src);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    /**
     * 自定义key 加密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:08
     * @version 0.0.1
     */
    public static String encrypt(String src, String key) throws Exception {
        if(Strings.isBlank(key) || key.length() != 16) {
            throw new Exception("key值不符合规范,长度应在16位数");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return byte2hex(encrypted);
    }

    /**
     * 自定义key 解密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:08
     * @version 0.0.1
     */
    public static String decrypt(String src, String key) throws Exception {
        if(Strings.isBlank(key) || key.length() != 16) {
            throw new Exception("key值不符合规范,长度应在16位数");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = hex2byte(src);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    private static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

}