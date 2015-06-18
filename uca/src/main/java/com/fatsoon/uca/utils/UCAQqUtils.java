package com.fatsoon.uca.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.fatsoon.uca.activity.LoginQqActivity;
import com.fatsoon.uca.callbacks.GetUserInfoCallBack;
import com.fatsoon.uca.callbacks.QqLoginCallBack;
import com.fatsoon.uca.callbacks.UniversalCallBack;
import com.fatsoon.uca.entity.QqReturnData;
import com.fatsoon.uca.entity.UserInfoData;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;


/**
 * @author fatsoon
 * @date 2013-5-9 上午10:31:26
 */
public class UCAQqUtils {

    private static Tencent tencent;

    public static Tencent getTencent(Context context) {
        if (tencent != null && tencent.isSessionValid()) {

        } else {
            String access_token = PreferenceManager.getString(context,
                    PreferenceManager.KEY_LOGIN_QQ_ACCESS_TOKEN, "");
            String openid = PreferenceManager.getString(context,
                    PreferenceManager.KEY_LOGIN_QQ_OPEN_ID, "");
            String expires_in = PreferenceManager.getString(context,
                    PreferenceManager.KEY_LOGIN_QQ_EXPIRES_IN, "0");
            tencent = Tencent.createInstance(LoginQqActivity.APP_ID, context);
            tencent.setAccessToken(access_token, expires_in);
            tencent.setOpenId(openid);
        }
        return tencent;
    }

    /**
     * 进行授权
     *
     * @param context
     */
    public static void login(Context context, QqLoginCallBack callBack) {
        LoginQqActivity.setCallBack(callBack);
        context.startActivity(new Intent(context, LoginQqActivity.class));
    }

    public static void login(Context context) {
        context.startActivity(new Intent(context, LoginQqActivity.class));
    }

