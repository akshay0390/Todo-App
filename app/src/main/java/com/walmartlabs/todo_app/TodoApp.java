package com.walmartlabs.todo_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TodoApp extends AppCompatActivity {

    private ListView listView;
    private final int ADD_REQUEST_CODE = 200;
    private final int EDIT_REQUEST_CODE = 205;

    private ArrayList<Todo> todoList;
    private TodoItemAdapter todoItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_app);

        readItems();
        listView = (ListView) findViewById(R.id.lv_Items);
        todoItemAdapter = new TodoItemAdapter(this,todoList);
        listView.setAdapter(todoItemAdapter);
        setupListViewListener();
    }

    private void setupListViewListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /*FragmentManager fm = getSupportFragmentManager();
                DeleteDialogFragment fragment = DeleteDialogFragment.newInstance("Delete");
                fragment.setPosition(position);
                fragment.show(fm,"fragment_alert");*/

                //items.remove(position);
                //itemsAdapter.notifyDataSetChanged();
                //writeItems();

                AlertDialog.Builder builder = new AlertDialog.Builder(TodoApp.this);
                builder.setCancelable(true);
                builder.setTitle("Do you want to Delete this Todo?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                       todoList.remove(position);
                        todoItemAdapter.notifyDataSetChanged();
                        writeItems();
                    }

                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }

                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TodoApp.this, AddTodo.class);

                Todo todoItem = todoList.get(position);

                intent.putExtra("todoItem", todoItem);
                intent.putExtra("item_position", position);
                intent.putExtra("requestCode", EDIT_REQUEST_CODE);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        ObjectInputStream ois = null;
        try{
            FileInputStream fis = new FileInputStream(todoFile);
            ois = new ObjectInputStream(fis);
            todoList = new ArrayList<Todo>();
            todoList = (ArrayList<Todo>) ois.readObject();
            ois.close();
        }catch (Exception e){
            todoList = new ArrayList<Todo>();
        }finally {
            if(ois!=null) {
                try{
                    ois.close();
                }catch (Exception e){
                    Log.e(getLocalClassName(),"Exception occurred while closing Object input stream:"+e.getMessage());
                }
            }
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        ObjectOutputStream oos = null;
        try{
            FileOutputStream fos = new FileOutputStream(todoFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(todoList);
            oos.close();
        }catch (IOException e){
            Log.e(getLocalClassName(), "Exception occurred while writing objects to File:" + e.getMessage());
        }finally{
            if(oos!=null) {
                try{
                    oos.close();
                }catch (Exception e){
                    Log.e(getLocalClassName(),"Exception occurred while closing Object Output stream:"+e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_app, menu);
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

    public void onAddItem(View view){
        Intent intent = new Intent(TodoApp.this,AddTodo.class);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ADD_REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == ADD_REQUEST_CODE) {
           //The below logic handles addition of new Todo Items
            Todo todoItem = (Todo) data.getSerializableExtra("todoItem");
            todoList.add(todoItem);
            todoItemAdapter.notifyDataSetChanged();
            writeItems();
        }else if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            //The below logic handles edited Todo Items
            Todo todoItem = (Todo) data.getSerializableExtra("todoItem");
            int itemPosition = data.getExtras().getInt("item_position");
            todoList.set(itemPosition,todoItem);
            todoItemAdapter.notifyDataSetChanged();
            writeItems();
        }else if(resultCode == RESULT_OK && requestCode == 210){
            int position = data.getExtras().getInt("item_position");
            todoList.remove(position);
            todoItemAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
