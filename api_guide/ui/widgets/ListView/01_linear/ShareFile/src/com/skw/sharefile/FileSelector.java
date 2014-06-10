package com.skw.sharefile;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class FileSelector extends ListActivity {
     
    private List<Map<String, Object>> mData;
    
    private static int[] ids = {
        R.drawable.leili_01,
        R.drawable.lufei02, 
        R.drawable.lufei03,
        R.drawable.lufei04,
        R.drawable.lufei1,
        R.drawable.lufei222};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getBaseData();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
    }

    private List<Map<String, Object>> getBaseData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        Resources res = getResources();
        for (int id:ids) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", res.getResourceName(id));
            map.put("info", res.getResourcePackageName(id));
            map.put("img", id);
            list.add(map);
        }

        return list;
    }
    
    // ListView 中某项被选中后的逻辑
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        
        Log.v("MyListView4-click", (String)mData.get(position).get("title"));
    }
    
    /**
     * listview中点击按键弹出对话框
     */
    public void showInfo(){
        new AlertDialog.Builder(this)
        .setTitle("ListViewTest")
        .setMessage("More...")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .show();
        
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView info;
        public Button viewBtn;
    }
    
    
    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;
        
        
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
            ViewHolder holder = null;
            if (convertView == null) {
                
                holder=new ViewHolder();  
                
                convertView = mInflater.inflate(R.layout.simple_adapter, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                holder.viewBtn = (Button)convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);
                
            }else {
                
                holder = (ViewHolder)convertView.getTag();
            }
            
            
            holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
            holder.title.setText((String)mData.get(position).get("title"));
            holder.info.setText((String)mData.get(position).get("info"));
            
            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    showInfo();                    
                }
            });

            return convertView;
        }
    }


    // =============================================

    private void showText() {
        // new ArrayAdapter<String>(Context context, int textViewResourceId, T[] objects)
        // context是上下文环境，
        // textViewResourceId是显示ArrayAdapter的View的id
        // objects是对象数组
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        setContentView(listView);
    }

    private List<String> getData(){
         
        List<String> data = new ArrayList<String>();
        data.add("item 1");
        data.add("item 2");
        data.add("item 3");
        data.add("item 4");
        data.add("item 5");

        return data;
    }
}
