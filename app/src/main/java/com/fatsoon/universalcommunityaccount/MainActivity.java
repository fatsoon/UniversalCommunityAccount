package com.fatsoon.universalcommunityaccount;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fatsoon.uca.activity.LoginQqActivity;
import com.fatsoon.uca.activity.LoginSinaWeiboActivity;
import com.fatsoon.uca.utils.ActivityUtils;
import com.fatsoon.uca.utils.PreferenceManager;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_loginqq) {
            startActivity(new Intent(MainActivity.this, LoginQqActivity.class));
            return true;
        }
        if (id == R.id.action_loginweibo) {
            startActivity(new Intent(MainActivity.this, LoginSinaWeiboActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
