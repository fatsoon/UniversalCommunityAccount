package com.fatsoon.uca.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author fatsoon
 * @since 2013-05-09
 */
public class ActivityUtils
{
	public static void requestNoTitlescreen(final Activity pActivity)
	{
		final Window window = pActivity.getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}

	public static void requestFullscreen(final Activity pActivity)
	{
		final Window window = pActivity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}

	public static void setScreenBrightness(final Activity pActivity, final float pScreenBrightness)
	{
		final Window window = pActivity.getWindow();
		final WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
		windowLayoutParams.screenBrightness = pScreenBrightness;
		window.setAttributes(windowLayoutParams);
	}

	public static void keepScreenOn(final Activity pActivity)
	{
		final Window window = pActivity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public static void showCenterToast(Context context, CharSequence text, int duration)
	{
		Toast pToast = Toast.makeText(context, text, duration);
		if (pToast != null)
		{
			pToast.setGravity(Gravity.CENTER, 0, 0);
			pToast.show();
		}
	}
	
	public static void showSoftKeyboard(final Context context, final EditText editText) {
		Timer timer = new Timer();  
        timer.schedule(new TimerTask() {  
              
            @Override  
            public void run()   
            {  
                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
            }
        }, 500);
        if(editText == null) {
        	return;
        }
        editText.requestFocus();
	}
	public static void hideSoftKeyboard(final Context context, EditText editText) {
		final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public static void hideSoftKeyboard(final Context context) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}