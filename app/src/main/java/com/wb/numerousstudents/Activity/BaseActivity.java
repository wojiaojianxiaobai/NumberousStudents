package com.wb.numerousstudents.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.socks.library.KLog;
import com.wb.numerousstudents.BuildConfig;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        KLog.init(BuildConfig.LOG_DEBUG);
    }
}
