package com.parse.offlinetodos;

import java.util.UUID;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Todo")
public class Todo extends ParseObject {
	
	public String getTitle() {
		return getString("title");
	}
	
	public void setTitle(String title) {
		put("title", title);
	}
	
	public ParseUser getAuthor() {
		return getParseUser("author");
	}
	
	public void setAuthor(ParseUser currentUser) {
		put("author", currentUser);
	}

    public void setReader(ParseUser receiver) { put("reader", receiver); }

    public ParseUser getReader() { return getParseUser("reader"); }

    /////////////////

    public void setMonth(int month) { put("month", month); }

    public void setDay(int day) { put("day", day); }

    public void setYear(int year) { put("year", year); }

    public int getMonth() { return getInt("month"); }

    public int getDay() { return getInt("day"); }

    public int getYear() { return getInt("year"); }

    ///////////////

    public void setHour(int hour) { put("hour", hour);}

    public int getHour() { return getInt("hour"); }

    public void setMin(int minute) { put("minute", minute); }

    public int getMin() { return getInt("minute"); }

	public boolean isDraft() {
		return getBoolean("isDraft");
	}
	
	public void setDraft(boolean isDraft) {
		put("isDraft", isDraft);
	}
	
	public void setUuidString() {
	    UUID uuid = UUID.randomUUID();
	    put("uuid", uuid.toString());
	}
	
	public String getUuidString() {
		return getString("uuid");
	}
	
	public static ParseQuery<Todo> getQuery() {
		return ParseQuery.getQuery(Todo.class);
	}
}
