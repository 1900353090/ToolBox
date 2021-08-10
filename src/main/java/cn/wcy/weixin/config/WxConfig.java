package cn.wcy.weixin.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Getter
@ConfigurationProperties(prefix = "weixin")
public class WxConfig {

    private String appid;

    private String secret;

    private Auth auth;

    public String getAppid() {
        if (StringUtils.isBlank(this.appid)) {
            throw new SecurityException("weixin.appid 未定义");
        }
        return appid;
    }

    public String getSecret() {
        if (StringUtils.isBlank(this.secret)) {
            throw new SecurityException("weixin.secret 未定义");
        }
        return secret;
    }

    public Auth getAuth() {
        if (Objects.isNull(this.auth)) {
            throw new SecurityException("weixin.auth 未定义");
        }
        return auth;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public static class Auth {
        private String api;
        private String grant_type;

        public void setApi(String api) {
            this.api = api;
        }

        public void setGrant_type(String grant_type) {
            this.grant_type = grant_type;
        }

        public String getApi() {
            if (StringUtils.isBlank(this.api)) {
                throw new SecurityException("weixin.auth.api 未定义");
            }
            return api;
        }

        public String getGrant_type() {
            if (StringUtils.isBlank(this.grant_type)) {
                throw new SecurityException("weixin.auth.grant_type 未定义");
            }
            return grant_type;
        }
    }

}