    /**
     * 检查是否已经授权
     *
     * @param context
     * @return
     */
    public static boolean hasAlreadyLogin(Context context) {
        try {
            String access_token = PreferenceManager.getString(context,
                    PreferenceManager.KEY_LOGIN_QQ_ACCESS_TOKEN, "");
            String openid = PreferenceManager.getString(context,
                    PreferenceManager.KEY_LOGIN_QQ_OPEN_ID, "");
            if (!TextUtils.isEmpty(openid) && !TextUtils.isEmpty(access_token)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取本地保存的授权信息
     *
     * @param context
     * @return
     */
    public static QqReturnData getLoginData(Context context) {
        QqReturnData data = new QqReturnData();
        try {
            String access_token = PreferenceManager.getString(context,
                    PreferenceManager.KEY_LOGIN_QQ_ACCESS_TOKEN, "");
            String openid = PreferenceManager.getString(context,
                    PreferenceManager.KEY_LOGIN_QQ_OPEN_ID, "");
            Long expires_time = PreferenceManager.getLong(context,
                    PreferenceManager.KEY_LOGIN_QQ_EXPIRES_TIME, 0L);
            data.setAccess_token(access_token);
            data.setOpenid(openid);
            data.setExpires_time(expires_time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    /**
     * 删除本地保存的授权信息
     *
     * @param context
     */
    public static void QqRemoveLoginData(Context context) {
        PreferenceManager.remove(context, PreferenceManager.KEY_LOGIN_QQ_ACCESS_TOKEN);
        PreferenceManager.remove(context, PreferenceManager.KEY_LOGIN_QQ_OPEN_ID);
        PreferenceManager.remove(context, PreferenceManager.KEY_LOGIN_QQ_EXPIRES_IN);
        PreferenceManager.remove(context, PreferenceManager.KEY_LOGIN_QQ_EXPIRES_TIME);
        getTencent(context).logout(context);
    }

    /**
     * 分享到qq好友
     *
     * @param context
     * @param title     标题
     * @param content   内容
     * @param appName   要显示的应用名称
     * @param targeturl 要显示的地址 格式必须为http://xxx.yy
     * @param imageUrl  图片地址，可以为null
     */
    public static void shareToQQ(Activity activity, String title,
                                 String content, String appName, String targetUrl, String imageUrl, String site,
                                 IUiListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("summary", content);
        bundle.putString("appName", appName);
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "";
        }
        bundle.putString("imageUrl", imageUrl);
        bundle.putString("targetUrl", targetUrl);
        bundle.putString("site", site);
        if (ready(activity)) {
            getTencent(activity).shareToQQ(activity, bundle, listener);
        } else {
            login(activity);
        }
    }

    /**
     * 分享到qq空间
     *
     * @param context
     * @param title   feeds的标题
     * @param url     分享所在网页资源的链接，点击后跳转至第三方网页，对应上文接口说明中2的超链接。请以http://开头。
     * @param comment 用户评论内容，也叫发表分享时的分享理由，对应上文接口说明的1。禁止使用系统生产的语句进行代替。最长40个中文字，
     *                超出部分会被截断。
     * @param summary 所分享的网页资源的摘要内容，或者是网页的概要描述，对应上文接口说明的3。最长80个中文字，超出部分会被截断。
     */
    public static void addShare(final Context context, String title,
                                String url, String comment, String summary, String imageUrl, String site, String fromUrl,
                                final UniversalCallBack callBack) {
        if (ready(context)) {
            Bundle paramas = new Bundle();
            paramas.putString("title", title);// 必须。feeds的标题，最长36个中文字，超出部分会被截断。
            paramas.putString("url", url);// 必须。分享所在网页资源的链接，点击后跳转至第三方网页，请以http://开头。
            paramas.putString("comment", comment);// 用户评论内容，也叫发表分享时的分享理由。禁止使用系统生产的语句进行代替。最长40个中文字，超出部分会被截断。
            paramas.putString("summary", summary);// 所分享的网页资源的摘要内容，或者是网页的概要描述。最长80个中文字，超出部分会被截断。
            if (!TextUtils.isEmpty(imageUrl)) {
                // 所分享的网页资源的代表性图片链接"，请以http://开头，长度限制255字符。多张图片以竖线（|）分隔，目前只有第一张图片有效，图片规格100*100为佳。
                paramas.putString("images", imageUrl);
            }
            paramas.putString("type", "4");// 分享内容的类型。
            paramas.putString("site", site);// 分享内容的类型。
            paramas.putString("fromurl", fromUrl);// 分享内容的类型。
            paramas.putString("nswb", "1");// 分享内容的类型。
            getTencent(context).requestAsync(Constants.GRAPH_ADD_SHARE,
                    paramas, Constants.HTTP_POST, new IRequestListener() {

                        @Override
                        public void onUnknowException(Exception arg0,
                                                      Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onSocketTimeoutException(
                                SocketTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onNetworkUnavailableException(
                                NetworkUnavailableException arg0, Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onMalformedURLException(
                                MalformedURLException arg0, Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onJSONException(JSONException arg0,
                                                    Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onIOException(IOException arg0, Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onHttpStatusException(
                                HttpStatusException arg0, Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onConnectTimeoutException(
                                ConnectTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                        }

                        @Override
                        public void onComplete(JSONObject arg0, Object arg1) {
                            callBack.onSuccess();
                        }
                    }, null);
        }
    }

    /**
     * 分享到腾讯微博
     *
     * @param context
     * @param content  分享内容
     * @param callBack 回调
     */
    public static void addT(final Context context, String content,
                            final UniversalCallBack callBack) {
        if (ready(context)) {
            Bundle paramas = new Bundle();
            paramas.putString("format", "json");
            paramas.putString("content", content);
            getTencent(context).requestAsync("t/add_t", paramas,
                    Constants.HTTP_POST, new IRequestListener() {

                        @Override
                        public void onUnknowException(Exception arg0,
                                                      Object arg1) {
                            callBack.onFaild();
                            System.err.println("onUnknowException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onSocketTimeoutException(
                                SocketTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onSocketTimeoutException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onNetworkUnavailableException(
                                NetworkUnavailableException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onNetworkUnavailableException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onMalformedURLException(
                                MalformedURLException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onMalformedURLException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onJSONException(JSONException arg0,
                                                    Object arg1) {
                            callBack.onFaild();
                            System.err.println("onJSONException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onIOException(IOException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onIOException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onHttpStatusException(
                                HttpStatusException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onHttpStatusException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onConnectTimeoutException(
                                ConnectTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onConnectTimeoutException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onComplete(JSONObject arg0, Object arg1) {
                            callBack.onSuccess();
                            System.err.println("onComplete" + arg0.toString());
                        }
                    }, null);
        }
    }

    /**
     * 分享到腾讯微博
     *
     * @param context
     * @param content  分享内容
     * @param callBack 回调
     * @param imageUrl 图片地址
     */
    public static void addPicT(final Context context, String content,
                               String imageUrl, final UniversalCallBack callBack) {
        if (ready(context)) {
            Bundle paramas = new Bundle();
            paramas.putString("format", "json");
            paramas.putString("content", content);
            paramas.putString("pic", imageUrl);
            getTencent(context).requestAsync("t/add_pic_t", paramas,
                    Constants.HTTP_POST, new IRequestListener() {

                        @Override
                        public void onUnknowException(Exception arg0,
                                                      Object arg1) {
                            callBack.onFaild();
                            System.err.println("onUnknowException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onSocketTimeoutException(
                                SocketTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onSocketTimeoutException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onNetworkUnavailableException(
                                NetworkUnavailableException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onNetworkUnavailableException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onMalformedURLException(
                                MalformedURLException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onMalformedURLException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onJSONException(JSONException arg0,
                                                    Object arg1) {
                            callBack.onFaild();
                            System.err.println("onJSONException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onIOException(IOException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onIOException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onHttpStatusException(
                                HttpStatusException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onHttpStatusException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onConnectTimeoutException(
                                ConnectTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onConnectTimeoutException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onComplete(JSONObject arg0, Object arg1) {
                            callBack.onSuccess();
                            System.err.println("onComplete" + arg0.toString());
                        }
                    }, null);
        }
    }

    /**
     * 收听某个微博用户
     *
     * @param context
     * @param name     要收听的用户的账户名
     * @param callBack
     */
    public static void createFriend(final Context context, String name, final UniversalCallBack callBack) {
        Bundle paramas = new Bundle();
        paramas.putString("format", "json");
        paramas.putString("name", name);
        if (ready(context)) {
            getTencent(context).requestAsync("t/add_idol", paramas,
                    Constants.HTTP_POST, new IRequestListener() {
                        @Override
                        public void onUnknowException(Exception arg0,
                                                      Object arg1) {
                            callBack.onFaild();
                            System.err.println("onUnknowException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onSocketTimeoutException(
                                SocketTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onSocketTimeoutException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onNetworkUnavailableException(
                                NetworkUnavailableException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onNetworkUnavailableException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onMalformedURLException(
                                MalformedURLException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onMalformedURLException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onJSONException(JSONException arg0,
                                                    Object arg1) {
                            callBack.onFaild();
                            System.err.println("onJSONException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onIOException(IOException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onIOException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onHttpStatusException(
                                HttpStatusException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onHttpStatusException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onConnectTimeoutException(
                                ConnectTimeoutException arg0, Object arg1) {
                            callBack.onFaild();
                            System.err.println("onConnectTimeoutException"
                                    + arg0.toString());
                        }

                        @Override
                        public void onComplete(JSONObject arg0, Object arg1) {
                            callBack.onSuccess();
                            System.err.println("onComplete" + arg0.toString());
                        }
                    }, null);
        }
    }

    public static void getUserInfo(final Context context,
                                   final GetUserInfoCallBack callBack) {
        if (ready(context)) {
            getTencent(context).requestAsync(Constants.GRAPH_SIMPLE_USER_INFO,
                    null, Constants.HTTP_GET, new IRequestListener() {
                        @Override
                        public void onUnknowException(Exception arg0,
                                                      Object arg1) {
                            callBack.onFaild("获取失败-未知错误");
                        }

                        @Override
                        public void onSocketTimeoutException(
                                SocketTimeoutException arg0, Object arg1) {
                            callBack.onFaild("获取失败-onSocketTimeoutException");
                        }

                        @Override
                        public void onNetworkUnavailableException(
                                NetworkUnavailableException arg0, Object arg1) {
                            callBack.onFaild("获取失败-onNetworkUnavailableException");
                        }

                        @Override
                        public void onMalformedURLException(
                                MalformedURLException arg0, Object arg1) {
                            callBack.onFaild("获取失败-onMalformedURLException");
                        }

                        @Override
                        public void onJSONException(JSONException arg0,
                                                    Object arg1) {
                            callBack.onFaild("获取失败-JSONException");
                        }

                        @Override
                        public void onIOException(IOException arg0, Object arg1) {
                            callBack.onFaild("获取失败-IOException");
                        }

                        @Override
                        public void onHttpStatusException(
                                HttpStatusException arg0, Object arg1) {
                            callBack.onFaild("获取失败-onHttpStatusException");
                        }

                        @Override
                        public void onConnectTimeoutException(
                                ConnectTimeoutException arg0, Object arg1) {
                            callBack.onFaild("获取失败-onConnectTimeoutException");
                        }

                        @Override
                        public void onComplete(JSONObject arg0, Object arg1) {
                            UserInfoData data = new UserInfoData();
                            data.setName(arg0.optString("nickname"));
                            data.setIconUrl(arg0.optString("figureurl_qq_1"));
                            data.setSex(arg0.optString("gender"));
                            callBack.onSuccess(data);
                        }
                    }, null);
        }
    }

    private static boolean ready(Context context) {
        boolean ready = getTencent(context).isSessionValid()
                && getTencent(context).getOpenId() != null;
        if (!ready)
            Toast.makeText(context, "请先授权", Toast.LENGTH_SHORT).show();
        return ready;
    }

}
