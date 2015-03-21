package com.parse.offlinetodos;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewTodoActivity extends Activity {

	private Button saveButton;
	private Button deleteButton;
	private EditText todoText;
    private TimePicker new_todo_time_picker;
    private DatePicker new_todo_date_picker;
	private Todo todo;
	private String todoId = null;
    private int hour;
    private int min;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_todo);

		// Fetch the todoId from the Extra data
		if (getIntent().hasExtra("ID")) {
			todoId = getIntent().getExtras().getString("ID");
		}

		todoText = (EditText) findViewById(R.id.todo_text);
		saveButton = (Button) findViewById(R.id.saveButton);
		deleteButton = (Button) findViewById(R.id.deleteButton);
        new_todo_time_picker = (TimePicker) findViewById(R.id.new_todo_time_picker);
        new_todo_date_picker = (DatePicker) findViewById(R.id.new_todo_date_picker);
        hour = -1;
        min = -1;

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

		saveButton.setOnClickListener(new OnClickListener() {

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

				todo.pinInBackground(TodoListApplication.TODO_GROUP_NAME,
						new SaveCallback() {

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

		});

		deleteButton.setOnClickListener(new OnClickListener() {

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
	}

}
