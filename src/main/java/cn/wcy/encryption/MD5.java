package cn.wcy.encryption;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>Title : MD5.java</p>
 * <p>Description : MD5加密的工具类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/5/28 9:48
 * @version : 0.0.1
 */
public class MD5 {

    /**
     * 字符串转化为MD5的方法
     * @param value 要转换的值
     * @return
     */
    public final static String valueOf(String value) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = value.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //public static void main(String[] args) {
    //    //try {
    //    //    String s = valueOf(new File("C:\\Users\\王晨阳\\Desktop\\mppos_api_test.zip"));
    //    //    System.out.println(s);
    //    //} catch (FileNotFoundException e) {
    //    //    e.printStackTrace();
    //    //}
    //    String s = "e1518d98691f35a5eeebd8367f70df5e";
    //    System.out.println(s.toUpperCase());
    //}

    public static void main(String... args) {
        extractZipFile("C:\\Users\\王晨阳\\Desktop\\mppos_api_test.zip","C:\\Users\\王晨阳\\Desktop", true);
    }

    public static boolean extractZipFile(String zipFilePath, String path, boolean overwrite) {
        return extractZipFile(new File(zipFilePath), path, overwrite);
    }

    public static boolean extractZipFile(File zipFilePath, String destDirectory, boolean overwrite) {
        InputStream inputStream = null;
        ZipInputStream zipInputStream = null;
        boolean status = true;

        try {
            inputStream = new FileInputStream(zipFilePath);

            zipInputStream = new ZipInputStream(inputStream);
            final byte[] data = new byte[1024];

            while (true) {
                ZipEntry zipEntry = null;
                FileOutputStream outputStream = null;

                try {
                    zipEntry = zipInputStream.getNextEntry();

                    if (zipEntry == null) {
                        break;
                    }

                    final String destination;
                    if (destDirectory.endsWith(File.separator)) {
                        destination = destDirectory + zipEntry.getName();
                    } else {
                        destination = destDirectory + File.separator + zipEntry.getName();
                    }

                    if (overwrite == false) {
                        if (isFileOrDirectoryExist(destination)) {
                            continue;
                        }
                    }

                    if (zipEntry.isDirectory()) {
                        createCompleteDirectoryHierarchyIfDoesNotExist(destination);
                    } else {
                        final File file = new File(destination);
                        // Ensure directory is there before we write the file.
                        createCompleteDirectoryHierarchyIfDoesNotExist(file.getParentFile());

                        int size = zipInputStream.read(data);

                        if (size > 0) {
                            outputStream = new FileOutputStream(destination);

                            do {
                                outputStream.write(data, 0, size);
                                size = zipInputStream.read(data);
                            } while (size >= 0);
                        }
                    }
                } catch (IOException exp) {
                    exp.printStackTrace();
                    status = false;
                    break;
                } finally {
                    close(outputStream);
                    closeEntry(zipInputStream);
                }

            } // while(true)
        } catch (IOException exp) {
            exp.printStackTrace();
            status = false;
        } finally {
            close(zipInputStream);
            close(inputStream);
        }
        return status;
    }

    public static boolean createCompleteDirectoryHierarchyIfDoesNotExist(String directory) {
        return createCompleteDirectoryHierarchyIfDoesNotExist(new File(directory));
    }

    private static boolean createCompleteDirectoryHierarchyIfDoesNotExist(File f) {
        if (f == null)
            return true;

        if (false == createCompleteDirectoryHierarchyIfDoesNotExist(f.getParentFile())) {
            return false;
        }

        final String path = f.getAbsolutePath();

        return createDirectoryIfDoesNotExist(path);
    }

    private static boolean createDirectoryIfDoesNotExist(String directory) {
        java.io.File f = new java.io.File(directory);

        if (f.exists() == false) {
            if (f.mkdir()) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Performs close operation on Closeable stream, without the need of
     * writing cumbersome try...catch block.
     *
     * @param closeable The closeable stream.
     */
    public static void close(Closeable closeable) {
        // Instead of returning boolean, we will just simply swallow any
        // exception silently. This is because this method will usually be
        // invoked within finally block. If we are having control statement
        // (return, break, continue) within finally block, a lot of surprise may
        // happen.
        // http://stackoverflow.com/questions/48088/returning-from-a-finally-block-in-java
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Performs close operation on ZIP input stream, without the need of
     * writing cumbersome try...catch block.
     *
     * @param zipInputStream The ZIP input stream.
     */
    public static void closeEntry(ZipInputStream zipInputStream) {
        // Instead of returning boolean, we will just simply swallow any
        // exception silently. This is because this method will usually be
        // invoked within finally block. If we are having control statement
        // (return, break, continue) within finally block, a lot of surprise may
        // happen.
        // http://stackoverflow.com/questions/48088/returning-from-a-finally-block-in-java
        if (null != zipInputStream) {
            try {
                zipInputStream.closeEntry();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isFileOrDirectoryExist(String fileOrDirectory) {
        java.io.File f = new java.io.File(fileOrDirectory);
        return f.exists();
    }

    /**
     * FileMD5加密
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String valueOf(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
