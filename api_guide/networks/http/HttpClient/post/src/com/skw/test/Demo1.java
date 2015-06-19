package com.skw.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Demo1 extends Activity {

    private static final String TAG = "##skywang-Demo1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);
        // textView_response = (TextView)findViewById(R.id.TextView1);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            
            //点击按钮时，执行postRequestWithHttpClient()方法里面的线程
            @Override
            public void onClick(View v) {
                // postRequestWithHttpClient();
                googlePost();
            }
        });
    }

    private void postRequestWithHttpClient() {
        Log.d(TAG, "post Request with HttpClient");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = getUrl();
                HttpClient client = getHttpClient();
                List<NameValuePair> params = getParams();
                doPost(client, url, params);
            }
        }).start();
    }

    public String doGet(HttpClient client, String url, Map params) {
        /* 建立HTTPGet对象 */
        String paramStr = "";

        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            paramStr += paramStr = "&" + key + "=" + val;
        }

        if (!paramStr.equals("")) {
            paramStr = paramStr.replaceFirst("&", "?");
            url += paramStr;
        }
        HttpGet httpRequest = new HttpGet(url);

        String strResult = "doGetError";

        try {
            /* 发送请求并等待响应 */
            HttpResponse httpResponse = client.execute(httpRequest);
            /* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /* 读返回数据 */
                strResult = EntityUtils.toString(httpResponse.getEntity());

            } else {
                strResult = "Error Response: "
                        + httpResponse.getStatusLine().toString();
            }
        } catch (ClientProtocolException e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        } catch (IOException e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        } catch (Exception e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        }

        Log.v("strResult", strResult);

        return strResult;
    }

    public String doPost(HttpClient client, String url, List<NameValuePair> params) {
        Log.d(TAG, "do Post");
        /* 建立HTTPPost对象 */
        HttpPost httpRequest = new HttpPost(url);
        String strResult = "doPostError";
        try {
            /* 添加请求参数到请求对象 */
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            /* 发送请求并等待响应 */
            HttpResponse httpResponse = client.execute(httpRequest);
            Log.d(TAG, "post: statusCode="+httpResponse.getStatusLine().getStatusCode());
            /* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /* 读返回数据 */
                strResult = EntityUtils.toString(httpResponse.getEntity());
            } else {
                strResult = "Error Response: " + httpResponse.getStatusLine().toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG,"result="+strResult);
        return strResult;
    }

    private String getUrl() {
        String url = "http://wap.kaixin001.com/home/";
        return url;
    }

    private List<NameValuePair> getParams() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("email", "firewings.r@gmail.com"));
        list.add(new BasicNameValuePair("password", "954619"));
        list.add(new BasicNameValuePair("remember", "1"));
        list.add(new BasicNameValuePair("from", "kx"));
        list.add(new BasicNameValuePair("login", "登 录"));
        list.add(new BasicNameValuePair("refcode", ""));
        list.add(new BasicNameValuePair("refuid", "0"));

        return list;
    }

    public HttpClient getHttpClient() {
        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
        HttpParams params = new BasicHttpParams();
        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        // 设置重定向，缺省为 true
        HttpClientParams.setRedirecting(params, true);

        // 设置 user agent
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        HttpProtocolParams.setUserAgent(params, userAgent);

        // 创建一个 HttpClient 实例
        // 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
        // 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
        return new DefaultHttpClient(params);
    }

    private void googlePost() {
        Log.d(TAG, "googlePost");
        new Thread(new Runnable() {
            @Override  
            public void run() {
                String strResult = "";
                String url = "http://www.google.cn/search";

                // 设置Post中的参数
                List<NameValuePair> params=new ArrayList<NameValuePair>();  
                params.add(new BasicNameValuePair("hl", "zh-CN"));
                params.add(new BasicNameValuePair("source", "hp"));
                params.add(new BasicNameValuePair("q", "haha"));
                params.add(new BasicNameValuePair("aq", "f"));
                params.add(new BasicNameValuePair("aqi", "g10"));
                params.add(new BasicNameValuePair("aql", ""));
                params.add(new BasicNameValuePair("oq", ""));

                // 建立HttpPost对象
                HttpPost post = new HttpPost(url);
                try {
                    // 新建HttpClient对象
                    HttpClient client=new DefaultHttpClient();  
                    // 添加请求参数到Post请求
                    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    // 发送请求并等待响应
                    HttpResponse httpResponse = client.execute(post);
                    Log.d(TAG, "post: statusCode="+httpResponse.getStatusLine().getStatusCode());
                    // 若状态码为200，则表示正常。
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        // 读返回数据 
                        strResult = EntityUtils.toString(httpResponse.getEntity());
                    } else {
                        strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d(TAG,"result="+strResult);
            }
        }).start();
    }
}
