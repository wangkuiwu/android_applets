package com.skw.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * HttpURLConnection测试程序
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo3 extends Activity 
    implements View.OnClickListener {
    private TextView mTvShow = null;
    private ImageView mImageView = null;
    private String resultStr = "";
    private ProgressBar progressBar = null;
    private ViewGroup viewGroup = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo3);

        mTvShow = (TextView)findViewById(R.id.textview_show);
        mImageView = (ImageView)findViewById(R.id.imagview_show);
        findViewById(R.id.btn_download_img).setOnClickListener(this);
        findViewById(R.id.btn_visit_web).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_visit_web: {
                mImageView.setVisibility(View.GONE);
                mTvShow.setVisibility(View.VISIBLE);
                Thread visitBaiduThread = new Thread(new VisitWebRunnable());
                visitBaiduThread.start();
                try {
                    visitBaiduThread.join();
                    if(!resultStr.equals("")){
                        mTvShow.setText(resultStr);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            }
            case R.id.btn_download_img: {
                mImageView.setVisibility(View.VISIBLE);
                mTvShow.setVisibility(View.GONE);
                String imgUrl = "http://www.shixiu.net/d/file/p/2bc22002a6a61a7c5694e7e641bf1e6e.jpg";
                new DownImgAsyncTask().execute(imgUrl);
                break;
            }
        }
    }

    /**
     * 获取指定URL的响应字符串
     * @param urlString
     * @return
     */
    private String getURLResponse(String urlString){
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(urlString); //URL对象
            conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }

        return resultData;
    }

    /**
     * 从指定URL获取图片
     * @param url
     * @return
     */
    private Bitmap getImageBitmap(String url){
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    private class VisitWebRunnable implements Runnable{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            String data = getURLResponse("http://www.baidu.com/");
            resultStr = data;
        }

    }

    private class DownImgAsyncTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mImageView.setImageBitmap(null);
            showProgressBar();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap b = getImageBitmap(params[0]);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result!=null){
                dismissProgressBar();
                mImageView.setImageBitmap(result);
            }
        }
    }
    /**
     * 在母布局中间显示进度条
     */
    private void showProgressBar(){
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,  RelativeLayout.TRUE);
        progressBar.setVisibility(View.VISIBLE);
        Context context = getApplicationContext();
        viewGroup = (ViewGroup)findViewById(R.id.parent_view);
        viewGroup.addView(progressBar, params);     
    }

    /**
     * 隐藏进度条
     */
    private void dismissProgressBar(){
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
            viewGroup.removeView(progressBar);
            progressBar = null;
        }
    }
}
