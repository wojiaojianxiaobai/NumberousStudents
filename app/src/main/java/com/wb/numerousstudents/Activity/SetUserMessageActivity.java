package com.wb.numerousstudents.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SetUserMessageActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    private ImageView mBackImageView;
    private TextView mSaveTextView;

    private Spinner mUserClassSpinner;
    private RadioGroup mSexRadioGroup;
    private EditText mUserAgeEditText;
    private EditText mUserPhoneEditText;
    private EditText mUserEmailAddressEditText;
    private EditText mUserAddressEditText;
    private EditText mUserNickNameEditText;
    private EditText userPersonalizedSignatureEditText;

    private String mUsername;
    private String mUserNickName;
    private String mUserPersonalizedSignature;
    private String mUserSex;
    private String mUserAge;
    private String mBirthday;
    private int mUserClass;
    private String mUserPhone;
    private String mUserEmailAddress;
    private String mUserAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_message);
        init();
    }

    private void init() {
        initData();
        initView();
    }

    private void initView() {
        mUserNickNameEditText = findViewById(R.id.et_set_user_nick_name);
        mUserNickNameEditText.setText(mUserNickName);

        userPersonalizedSignatureEditText = findViewById(R.id.et_set_user_personalized_signature);
        userPersonalizedSignatureEditText.setText(mUserPersonalizedSignature);

        mSexRadioGroup = findViewById(R.id.rg_select_user_sex);
        initRadioGroup();

        mUserAgeEditText = findViewById(R.id.et_set_user_age);
        mUserAgeEditText.setText(mUserAge);

        mUserClassSpinner = findViewById(R.id.set_class_spinner);
        initSpinner();

        mUserPhoneEditText = findViewById(R.id.et_set_user_phone);
        mUserPhoneEditText.setText(mUserPhone);

        mUserEmailAddressEditText = findViewById(R.id.et_set_user_mail);
        mUserEmailAddressEditText.setText(mUserEmailAddress);

        mUserAddressEditText = findViewById(R.id.et_set_user_address);
        mUserAddressEditText.setText(mUserAddress);

        mBackImageView = findViewById(R.id.set_message_img_back);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEditActivity();
            }
        });

        mSaveTextView = findViewById(R.id.tv_save_user_config);
        mSaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserMessage();
            }
        });

    }

    private void saveUserMessage() {
        JSONObject jsonObject = new JSONObject();
        try {
            mUserNickName = mUserNickNameEditText.getText().toString();
            jsonObject.put("userNickName", mUserNickName);

            mUserPersonalizedSignature = userPersonalizedSignatureEditText.getText().toString();
            jsonObject.put("userPersonalizedSignature", mUserPersonalizedSignature);

            if (mSexRadioGroup.getCheckedRadioButtonId() == R.id.rb_set_male) {
                mUserSex = "male";
                jsonObject.put("userSex", "male");
            } else {
                mUserSex = "female";
                jsonObject.put("userSex", "female");
            }
            mUserAge = mUserAgeEditText.getText().toString();
            jsonObject.put("userAge", mUserAge);

            mUserClass = mUserClassSpinner.getSelectedItemPosition();
            jsonObject.put("userClass", mUserClass);

            mUserPhone = mUserPhoneEditText.getText().toString();
            jsonObject.put("userPhone", mUserPhone);

            mUserEmailAddress = mUserEmailAddressEditText.getText().toString();
            jsonObject.put("emailAddress", mUserEmailAddress);

            mUserAddress = mUserAddressEditText.getText().toString();
            jsonObject.put("address", mUserAddress);

            KLog.v("wb.z save message:" + jsonObject.toString());


//            jsonObject.put("userNickName",mUserNickNameEditText.getText());
//            jsonObject.put("userPersonalizedSignature",userPersonalizedSignatureEditText.getText());
//
//            if (mSexRadioGroup.getCheckedRadioButtonId() == R.id.rb_set_male){
//                jsonObject.put("userSex","male");
//            }else {
//                jsonObject.put("userSex","female");
//            }
//            jsonObject.put("userAge", mUserAgeEditText.getText());
//            jsonObject.put("userClass",mUserClassSpinner.getSelectedItemPosition());
//            jsonObject.put("userPhone", mUserPhoneEditText.getText());
//            jsonObject.put("emailAddress", mUserEmailAddressEditText.getText());
//            jsonObject.put("address", mUserAddressEditText.getText());


            mUsername = mSharedPreferences.getString("loginUserName", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveUserMessageInLocal();

    }

    private void initData() {
        mSharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        mUsername = mSharedPreferences.getString("loginUserName", "");
        mUserNickName = mSharedPreferences.getString("userNickName", "未登录");
        mUserPersonalizedSignature = mSharedPreferences.getString("userPersonalizedSignature", "未设置");
        mUserSex = mSharedPreferences.getString("userSex", "未设置");
        mUserAge = mSharedPreferences.getString("userAge", "");
//        mBirthday = mSharedPreferences.getString("userBirthday","未设置");  //先不要
        mUserClass = mSharedPreferences.getInt("userClass", 0);
        mUserPhone = mSharedPreferences.getString("userPhone", "");
        mUserEmailAddress = mSharedPreferences.getString("emailAddress", "");
        mUserAddress = mSharedPreferences.getString("address", "");
    }

    private void saveUserMessageInLocal() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("userNickName", mUserNickName);
        editor.putString("userPersonalizedSignature", mUserPersonalizedSignature);
        editor.putString("userSex", mUserSex);
        editor.putString("userAge", mUserAge);
        editor.putInt("userClass", mUserClass);
        editor.putString("userPhone", mUserPhone);
        editor.putString("emailAddress", mUserEmailAddress);
        editor.putString("address", mUserAddress);
        editor.apply();
    }


    /**
     * Spinner自定义样式
     * 1、Spinner内的TextView样式：item_select
     * 2、Spinner下拉中每个item的TextView样式：item_drop
     * 3、Spinner下拉框样式，属性设置
     */
    private void initSpinner() {
//        mUserClassSpinner.setDropDownWidth(400); //下拉宽度
//        mUserClassSpinner.setDropDownHorizontalOffset(100); //下拉的横向偏移
//        mUserClassSpinner.setDropDownVerticalOffset(100); //下拉的纵向偏移
        //mSpinnerSimple.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
        //mSpinnerSimple.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效

        String[] spinnerItems = {"小学", "初中", "高中", "大学"};
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        mUserClassSpinner.setAdapter(spinnerAdapter);
        mUserClassSpinner.setSelection(mUserClass);
//        if (mUserClass.equals("初中")){
//            mUserClassSpinner.setSelection(1);
//        }else if (mUserClass.equals("高中")){
//            mUserClassSpinner.setSelection(2);
//        }else if (mUserClass.equals("大学")){
//            mUserClassSpinner.setSelection(3);
//        }else {
//            mUserClassSpinner.setSelection(0);
//        }
    }

    private void initRadioGroup() {
        if (mUserSex.equals("female")) {
            mSexRadioGroup.check(R.id.rb_set_female);
        } else {
            mSexRadioGroup.check(R.id.rb_set_male);
        }
    }

    private void exitEditActivity() {

        new AlertDialog.Builder(SetUserMessageActivity.this)
                .setTitle("信息将不会被保存")
                .setMessage("确定离开吗")
                .setPositiveButton("离开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


}
