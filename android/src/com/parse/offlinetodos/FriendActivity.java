package com.parse.offlinetodos;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

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
    }


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