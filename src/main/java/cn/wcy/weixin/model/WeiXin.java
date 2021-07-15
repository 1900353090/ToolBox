package cn.wcy.weixin.model;

import lombok.Data;
import lombok.experimental.Accessors;

public class WeiXin {

    @Data
    public static class Auth {
        //用户唯一标识
        private String openid;
        //会话密钥
        private String session_key;
        //用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
        private String unionid;
        //错误码
        private int errcode;
        //错误信息
        private String errmsg;
    }
    @Data
    @Accessors(chain = true)
    public static class AuthPara {
        //登录时获取的 code
        private String code;
    }

}
