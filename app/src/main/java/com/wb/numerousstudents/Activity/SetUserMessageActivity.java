package com.wb.numerousstudents.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.Utils.MD5Utils;
import com.wb.numerousstudents.Utils.MyOKhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

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
            mUserNickName = mUserNickNameEditText.getText().toString().trim();
//            jsonObject.put("userNickName", mUserNickName);

            mUserPersonalizedSignature = userPersonalizedSignatureEditText.getText().toString().trim();
//            jsonObject.put("userPersonalizedSignature", mUserPersonalizedSignature);

            if (mSexRadioGroup.getCheckedRadioButtonId() == R.id.rb_set_male) {
                mUserSex = "male";
                jsonObject.put("userSex", "male");
            } else {
                mUserSex = "female";
                jsonObject.put("userSex", "female");
            }
            mUserAge = mUserAgeEditText.getText().toString().trim();
            jsonObject.put("userAge", mUserAge);

            mUserClass = mUserClassSpinner.getSelectedItemPosition();
            jsonObject.put("userClass", mUserClass);

            mUserPhone = mUserPhoneEditText.getText().toString().trim();
            jsonObject.put("userPhone", mUserPhone);

            mUserEmailAddress = mUserEmailAddressEditText.getText().toString().trim();
            jsonObject.put("emailAddress", mUserEmailAddress);

            mUserAddress = mUserAddressEditText.getText().toString().trim();
            jsonObject.put("address", mUserAddress);

            KLog.v("wb.z save message:" + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ProgressDialog progressDialog = new ProgressDialog(SetUserMessageActivity.this);
        progressDialog.setMessage("正在登陆...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String url = "http://175.24.23.24:8080/updateUserMessage";
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("username",mUsername);
        formBody.add("userNickName",mUserNickName);
        formBody.add("userPersonalizedSignature",mUserPersonalizedSignature);

        String jsString = jsonObject.toString();
        formBody.add("userMessage",jsString);
        MyOKhttpUtil.getInstance().get(url,formBody);

        MyOKhttpUtil.getInstance().setMyOKHttpUtilListener(new MyOKhttpUtil.ResponseInterface() {
            @Override
            public void findOnSuccess(String response) {
                progressDialog.dismiss();
                saveUserMessageInLocal();
                finish();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void findOnFail(String response) {
                progressDialog.dismiss();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });

    }

    private void initData() {
        mSharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        mUsername = mSharedPreferences.getString("loginUserName", "");
        mUserNickName = mSharedPreferences.getString("userNickName", "未登录");
        mUserPersonalizedSignature = mSharedPreferences.getString("userPersonalizedSignature", "未设置");
        mUserSex = mSharedPreferences.getString("userSex", "未设置");
        mUserAge = mSharedPreferences.getString("userAge", "");
//        mBirthday = mSharedPreferences.getString("userBirthday","未设置");  //先不要
        String classString = mSharedPreferences.getString("userClass", "0");
        mUserClass = Integer.parseInt(classString);
        mUserPhone = mSharedPreferences.getString("userPhone", "");
        mUserEmailAddress = mSharedPreferences.getString("userEmailAddress", "");
        mUserAddress = mSharedPreferences.getString("userAddress", "");
    }

    private void saveUserMessageInLocal() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("userNickName", mUserNickName);
        editor.putString("userPersonalizedSignature", mUserPersonalizedSignature);
        editor.putString("userSex", mUserSex);
        editor.putString("userAge", mUserAge);
        editor.putString("userClass", mUserClass + "");
        editor.putString("userPhone", mUserPhone);
        editor.putString("userEmailAddress", mUserEmailAddress);
        editor.putString("userAddress", mUserAddress);
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
