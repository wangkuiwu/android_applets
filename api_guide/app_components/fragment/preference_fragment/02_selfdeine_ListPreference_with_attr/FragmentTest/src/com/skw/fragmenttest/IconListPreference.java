package com.skw.fragmenttest;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.util.Log;

public class IconListPreference extends ListPreference {
    private static final String TAG = "##IconListPreference##";

    private Context mContext;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private int[] mEntryIcons;
    private int mPosition = -1;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private String mKey;

    public IconListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        // 获取自定义的属性(attrs.xml中)对应行的TypedArray
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconListPreference);
        // 获取entryIcons属性对应的值
        int iconResId = a.getResourceId(R.styleable.IconListPreference_entryIcons, -1);
        if (iconResId != -1) {
            setEntryIcons(iconResId);
        }

        // 获取Preferece对应的key
        mKey = getKey();
        // 获取SharedPreferences
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        // 获取SharedPreferences.Editor
        mEditor = mPref.edit();
        // 获取Entry
        // 注意：如果配置文件中没有android:entries属性，则getEntries()为空；
        mEntries = getEntries();
        // 获取Entry对应的值
        // 注意：如果配置文件中没有android:entryValues属性，则getEntries()为空
        mEntryValues = getEntryValues();

        // 获取该ListPreference保存的值
        String value = mPref.getString(mKey, "");
        mPosition = findIndexOfValue(value);
        // 设置Summary
        if (mPosition!=-1) {
            setSummary(mEntries[mPosition]);
            setIcon(mEntryIcons[mPosition]);
        }

        a.recycle();
   }

    /**
     * 设置图标：icons数组
     */
    private void setEntryIcons(int[] entryIcons) {
        mEntryIcons = entryIcons;
    }

    /**
     * 设置图标：根据icon的id数组
     */
    public void setEntryIcons(int entryIconsResId) {
        TypedArray icons = getContext().getResources().obtainTypedArray(entryIconsResId);
        int[] ids = new int[icons.length()];
        for (int i = 0; i < icons.length(); i++) 
            ids[i] = icons.getResourceId(i, -1);
        setEntryIcons(ids);
        icons.recycle();
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);

        IconAdapter adapter = new IconAdapter(mContext);
        builder.setAdapter(adapter, null);
    }

    public class IconAdapter extends BaseAdapter{

        private LayoutInflater mInflater;
        
        
        public IconAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mEntryIcons.length;
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
                
                holder = new ViewHolder();  
                
                convertView = mInflater.inflate(R.layout.icon_adapter, parent, false);
                holder.layout = (LinearLayout)convertView.findViewById(R.id.icon_layout);
                holder.img = (ImageView)convertView.findViewById(R.id.icon_img);
                holder.info = (TextView)convertView.findViewById(R.id.icon_info);
                holder.check = (RadioButton)convertView.findViewById(R.id.icon_check);
                convertView.setTag(holder);
                
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            
            holder.img.setBackgroundResource(mEntryIcons[position]);
            holder.info.setText(mEntries[position]);
            holder.check.setChecked(mPosition == position);

            final ViewHolder fholder = holder;
            final int fpos = position;
            convertView.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    v.requestFocus();
                    // 选中效果
                    fholder.layout.setBackgroundColor(Color.CYAN);

                    // 更新mPosition
                    mPosition = fpos;
                    // 更新Summary
                    IconListPreference.this.setSummary(mEntries[fpos]);
                    IconListPreference.this.setIcon(mEntryIcons[fpos]);
                    // 更新该ListPreference保存的值
                    mEditor.putString(mKey, mEntryValues[fpos].toString());
                    mEditor.commit();

                    // 取消ListPreference设置对话框
                    getDialog().dismiss();
                }
            });

            return convertView;
        }

        // ListPreference每一项对应的Layout文件的结构体
        private final class ViewHolder {
            ImageView img;
            TextView info;
            RadioButton check;
            LinearLayout layout;
        }
    }
}
