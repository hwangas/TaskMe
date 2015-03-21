package com.parse.offlinetodos;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class TodoListApplication extends Application {
	
	public static final String TODO_GROUP_NAME = "ALL_TODOS";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// add Todo subclass
		ParseObject.registerSubclass(Todo.class);
        ParseObject.registerSubclass(Friend.class);
		
		// enable the Local Datastore
		Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "H2087faZJdp23ZDEstriQ1wjjsR6JT5gWIPdx06Z", "5kJESLyyujCILT434DXEfgDx2Bxc2BtU2IKMjlhk");
		ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().saveInBackground();
		ParseACL defaultACL = new ParseACL();
		ParseACL.setDefaultACL(defaultACL, true);	
	}
	
	

}
