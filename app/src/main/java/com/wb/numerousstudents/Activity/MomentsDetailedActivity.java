package com.wb.numerousstudents.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.Utils.MyOKhttpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MomentsDetailedActivity extends AppCompatActivity implements View.OnClickListener {
    private static final boolean DEBUG = false;

    private static final int REQUEST_ALBUM_CODE = 100;
    private static final int REQUEST_TAKE_PHOTO_CODE = 200;

    ImageView mExitMomentsImageView;
    ImageView mMomentsPictureImageView;
    TextView mAddMomentsTextView;
    PopupWindow mAddPictureSelectPopupWindow;

    TextView mPopUpWindowDisMissTextView;
    TextView mPopUpWindowUploadPictureByCamera;
    TextView mPopUpWindowUploadPictureByAlbum;

    EditText mMomentTitleEditText;
    EditText mMomentContentEditText;

    View mRootView;

    String mUserName;
    String mUserNickName;


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
                finish();
                break;
            }

            case R.id.iv_moments_add_new_moment:{
                addNewMoments();
                break;
            }
            case R.id.iv_moments_picture:{
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
                String curTime = System.currentTimeMillis()+ "";
                String fileName = curTime + ".jpg";
                String momentID = curTime + "";
                File file = new File(photoPath);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日\nHH:mm");
                Date date = new Date();
                String dateString = dateFormat.format(date);

                RequestBody requestBody = new MultipartBody.Builder()      //todo 这里传入过多，后期需要优化，用map等
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("momentsId", momentID)
                        .addFormDataPart("momentPicture", fileName, RequestBody.create(MediaType.parse("image/jpg"), file))
                        .addFormDataPart("userName",mUserName)
                        .addFormDataPart("userNickName",mUserNickName)
                        .addFormDataPart("addMomentTime",dateString)
                        .addFormDataPart("momentTitle",mMomentTitleEditText.getText().toString())
                        .addFormDataPart("momentContent",mMomentContentEditText.getText().toString())
                        .build();
                MyOKhttpUtil.getInstance().post("http://175.24.23.24:8080/addMoment",requestBody);
            }
        }
    }

    private void dismissSelectPicturePopWindow(){
        if (mAddPictureSelectPopupWindow != null){
            mAddPictureSelectPopupWindow.dismiss();
        }
    }


    private void addNewMoments(){
        String momentContent = mAddMomentsTextView.getText().toString();
        if (momentContent.equals("")){
            Toast.makeText(this,"请先简单描述几句",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void addMomentsPictureByAlbum(){
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent,REQUEST_ALBUM_CODE);
    }

    Uri fileUri;
    private void addMomentsPictureByCamera(){
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String f = System.currentTimeMillis()+".jpg";
//        fileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory("").getPath()+f));
        fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+f));
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
        startActivityForResult(openCameraIntent, REQUEST_TAKE_PHOTO_CODE);
    }

    //调用相机（指定相机拍摄照片保存地址，相片清晰度高）
    String photoPath;
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
            KLog.v("wb.z : resultCode:" + resultCode);
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
                mMomentsPictureImageView.setImageURI(uri);
                break;
            }

            case REQUEST_TAKE_PHOTO_CODE:{
//                if (data == null){
//                    KLog.v("wb.z : 取消拍照");
//                    return;
//                }
                File tempFile = new File(photoPath);
//                try {
                    Bitmap bitmap;
                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                    options2.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmap = BitmapFactory.decodeFile(photoPath, options2);

//                    bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(tempFile)));
                    bitmap = saveBitmapAsFile(bitmap);

                    mMomentsPictureImageView.setImageBitmap(bitmap);


//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

//                mMomentsPictureImageView.setImageURI(getImageContentUri(tempFile));


//                String filepath = PhotoBitmapUtils.amendRotatePhoto(tempFile.getAbsolutePath(), this);
//                Uri uri = Uri.parse(filepath);
//                mMomentsPictureImageView.setImageURI(uri);
                break;
            }
        }
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public Bitmap saveBitmapAsFile(Bitmap bitmap) {
        photoPath = Environment.getExternalStorageDirectory().getPath()+"/"+File.separator+ System.currentTimeMillis() + ".jpg";
        KLog.v("wb.z : " + photoPath);
        File saveFile = new File(photoPath);
        boolean saved = false;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
            saved = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            bitmap.recycle();
        }

        return bitmap;
    }

}
