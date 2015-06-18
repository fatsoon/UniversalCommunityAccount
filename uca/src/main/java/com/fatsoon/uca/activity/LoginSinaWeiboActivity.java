package com.fatsoon.uca.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.fatsoon.uca.callbacks.SinaLoginCallBack;
import com.fatsoon.uca.utils.ActivityUtils;
import com.fatsoon.uca.utils.PreferenceManager;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * @author fanshuo
 * @date 2013-5-9 上午11:28:20
 */
public class LoginSinaWeiboActivity extends Activity {

    public static final String Bundle_Key_Auth_Type = "LoginSinaWeiboActivity.Bundle_Key_Auth_Type";// 0：授权，1：重新授权

    private SsoHandler mSsoHandler;
    public static String SinaWeibo_APPKEY = "";
    public static String SinaWeibo_RedirectUrl = "";
    public static String SinaWeibo_Scope = "";

    public static SinaLoginCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readAppInfo();
        int authType = getIntent().getIntExtra(Bundle_Key_Auth_Type, 0);
        if (authType == 0) {
            authSina();
        } else if (authType == 1) {
            reAuthSina();
        }
    }

    private void readAppInfo() {
        try {
            ApplicationInfo appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            String weiboKey = appInfo.metaData.getString("uca_weibo_key");
            String weiboDirectUrl = appInfo.metaData.getString("uca_weibo_redirect_url");
            if(weiboKey == null){
                throw new RuntimeException("Manifest文件中找不到key为uca_weibo_key的metadata");
            }
            if(weiboDirectUrl == null){
                throw new RuntimeException("Manifest文件中找不到key为uca_weibo_redirect_url的metadata");
            }
            SinaWeibo_APPKEY = weiboKey.substring(5);
            SinaWeibo_RedirectUrl = weiboDirectUrl;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 重新授权，授权结束后会检查uid是否跟之前的一致，不一致会返回失败
     */
    private void reAuthSina() {
        AuthInfo mWeiboAuth = new AuthInfo(this, SinaWeibo_APPKEY,
                SinaWeibo_RedirectUrl, SinaWeibo_Scope);
        mSsoHandler = new SsoHandler(this, mWeiboAuth);
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(LoginSinaWeiboActivity.this,
                        "授权出错 : " + e.getMessage(), Toast.LENGTH_LONG).show();
                LoginSinaWeiboActivity.this.finish();
            }

            @Override
            public void onComplete(Bundle values) {
                Oauth2AccessToken mAccessToken = Oauth2AccessToken
                        .parseAccessToken(values); // 从 Bundle 中解析 Token
                if (mAccessToken.isSessionValid()) {
                    String token = mAccessToken.getToken();
                    String expires_in = mAccessToken.getExpiresTime() + "";
                    String uid = mAccessToken.getUid();
                    // 检查新授权的uid是否跟之前的一致
                    String oldUid = PreferenceManager.getString(
                            LoginSinaWeiboActivity.this,
                            PreferenceManager.KEY_WEIBO_SINA_WEIBO_UID, "");
                    if (uid.equals(oldUid)) {
                        PreferenceManager.putString(
                                LoginSinaWeiboActivity.this,
                                PreferenceManager.KEY_WEIBO_SINA_TOKEN, token);
                        PreferenceManager
                                .putString(
                                        LoginSinaWeiboActivity.this,
                                        PreferenceManager.KEY_WEIBO_SINA_WEIBO_UID,
                                        uid);
                        PreferenceManager.putLong(LoginSinaWeiboActivity.this,
                                PreferenceManager.KEY_WEIBO_SINA_EXPIRES_IN,
                                Long.parseLong(expires_in));
                        if (callBack != null) {
                            callBack.onSuccess();
                        } else {
                            ActivityUtils.showCenterToast(LoginSinaWeiboActivity.this,
                                    "重新授权成功", Toast.LENGTH_SHORT);
                        }
                    } else {
                        if (callBack != null) {
                            callBack.onFaild(1001, "请使用原微博帐号进行重新授权");
                        } else {
                            ActivityUtils.showCenterToast(LoginSinaWeiboActivity.this,
                                    "请使用原微博帐号进行重新授权", Toast.LENGTH_SHORT);
                        }
                    }
                    LoginSinaWeiboActivity.this.finish();
                } else {
                    // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
                    String code = values.getString("code");
                    ActivityUtils.showCenterToast(LoginSinaWeiboActivity.this,
                            "授权完成时出现错误，错误码" + code, Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginSinaWeiboActivity.this, "授权取消",
                        Toast.LENGTH_LONG).show();
                LoginSinaWeiboActivity.this.finish();
            }
        });
    }

    /**
     * 新浪微博授权
     */
    private void authSina() {
        AuthInfo mWeiboAuth = new AuthInfo(this, SinaWeibo_APPKEY,
                SinaWeibo_RedirectUrl, SinaWeibo_Scope);
        mSsoHandler = new SsoHandler(this, mWeiboAuth);
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(LoginSinaWeiboActivity.this,
                        "授权出错 : " + e.getMessage(), Toast.LENGTH_LONG).show();
                LoginSinaWeiboActivity.this.finish();
            }

            @Override
            public void onComplete(Bundle values) {
                Oauth2AccessToken mAccessToken = Oauth2AccessToken
                        .parseAccessToken(values); // 从 Bundle 中解析 Token
                if (mAccessToken.isSessionValid()) {
                    String token = mAccessToken.getToken();
                    String expires_in = mAccessToken.getExpiresTime() + "";
                    String uid = mAccessToken.getUid();
                    PreferenceManager.putString(LoginSinaWeiboActivity.this,
                            PreferenceManager.KEY_WEIBO_SINA_TOKEN, token);
                    PreferenceManager.putString(LoginSinaWeiboActivity.this,
                            PreferenceManager.KEY_WEIBO_SINA_WEIBO_UID, uid);
                    PreferenceManager.putLong(LoginSinaWeiboActivity.this,
                            PreferenceManager.KEY_WEIBO_SINA_EXPIRES_IN,
                            Long.parseLong(expires_in));
                    if (callBack != null) {
                        callBack.onSuccess();
                    }
                    Toast.makeText(LoginSinaWeiboActivity.this,
                            "授权成功", Toast.LENGTH_LONG).show();
                    LoginSinaWeiboActivity.this.finish();
                } else {
                    // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
                    String code = values.getString("code");
                    ActivityUtils.showCenterToast(LoginSinaWeiboActivity.this,
                            "授权完成时出现错误，错误码" + code, Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginSinaWeiboActivity.this, "授权取消",
                        Toast.LENGTH_LONG).show();
                LoginSinaWeiboActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(final int pRequestCode, int pResultCode,
                                    final Intent pData) {
        super.onActivityResult(pRequestCode, pResultCode, pData);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(pRequestCode, pResultCode, pData);
        }
    }

    public static SinaLoginCallBack getCallBack() {
        return callBack;
    }

    public static void setCallBack(SinaLoginCallBack callBack) {
        LoginSinaWeiboActivity.callBack = callBack;
    }

}
