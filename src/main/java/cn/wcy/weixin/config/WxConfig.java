package cn.wcy.weixin.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties
public class WxConfig {

    @Value("${weixin.auth.api}")
    private String authApi;

    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.secret}")
    private String secret;

    @Value("${weixin.auth.grant_type}")
    private String grantType;


}
