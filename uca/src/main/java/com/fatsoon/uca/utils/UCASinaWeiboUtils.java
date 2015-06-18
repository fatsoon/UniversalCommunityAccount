package com.fatsoon.uca.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.fatsoon.uca.activity.LoginSinaWeiboActivity;
import com.fatsoon.uca.entity.SinaWeiboReturnData;
import com.fatsoon.uca.entity.UserInfoData;
import com.fatsoon.uca.callbacks.GetUserInfoCallBack;
import com.fatsoon.uca.callbacks.SinaLoginCallBack;

/**
 * @author fanshuo
 * @date 2013-5-9 上午10:31:26
 */
public class UCASinaWeiboUtils {

	private static Oauth2AccessToken accessToken;

	private static Oauth2AccessToken getAccessToken(Context context) {
		if (accessToken == null) {
			String access_token = PreferenceManager.getString(context,
					PreferenceManager.KEY_WEIBO_SINA_TOKEN, "");
			Long expires_in = PreferenceManager.getLong(context,
					PreferenceManager.KEY_WEIBO_SINA_EXPIRES_IN, 0L);
			accessToken = new Oauth2AccessToken(access_token,
					expires_in.toString());
		}
		return accessToken;
	}

	/**
	 * 进行授权
	 * 
	 * @param context
	 * @param callBack
	 *            可以传入null
	 */
	public static void login(Context context, SinaLoginCallBack callBack) {
		LoginSinaWeiboActivity.setCallBack(callBack);
		Intent intent = new Intent(context, LoginSinaWeiboActivity.class);
		intent.putExtra(LoginSinaWeiboActivity.Bundle_Key_Auth_Type, 0);
		context.startActivity(intent);
	}

	public static void reLogin(Context context, SinaLoginCallBack callBack) {
		LoginSinaWeiboActivity.setCallBack(callBack);
		Intent intent = new Intent(context, LoginSinaWeiboActivity.class);
		intent.putExtra(LoginSinaWeiboActivity.Bundle_Key_Auth_Type, 1);
		context.startActivity(intent);
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
					PreferenceManager.KEY_WEIBO_SINA_TOKEN, "");
			Long expireTime = PreferenceManager.getLong(context,
					PreferenceManager.KEY_WEIBO_SINA_EXPIRES_IN, 0L);
			if (expireTime != 0 && !TextUtils.isEmpty(access_token)) {
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
	public static SinaWeiboReturnData getLoginData(Context context) {
		SinaWeiboReturnData data = new SinaWeiboReturnData();
		String access_token = PreferenceManager.getString(context,
				PreferenceManager.KEY_WEIBO_SINA_TOKEN, "");
		Long expires_time = PreferenceManager.getLong(context,
				PreferenceManager.KEY_WEIBO_SINA_EXPIRES_IN, 0L);
		String uid = PreferenceManager.getString(context,
				PreferenceManager.KEY_WEIBO_SINA_WEIBO_UID, "");
		data.setAccess_token(access_token);
		data.setExpires_time(expires_time);
		data.setUid(uid);
		return data;
	}

	public static void SinaRemoveLoginData(Context context) {
		PreferenceManager.remove(context,
				PreferenceManager.KEY_WEIBO_SINA_EXPIRES_IN);
		PreferenceManager.remove(context,
				PreferenceManager.KEY_WEIBO_SINA_TOKEN);
		PreferenceManager.remove(context,
				PreferenceManager.KEY_WEIBO_SINA_WEIBO_UID);
	}

	private static boolean checkValid(Context context) {
		Long sinaExpiresTime = PreferenceManager.getLong(context,
				PreferenceManager.KEY_WEIBO_SINA_EXPIRES_IN, 0L);
		if (!hasAlreadyLogin(context)) {
			ActivityUtils.showCenterToast(context, "尚未授权", Toast.LENGTH_SHORT);
			login(context, null);
			return false;
		}
		if (System.currentTimeMillis() > sinaExpiresTime) {
			ActivityUtils.showCenterToast(context, "授权已过期，需要重新授权",
					Toast.LENGTH_SHORT);
			reLogin(context, null);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 发送一条微博
	 * 
	 * @param context
	 * @param content
	 * @param imageUrl
	 *            图片文件的路径
	 * @param listener
	 */
	public static void shareWeibo(final Context context, String content,
			String imageUrl, RequestListener listener) {
		if (checkValid(context)) {
			StatusesAPI api = new StatusesAPI(context, LoginSinaWeiboActivity.SinaWeibo_APPKEY, getAccessToken(context));
			if (imageUrl == null) {
				api.update(content, "0.0", "0.0", listener);
			} else {
				api.uploadUrlText(content, imageUrl, null, "0.0", "0.0",
						listener);
			}
		}
	}

	/**
	 * 评论微博
	 * 
	 * @param context
	 * @param comment
	 *            内容
	 * @param weibo_id
	 *            微博id
	 * @param listener
	 */
	public static void commentWeibo(final Context context, String comment,
			String weibo_id, RequestListener listener) {
		if (checkValid(context)) {
			CommentsAPI api = new CommentsAPI(context, LoginSinaWeiboActivity.SinaWeibo_APPKEY, getAccessToken(context));
			api.create(comment, Long.parseLong(weibo_id), false, listener);
		}
	}

	/**
	 * 关注一个用户
	 * 
	 * @param context
	 * @param uid
	 *            需要关注的用户ID。
	 * @param screen_name
	 *            需要关注的用户昵称。
	 * @param listener
	 */
	public static void createFriend(Context context, long uid,
			String screen_name, RequestListener listener) {
		if (checkValid(context)) {
			FriendshipsAPI api = new FriendshipsAPI(context, LoginSinaWeiboActivity.SinaWeibo_APPKEY, getAccessToken(context));
			api.create(uid, screen_name, listener);
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @param callBack
	 */
	public static void getUserInfo(final Context context,
			final GetUserInfoCallBack callBack) {
		if (checkValid(context)) {
			String uid = PreferenceManager.getString(context,
					PreferenceManager.KEY_WEIBO_SINA_WEIBO_UID, "");
			UsersAPI userApi = new UsersAPI(context, LoginSinaWeiboActivity.SinaWeibo_APPKEY, getAccessToken(context));
			userApi.show(Long.parseLong(uid), new RequestListener() {
				@Override
				public void onComplete(String arg0) {
					UserInfoData data = new UserInfoData();
					try {
						JSONObject obj = new JSONObject(arg0);
						data.setName(obj.optString("screen_name"));
						data.setIconUrl(obj.optString("profile_image_url"));
						data.setSex(obj.optString("gender"));
					} catch (JSONException e) {
						e.printStackTrace();
						callBack.onFaild("解析出错-JSONException");
					}
					callBack.onSuccess(data);
				}

				@Override
				public void onWeiboException(WeiboException arg0) {
					callBack.onFaild("获取失败-WeiboException");
				}
			});
		}
	}
}
