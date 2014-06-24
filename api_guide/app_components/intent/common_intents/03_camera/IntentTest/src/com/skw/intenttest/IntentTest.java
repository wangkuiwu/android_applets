package com.skw.intenttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Display;
import android.content.Intent;
import android.content.ContentValues;
import android.widget.ImageView;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Calendar;

public class IntentTest extends Activity {
    private static final String TAG = "##IntentTest##";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri mLocationForPhotos;
    private ImageView mImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mImage = (ImageView) findViewById(R.id.image);
    }

    public void sendMessage(View view) {
        capturePhoto() ;
    }

    public void capturePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
      
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "MyImage");  
        values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");  
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");  
        mLocationForPhotos = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mLocationForPhotos);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {  
                // 首先取得屏幕对象  
                Display display = getWindowManager().getDefaultDisplay();  
                // 获取屏幕的宽和高  
                int dw = display.getWidth();  
                int dh = display.getHeight();  
                // 获取图片原始大小
                BitmapFactory.Options op = new BitmapFactory.Options();  
                op.inJustDecodeBounds = true;  
                Bitmap pic = BitmapFactory.decodeStream(
                        this.getContentResolver().openInputStream(mLocationForPhotos),  
                        null, op);  

                int wRatio = (int) Math.ceil(op.outWidth / (float) dw); //计算宽度比例  
                int hRatio = (int) Math.ceil(op.outHeight / (float) dh); //计算高度比例  
                Log.d(TAG, "wRatio="+wRatio+", hRatio="+hRatio);
                // 设置缩放比例
                if (wRatio > 1 && hRatio > 1) {  
                    if (wRatio > hRatio) {  
                        op.inSampleSize = wRatio;  
                    } else {  
                        op.inSampleSize = hRatio;  
                    }  
                } else {
                    op.inSampleSize = 4; // 默认缩小为4倍 
                }

                op.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了  
                pic = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(mLocationForPhotos), 
                        null, op);
                mImage.setImageBitmap(pic);  
            } catch (Exception e) {  
                e.printStackTrace();  
            } 
        }
    }
}
