package com.ting.play.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.play.PlayingVO;
import com.ting.db.DBChapter;
import com.ting.download.DownLoadService;
import com.ting.download.DownloadController;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilListener;
import com.ting.util.UtilStr;

import java.util.List;

/**
 * Created by liu on 15/11/4.
 */
public class DownloadMoreDialog extends Dialog implements View.OnClickListener {
    private BookDetailsActivity activity;
    private TextView tv_total_chapter;
    private int total = 0;
    private EditText et_start_chapter;
    private EditText et_end_chapter;
    private Button btnCancle;
    private Button btnOk;
    private DownloadController controller;
    private List<PlayingVO> data;
    private int bookid = -1;
    private String bookname;
    private String hostname;
    private String imgUrl;

    public DownloadMoreDialog(BookDetailsActivity activity) {
        super(activity, R.style.CustomDialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_more);
        setCanceledOnTouchOutside(false);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        init();
        controller = new DownloadController();
    }


    public void setResult(List<PlayingVO> result, int bookid, String bookname, String hostname, String imgUrl) {
        this.data = result;
        this.bookid = bookid;
        this.bookname = bookname;
        this.hostname = hostname;
        this.imgUrl = imgUrl;

    }

    private void init() {
        tv_total_chapter = findViewById(R.id.tv_total_chapter);
        btnCancle = findViewById(R.id.btn_cancle);
        btnOk = findViewById(R.id.btn_ok);
        et_start_chapter = findViewById(R.id.et_start_chapter);
        et_end_chapter = findViewById(R.id.et_end_chapter);
        btnCancle.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (data == null) {
                    activity.showToast("没有可下载章节");
                }
                String startStr = et_start_chapter.getText().toString();
                String endStr = et_end_chapter.getText().toString();
                if (UtilStr.isEmpty(startStr) || startStr.equals("0")) {
                    activity.showToast("请输入开始章节");
                    return;
                }
                if (UtilStr.isEmpty(endStr) || endStr.equals("0")) {
                    activity.showToast("请输入结束章节");
                    return;
                }
                int start = Integer.parseInt(startStr);
                int end = Integer.parseInt(endStr);
                int startPosition = data.get(0).getPosition() + 1;
                int endPosition = data.get(data.size() - 1).getPosition() + 1;
                if (start >= end || start < startPosition || end > endPosition) {
                    activity.showToast("输入错误，请重新输入");
                    return;
                }
                download(start, end);
                activity.showToast("开始下载");
                dismiss();
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }

    private void download(int start, int end) {
        for (int i = start; i < end; i++) {
            PlayingVO vo = getChapterPosition(i);
            DBChapter bVo = controller.query(bookid + "", vo.getId() + "");
            if (bVo == null) {
                if (vo != null) {
                    if (vo.getDownload() == 1 && !UtilStr.isEmpty(vo.getUrl())) {
                        Intent intent = new Intent(activity, DownLoadService.class);
                        intent.putExtra("MSG", 1);
                        intent.putExtra("vo", UtilListener.PlayingVOToBook(vo, bookid, bookname, hostname, imgUrl));
                        activity.startService(intent);
                    }
                }

            } else {
                if (bVo.getState() == 0 || bVo.getState() == 3) {
                    Intent intent = new Intent(activity, DownLoadService.class);
                    intent.putExtra("MSG", 1);
                    intent.putExtra("vo", bVo);
                    activity.startService(intent);
                }
            }
        }
    }

    public PlayingVO getChapterPosition(int position) {
        PlayingVO vo = null;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getPosition() == position - 1) {
                vo = data.get(i);
            }
        }
        return vo;
    }

    @Override
    public void show() {
        super.show();
        if (data != null) {
            total = data.size();
            tv_total_chapter.setText("请输入要下载的章节" + "(共" + total + "章）");
        }
    }
}
