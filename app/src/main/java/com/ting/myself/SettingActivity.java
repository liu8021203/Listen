package com.ting.myself;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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
import java.util.HashMap;
import java.util.List;
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
    private EditText person_sign_text;
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
        touxiang_setting = (RelativeLayout) findViewById(R.id.touxiang_setting);//头像
        sex_layout = (LinearLayout) findViewById(R.id.sex_layout);//性别
        clear_user_message = (TextView) findViewById(R.id.clear_user_message);//清除个人信息
        nick_name_text = (EditText) findViewById(R.id.nick_name_text);//昵称
        sex_text = (TextView) findViewById(R.id.sex_text);//性别
        person_sign_text = (EditText) findViewById(R.id.person_sign_text);//个性签名
        touxiang_image = (CircleImageView) findViewById(R.id.touxiang_image);//头像
        if (TokenManager.getInfo(mActivity) != null) {
            UserInfoResult infoResult = TokenManager.getInfo(mActivity);
            if(!TextUtils.isEmpty(infoResult.getThumb())) {
                UtilGlide.loadAnchorImg(this, infoResult.getThumb(), touxiang_image);
            }
            if (infoResult.getType() == 0) {
                if (infoResult.getSexual() == 1) {
                    sex_text.setText("男听迷");
                } else if (infoResult.getSexual() == 2) {
                    sex_text.setText("女听迷");
                } else {
                    sex_text.setText("未设置");
                }
            } else if (infoResult.getType() == 1) {
                if (infoResult.getSexual() == 1) {
                    sex_text.setText("男主播");
                } else if (infoResult.getSexual() == 2) {
                    sex_text.setText("女主播");
                } else {
                    sex_text.setText("未设置");
                }
            }
            nick_name_text.setText(infoResult.getNickname());
            nick_name_text.setSelection(infoResult.getNickname().length());
            person_sign_text.setText(infoResult.getSignature());
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
        String sexStr = sex_text.getText().toString();
        String sign = person_sign_text.getText().toString().trim();
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), TokenManager.getUid(this));
        map.put("uid", uidBody);
        if (!UtilStr.isEmpty(nameStr)) {
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), nameStr);
            map.put("nickname", nameBody);
        }
        if (!UtilStr.isEmpty(sign)) {
            RequestBody signBody = RequestBody.create(MediaType.parse("text/plain"), sign);
            map.put("signature", signBody);
        }
        if (!sexStr.equals("未设置")) {
            if (sex_text.getText().toString().equals("男听迷")) {
                RequestBody sexualBody = RequestBody.create(MediaType.parse("text/plain"), "1");
                map.put("sexual", sexualBody);
            } else if (sex_text.getText().toString().equals("女听迷")) {
                RequestBody sexualBody = RequestBody.create(MediaType.parse("text/plain"), "2");
                map.put("sexual", sexualBody);
            }
        }
        if (file != null) {
            RequestBody avatarBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            map.put("avatar\"; filename=\"touxiang.jpg", avatarBody);
        }
        BaseObserver baseObserver = new BaseObserver<UserInfoResult>(this) {
            @Override
            public void success(UserInfoResult data) {
                super.success(data);
                TokenManager.setInfo(mActivity, data);
                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.LOGIN));
                finish();
                showToast("修改成功");
            }

            @Override
            public void error() {
                showToast("修改失败");
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).setUserInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    public void showSexSeeting(String chooseSex) {
        sex_text.setText(chooseSex);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
                        touxiang_image.setImageBitmap(bitmap1);
                        file = UtilBitmap.compressBmpToFile(this, bitmap1, 600);
                    } catch (Exception e) {
                        e.printStackTrace();
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
                        Bitmap bitmap = BitmapFactory.decodeFile(mCameraFile.getAbsolutePath());
                        touxiang_image.setImageBitmap(bitmap);
                        file = UtilBitmap.compressBmpToFile(this, bitmap, 600);
                    }catch (Exception e){
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
}
