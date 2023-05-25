package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taskmaster.activities.AddTaskActivity;
import com.example.taskmaster.activities.AllTasksActivity;
import com.example.taskmaster.activities.SettingsActivity;
import com.example.taskmaster.activities.TaskDetailActivity;
import com.example.taskmaster.adapters.TaskListRecyclerViewAdapter;
import com.example.taskmaster.models.Task;
import com.example.taskmaster.models.TaskState;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String USER_TASK_TAG = "taskName";
    public static final String USER_TASK_BODY_TAG = "taskBody";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// step 2.2 create data itmes
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Mow the grass", "liokjojwojdoqwd", TaskState.NEW ));
        tasks.add(new Task("Wash The Car", "kjwhqkjhnqd",TaskState.ASSIGNED));
        tasks.add(new Task("Clean the dishes","qkjhbwdhkqbj", TaskState.COMPLETE ));
        tasks.add(new Task("Fold laundry", "kjqbnwdqjkw", TaskState.ASSIGNED));
        tasks.add(new Task("Go for a run", "qwjhdbqw", TaskState.IN_PROGRESS));

        // step 2.3 hand in data items in here and recycler view






        // Create and trigger intent; grab the button
        setUpSettingsButton();
        setUpRecyclerView(tasks);
        addTaskButton();
        viewAllTasksButton();

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected  void onResume(){
        super.onResume();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString(SettingsActivity.USER_NICKNAME_TAG, "No User Name");
        ((TextView) findViewById(R.id.homeNameFromSettings)).setText(userName + "'s Tasks!");
    }

    public void setUpSettingsButton() {
        ((ImageView) findViewById(R.id.mainActivitiySettingsImageView)).setOnClickListener(v -> {
            Intent goToSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(goToSettingsIntent);
        });
    }

    public void setUpRecyclerView(List<Task> tasks){
        // Step 1.2 grab RecyclerView
        RecyclerView taskListRecyclerView = (RecyclerView) findViewById(R.id.homeTaskListRecyclerView);
        // Step 1-3 set the layout manager of the RecyclerView to a LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(layoutManager);

// step 1.5 create and attach the recyclerView.adapter
//        TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter();
        // 2-3 change the creation of adapter to take list of tasks
        // 3-1 hand in activity context to the recycler view adapter creation
        TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter(tasks, this );
        taskListRecyclerView.setAdapter(adapter);
    }

    public void addTaskButton() {
        Button goToAllTasksButton = (Button) findViewById(R.id.homeAllTasksButton);
        //Add onClickListener
        goToAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent -> where you're coming from and where you're going
                Intent goToAddTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                // Start Intent
                startActivity(goToAddTaskIntent);
            }
        });
    }

    public void viewAllTasksButton() {
        Button goToAddTaskButton = (Button) findViewById(R.id.homeAddTaskButton);

        goToAddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
                startActivity(goToAllTasksIntent);
            }
        });
    }


}