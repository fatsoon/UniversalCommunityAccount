package com.fatsoon.uca.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author fanshuo
 * @since 2013-05-09
 */
public class PreferenceManager {
	public static final String SharedPreferencesName = "UCA_SharedPreferences";

	// QQ互连
	public static final String KEY_LOGIN_QQ_ACCESS_TOKEN = "uca_key_login_qq_access_token";
	public static final String KEY_LOGIN_QQ_OPEN_ID = "uca_key_login_qq_open_id";
	public static final String KEY_LOGIN_QQ_EXPIRES_IN = "uca_key_login_qq_expires_in";
	public static final String KEY_LOGIN_QQ_EXPIRES_TIME = "uca_key_login_qq_expires_time";// 过期时间

	// 新浪微博
	public static final String KEY_WEIBO_SINA_TOKEN = "uca_key_weibo_sina_token";
	public static final String KEY_WEIBO_SINA_EXPIRES_IN = "uca_key_weibo_sina_expires_in";
	public static final String KEY_WEIBO_SINA_WEIBO_UID = "uca_key_weibo_sina_weibo_uid";

	private static SharedPreferences getSharedPreferences(final Context pContext) {
		if (pContext == null) {
			return null;
		}
		SharedPreferences pre = pContext.getSharedPreferences(
				SharedPreferencesName, 0);
		return pre;
	}

	public static float getFloat(final Context pContext, final String pKey,
			final float pDefaultValue) {
		return PreferenceManager.getSharedPreferences(pContext).getFloat(pKey,
				pDefaultValue);
	}

	public static boolean putFloat(final Context pContext, final String pKey,
			final float pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putFloat(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int getInt(final Context pContext, final String pKey,
			final int pDefaultValue) {
		return PreferenceManager.getSharedPreferences(pContext).getInt(pKey,
				pDefaultValue);
	}

	public static boolean putInt(final Context pContext, final String pKey,
			final int pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putInt(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static long getLong(final Context pContext, final String pKey,
			final long pDefaultValue) {
		return PreferenceManager.getSharedPreferences(pContext).getLong(pKey,
				pDefaultValue);
	}

	public static boolean putLong(final Context pContext, final String pKey,
			final long pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putLong(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean getBoolean(final Context pContext, final String pKey,
			final boolean pDefaultValue) {
		return PreferenceManager.getSharedPreferences(pContext).getBoolean(
				pKey, pDefaultValue);
	}

	public static boolean putBoolean(final Context pContext, final String pKey,
			final boolean pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putBoolean(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getString(final Context pContext, final String pKey,
			final String pDefaultValue) {
		return PreferenceManager.getSharedPreferences(pContext).getString(pKey,
				pDefaultValue);
	}

	public static boolean putString(final Context pContext, final String pKey,
			final String pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putString(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean remove(final Context context, final String key) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(context).edit();
			editor.remove(key);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}