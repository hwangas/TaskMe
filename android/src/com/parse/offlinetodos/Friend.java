package com.parse.offlinetodos;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

/**
 * Created by Anna Hwang on 3/21/2015.
 */
public class Friend extends ParseObject {

    public void initFriend(ParseUser person) {
        put("name", person.getUsername());
        put("friends", new JSONArray());
    }

    public void addFriend(ParseUser person, ParseUser friend) {
        JSONArray friends = person.getJSONArray("friends");
        friends.put(friend.getUsername());
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
