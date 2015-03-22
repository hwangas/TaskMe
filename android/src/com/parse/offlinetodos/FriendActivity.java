package com.parse.offlinetodos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.ui.ParseFriend;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Nick on 3/21/2015.
 */
public class FriendActivity extends Activity {

    //private ParseFriend friend_;
    private MenuItem action_friend;
    private EditText editText2;
    private KeyListener originalKeyListener;
    private Button buttonShowIme;
    private LayoutInflater inflater;
    private ParseQueryAdapter<ParseFriend> friendListAdapter;
    private ParseQueryAdapter.QueryFactory<ParseFriend> friendQueryFactory;
    private ListView listview;
    private ParseUser person;

    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList<String> list = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);
        person = ParseUser.getCurrentUser();
        listview = (ListView) findViewById(R.id.friendListView);

        addAndRefresh(null);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
            }
        });
    }

    public void addAndRefresh(ParseUser friend)
    {
        boolean alreadyAdded = false;
        list = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        JSONArray friends = ParseUser.getCurrentUser().getJSONArray("friends");
        for(int i = 0; i < friends.length() - 1; ++i) {
            Toast.makeText(getApplicationContext(), i + ": " + ((ParseUser) friends.opt(i)).getUsername(),
                    Toast.LENGTH_LONG).show();

            if(friend != null &&
               ((ParseUser) friends.opt(i)).getUsername().equals(friend.getUsername())) {
                alreadyAdded = true;
            }

            list.add(((ParseUser) friends.opt(i)).getUsername());
        }

        if(friend != null && !alreadyAdded) {
            list.add(friend.getUsername());
            JSONArray arr = person.getJSONArray("friends");
            arr.put(friend);
            person.put("friends", arr);
        }

        person.saveInBackground();
        /** Setting the adapter to the ListView */
        listview.setAdapter(adapter);
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
                ParseQuery<ParseUser> friend = ParseUser.getQuery();
                friend.whereEqualTo("username", ((EditText) findViewById(R.id.edit_text_2)).getText().toString());

                friend.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if(parseUser == null) {
                            Toast.makeText(getApplicationContext(), "parseUser == null",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            addAndRefresh(parseUser);
                        }
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
            }
    }
}