package cn.wcy.weixin;

import cn.wcy.weixin.model.WeiXin;

public interface WxApiService {

    WeiXin.Auth auth(WeiXin.AuthPara authPara);

}
