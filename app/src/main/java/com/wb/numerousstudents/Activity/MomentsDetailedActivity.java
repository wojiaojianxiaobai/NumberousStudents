package com.wb.numerousstudents.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.Utils.MyOKhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MomentsDetailedActivity extends AppCompatActivity implements View.OnClickListener {
    private static final boolean DEBUG = false;

    private static final int REQUEST_ALBUM_CODE = 100;
    private static final int REQUEST_TAKE_PHOTO_CODE = 200;
    private static final int ADD_MOMENTS_ITEM_SUCCESS = 1;
    private static final int ADD_MOMENTS_ITEM_FAIL = 2;

    private ImageView mExitMomentsImageView;
    private ImageView mMomentsPictureImageView;
    private TextView mAddMomentsTextView;
    private PopupWindow mAddPictureSelectPopupWindow;

    private TextView mPopUpWindowDisMissTextView;
    private TextView mPopUpWindowUploadPictureByCamera;
    private TextView mPopUpWindowUploadPictureByAlbum;

    private EditText mMomentTitleEditText;
    private EditText mMomentContentEditText;

    private View mRootView;

    private String mUserName;
    private String mUserNickName;

    private String photoPath;

    private ProgressDialog mProgressDialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.arg1;
            switch (code){
                case ADD_MOMENTS_ITEM_SUCCESS:{
                    Toast.makeText(MomentsDetailedActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    break;
                }
                case ADD_MOMENTS_ITEM_FAIL:{
                    Toast.makeText(MomentsDetailedActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        setContentView(R.layout.activity_moments_detailed);
        init();
    }

    private void init(){
        initView();
        initData();
    }

    private void initView(){
        mExitMomentsImageView = findViewById(R.id.iv_exit_add_new_moment);
        mExitMomentsImageView.setOnClickListener(this);
        mAddMomentsTextView = findViewById(R.id.tv_add_moments);
        mAddMomentsTextView.setOnClickListener(this);
        mMomentsPictureImageView = findViewById(R.id.iv_moments_picture);
        mMomentsPictureImageView.setOnClickListener(this);

        mMomentContentEditText = findViewById(R.id.et_content);
        mMomentTitleEditText = findViewById(R.id.et_moment_title);


        View contentView = LayoutInflater.from(MomentsDetailedActivity.this).inflate(R.layout.add_moments_picture_layout,null);

        mPopUpWindowDisMissTextView = contentView.findViewById(R.id.dismiss_add_picture_popwindow);
        mPopUpWindowUploadPictureByAlbum = contentView.findViewById(R.id.pick_album);
        mPopUpWindowUploadPictureByCamera = contentView.findViewById(R.id.take_photo);

        mPopUpWindowDisMissTextView.setOnClickListener(this);
        mPopUpWindowUploadPictureByAlbum.setOnClickListener(this);
        mPopUpWindowUploadPictureByCamera.setOnClickListener(this);

        mRootView = LayoutInflater.from(MomentsDetailedActivity.this).inflate(R.layout.activity_moments_detailed,null);
        mAddPictureSelectPopupWindow = new PopupWindow(MomentsDetailedActivity.this);
        mAddPictureSelectPopupWindow.setContentView(contentView);
        mAddPictureSelectPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mAddPictureSelectPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mAddPictureSelectPopupWindow.setOutsideTouchable(true);
        mAddPictureSelectPopupWindow.setBackgroundDrawable(new BitmapDrawable());

    }

    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        mUserName = sharedPreferences.getString("loginUserName","");
        mUserNickName = sharedPreferences.getString("userNickName","");
    }

    @Override
    public void onClick(View v) {
        int onClickId = v.getId();

        switch (onClickId){
            case R.id.iv_exit_add_new_moment:{
                KLog.v("wb.z : finish");
                this.finish();
                break;
            }

            case R.id.iv_moments_picture:{
                hideSoftKeyboard(this);
                if (mAddPictureSelectPopupWindow != null){
                    mAddPictureSelectPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM,0,0);
                }
                break;
            }

            case R.id.take_photo:{
                openCamera2();
//                addMomentsPictureByCamera();
                dismissSelectPicturePopWindow();
                break;
            }

            case R.id.pick_album:{
                addMomentsPictureByAlbum();
                dismissSelectPicturePopWindow();
                break;
            }

            case R.id.dismiss_add_picture_popwindow:{
                dismissSelectPicturePopWindow();
                break;
            }

            case R.id.tv_add_moments:{
                if (mMomentTitleEditText.getText().toString().equals("")){
                    Toast.makeText(this,"请输入标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mMomentContentEditText.getText().toString().equals("")){
                    Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressDialog = new ProgressDialog(MomentsDetailedActivity.this);
                mProgressDialog.setMessage("正在发送");
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();

                String curTime = System.currentTimeMillis()+ "";
                String fileName = curTime + ".jpg";
                String momentID = curTime + "";
                File file = null;
                RequestBody requestBody;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日\nHH:mm");
                Date date = new Date();
                String dateString = dateFormat.format(date);
                boolean hasPicture = false;
                if (photoPath != null && !photoPath.equals("")){
                    hasPicture = true;
                }
                if (hasPicture){
                    file = new File(photoPath);
                    requestBody = new MultipartBody.Builder()      //todo 这里传入过多，后期需要优化，用map等
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("momentsId", momentID)
                            .addFormDataPart("userName",mUserName)
                            .addFormDataPart("userNickName",mUserNickName)
                            .addFormDataPart("momentPicture", fileName, RequestBody.create(MediaType.parse("image/jpg"),file))
                            .addFormDataPart("addMomentTime",dateString)
                            .addFormDataPart("momentTitle",mMomentTitleEditText.getText().toString())
                            .addFormDataPart("momentContent",mMomentContentEditText.getText().toString())
                            .build();
                    MyOKhttpUtil.getInstance().post("http://175.24.23.24:8080/addMoment",requestBody);
                }else {
                    requestBody = new MultipartBody.Builder()      //todo 这里传入过多，后期需要优化，用map等
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("momentsId", momentID)
                            .addFormDataPart("userName",mUserName)
                            .addFormDataPart("userNickName",mUserNickName)
                            .addFormDataPart("addMomentTime",dateString)
                            .addFormDataPart("momentTitle",mMomentTitleEditText.getText().toString())
                            .addFormDataPart("momentContent",mMomentContentEditText.getText().toString())
                            .build();
                    MyOKhttpUtil.getInstance().post("http://175.24.23.24:8080/addNoPictureMoment",requestBody);
                }

                MyOKhttpUtil.getInstance().setMyOKHttpUtilListener(new MyOKhttpUtil.ResponseListener() {
                    @Override
                    public void findOnSuccess(String response) {
                        try {           //todo 这里没有获取到code200，后面再处理，现在是请求成功则判定发送成功。
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONObject jsonArray = jsonObject.getJSONObject("Response");
                            int code = jsonArray.getInt("code");
                            KLog.v("wb.z : code : " + code );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        KLog.v("wb.z :" + response.toString());



                        Message message = handler.obtainMessage();
                        message.arg1 = ADD_MOMENTS_ITEM_SUCCESS;
                        handler.sendMessage(message);
                        finish();
                    }

                    @Override
                    public void findOnFail(String response) {
                        Message message = handler.obtainMessage();
                        message.arg1 = ADD_MOMENTS_ITEM_FAIL;
                        handler.sendMessage(message);
                    }
                });
            }
        }
    }

    private void dismissSelectPicturePopWindow(){
        if (mAddPictureSelectPopupWindow != null){
            mAddPictureSelectPopupWindow.dismiss();
        }
    }


    private void addMomentsPictureByAlbum(){
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent,REQUEST_ALBUM_CODE);
    }


    //调用相机（指定相机拍摄照片保存地址，相片清晰度高）

    private void openCamera2(){
//        photoPath = Environment.getExternalStorageDirectory().getPath()+"/"+File.separator+ System.currentTimeMillis() + ".jpg";
        photoPath = Environment.getExternalStorageDirectory().getPath()+"/"+File.separator+ "tempPictureFile" + ".jpg";
        KLog.v("wb.z : " + photoPath);
        File pictureFile = new File(photoPath);
        Uri picUri;
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String packageName = this.getApplicationContext().getPackageName();
            picUri = FileProvider.getUriForFile(this, new StringBuilder(packageName).append(".fileprovider").toString(), pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            picUri = Uri.fromFile(pictureFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (DEBUG){
            KLog.v("wb.z : requestCode:" + requestCode);
            if (data != null){
                KLog.v("wb.z : data:" + data.toString());
            }
        }

        switch (requestCode){
            case REQUEST_ALBUM_CODE:{
                if (data == null){
                    KLog.v("wb.z : 没有选择图片");
                    return;
                }
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    bitmap = saveBitmapHumbnailAsFile(bitmap);
                    mMomentsPictureImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                photoPath = uri.getPath();
//                KLog.v("wb.z : " + photoPath);
//                mMomentsPictureImageView.setImageURI(uri);
                break;
            }

            case REQUEST_TAKE_PHOTO_CODE:{
                    Bitmap bitmap;
                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                    options2.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmap = BitmapFactory.decodeFile(photoPath, options2);

                    bitmap = saveBitmapHumbnailAsFile(bitmap);

                    mMomentsPictureImageView.setImageBitmap(bitmap);

                break;
            }
        }
    }

    public Bitmap saveBitmapHumbnailAsFile(Bitmap bitmap) {
        photoPath = Environment.getExternalStorageDirectory().getPath()+"/"+File.separator+ System.currentTimeMillis() + ".jpg";
        KLog.v("wb.z : " + photoPath);
        File saveFile = new File(photoPath);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            bitmap.recycle();
        }

        return bitmap;
    }

    /**
     * 隐藏软键盘(可用于Activity)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
