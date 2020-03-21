package com.wb.numerousstudents.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wb.numerousstudents.Activity.LoginActivity;
import com.wb.numerousstudents.Activity.SetUserMessageActivity;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.View.ConfigTextItem;


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
        String classString = sharedPreferences.getString("userClass","0");
        mClass = Integer.parseInt(classString);
        mPhone = sharedPreferences.getString("userPhone","");
        mUserAddress = sharedPreferences.getString("userAddress","");
        mUserMail = sharedPreferences.getString("userEmailAddress","");
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
            mSexConfigTextItem.setSummary("未设置");
        }
        mMailConfigTextItem.setSummary(mUserMail);
        mAddressConfigTextItem.setSummary(mUserAddress);
        mAgeConfigTextItem.setSummary(mAge);
        mBirthdayConfigTextItem.setSummary(mBirthday);
        String[] mClassArray = {"小学","初中","高中","大学"};
        mClassConfigTextItem.setSummary(mClassArray[mClass]);
        mPhoneConfigTextItem.setSummary(mPhone);
    }


    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        if (buttonId == R.id.tv_config_change_user){
            showExitDialog();
        }else if (buttonId == R.id.tv_edit_user_config){
            Intent intent = new Intent(getActivity(), SetUserMessageActivity.class);
            startActivity(intent);
            //todo 这里处理编辑个人信息页面
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
