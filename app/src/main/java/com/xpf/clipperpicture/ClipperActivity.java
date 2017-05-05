
package com.xpf.clipperpicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xpf.library.CropImageView;


public class ClipperActivity extends Activity {

    private Context mContext;
    private static final int CAMERA = 1;
    private static final int PICTURE = 2;
    private int mCurrentAction = -1;
    private static final int GUIDELINES_ON_TOUCH = 1;
    private CropImageView cropImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        final ToggleButton fixedAspectRatioToggleButton = (ToggleButton) findViewById(R.id.fixedAspectRatioToggle);
        final TextView aspectRatioXTextView = (TextView) findViewById(R.id.aspectRatioX);
        final SeekBar aspectRatioXSeekBar = (SeekBar) findViewById(R.id.aspectRatioXSeek);
        final TextView aspectRatioYTextView = (TextView) findViewById(R.id.aspectRatioY);
        final SeekBar aspectRatioYSeekBar = (SeekBar) findViewById(R.id.aspectRatioYSeek);
        final Spinner guidelinesSpinner = (Spinner) findViewById(R.id.showGuidelinesSpin);
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        final ImageView croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
        final Button cropButton = (Button) findViewById(R.id.Button_crop);

        // Initializes fixedAspectRatio toggle button.
        fixedAspectRatioToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cropImageView.setFixedAspectRatio(isChecked);
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioXSeekBar.setEnabled(isChecked);
                aspectRatioYSeekBar.setEnabled(isChecked);
            }
        });
        // Set seek bars to be disabled until toggle button is checked.
        aspectRatioXSeekBar.setEnabled(false);
        aspectRatioYSeekBar.setEnabled(false);

        aspectRatioXTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));
        aspectRatioYTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));

        // Initialize aspect ratio X SeekBar.
        aspectRatioXSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioXSeekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    aspectRatioXSeekBar.setProgress(1);
                }
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioXTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }
        });

        // Initialize aspect ratio Y SeekBar.
        aspectRatioYSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioYSeekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    aspectRatioYSeekBar.setProgress(1);
                }
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioYTextView.setText(String.valueOf(aspectRatioYSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }
        });

        // Set up the Guidelines Spinner.
        guidelinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropImageView.setGuidelines(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing.
            }
        });
        guidelinesSpinner.setSelection(GUIDELINES_ON_TOUCH);

        // Initialize the Crop button.
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap croppedImage = cropImageView.getCroppedImage();
                croppedImageView.setImageBitmap(croppedImage);
//                Intent resultIntent = new Intent();
//                Bundle bundle = new Bundle();
            }
        });

        Intent intent = getIntent();
        int action = intent.getIntExtra("action", -1);
        switch (action) {
            case CAMERA: // 打开系统拍照
                mCurrentAction = CAMERA;
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA);
                break;
            case PICTURE: // 打开系统图库选择图片
                mCurrentAction = PICTURE;
                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, PICTURE);
                break;
        }
    }

    /**
     * Bimap:对应图片在内存中的对象
     * 存储--->内存：BitmapFactory.decodeFile(String filePath)
     * BitmapFactory.decodeStream(InputStream is)
     * 内存--->存储：bitmap.compress(Bitmap.CompressFormat.PNG,100,OutputStream os)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) { // 拍照
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            cropImageView.setImageBitmap(bitmap);
//            // bitmap裁剪
//            bitmap = BitmapUtils.zoom(bitmap, DensityUtil.dp2px(this, 32), DensityUtil.dp2px(this, 32));
//            // 将图片上传到服务器
//            uploadHeadpic(bitmap);

        } else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            String pathResult = null;  // 获取图片路径的方法调用
            try {
                Uri uri = data.getData();
                pathResult = PictureHelper.getPath(mContext, uri);
                Log.e("TAG", "图片路径===" + pathResult);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            cropImageView.setImageBitmap(decodeFile);
//            Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, DensityUtil.dp2px(this, 32), DensityUtil.dp2px(this, 32));
//            uploadHeadpic(zoomBitmap);
        }
    }

}
