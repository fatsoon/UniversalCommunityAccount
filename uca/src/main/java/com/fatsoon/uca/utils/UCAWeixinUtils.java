package com.fatsoon.uca.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by fanshuo on 15/7/2.
 */
public class UCAWeixinUtils {

    public static String WeiXin_APPID = "";
    public static final int SUPPORT_FRIENDS_VERSION = 0x21020001;
    private static IWXAPI api;
    private static boolean hasRegistered = false;

    public static IWXAPI getApi(Context context) {
        if (!hasRegistered) {
            regToWx(context);
        }
        return api;
    }

    private static void readAppInfo(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            String value = appInfo.metaData.getString("uca_wx_appid");
            if(value == null){
                throw new RuntimeException("Manifest文件中找不到key为uca_wx_appid的metadata");
            }
            WeiXin_APPID = value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void regToWx(Context context) {
        if(TextUtils.isEmpty(WeiXin_APPID)){
            readAppInfo(context);
        }
        api = WXAPIFactory.createWXAPI(context, WeiXin_APPID, true);
        hasRegistered = api.registerApp(WeiXin_APPID);
    }

    public static void sendTextMsgToWx(Context context, String title,
                                       String description, String url, Bitmap picture, boolean isToTimeline) {
        if (!hasRegistered) {
            regToWx(context);
        }
        if(!api.isWXAppInstalled()){
            Toast.makeText(context, "分享失败，没有安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        WXTextObject textObj = new WXTextObject(description);
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        if (isToTimeline) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }

        final WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = description;
        msg.title = title;
        msg.setThumbImage(picture);
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        api.sendReq(req);
    }

}
