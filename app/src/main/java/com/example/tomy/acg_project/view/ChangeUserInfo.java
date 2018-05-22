package com.example.tomy.acg_project.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.ImageUtils;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import com.example.tomy.acg_project.domain.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by tomy on 18-5-6.
 */
public class ChangeUserInfo extends AppCompatActivity implements View.OnClickListener{

    private String address=Domain.Server_Address+"setUserInfo";
    private EditText accountInput,nickNameInput,sexInput,meEmailInput,meSignInput;
    private ImageButton imgButton;
    private Button sureChange;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.obj.toString()){
                case "ok":
                    Toast.makeText(ChangeUserInfo.this,"ok",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case "fail":
                    Toast.makeText(ChangeUserInfo.this,"fail",Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_change);

        //获取界面组件
        accountInput=(EditText)findViewById(R.id.accountInput);
        nickNameInput=(EditText)findViewById(R.id.nickNameInput);
        sexInput=(EditText)findViewById(R.id.sexInput);
        meEmailInput=(EditText)findViewById(R.id.meEmailInput);
        meSignInput=(EditText)findViewById(R.id.meSignInput);
        imgButton=(ImageButton)findViewById(R.id.imgButton);
        sureChange=(Button)findViewById(R.id.changeSure);

        //
        imgButton.setOnClickListener(this);
        sureChange.setOnClickListener(this);

        //初始化界面
        initUserInfo();
        if(Domain.img!=null){
            imgButton.setImageBitmap(Domain.img);
        }
    }

    //设置用户信息
    public void initUserInfo(){
        accountInput.setText(Domain.getUserInfo().getAccount());
        nickNameInput.setText(Domain.getUserInfo().getUserName());
        sexInput.setText(Domain.getUserInfo().getAccountSex());
        meEmailInput.setText(Domain.getUserInfo().getEmail());
        meSignInput.setText(Domain.getUserInfo().getAccountSign());
    }

    //更新用户信息
    public void changeInfo(){
        Domain.getUserInfo().setAccountSex(sexInput.getText().toString());
        Domain.getUserInfo().setAccountSign(meSignInput.getText().toString());
        Domain.getUserInfo().setEmail(meEmailInput.getText().toString());
        Domain.getUserInfo().setUserName(nickNameInput.getText().toString());
    }
    //修改服务器用户信息
    public void changeServiceInfo(){
        JSONObject request=new JSONObject();
        try {
            request.put("userId",Domain.getUserId());
            request.put("userName",Domain.getUserInfo().getUserName());
            request.put("email",Domain.getUserInfo().getEmail());
            request.put("accountSex",Domain.getUserInfo().getAccountSex());
            request.put("accountSign",Domain.getUserInfo().getAccountSign());
            request.put("accountImg",Domain.getUserInfo().getAccountImg());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(request, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                String result=responseMsg.getString("data");
                Message msg=new Message();
                msg.obj=result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.e("ChangeUserInfo.class","connectError");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changeSure:
                System.out.println("change");
                //更新程序保存信息
                changeInfo();
                //上传到服务端更新
                changeServiceInfo();
                Domain.updata.updataPhoto();
                break;
            case R.id.imgButton:
                showChoosePicDialog();
                break;
            default:break;
        }
    }

//    /**
//     * 显示修改头像的对话框
//     */
//    protected void showChoosePicDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("设置头像");
//        String[] items = { "选择本地照片", "拍照" };
//        builder.setNegativeButton("取消", null);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case CHOOSE_PICTURE: // 选择本地照片
//                        if (ActivityCompat.checkSelfPermission(ChangeUserInfo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(ChangeUserInfo.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                        }
//                        Intent openAlbumIntent = new Intent(
//                                Intent.ACTION_GET_CONTENT);
//                        openAlbumIntent.setType("image/*");
//                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//                        break;
//                    case TAKE_PICTURE: // 拍照
//                        Intent openCameraIntent = new Intent(
//                                MediaStore.ACTION_IMAGE_CAPTURE);
//                        tempUri = Uri.fromFile(new File(Environment
//                                .getExternalStorageDirectory(), "image.jpg"));
//                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//                        break;
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
//            switch (requestCode) {
//                case TAKE_PICTURE:
//                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
//                    break;
//                case CHOOSE_PICTURE:
//                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
//                    break;
//                case CROP_SMALL_PICTURE:
//                    if (data != null) {
//                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 裁剪图片方法实现
//     *
//     * @param uri
//     */
//    protected void startPhotoZoom(Uri uri) {
//        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
//        }
//        tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_SMALL_PICTURE);
//    }
//
//    /**
//     * 保存裁剪之后的图片数据
//     *
//     * @param
//     *
//     * @param //picdata
//     */
//    protected void setImageToView(Intent data) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            //photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            imgButton.setImageBitmap(photo);
//            //uploadPic(photo);
//        }
//    }
//
//    private void uploadPic(Bitmap bitmap) {
//        // 上传至服务器
//        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
//        // 注意这里得到的图片已经是圆形图片了
//        // bitmap是没有做个圆形处理的，但已经被裁剪了
//
////        String imagePath = Utils.savePhoto(bitmap, Environment
////                .getExternalStorageDirectory().getAbsolutePath(), String
////                .valueOf(System.currentTimeMillis()));
////        Log.e("imagePath", imagePath+"");
////        if(imagePath != null){
////            // 拿着imagePath上传了
////            // ...
////        }
//    }

    /**
     * 显示修改头像的对话框
     */
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment
                .getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.d("error","setImageToView:"+photo);
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            Domain.img=photo;
            imgButton.setImageBitmap(photo);
            //Domain.updata.updataPhoto();
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
            System.out.println("上传照片");
            HttpUnit.uploadFile(imagePath,Domain.Server_Address+"upload");
            Log.d("error","imagePath:"+imagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }
}
