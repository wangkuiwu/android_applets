package com.skw.menutest;

import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;  
import android.view.MenuInflater; 
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.List;
import java.util.ArrayList;

public class MenuTest extends Activity {
     
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        registerForContextMenu(listView);
        setContentView(listView);
    }

    private List<String> getData(){
         
        List<String> data = new ArrayList<String>();
        data.add("item 1");
        data.add("item 2");
        data.add("item 3");
        data.add("item 4");
        data.add("item 5");
        data.add("item 6");

        return data;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(getApplicationContext(), "delete menu", 0).show();
                return true;
            case R.id.share:
                Toast.makeText(getApplicationContext(), "share menu", 0).show();
                return true;
            case R.id.detail:
                Toast.makeText(getApplicationContext(), "detail menu", 0).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
