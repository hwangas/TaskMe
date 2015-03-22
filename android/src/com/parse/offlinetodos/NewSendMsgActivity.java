package com.parse.offlinetodos;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

/**
 * Created by Anna Hwang on 3/21/2015.
 */
public class NewSendMsgActivity extends Activity {

    private Button saveButton;
    private Button deleteButton;
    private EditText todoText;
    private EditText receiver_name;
    private TimePicker new_todo_time_picker;
    private DatePicker new_todo_date_picker;
    private Todo todo;
    private String todoId = null;
    private int hour;
    private int min;
    private boolean VALID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        VALID = false;

        todoText = (EditText) findViewById(R.id.todo_text);
        receiver_name = (EditText) findViewById(R.id.receiver_name);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        new_todo_time_picker = (TimePicker) findViewById(R.id.new_todo_time_picker);
        new_todo_date_picker = (DatePicker) findViewById(R.id.new_todo_date_picker);
        hour = -1;
        min = -1;

        // Fetch the todoId from the Extra data
        if (getIntent().hasExtra("ID")) {
            todoId = getIntent().getExtras().getString("ID");
        }

        if (todoId == null) {
            todo = new Todo();
            todo.setUuidString();
        } else {
            ParseQuery<Todo> query = Todo.getQuery();
            query.fromLocalDatastore();
            query.whereEqualTo("uuid", todoId);
            query.getFirstInBackground(new GetCallback<Todo>() {

                @Override
                public void done(Todo object, ParseException e) {
                if (!isFinishing()) {
                    todo = object;
                    todoText.setText(todo.getTitle());
                    deleteButton.setVisibility(View.VISIBLE);
                    new_todo_time_picker.setCurrentHour(todo.getHour());
                    new_todo_time_picker.setCurrentMinute(todo.getMin());
                    new_todo_date_picker.updateDate(todo.getYear(), todo.getMonth(), todo.getDay());
                }
                }

            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                todo.setTitle(todoText.getText().toString());
                todo.setDraft(true);
                todo.setAuthor(ParseUser.getCurrentUser());
                todo.setHour(hour);
                todo.setMin(min);
                todo.setMonth(new_todo_date_picker.getMonth());
                todo.setDay(new_todo_date_picker.getDayOfMonth());
                todo.setYear(new_todo_date_picker.getYear());

                synchronized (this) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", receiver_name.getText().toString());
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        public void done(ParseUser user, ParseException e) {
                            if (user == null) {
                                Toast.makeText(getApplicationContext(),
                                        "It seems your pal isn't in touch!",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                todo.setReader(user);
                                VALID = true;
                            }
                        }
                    });
                }

                synchronized(this) {
                    todo.pinInBackground(TodoListApplication.TODO_GROUP_NAME, new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (isFinishing()) {
                                return;
                            }
                            if (e == null) {
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            };
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // The todo will be deleted eventually but will
                // immediately be excluded from query results.
                todo.deleteEventually();
                setResult(Activity.RESULT_OK);
                finish();
            }

        });

        new_todo_time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                min = minute;
                hour = hourOfDay;
            }
        });


        class DialogFrag extends DialogFragment {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(todo.getTitle())
                        .setPositiveButton("Accept request", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                                PendingIntent alarmIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
                                AlarmManager alarmMgr = (AlarmManager)getBaseContext().getSystemService(getBaseContext().ALARM_SERVICE);


                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, min);


                                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        1000 * 60 * 20, alarmIntent);


                            }
                        })
                        .setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                return builder.create();
            }
        }

        FragmentManager manager = getFragmentManager();

        DialogFrag Alarm = new DialogFrag();
        Alarm.show(manager, "TASK");
    }
}
