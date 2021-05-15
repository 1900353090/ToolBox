package cn.wcy.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * <p>Title : DES.java</p>
 * <p>Description : DES加密解密</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2021/1/12 17:11
 * @version : 0.0.1
 */
public class DES {

    private final static String DES = "DES";
    private final static String ENCODE = "utf-8";
    private final static String DEFAULT_KEY = "00000000";

    //public static void main(String[] args) throws Exception {
    //    String data = "测试ss";
    //    // System.err.println(encrypt(data, key));
    //    // System.err.println(decrypt(encrypt(data, key), key));
    //    System.out.println(encrypt(data));
    //    System.out.println(decrypt(encrypt(data)));
    //}

    /**
     * 使用 默认key 加密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:09
     * @version 0.0.1
    */
    public static String encrypt(String data) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), DEFAULT_KEY.getBytes(ENCODE));
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * 使用 默认key 解密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:09
     * @version 0.0.1
    */
    public static String decrypt(String data) throws IOException, Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, DEFAULT_KEY.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    /**
     * 根据自定义键值进行加密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:10
     * @version 0.0.1
    */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), DEFAULT_KEY.getBytes(ENCODE));
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * 根据自定义键值进行解密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:10
     * @version 0.0.1
    */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    /**
     * 根据自定义键值进行加密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:10
     * @version 0.0.1
    */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * 根据自定义键值进行解密
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/12-17:10
     * @version 0.0.1
    */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
}
