package cn.wcy.inform;

import cn.wcy.interaction.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Note253Util {

    @Value("${chuanglan.url}")
    private String url;

    @Value("${chuanglan.account}")
    private String account;

    @Value("${chuanglan.password}")
    private String password;

    /**
     * msgid	消息id
     * errorMsg	状态码说明（成功返回值）
     * code	状态码（”返回0为成功，失败的返回值请查看“验证码通知和营销短信提交响应状态码“）
     */
    @Data
    public class InformResult {
        private String code;
        private String msgId;
        private String errorMsg;
    }

    /**
     * account	创蓝API账号	是	具体的值可以登录我司官网zz.253.com，进入管理后台查看
     * password	创蓝API密码	是	具体的值可以登录我司官网zz.253.com，进入管理后台查看
     * msg	短信内容	是	长度不能超过536个字符，签名需要加在内容里， 如：【253云通讯】您的验证码是123456
     * phone	手机号码	是	多个手机号码使用英文逗号分隔
     * sendtime	定时发送短信时间	否	格式为yyyyMMddHHmm，值小于或等于当前时间则立即发送，默认立即发送；且定时时间在7天以内
     * report	是否需要状态报告（默认false）	否	如需状态报告则传true（如需要状态报告和上行的拉取或者推送，必须为true）
     * extend	下发短信号码扩展码	否	纯数字，需保证手机端口号加自定义扩展码总位数不超过20位，建议1-3位，如需上行短信推送或拉取则必填
     * uid	该条短信在您业务系统内的ID	否	一般可以设置订单号或者短信发送记录流水号，用于区分短信业务，总位数不超过40位
     */
    public InformResult sendCodeInform(String uid, String extend, Boolean report, Long sendtime, String msg, String... phones) {
        JSONObject body = new JSONObject();
        body.put("account", account);
        body.put("password", password);
        body.put("msg", msg);
        body.put("phone", String.join(",", phones));
        body.put("sendtime", sendtime);
        body.put("report", report);
        body.put("extend", extend);
        body.put("uid", uid);
        return getResultInform(HttpUtil.post(body, url));
    }
    public InformResult sendCodeInform(String msg, String... phones) {
        JSONObject body = new JSONObject();
        body.put("account", account);
        body.put("password", password);
        body.put("msg", msg);
        body.put("phone", String.join(",", phones));
        return getResultInform(HttpUtil.post(body, url));
    }
    private InformResult getResultInform(String result) {
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("请求253接口失败, 返回信息：" + result);
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        InformResult res = new InformResult();
        res.setCode(jsonObject.getString("code"));
        res.setErrorMsg(jsonObject.getString("errorMsg"));
        res.setMsgId(jsonObject.getString("msgId"));
        return res;
    }

}
