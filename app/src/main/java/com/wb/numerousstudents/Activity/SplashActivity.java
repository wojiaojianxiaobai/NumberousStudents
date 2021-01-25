package com.wb.numerousstudents.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import com.socks.library.KLog;
import com.wb.numerousstudents.Config.Config;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.processManage.Service.MyService;
import com.wb.numerousstudents.processManage.Utils.AppLockUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean statusStatis = sharedPreferences.getBoolean("studyState", false);
        Config.getInstance().setProcessServerState(statusStatis);
        checkStudyState();
    }


    private void checkStudyState() {
        if (!AppLockUtil.isPermissionForTest(SplashActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(SplashActivity.this, "权限不够\n请打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾", Toast.LENGTH_LONG).show();
        } else {
            KLog.v("打开进程服务");
            //打开service
            Intent intent = new Intent(SplashActivity.this, MyService.class);
            startService(intent);
        }


    }
}
