package com.skw.test;

import android.net.http.AndroidHttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

/**
 * AndroidHttpClient测试程序(Get获取数据)
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo2 extends Activity {
    public static final String TAG = "##skywang-Demo2";

    public static final int SHOW_RESPONSE = 0;
    
    private Button button_sendRequest;
    private TextView textView_response;
    
    //新建Handler的对象，在这里接收Message，然后更新TextView控件的内容
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case SHOW_RESPONSE:
                String response = (String) msg.obj;
                textView_response.setText(response);
                break;

            default:
                break;
            }            
        }

    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2);
        textView_response = (TextView)findViewById(R.id.TextView1);
        button_sendRequest = (Button)findViewById(R.id.button1);
        
        button_sendRequest.setOnClickListener(new OnClickListener() {
            
            //点击按钮时，执行sendRequestWithHttpClient()方法里面的线程
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // sendRequestWithHttpClient();
                postRequest();
            }
        });
    }

    //方法：发送网络请求，获取百度首页的数据。在里面开启线程
    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                // Toast.makeText(Demo2.this, "AndroidHttpClient", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "AndroidHttpClient");
                HttpClient httpCient = AndroidHttpClient.newInstance("");
                // HttpGet httpGet = new HttpGet("http://www.baidu.com");
                HttpGet httpGet = new HttpGet("http://www.baidu.com");
                
                //放入请求头的内容，必须是以键值对的形式，这里以Accept-language为例
                httpGet.addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
                //获取请求头，并用Header数组接收
                Header [] reqHeaders = httpGet.getAllHeaders();
                //遍历Header数组，并打印出来
                for (int i = 0; i < reqHeaders.length; i++) {
                    String name = reqHeaders[i].getName();
                    String value = reqHeaders[i].getValue();
                    Log.d("http01", "Http request: Name--->" + name + ",Value--->" + value);
                }
                
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    
                    //获取响应头，并用Header数组接收
                    Header [] responseHeaders = httpResponse.getAllHeaders();
                    //遍历Header数组，并打印出来
                    for (int i = 0; i < responseHeaders.length; i++) {
                        String name = responseHeaders[i].getName();
                        String value = responseHeaders[i].getValue();
                        Log.d("http01", "Http response: Name--->" + name + ",Value--->" + value);
                    }                    
                    
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }
                    
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }).start();//这个start()方法不要忘记了        
    }

    private void postRequest() {
        new Thread(new Runnable() {  
            @Override  
            public void run() {
                String url = "http://172.16.20.77:8080/app/app_token_654321654321654321654321654321";
                Log.d(TAG, "post request, url is "+url);
                HttpPost post=new HttpPost(url);  
                //设置POST请求中的参数  
                List<NameValuePair> params=new ArrayList<NameValuePair>();  

                params.add(new BasicNameValuePair("method", "app_common_signin"));
                params.add(new BasicNameValuePair("version", "1.0"));
                params.add(new BasicNameValuePair("user", "test01"));
                params.add(new BasicNameValuePair("password", "123456"));
                params.add(new BasicNameValuePair("admin", "0"));
                params.add(new BasicNameValuePair("type", "android"));
                try {
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));//将参数设置入POST请求  
                    HttpClient client=new DefaultHttpClient();  
                    HttpResponse response=client.execute(post);  
                    Log.d(TAG, "Response code:"+response.getStatusLine().getStatusCode());
                    if(response.getStatusLine().getStatusCode()==200){  
                        String content=EntityUtils.toString(response.getEntity());    //得到返回字符串  
                        Log.d(TAG, "Response content:"+content);//打印到logcat  
                        // analysisJSON(content);
                    }  
                } catch (UnsupportedEncodingException e) {  
                    e.printStackTrace();  
                } catch (ClientProtocolException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                // } catch (JSONException e) {  
                    // TODO Auto-generated catch block  
                    // e.printStackTrace();  
                }  
                  
            }  
        }).start();  
    }
}
