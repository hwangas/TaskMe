package com.parse.offlinetodos;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

/**
 * Created by Anna Hwang on 3/21/2015.
 */
@ParseClassName("Friend")
public class Friend extends ParseObject {

    private JSONArray arr = new JSONArray();
    private ParseUser person = null;

    public Friend(){}

    public Friend(ParseUser person_) {
        person = person_;
    }

    public void addFriend(ParseUser friend) {
        arr.put(friend);
    }

    /*
    public void removeFriend(ParseUser person, ParseUser friend) {
        JSONArray friends = person.getJSONArray("friends");
        for (int i = 0; i < friends.length(); ++i) {
            if(!friends.optString(0, "INVALID").equals("INVALID")) {
                String n = "NULL";
                friends.put(i, 0);
            }
        }
    }
    */

    public static ParseQuery<Friend> getQuery() {
        return ParseQuery.getQuery(Friend.class);
    }

}
