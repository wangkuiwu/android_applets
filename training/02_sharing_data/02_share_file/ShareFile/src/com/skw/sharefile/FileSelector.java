package com.skw.sharefile;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.support.v4.content.FileProvider;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSelector extends Activity 
    implements AdapterView.OnItemClickListener {
    private static final String TAG = "##FileSelector##";

    // The path to the "images" subdirectory
    private File mImagesDir;
    // Array of files in the images subdirectory
    private File[] mImageFiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_selector);

        // the "images" is correspond to the "path" property of res/xml/filepaths
        mImagesDir = new File(getFilesDir(), "images");
        if (!mImagesDir.exists())
            mImagesDir.mkdir();
        // copy images to .../files/images/
        copyImageFiles();
        // get image under .../file/images/
        mImageFiles = mImagesDir.listFiles();

        ListView mListView = (ListView)findViewById(R.id.lv_choose);
        MyAdapter adapter = new MyAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }
    
    @Override  
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
        File requestFile = mImageFiles[position];

        Log.d(TAG, " onItemClick: position="+position+", requestFile="+requestFile);
        Uri fileUri=null;
        try {
            fileUri = FileProvider.getUriForFile(FileSelector.this, "com.skw.sharefile.fileprovider", requestFile);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " onItemClick: fileUri="+fileUri);

        if(fileUri != null) {
            Intent resultIntent = new Intent();
            resultIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            resultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
            FileSelector.this.setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    /*
     * copy images to "/data/data/com.skw.sharefile/files/images/"
     */
    private void copyImageFiles() {
        int[] ids = {
            R.drawable.leili_01,
            R.drawable.lufei02, 
            R.drawable.lufei03,
            R.drawable.lufei04,
            R.drawable.lufei1,
            R.drawable.lufei222
        };

        Resources res = getResources();

        try {
            for (int id:ids) {

                String resName = res.getResourceName(id);
                if (resName==null || resName=="")
                    continue;
                // get the file name of the picture. ex, "leili_01.jpg"
                String fileName = resName.substring(resName.lastIndexOf("/"))+".jpg";
                // copy images to dir(.../files/images/)
                File outFile = new File(mImagesDir, fileName);
                if (!outFile.exists()) {
                    InputStream in = res.openRawResource(id);
                    FileOutputStream out = new FileOutputStream(outFile);

                    int ret = 0;
                    byte[] buffer = new byte[1024];
                    while ( (ret = in.read(buffer)) != -1) {
                        out.write(buffer, 0, ret);
                    }

                    in.close();
                    out.close();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * listview's item choose messagebox
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

    public Bitmap getLoacalBitmap(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            return BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;
        
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mImageFiles.length;
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
            
            
            File file = mImageFiles[position];
            Bitmap bitmap = getLoacalBitmap(file);
            holder.img.setImageBitmap(bitmap);
            holder.title.setText(file.getName());
            holder.info.setText(file.getPath());
            
            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    showInfo();                    
                }
            });

            return convertView;
        }

        private final class ViewHolder{
            ImageView img;
            TextView title;
            TextView info;
            Button viewBtn;
        }
    }
}
