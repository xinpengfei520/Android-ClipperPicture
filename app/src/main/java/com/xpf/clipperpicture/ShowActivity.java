package com.xpf.clipperpicture;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class ShowActivity extends AppCompatActivity {

    private static final int CAMERA = 1;
    private static final int PICTURE = 2;

    private ImageView ivIcon;
    private PopupWindow popupWindow;
    private View popupView;
    private TranslateAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIcon();
                lightoff();
            }
        });
    }

    /**
     * 弹出popupWindow更改头像
     */
    private void changeIcon() {
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.item_change_icon, null);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lighton();
                }
            });

            // 设置背景图片， 必须设置，不然动画没作用
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setOutsideTouchable(true);

            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);

            popupView.findViewById(R.id.tvTakePhoto).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打开系统拍照程
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Intent camera = new Intent(ShowActivity.this, ClipperActivity.class);
                    camera.putExtra("action", CAMERA);
                    startActivityForResult(camera, CAMERA);
                    popupWindow.dismiss();
                    lighton();
                }
            });
            popupView.findViewById(R.id.tvSelectPhoto).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打开系统图库选择图片
//                    Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent picture = new Intent(ShowActivity.this, ClipperActivity.class);
                    picture.putExtra("action", PICTURE);
                    startActivityForResult(picture, PICTURE);
                    popupWindow.dismiss();
                    lighton();
                }
            });
        }

        // 在点击之后设置popupwindow的销毁
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lighton();
        }

        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(ShowActivity.this.findViewById(R.id.activity_show),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.startAnimation(animation);
    }

    /**
     * 设置手机屏幕亮度变暗
     */
    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
