package cn.wcy.inform;

import cn.wcy.inform.conf.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Component
public class MailUtil {


    // 邮箱发送方对象
    @Autowired(required = false)
    private JavaMailSender jms;

    @Autowired
    private Mail mail;

    /**
     * @描述：发送邮箱验证码
     * @创建人：WangChenYang
     * @创建时间：2018年11月20日 下午5:48:10
     * @param receiver
     * @param code
     * @return
     */
    public boolean sendMailCode(String receiver,String code) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<html><head><title>验证码</title></head><body>");
        stringBuilder.append("亲爱的用户,您的当前的验证码是："+code);
        if (Objects.isNull(jms)) {
            throw new SecurityException("yml内 spring.mail 未配置");
        }
        MimeMessage mimeMessage = jms.createMimeMessage();
        // multipart模式
        try {
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage, true);
            // 收件人邮箱user.getMail()
            mimeMessageHelper.setTo(receiver);
            // 发件人邮箱
            mimeMessageHelper.setFrom(mail.getUsername());
            mimeMessage.setSubject("验证码");
            // 启用html
            mimeMessageHelper.setText(stringBuilder.toString(),true);
            jms.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @描述：发送邮箱信息
     * @创建人：WangChenYang
     * @创建时间：2018年11月20日 下午5:48:10
     * @param receiver
     * @param code
     * @return
     */
    public boolean sendMailDiy(String receiver, String htmlContent) {
        MimeMessage mimeMessage = jms.createMimeMessage();
        if (Objects.isNull(jms)) {
            throw new SecurityException("yml内 spring.mail 未配置");
        }
        // multipart模式
        try {
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage, true);
            // 收件人邮箱user.getMail()
            mimeMessageHelper.setTo(receiver);
            // 发件人邮箱
            mimeMessageHelper.setFrom(mail.getUsername());
            mimeMessage.setSubject("验证码");
            // 启用html
            mimeMessageHelper.setText(htmlContent,true);
            jms.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
