package com.ting.myself;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.base.ListenDialog;
import com.ting.base.MessageEventBus;
import com.ting.bean.BaseResult;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.dialog.CamerChooseDialog;
import com.ting.myself.dialog.SexChooseDialog;
import com.ting.myself.dialog.SureSaveDialog;
import com.ting.bean.UserInfoResult;
import com.ting.util.UtilBitmap;
import com.ting.util.UtilGlide;
import com.ting.util.UtilPermission;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilStr;
import com.ting.welcome.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by gengjiajia on 15/9/14.
 * 设置activity
 */
public class SettingActivity extends BaseActivity implements UtilPermission.PermissionCallbacks {
    private EditText nick_name_text;
    private TextView sex_text;
    private RelativeLayout touxiang_setting;
    private LinearLayout sex_layout;
    private CircleImageView touxiang_image;
    private Uri imageuri;
    private Bitmap cameraBitmap;
    private TextView clear_user_message;
    private Map<String, String> map = new HashMap<>();
    private File file;
    private File mCameraFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.setting_new);
    }

    @Override
    protected String setTitle() {
        return "设置";
    }

    @Override
    protected void initView() {
        touxiang_setting = findViewById(R.id.touxiang_setting);//头像
        sex_layout = findViewById(R.id.sex_layout);//性别
        clear_user_message = findViewById(R.id.clear_user_message);//清除个人信息
        nick_name_text = findViewById(R.id.nick_name_text);//昵称
        sex_text = findViewById(R.id.sex_text);//性别
        touxiang_image = findViewById(R.id.touxiang_image);//头像
        if (TokenManager.getInfo(mActivity) != null) {
            UserInfoResult infoResult = TokenManager.getInfo(mActivity);
            UtilGlide.loadAnchorImg(this, infoResult.getImage(), touxiang_image);
            if (infoResult.getSex() == 1) {
                sex_text.setText("男听友");
            } else {
                sex_text.setText("女听友");
            }
            if(!TextUtils.isEmpty(infoResult.getNickname())) {
                nick_name_text.setText(infoResult.getNickname());
                nick_name_text.setSelection(infoResult.getNickname().length());
            }
        }
        touxiang_setting.setOnClickListener(this);
        sex_layout.setOnClickListener(this);
        clear_user_message.setOnClickListener(this);
        showRightText("修改");

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.touxiang_setting:
                if (UtilPermission.hasPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                    CamerChooseDialog camerChooseDialog = new CamerChooseDialog(this);
                    camerChooseDialog.show();
                } else {
                    UtilPermission.requestPermissions(this, AppData.PERMISSION_CODE, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                }
                break;

            case R.id.sex_layout:
                SexChooseDialog sexChooseDialog = new SexChooseDialog(this);
                sexChooseDialog.setListener(new SexChooseDialog.SexChooseCallBackListener() {
                    @Override
                    public void callback(int sex) {
                        if (sex == 1) {
                            sex_text.setText("男听友");
                        } else {
                            sex_text.setText("女听友");
                        }
                    }
                });
                sexChooseDialog.show();
                break;

            case R.id.clear_user_message:
                showToast("注销账户成功");
                TokenManager.claerUid(mActivity);
                TokenManager.clearToken(mActivity);
                intent(MainActivity.class);
                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.LOGIN_OUT));
                this.finish();

                break;

            case R.id.tv_right:
                SureSaveDialog sureSaveDialog = new SureSaveDialog(this);
                sureSaveDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 保存修改资料
     */
    public void SaveMess() {
        // 添加文件参数
        String nameStr = nick_name_text.getText().toString().trim();
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), TokenManager.getUid(this));
        map.put("uid", uidBody);
        if (!UtilStr.isEmpty(nameStr)) {
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), nameStr);
            map.put("nickname", nameBody);
        }
        if (sex_text.getText().toString().equals("男听友")) {
            RequestBody sexualBody = RequestBody.create(MediaType.parse("text/plain"), "1");
            map.put("sexual", sexualBody);
        } else if (sex_text.getText().toString().equals("女听友")) {
            RequestBody sexualBody = RequestBody.create(MediaType.parse("text/plain"), "2");
            map.put("sexual", sexualBody);
        }
        if (file != null) {
            RequestBody avatarBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            map.put("avatar\"; filename=\"touxiang.jpg", avatarBody);
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult<UserInfoResult>>(this, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult<UserInfoResult> data) {
                super.success(data);
                UserInfoResult result = data.getData();
                TokenManager.setInfo(result);
                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.MODIFY));
                showToast("修改成功");
                finish();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).modifyUserInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    try {
                        Bitmap bp = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                        touxiang_image.setImageBitmap(bp);
                        file = UtilBitmap.compressBmpToFile(this, bp, 600);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

                case 1: {
                    Uri imageUri = data.getData();
                    startActionCrop(imageUri);
                }

                    break;
                case 2:
                    try {
                        Uri uri = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(this, "com.ting.fileprovider", mCameraFile);
                        } else {
                            uri = Uri.fromFile(mCameraFile);
                        }
                        startActionCrop(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    }


    public void setCameraFile(File cameraFile) {
        mCameraFile = cameraFile;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        CamerChooseDialog camerChooseDialog = new CamerChooseDialog(this);
        camerChooseDialog.show();
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        ListenDialog.makeListenDialog(SettingActivity.this, "提示", "更换头像需要开启摄像头和SD卡存储权限，请开启", false, null, true, "去允许", new ListenDialog.CallBackListener() {
            @Override
            public void callback(ListenDialog dialog, int mark) {
                dialog.dismiss();
                if (mark == ListenDialog.RIGHT) {
                    Intent intent = new Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
        }).show();
    }



    /**
     * 拍照后裁剪
     *
     * @param input 裁剪后图片
     */
    private void startActionCrop(Uri input) {
        /**
         * 图片存储路径
         */
        String fileSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/listen/";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String absoluteCropPicPath = fileSavePath + "crop_" + timeStamp + ".jpg";
        File file = new File(absoluteCropPicPath);

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri cropOutput = Uri.fromFile(file);

        Intent intentCamera = new Intent("com.android.camera.action.CROP");
        // 源文件地址
        intentCamera.setDataAndType(input, "image/*");
        intentCamera.putExtra("crop", true);
        // intentCamera.putExtra("scale", false);
        // 不需要人脸识别功能
        // intentCamera.putExtra("noFaceDetection", true);
        // 设定此方法选定区域会是圆形区域
        // intentCamera.putExtra("circleCrop", "");
        // aspectX aspectY是宽高比例
        intentCamera.putExtra("aspectX", 1);
        intentCamera.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片的宽高
        intentCamera.putExtra("outputX", 200);
        intentCamera.putExtra("outputY", 200);
        // 输出地址
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, cropOutput);
        intentCamera.putExtra("return-data", false);
        intentCamera.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intentCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intentCamera, 0);
    }
}
