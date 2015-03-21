package com.parse.offlinetodos;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Nick on 3/21/2015.
 */
public class FriendActivity extends Activity {

    private EditText editText2;
    private KeyListener originalKeyListener;
    private Button buttonShowIme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);

        ListView list = (ListView)findViewById(R.id.listView);
        MenuItem goToList = (MenuItem) findViewById(R.id.todo_list);
        ArrayList <String> aList = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, aList);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


            }
        });

        goToList.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem view) {
                finish();
                return true;
            }
        });
    }
//hi


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friend_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_friend:
                openAddFriend();
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }

    }




    public void openAddFriend()
    {
        // DO THE FRAGMENT

    }

}