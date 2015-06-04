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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * HttpURLConnection测试程序
 *
 * @desc 本文共展示了3种"子线程如何将Http请求结果反馈给主线程的方式"，可以使用的是第1种和第3种。
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo1 extends Activity 
    implements View.OnClickListener/*, UrlRequestListener*/ {
    private static final String TAG = "##skywang-Demo1";
    private TextView mTvShow = null;
    private String mResultString = "";
    private ViewGroup viewGroup = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    String resultData = (String)msg.obj;
                    mTvShow.setText(resultData);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);

        mTvShow = (TextView)findViewById(R.id.textview_show);
        findViewById(R.id.btn_method1).setOnClickListener(this);
        findViewById(R.id.btn_method2).setOnClickListener(this);
        findViewById(R.id.btn_method3).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_method1: {
                // 方式一：在子线程中进行Http请求，
                // 获取请求结果之后，通过Handler告知主线程。
                new MyThread("http://www.baidu.com").start();
                break;
            }
            case R.id.btn_method2: {
                // 方式二：在子线程中进行Http请求，
                // 然后主线成阻塞等待，直到子线程Http请求完成为止。
                // 注：不建议使用该方式，容易造成ANR！
                Thread visitBaiduThread = new Thread(new VisitWebRunnable());
                visitBaiduThread.start();
                try {
                    visitBaiduThread.join();
                    if(!mResultString.equals("")){
                        mTvShow.setText(mResultString);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            }
            case R.id.btn_method3: {
                // 方式三：通过AsyncTask去处理Http请求，并更新UI。
                new MyAsyncTask().execute("http://www.baidu.com/");
                break;
            }
        }
    }

    // 进行Http请求的子线程
    private final class MyThread extends Thread {
        private String mUrlString;

        public MyThread(String urlString) {
            mUrlString = urlString;
        }

        @Override
        public void run() {
            executeRequest("http://www.baidu.com/");
        }

        private void executeRequest(String urlString){
            HttpURLConnection conn = null; //连接对象
            InputStream is = null;
            String resultData = "";
            try {
                URL url = new URL(urlString); // 新建URL对象
                conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
                conn.setDoInput(true); //允许输入流，即允许下载
                conn.setDoOutput(true); //允许输出流，即允许上传
                conn.setUseCaches(false); //不使用缓冲
                conn.setRequestMethod("GET"); //使用get请求
                is = conn.getInputStream();   //获取输入流，此时才真正建立链接
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
                String inputLine  = "";
                while((inputLine = bufferReader.readLine()) != null){
                    resultData += inputLine + "\n";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(conn != null){
                    conn.disconnect();
                }
            }

            // 发送消息给主线程
            mHandler.sendMessage(mHandler.obtainMessage(1, resultData));
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

    private class VisitWebRunnable implements Runnable {
        @Override
        public void run() {
            mResultString = getURLResponse("http://www.baidu.com");
        }
    }

    private final class MyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return getURLResponse(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!=null){
                mTvShow.setText(result);
            }
        }
    }
}
