package cn.wcy.weixin.impl;

import cn.wcy.interaction.HttpUtil;
import cn.wcy.weixin.WxApiService;
import cn.wcy.weixin.config.WxConfig;
import cn.wcy.weixin.model.WeiXin;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxApiServiceImpl implements WxApiService {

    @Autowired
    private WxConfig wxConfig;

    private void judgeConfig() {
        if (StringUtils.isBlank(wxConfig.getAppId()) || StringUtils.isBlank(wxConfig.getSecret())) {
            throw new NullPointerException("application.yml inexistence weixin.appid or weixin.secrect");
        }
    }

    @Override
    public WeiXin.Auth auth(WeiXin.AuthPara authPara) {
        this.judgeConfig();
        if (StringUtils.isBlank(authPara.getCode())) {
            throw new NullPointerException("PARAMETER CODE IS NULL");
        }
        String grantType = StringUtils.isBlank(wxConfig.getGrantType())?"authorization_code":wxConfig.getGrantType();
        String paras = "appid="+wxConfig.getAppId()+"&secret="+wxConfig.getSecret()+"&js_code="+authPara.getCode()+"&grant_type="+grantType;
        String authApi = StringUtils.isBlank(wxConfig.getAuthApi())?"https://api.weixin.qq.com/sns/jscode2session":wxConfig.getAuthApi();
        String sendGet = HttpUtil.sendGet(authApi, paras);
        if (StringUtils.isBlank(sendGet)) {
            throw new RuntimeException("AUTH API RESPONSE ERROR, RESPONSE PARAMETER: " + sendGet);
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(sendGet), WeiXin.Auth.class);
    }
}
