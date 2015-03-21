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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.ui.ParseFriend;

/**
 * Created by Nick on 3/21/2015.
 */
public class FriendActivity extends Activity {

    private ParseFriend friend_;
    private MenuItem action_friend;
    private EditText editText2;
    private KeyListener originalKeyListener;
    private Button buttonShowIme;
    private LayoutInflater inflater;
    private ParseQueryAdapter<ParseFriend> friendListAdapter;
    private ParseQueryAdapter.QueryFactory<ParseFriend> friendQueryFactory;
    private ListView friendListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);

        Button action_friend = (Button)findViewById(R.id.action_friend);
        ListView list = (ListView)findViewById(R.id.friendListView);

        getFriend();

        // Set up the Parse query to use in the adapter
        friendQueryFactory = new ParseQueryAdapter.QueryFactory<ParseFriend>() {
            public ParseQuery<ParseFriend> create() {
                ParseQuery<ParseFriend> query = ParseFriend.getQuery();
                query.orderByDescending("createdAt");
                query.fromLocalDatastore();
                return query;
            }
        };
/*
        // Set up the adapter
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        friendListAdapter = new FriendListAdapter(this, friendQueryFactory);

        // Attach the query adapter to the view
        ListView friendListView = (ListView) findViewById(R.id.friendListView);
        friendListView.setAdapter(friendListAdapter);
*/
        friendListView = refresh();
        friendListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ParseFriend f = friendListAdapter.getItem(position);
                //openEditView(f);
            }
        });
/*
        action_friend.setOnClickListener(new Button.onClickListener() {

        });
*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
            }
        });
    }

    public ListView refresh()
    {
        // Set up the adapter
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        friendListAdapter = new FriendListAdapter(this, friendQueryFactory);

        // Attach the query adapter to the view
        ListView friendListView = (ListView) findViewById(R.id.friendListView);
        friendListView.setAdapter(friendListAdapter);
        return friendListView;
    }

/*
    private void openEditView(Todo todo) {
        Intent i = new Intent(this, NewTodoActivity.class);
        i.putExtra("ID", todo.getUuidString());
        startActivityForResult(i, EDIT_ACTIVITY_CODE);
    }
*/

    public void getFriend()
    {
        ParseQuery<ParseFriend> person = ParseQuery.getQuery("Friend");
        person.whereEqualTo("person", ParseUser.getCurrentUser());
        person.getFirstInBackground(new GetCallback<ParseFriend>() {
            public void done(ParseFriend f, com.parse.ParseException e) {
                if(f == null) {
                    friend_ = null;
                } else {
                    friend_ = f;
                }
            }
        });
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
            /*
                ParseQuery<Friend> person = ParseQuery.getQuery("Friend");
                person.whereEqualTo("person", ParseUser.getCurrentUser());
                person.getFirstInBackground(new GetCallback<Friend>() {
                    public void done(Friend f, com.parse.ParseException e) {
                        if(f == null) {
                            Log.d("onOptionsItemSelected", "oops no exist");
                        } else {
                            friend_ = f;
                            ParseQuery<ParseUser> friend = ParseQuery.getQuery("ParseUser");
                            friend.whereEqualTo("username", ((EditText )findViewById(R.id.edit_text_2)).getText().toString());
                            friend.getFirstInBackground(new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser parseUser, com.parse.ParseException e) {
                                    if(parseUser == null) {
                                        Log.d("onOptionsItemSelected", "u fuked up");
                                    } else {
                                        friend_.addFriend(parseUser);

                                    }
                                }
                            });
                        }
                    }
                });
                */
                ParseQuery<ParseUser> friend = ParseQuery.getQuery("ParseUser");
                Log.d("AOWEMGOAWM", ((EditText) findViewById(R.id.edit_text_2)).getText().toString());
                friend.whereEqualTo("username", ((EditText) findViewById(R.id.edit_text_2)).getText().toString());
                friend.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if(parseUser == null) {
                            Log.d("onOptionsItemSelected", "u fuked up");
                            //throw new exception(e);
                        } else {x`
                            friend_.addFriend(parseUser);
                            friend_.saveInBackground();
                            friendListView = refresh();
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
    }

    public void openAddFriend()
    {
        // DO THE FRAGMENT
    }

    private class FriendListAdapter extends ParseQueryAdapter<ParseFriend> {

        public FriendListAdapter(Context context,
                                 ParseQueryAdapter.QueryFactory<ParseFriend> queryFactory) {
            super(context, queryFactory);
        }

        //@Override
        public View getItemView(ParseFriend friend, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.friend_activity, parent, false);
                holder = new ViewHolder();
                holder.todoTitle = (TextView) view.findViewById(R.id.edit_text_2);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            TextView friend_title = holder.todoTitle;
            friend_title.setText((String) friend.get("name"));
            friend_title.setTypeface(null, Typeface.NORMAL);
            return view;
        }
    }

    private static class ViewHolder {
        TextView todoTitle;
    }
}