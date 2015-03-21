package com.parse.offlinetodos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.KeyListener;
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

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

/**
 * Created by Nick on 3/21/2015.
 */
public class FriendActivity extends Activity {

    private EditText editText2;
    private KeyListener originalKeyListener;
    private Button buttonShowIme;
    private LayoutInflater inflater;
    // Adapter for the Todos Parse Query
    private ParseQueryAdapter<Friend> friendListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);

        ListView list = (ListView)findViewById(R.id.listView);
        MenuItem goToList = (MenuItem) findViewById(R.id.todo_list);
        ArrayList <String> aList = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, aList);


        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<Friend> friendQueryFactory = new ParseQueryAdapter.QueryFactory<Friend>() {
            public ParseQuery<Friend> create() {
                ParseQuery<Friend> query = Friend.getQuery();
                query.orderByDescending("createdAt");
                query.fromLocalDatastore();
                return query;
            }
        };

        // Set up the adapter
        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        friendListAdapter = new FriendListAdapter(this, friendQueryFactory);

        // Attach the query adapter to the view
        ListView todoListView = (ListView) findViewById(R.id.todo_list_view);
        todoListView.setAdapter(friendListAdapter);


        todoListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Friend f = friendListAdapter.getItem(position);
                //openEditView(f);
            }
        });

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
/*
    private void openEditView(Todo todo) {
        Intent i = new Intent(this, NewTodoActivity.class);
        i.putExtra("ID", todo.getUuidString());
        startActivityForResult(i, EDIT_ACTIVITY_CODE);
    }
*/
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

    private class FriendListAdapter extends ParseQueryAdapter<Friend> {

        public FriendListAdapter(Context context,
                               ParseQueryAdapter.QueryFactory<Friend> queryFactory) {
            super(context, queryFactory);
        }

        //@Override
        public View getItemView(Todo todo, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_todo, parent, false);
                holder = new ViewHolder();
                holder.todoTitle = (TextView) view
                        .findViewById(R.id.todo_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            TextView todoTitle = holder.todoTitle;
            todoTitle.setText(todo.getTitle());
            if (todo.isDraft()) {
                todoTitle.setTypeface(null, Typeface.ITALIC);
            } else {
                todoTitle.setTypeface(null, Typeface.NORMAL);
            }
            return view;
        }
    }

    private static class ViewHolder {
        TextView todoTitle;
    }

}