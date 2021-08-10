package cn.wcy.inform.conf;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chuanglan")
public class Note253 {

    private String url;

    private String account;

    private String password;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        if (StringUtils.isBlank(this.url)) {
            throw new SecurityException("chuanglan.url 未定义");
        }
        return url;
    }

    public String getAccount() {
        if (StringUtils.isBlank(this.account)) {
            throw new SecurityException("chuanglan.account 未定义");
        }
        return account;
    }

    public String getPassword() {
        if (StringUtils.isBlank(this.password)) {
            throw new SecurityException("chuanglan.password 未定义");
        }
        return password;
    }
}
