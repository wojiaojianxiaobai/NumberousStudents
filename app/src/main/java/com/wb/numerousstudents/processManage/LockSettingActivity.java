package com.wb.numerousstudents.processManage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.socks.library.KLog;
import com.wb.numerousstudents.Config.Config;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.processManage.Service.MyService;
import com.wb.numerousstudents.processManage.Utils.AppLockUtil;

public class LockSettingActivity extends Activity {

    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setting);

        Button button = (Button) findViewById(R.id.start_monitor_button);


        /*//判断辅助功能有没有开
        AccessbilityUtil.anyMethod(this);*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AppLockUtil.isPermissionForTest(LockSettingActivity.this)){
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(LockSettingActivity.this,"权限不够\n请打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾", Toast.LENGTH_LONG).show();
                }else {

                    if (Config.getInstance().getProcessServerState()){
                        //关闭service
                        Config.getInstance().setProcessServerState(false);
                        Intent intent = new Intent(LockSettingActivity.this, MyService.class);
                        stopService(intent);
                        KLog.v("关闭进程服务");

                    }else {
                        Config.getInstance().setProcessServerState(true);
                        KLog.v("打开进程服务");
                        //打开service
                        Intent intent = new Intent(LockSettingActivity.this, MyService.class);
                        startService(intent);
//                        Intent MyIntent = new Intent(Intent.ACTION_MAIN);
//                        startActivity(MyIntent);
//                        finish();
                    }


                }

            }
        });

    }
}
