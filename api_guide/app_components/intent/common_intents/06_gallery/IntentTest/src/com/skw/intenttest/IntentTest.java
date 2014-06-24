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

    static final int REQUEST_IMAGE_GET = 1;
    private ImageView mImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mImage = (ImageView) findViewById(R.id.image);
    }

    public void sendMessage(View view) {
        selectImage() ;
    }

    public void selectImage() {
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {

            try {  
                Uri mLocationForPhotos = data.getData();
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
