package com.skw.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * DefaultHttpClient测试程序(Get获取数据)
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo1 extends Activity {

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
        setContentView(R.layout.demo1);
        textView_response = (TextView)findViewById(R.id.TextView1);
        button_sendRequest = (Button)findViewById(R.id.button1);
        
        button_sendRequest.setOnClickListener(new OnClickListener() {
            
            //点击按钮时，执行sendRequestWithHttpClient()方法里面的线程
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendRequestWithHttpClient();
            }
        });
    }

    //方法：发送网络请求，获取百度首页的数据。在里面开启线程
    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址
                HttpGet httpGet = new HttpGet("http://www.baidu.com");
                
                try {
                    //第三步：执行请求，获取服务器返还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
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
}
