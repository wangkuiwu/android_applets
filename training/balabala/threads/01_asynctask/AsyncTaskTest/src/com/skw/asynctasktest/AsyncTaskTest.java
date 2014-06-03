package com.skw.asynctasktest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncTaskTest extends Activity {
    
    private static final String TAG = "##AsyncTask##";
    
    private Button execute;
    private Button cancel;
    private ProgressBar progressBar;
    private TextView textView;
    
    private MyTask mTask;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        execute = (Button) findViewById(R.id.execute);
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意每次需new一个实例,新建的任务只能执行一次,否则会出现异常
                mTask = new MyTask();
                mTask.execute("http://www.baidu.com");
                
                execute.setEnabled(false);
                cancel.setEnabled(true);
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消一个正在执行的任务,onCancelled方法将会被调用
                mTask.cancel(true);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.text_view);
    }
    
    private class MyTask extends AsyncTask<String, Integer, String> {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            textView.setText("loading...");
        }
        
        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            try {
                for (int i=0; i<6; i++) {
                    Log.d(TAG, "doInBackground: publishProgress="+i);
                    publishProgress(20*i);
                    Thread.sleep(500);
                }

                Log.d(TAG, "doInBackground: return OK!");
                return "OK";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "doInBackground: return FAIL!");
            return "FAIL";
        }
        
        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {
            Log.i(TAG, "onProgressUpdate");
            progressBar.setProgress(progresses[0]);
            textView.setText("loading..." + progresses[0] + "%");
        }
        
        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");
            textView.setText(result);
            
            execute.setEnabled(true);
            cancel.setEnabled(false);
        }
        
        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            Log.i(TAG, "onCancelled");
            textView.setText("cancelled");
            progressBar.setProgress(0);
            
            execute.setEnabled(true);
            cancel.setEnabled(false);
        }
    }
}
