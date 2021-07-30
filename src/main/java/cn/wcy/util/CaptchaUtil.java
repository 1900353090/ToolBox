package cn.wcy.util;

import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * <p>Title : CaptchaUtil</p>
 * <p>Description : 生成验证码的类</p>
 * <p>DevelopTools : Eclipse_x64_v4.7.1</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2018年11月19日 下午4:30:56
 * @version : 0.0.1
 */
public class CaptchaUtil {
	
	//数字验证码库
	private static String[] NUMBERCODE = {"0","1","2","3","4","5","6","7","8","9"};
	//字母
	private static String[] LETTERCODE = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
			"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	
	/**
	 * @描述：获取随机出来的验证码并返回
	 * @创建人：WangChenYang
	 * @创建时间：2018年11月19日 下午4:31:08
	 * @param size 验证码长度
	 * @param type 验证码类型0字母数字混合,1数字验证码,2字母验证码
	 * @return
	 */
	public static String createCodes(int size,int type) {
		String code[] = {};	//储存验证码类型的变量
		//当类型等于0时将数字验证码库和字母验证码库合并到code数组内
		if(type == 0) {
			code = ArrayUtils.addAll(NUMBERCODE, LETTERCODE);	//合并数组
		}
		//当类型等于1时使用数字验证码库
		if(type == 1) {
			code = NUMBERCODE;	//将数字验证码库赋值到code数组内
		}
		//当类型等于2时使用字母验证码库
		if(type == 2) {
			code = LETTERCODE;	//将字母验证码库赋值到code数组内
		}
		Random ran = new Random();
		//随机的验证码
		StringBuffer result = new StringBuffer();
		//循环传入的验证码长度的次数生成验证码
		for(int i=0;i<size;i++) {
			//获取随机下标
			int index = ran.nextInt(code.length);
			//获取code数组内随机下标的值
			String msg = code[index];
			//添加到验证码内
			result.append(msg);
		}
		//返回随机出来的验证码
		return result.toString();
	}
	
	/**
	 * 获取图片验证码
	 * @param width 	宽
	 * @param height 	高
	 * @param code 		验证码
	 * @return 			图片验证码
	 */
	public static BufferedImage newImgCode(int width, int height, String code) {
		int size = code.length();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();						// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
		g.fillRect(0, 0, width, height);
		g.setColor(new Color(245, 244, 208)); 					// 默认文字颜色
		g.setFont(new Font("Arial", Font.CENTER_BASELINE, 24)); // 设置默认文字大小
		// 画干拢线
		Random random = new Random();
		for (int i = 0; i <= size * 4; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(13);
			int yl = random.nextInt(15);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 将文字写入图片
		String randomString = code;
		for (int i = 1; i <= size; i++) {
			g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121))); // 设置颜色区间变化
			g.drawString(randomString.charAt(i - 1) + "", 18 * i, 26);
		}
		return image;
	}
}
