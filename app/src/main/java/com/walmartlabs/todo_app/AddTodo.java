package com.walmartlabs.todo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTodo extends AppCompatActivity {

    private EditText todoItem;
    private Spinner priority;
    private static final String[] priorites = {"HIGH","MEDIUM","LOW"};
    private CalendarView calender;
    private String selectedDate;
    private int itemPosition;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        todoItem = (EditText) findViewById(R.id.et_editTodo);
        priority = (Spinner) findViewById(R.id.todoPrioritySpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddTodo.this,
                android.R.layout.simple_spinner_item,priorites);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(spinnerAdapter);
        calender = (CalendarView) findViewById(R.id.todoCalender);
        SimpleDateFormat ss = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        selectedDate = ss.format(date);
        setUpCalenderViewListener();

        requestCode = getIntent().getIntExtra("requestCode",200);
        if(requestCode == 205){
            Todo item = (Todo) getIntent().getSerializableExtra("todoItem");
            itemPosition = getIntent().getIntExtra("item_position",0);
            todoItem.setText(item.getDescription());

            for(int i=0;i<priorites.length;i++){
                if((priorites[i]).equals(item.getPriority())){
                    priority.setSelection(i);
                    break;
                }
            }
            selectedDate = item.getDueDate();
            try {
                Date dt = ss.parse(item.getDueDate());
                calender.setDate(dt.getTime());
            }catch (ParseException e){
                Log.e(getLocalClassName(),"Error occurred while parsing date:" + e.getMessage());
            }
        }

    }


    private void setUpCalenderViewListener(){
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                selectedDate = (month+1)+"-"+dayOfMonth+"-"+year;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveTodo(View view){

        String todoText = todoItem.getText().toString();
        if(todoText == null || todoText.isEmpty()){
            Toast.makeText(this,"Description Is Required",Toast.LENGTH_LONG).show();
            return;
        }

        String todoPriority = priority.getSelectedItem().toString();
        Todo todoItem = new Todo();
        todoItem.setDescription(todoText);
        todoItem.setPriority(todoPriority);
        todoItem.setDueDate(selectedDate);
        Intent intent = new Intent();
        intent.putExtra("todoItem", todoItem);
        if(requestCode == 205){
            intent.putExtra("item_position",itemPosition);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
