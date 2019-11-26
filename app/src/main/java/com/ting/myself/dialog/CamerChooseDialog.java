package com.ting.myself.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.myself.SettingActivity;

import java.io.File;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by gengjiajia on 15/9/16.
 * 相机相册选择dialog
 */
public class CamerChooseDialog extends Dialog implements View.OnClickListener {
    private SettingActivity activity;
    private RelativeLayout photo_choose_layout;
    private RelativeLayout camer_pai_se_layout;
    private ImageView colse_dialog;

    public CamerChooseDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.SettingDialog);
        this.activity = (SettingActivity) baseActivity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_camer_choose);
        initView();
    }

    private void initView() {
        photo_choose_layout = (RelativeLayout) findViewById(R.id.photo_choose_layout);//相册选取
        camer_pai_se_layout = (RelativeLayout) findViewById(R.id.camer_pai_se_layout);//相册拍摄
        colse_dialog=(ImageView)findViewById(R.id.colse_dialog);
        colse_dialog.setOnClickListener(this);
        photo_choose_layout.setOnClickListener(this);
        camer_pai_se_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_choose_layout:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                activity.startActivityForResult(intent, 1);
                dismiss();
                break;
            case R.id.camer_pai_se_layout:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = null;
                Uri uri = null;
                try {
                    file = File.createTempFile("touxiang",".jpg", storageDir);
                    activity.setCameraFile(file);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        intent1.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                        intent1.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(activity.getApplicationContext(), "com.ting.fileprovider", file);
                    }else{
                        uri = Uri.fromFile(file);
                    }
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                            uri);
                    activity.startActivityForResult(intent1, 2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                dismiss();
                break;
            case R.id.colse_dialog:
                dismiss();
                break;
        }
    }


}
