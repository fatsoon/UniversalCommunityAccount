package com.fatsoon.uca.callbacks;

public interface SinaLoginCallBack {

	public abstract void onSuccess();
	
	/**
	 * 错误码：<br>
	 * <b>1001</b> : 新授权的得到的uid和原来的uid不一致
	 * @param code
	 * @param message
	 */
	public abstract void onFaild(int code, String message);
	
}
