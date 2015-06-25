package com.fatsoon.uca.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.fatsoon.uca.callbacks.QqLoginCallBack;
import com.fatsoon.uca.utils.ActivityUtils;
import com.fatsoon.uca.utils.PreferenceManager;
import com.fatsoon.uca.utils.UCAQqUtils;

public class LoginQqActivity extends Activity {

	private Tencent mTencent;
	public static String APP_ID = "";
	public static final String SCOPE = "get_simple_userinfo";
	public static QqLoginCallBack callBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		readAppInfo();
		mTencent = Tencent.createInstance(APP_ID, getApplicationContext());
		String access_token = PreferenceManager.getString(this,
				PreferenceManager.KEY_LOGIN_QQ_ACCESS_TOKEN, "");
		String openid = PreferenceManager.getString(this,
				PreferenceManager.KEY_LOGIN_QQ_OPEN_ID, "");
		String expires_in = PreferenceManager.getString(this,
				PreferenceManager.KEY_LOGIN_QQ_EXPIRES_IN, "0");
		mTencent.setAccessToken(access_token, expires_in);
		mTencent.setOpenId(openid);
		qqLogin(null);
	}

	private void readAppInfo() {
		try {
			ApplicationInfo appInfo = this.getPackageManager()
					.getApplicationInfo(getPackageName(),
							PackageManager.GET_META_DATA);
			String qqAppId = appInfo.metaData.getString("uca_qq_appid");
			if(qqAppId == null){
				throw new RuntimeException("Manifest文件中找不到key为uca_qq_appid的metadata");
			}
			APP_ID = qqAppId.substring(7);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void qqLogin(View v) {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener();
			mTencent.login(this, SCOPE, listener);
		} else {
			// mTencent.logout(this);
			UCAQqUtils.QqRemoveLoginData(this);
			IUiListener listener = new BaseUiListener();
			mTencent.login(this, SCOPE, listener);
//			ActivityUtils.showCenterToast(this, "已经授权过了", Toast.LENGTH_SHORT);
//			finish();
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {
			// 保存到SharedPreferences
			String access_token = values.optString("access_token");
			String openid = values.optString("openid");
			String expires_in = values.optString("expires_in");
			Long expires_time = System.currentTimeMillis()
					+ Long.parseLong(expires_in) * 1000;
			PreferenceManager.putString(LoginQqActivity.this,
					PreferenceManager.KEY_LOGIN_QQ_ACCESS_TOKEN, access_token);
			PreferenceManager.putString(LoginQqActivity.this,
					PreferenceManager.KEY_LOGIN_QQ_OPEN_ID, openid);
			PreferenceManager.putString(LoginQqActivity.this,
					PreferenceManager.KEY_LOGIN_QQ_EXPIRES_IN, expires_in);
			PreferenceManager.putLong(LoginQqActivity.this,
					PreferenceManager.KEY_LOGIN_QQ_EXPIRES_TIME, expires_time);
			LoginQqActivity.this.finish();
			if(callBack != null){
				callBack.onSuccess();
			}
		}

		@Override
		public void onError(UiError e) {
			ActivityUtils.showCenterToast(LoginQqActivity.this, "onError----"
					+ "code:" + e.errorCode + ", msg:" + e.errorMessage
					+ ", detail:" + e.errorDetail, Toast.LENGTH_SHORT);
			LoginQqActivity.this.finish();
		}

		@Override
		public void onCancel() {
			ActivityUtils.showCenterToast(LoginQqActivity.this, "取消授权",
					Toast.LENGTH_SHORT);
			LoginQqActivity.this.finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	public static QqLoginCallBack getCallBack() {
		return callBack;
	}

	public static void setCallBack(QqLoginCallBack callBack) {
		LoginQqActivity.callBack = callBack;
	}

}
