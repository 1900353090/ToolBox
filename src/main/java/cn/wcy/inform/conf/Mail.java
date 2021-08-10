package cn.wcy.inform.conf;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
public class Mail {

    // 获取配置文件内的发送者邮箱
    private String username;

    public String getUsername() {
        if (StringUtils.isBlank(this.username)) {
            throw new SecurityException("spring.mail.username 未定义");
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
