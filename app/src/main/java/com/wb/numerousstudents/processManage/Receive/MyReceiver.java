package com.wb.numerousstudents.processManage.Receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.socks.library.KLog;
import com.wb.numerousstudents.Config.Config;
import com.wb.numerousstudents.processManage.Service.MyService;

/**
 * Created by 梅梅 on 2016/9/5.
 */
public class MyReceiver extends BroadcastReceiver {
    private static final boolean DEBUG = false;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Config.getInstance().isProcessServer()){
            if (DEBUG){
                KLog.v("wb.z : 服务打开" );
            }
            Intent i = new Intent(context, MyService.class);
            context.startService(i);
        }else {
            if (DEBUG){
                KLog.v("wb.z : 服务关闭");
            }
            Intent i = new Intent(context, MyService.class);
            context.stopService(i);
        }

    }
}
