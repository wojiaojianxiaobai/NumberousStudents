package com.wb.numerousstudents.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.socks.library.KLog;
import com.wb.numerousstudents.Fragment.MomentsFragment;
import com.wb.numerousstudents.Fragment.MyConfigFragment;
import com.wb.numerousstudents.Fragment.StartAnswerFragment;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.processManage.LockSettingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STUDY_STATE = 0;
    private static final int FIND_MORE_STATE = 1;
    private static final int MY_CONFIG_STATE = 2;

    private int mCurState;

    private Fragment mStartAnswerFragment;
    private Fragment mMyConfigFragment;
    private Fragment mFindMoreFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        if (item == R.id.goToLockSettingAcitvityButton) {
            Intent intent = new Intent(MainActivity.this, LockSettingActivity.class);
            startActivity(intent);
        }else if (item == R.id.start_answer_button){
            setStudyFragment();
        }else if (item == R.id.find_more){
            setFindMoreFragment();
        }else if (item == R.id.my_config){
            setMyConfigFragment();
        }
    }

    private void init(){
        initButton();
        initView();
    }

    private void initButton(){
        Button mLockButton = findViewById(R.id.goToLockSettingAcitvityButton);
        Button mStartAnswerButton = findViewById(R.id.start_answer_button);
        Button findMore = findViewById(R.id.find_more);
        Button myConfig = findViewById(R.id.my_config);

        mStartAnswerButton.setOnClickListener(this);
        mLockButton.setOnClickListener(this);
        findMore.setOnClickListener(this);
        myConfig.setOnClickListener(this);
    }


    private void initView(){
        mStartAnswerFragment = new StartAnswerFragment();
        mFindMoreFragment = new MomentsFragment();
        mMyConfigFragment = new MyConfigFragment();

        mCurState = STUDY_STATE;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_content,mStartAnswerFragment).commit();
//        mFragmentTransaction.add(R.id.main_content,new StartAnswerFragment()).commit();
    }

    private void setStudyFragment(){
        mCurState = STUDY_STATE;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.main_content,mStartAnswerFragment).commit();
//        mFragmentTransaction.replace(R.id.main_content,new StartAnswerFragment()).commit();
    }

    private void setMyConfigFragment(){
        mCurState = MY_CONFIG_STATE;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.main_content,mMyConfigFragment).commit();
//        mFragmentTransaction.replace(R.id.main_content,new MyConfigFragment()).commit();
    }

    private void setFindMoreFragment(){
        mCurState = FIND_MORE_STATE;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, mFindMoreFragment).commit();
//        fragmentTransaction.replace(R.id.main_content, new MomentsFragment()).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KLog.i("wb.z :onActivityResult :" + requestCode);
        if (mCurState == STUDY_STATE){
            mStartAnswerFragment.onActivityResult(requestCode,resultCode,data);
        }
    }

}
