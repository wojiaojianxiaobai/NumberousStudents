package com.wb.numerousstudents.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.Utils.MD5Utils;
import com.wb.numerousstudents.Utils.MyOKhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back,tv_register,tv_find_psw;
    private Button btn_login;
    private String userName,psw;
    private EditText et_user_name,et_psw;

    private MyOKhttpUtil.ResponseListener mLoginInterface;

    private static final boolean DEBUG = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        
    }
    /**
     * 获取界面控件
     */
    private void init(){
        tv_back=(TextView) findViewById(R.id.tv_back);
        tv_register=(TextView) findViewById(R.id.tv_register);
        btn_login=(Button) findViewById(R.id.btn_login);
        et_user_name=(EditText) findViewById(R.id.et_user_name);
        et_psw=(EditText) findViewById(R.id.et_psw);
        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DEBUG){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("正在登陆...");
                progressDialog.setCancelable(true);

                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();
                final String md5Psw= MD5Utils.md5(psw);
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    progressDialog.show();

                    String url = "http://175.24.23.24:8080/login";
                    FormBody.Builder formBody = new FormBody.Builder();
                    formBody.add("username",userName);
                    String request_md5Psw= MD5Utils.md5(psw);
                    formBody.add("password",request_md5Psw);
                    MyOKhttpUtil.getInstance().get(url,formBody);
                    MyOKhttpUtil.getInstance().setMyOKHttpUtilListener(new MyOKhttpUtil.ResponseListener() {
                        @Override
                        public void findOnSuccess(String response) {
                            progressDialog.dismiss();

                            if (DEBUG){
                                KLog.v("wb.z login response: " + response);
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean state = (boolean)jsonObject.get("state");
                                String message = jsonObject.getString("message");
                                if (state){
                                    String userMessage = jsonObject.getString("userMessage");
                                    String userNickName = jsonObject.getString("userNickName");
                                    String userPersonalizedSignature = jsonObject.getString("userPersonalizedSignature");

                                    saveLoginStatus(state,userName,userNickName,userPersonalizedSignature);
                                    getLoginConfig(userMessage);
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);

                                    if (DEBUG){
                                        KLog.v("wb.z userMessage:" + userMessage );
                                    }
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }else {
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                                if (DEBUG){
                                    KLog.v("wb.z :state :" + state);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (DEBUG){
                                KLog.v("wb.z :response :" + response);
                            }
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
            }
        });
    }

    private void saveLoginStatus(boolean status,String userName,String userNickName,String userPersonalizedSignature){
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        editor.putBoolean("isLogin", status);//存入boolean类型的登录状态
        editor.putString("loginUserName", userName);//存入登录状态时的用户名
        editor.putString("userNickName",userNickName);
        editor.putString("userPersonalizedSignature",userPersonalizedSignature);
        editor.apply();
    }
    private void getLoginConfig(String userConfig){
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        if (!userConfig.equals("null")){
            try {
                JSONObject jsonObject = new JSONObject(userConfig);
                editor.putString("userSex",jsonObject.getString("userSex"));
                editor.putString("userAge",jsonObject.getString("userAge"));
//                editor.putString("userBirthday",jsonObject.getString("userBirthday"));
                editor.putString("userClass",jsonObject.getString("userClass"));
                editor.putString("userPhone",jsonObject.getString("userPhone"));
                editor.putString("userAddress",jsonObject.getString("userAddress"));
                editor.putString("userEmailAddress",jsonObject.getString("userEmailAddress"));
                KLog.v("wb.z :" + userConfig);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            editor.putString("userSex","");
            editor.putString("userAge","");
//                editor.putString("userBirthday",jsonObject.getString("userBirthday"));
            editor.putString("userClass","4");
            editor.putString("userPhone","");
            editor.putString("userAddress","");
            editor.putString("userEmailAddress","");
        }
        editor.apply();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //从注册界面传递过来的用户名
            String userName =data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                et_user_name.setText(userName);
                //设置光标的位置
                et_user_name.setSelection(userName.length());
            }
        }
    }

}