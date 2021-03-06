package com.wb.numerousstudents.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.socks.library.KLog;
import com.wb.numerousstudents.Activity.LoginActivity;
import com.wb.numerousstudents.Activity.SetUserMessageActivity;
import com.wb.numerousstudents.Config.Config;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.View.ConfigSwitchItem;
import com.wb.numerousstudents.View.ConfigTextItem;
import com.wb.numerousstudents.processManage.Service.MyService;
import com.wb.numerousstudents.processManage.Utils.AppLockUtil;


public class MyConfigFragment extends Fragment implements View.OnClickListener {

    private TextView mChangeUserTextView;
    private TextView mUsernameTextView;
    private TextView mUserNickNameTextView;
    private TextView mEditUserTextView;
    private TextView mPersonalizedSignatureTextView;
    private ConfigTextItem mSexConfigTextItem;
    private ConfigTextItem mAgeConfigTextItem;
    private ConfigTextItem mBirthdayConfigTextItem;
    private ConfigTextItem mClassConfigTextItem;
    private ConfigTextItem mPhoneConfigTextItem;
    private ConfigTextItem mAddressConfigTextItem;
    private ConfigTextItem mMailConfigTextItem;

    private ConfigSwitchItem mStudyStateConfigSwitchItem;

    private String mUsername;
    private String mUserNickName;
    private String mPersonalizedSignature;
    private String mSex;
    private String mAge;
    private String mBirthday;
    private int mClass;
    private String mPhone;
    private String mUserAddress;
    private String mUserMail;
    private boolean mStudyState;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_config,null);
        init(view);
        return view;
    }

    private void init(View view){
        initData();
        initButton(view);
    }

    private void initData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString("loginUserName","");
        mUserNickName = sharedPreferences.getString("userNickName","");
        mPersonalizedSignature = sharedPreferences.getString("userPersonalizedSignature","");
        mSex = sharedPreferences.getString("userSex","");
        mAge = sharedPreferences.getString("userAge","");
        mBirthday = sharedPreferences.getString("userBirthday","");
        String classString = sharedPreferences.getString("userClass","4");
        KLog.v("wb.z mClass: " + classString);
        mClass = Integer.parseInt(classString);
        mPhone = sharedPreferences.getString("userPhone","");
        mUserAddress = sharedPreferences.getString("userAddress","");
        mUserMail = sharedPreferences.getString("userEmailAddress","");
        mStudyState = sharedPreferences.getBoolean("studyState",false);
    }
    private void initButton(View view){
        mChangeUserTextView = view.findViewById(R.id.tv_config_change_user);
        mChangeUserTextView.setOnClickListener(this);
        mUsernameTextView = view.findViewById(R.id.tv_config_username);
        mUserNickNameTextView = view.findViewById(R.id.tv_user_nick_name);
        mEditUserTextView = view.findViewById(R.id.tv_edit_user_config);
        mEditUserTextView.setOnClickListener(this);
        mPersonalizedSignatureTextView = view.findViewById(R.id.tv_config_personalized_signature);
        mSexConfigTextItem = view.findViewById(R.id.config_sex);
        mMailConfigTextItem = view.findViewById(R.id.config_mail);
        mAddressConfigTextItem = view.findViewById(R.id.config_address);
        mAgeConfigTextItem = view.findViewById(R.id.config_age);
        mBirthdayConfigTextItem = view.findViewById(R.id.config_birthday);
        mClassConfigTextItem = view.findViewById(R.id.config_class);
        mPhoneConfigTextItem = view.findViewById(R.id.config_phone);
        mStudyStateConfigSwitchItem = view.findViewById(R.id.config_study_state_item);
        initViewData();
    }

    private void initViewData(){
        mUserNickNameTextView.setText(mUserNickName);
        mUsernameTextView.setText(mUsername);
        mPersonalizedSignatureTextView.setText(mPersonalizedSignature);
        if (mSex.equals("female")){
            mSexConfigTextItem.setSummary("女");
        }else if (mSex.equals("male")){
            mSexConfigTextItem.setSummary("男");
        }else {
            mSexConfigTextItem.setSummary("");
        }
        mMailConfigTextItem.setSummary(mUserMail);
        mAddressConfigTextItem.setSummary(mUserAddress);
        mAgeConfigTextItem.setSummary(mAge);
        mBirthdayConfigTextItem.setSummary(mBirthday);
        String[] mClassArray = {"小学","初中","高中","大学",""};
        mClassConfigTextItem.setSummary(mClassArray[mClass]);
        mPhoneConfigTextItem.setSummary(mPhone);

        mStudyStateConfigSwitchItem.setSummary(mStudyState);
        mStudyStateConfigSwitchItem.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        switch (buttonId){
            case R.id.tv_config_change_user:{
                showExitDialog();
                break;
            }
            case R.id.tv_edit_user_config:{
                Intent intent = new Intent(getActivity(), SetUserMessageActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.config_study_state_item:{
                mStudyStateConfigSwitchItem.changeState();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("loginInfo",Context.MODE_PRIVATE).edit();
                if (mStudyState){
                    mStudyState = false;
                    editor.putBoolean("studyState",mStudyState);
                    Config.getInstance().setProcessServerState(false);
                    KLog.v("关闭学霸模式");
                }else {
                    mStudyState = true;
                    editor.putBoolean("studyState",mStudyState);
                    Config.getInstance().setProcessServerState(true);
                    KLog.v("开启学霸模式");
//                    startStudyState();
                }
                editor.apply();

                break;
            }
        }

    }


    private void showExitDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("将会注销当前用户")
                .setMessage("确定吗")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        initViewData();
    }
}
