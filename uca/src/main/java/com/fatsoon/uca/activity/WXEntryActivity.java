package com.fatsoon.uca.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.fatsoon.uca.utils.UCAWeixinUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by fanshuo on 15/7/2.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UCAWeixinUtils.getApi(this).handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO 响应微信发出的请求，暂时没用
        this.finish();
    }

    @Override
    public void onResp(BaseResp arg0) {
        String result = "";

        switch (arg0.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "授权出错";
                break;
            default:
                result = "未知错误";
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
