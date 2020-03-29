package com.wb.numerousstudents.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class RegisterActivity extends AppCompatActivity {

    private TextView tv_main_title;
    private TextView tv_back;
    private Button btn_register;
    private EditText et_user_name,et_psw,et_psw_again;
    private String userName,psw,pswAgain;
    private RelativeLayout rl_title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置页面布局
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    private void init(){
        //从main_title_bar.xml页面布局中获得对应的UI控件
        tv_main_title=(TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back=(TextView) findViewById(R.id.tv_back);
        rl_title_bar=(RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        //从activity_register.xml页面布局中获得对应的UI控件
        btn_register=(Button) findViewById(R.id.btn_register);
        et_user_name=(EditText) findViewById(R.id.et_register_user_name);
        et_psw=(EditText) findViewById(R.id.et_registerpsw);
        et_psw_again=(EditText) findViewById(R.id.et_resgiter_psw_again);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("正在注册...");
                progressDialog.setCancelable(true);

                getEditString();//获取输入在相应控件中的字符串

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"请再次输入密码",Toast.LENGTH_SHORT).show();

                }else if (!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"输入的两次密码不一致",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();

                    final String request_md5Psw= MD5Utils.md5(psw);
                    String url = "http://175.24.23.24:8080/register";
                    FormBody.Builder formBody = new FormBody.Builder();
                    formBody.add("username",userName);
                    formBody.add("password",request_md5Psw);
                    formBody.add("studyPassword","null");
                    MyOKhttpUtil.getInstance().get(url,formBody);
                    MyOKhttpUtil.getInstance().setMyOKHttpUtilListener(new MyOKhttpUtil.ResponseInterface() {
                        @Override
                        public void findOnSuccess(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean state = jsonObject.getBoolean("state");
                                String message = jsonObject.getString("message");
                                if (state){
                                    progressDialog.dismiss();
                                    saveRegisterInfo(userName, psw);
                                    Intent data = new Intent();
                                    data.putExtra("userName", userName);
                                    setResult(RESULT_OK, data);
                                    RegisterActivity.this.finish();
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }else {
                                    progressDialog.dismiss();
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            KLog.v("wb.z : " + "progressDialog.dismiss()");

                        }

                        @Override
                        public void findOnFail(String response) {
                            progressDialog.dismiss();
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    });


                }
            }
        });
    }
    /**
     * 获取控件中的字符串
     */
    private void getEditString(){
        userName=et_user_name.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        pswAgain=et_psw_again.getText().toString().trim();
    }
    /**
     *从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
     */
    private boolean isExistUserName(String userName){
        boolean has_userName=false;
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw=sp.getString(userName, "");
        if(!TextUtils.isEmpty(spPsw)) {
            has_userName=true;
        }
        return has_userName;
    }





    /**
     * 保存账号和密码到SharedPreferences中
     */
    private void saveRegisterInfo(String userName,String psw){
        String md5Psw= MD5Utils.md5(psw);//把密码用MD5加密
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        //以用户名为key，密码为value保存在SharedPreferences中
        editor.putString(userName, md5Psw);
        editor.commit();//提交修改
    }

}
