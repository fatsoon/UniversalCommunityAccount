package com.fatsoon.uca.callbacks;

import com.fatsoon.uca.entity.UserInfoData;

public interface GetUserInfoCallBack {

	public abstract void onSuccess(UserInfoData data);
	public abstract void onFaild(String message);
	
}
