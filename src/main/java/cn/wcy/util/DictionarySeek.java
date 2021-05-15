package cn.wcy.util;

/**
 * 利用Java实现字母(大小写)+数字+字符的穷举，可用于密码爆破等
 * 如果需要其他的字符，直接接到字符数组中即可
 * 如果只需要
 *  1.数字
 *  2.字母
 *  3.字符
 *  4.数字+字母
 *  5.字母+字符
 *  6.数字+字符
 *  拆分fullCharSource数组即可
 * @author 冰河
 *
 */
public class DictionarySeek {


    //密码可能会包含的字符集合
    private static char[] fullCharSource = { '1','2','3','4','5','6','7','8','9','0',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',  'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',  'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}', '|', ':', '"', '<', '>', '?', ';', '\'', ',', '.', '/', '-', '=', '`'};
    //将可能的密码集合长度
    private static int fullCharLength = fullCharSource.length;

    /**
     * 穷举打印输出，可以将打印输出的文件形成字典
     * @param maxLength：生成的字符串的最大长度
     */
    public static void generate(int maxLength) {
        //计数器，多线程时可以对其加锁，当然得先转换成Integer类型。
        int counter = 0;
        StringBuilder buider = new StringBuilder();
        while (buider.toString().length() <= maxLength) {
            buider = new StringBuilder(maxLength*2);
            int _counter = counter;
            //10进制转换成26进制
            while (_counter >= fullCharLength) {
                //获得低位
                buider.insert(0, fullCharSource[_counter % fullCharLength]);
                _counter = _counter / fullCharLength;
                //精髓所在，处理进制体系中只有10没有01的问题，在穷举里面是可以存在01的
                _counter--;
            }
            //最高位
            buider.insert(0,fullCharSource[_counter]);
            counter++;
            System.out.println(buider.toString());
        }
    }

    public static void main(String[] args) {
        long beginMillis = System.currentTimeMillis();
        System.out.println(beginMillis);//开始时间
        generate(50);					//以最大长度为50测试
        long endMillis = System.currentTimeMillis();
        System.out.println(endMillis);//结束时间
        System.out.println(endMillis - beginMillis);//总耗时，毫秒
    }
}
