package com.ddhigh.dodo;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.ddhigh.dodo.orm.User;
import com.ddhigh.dodo.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.File;

/**
 * @project Study
 * @package com.ddhigh.dodo
 * @user xialeistudio
 * @date 2016/2/25 0025
 */
public class MyApplication extends Application {
    public final static String TAG = "dodo-1";

    public User user;
    public User.AccessToken accessToken;
    public File appPath;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        HttpUtil.setApi("https://d.apicloud.com");

        initUser();

        //创建目录
        appPath = new File(Environment.getExternalStorageDirectory(), getPackageName());
        if (!appPath.isDirectory()) {
            appPath.mkdir();
        }
    }

    private void initUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = sharedPreferences.getString(User.PREF_USER_ID, null);
        String token = sharedPreferences.getString(User.PREF_USER_TOKEN, null);
        String userInfo = sharedPreferences.getString(User.PREF_USER, null);
        user = new User();
        accessToken = new User.AccessToken();
        if (userId != null && !TextUtils.isEmpty(userId)) {
            user.setId(userId);
            accessToken.setUserId(userId);
        }
        if (token != null && !TextUtils.isEmpty(token)) {
            accessToken.setId(token);
        }

        if (userInfo != null) {
            try {
                JSONObject u = new JSONObject(userInfo);
                user.parse(u);
                Log.d(MyApplication.TAG, "loadUser From Local");
            } catch (JSONException e) {
                Log.w(MyApplication.TAG, "解析userInfo失败");
            }
        }
    }
}
