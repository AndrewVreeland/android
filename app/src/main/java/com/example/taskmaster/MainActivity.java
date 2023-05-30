package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
import com.example.taskmaster.database.TaskMasterDatabase;
import com.example.taskmaster.models.Task;
import com.example.taskmaster.models.TaskState;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String USER_TASK_TAG = "taskName";
    public static final String USER_TASK_BODY_TAG = "taskBody";
    SharedPreferences preferences;

    TaskMasterDatabase taskMasterDatabase;

    public static final String DATABASE_NAME = "tasks_database";
    List<Task> tasks;
    TaskListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskMasterDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskMasterDatabase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        tasks = taskMasterDatabase.taskDao().findAllTasks();

        setUpSettingsButton();
        setUpRecyclerView();
        addTaskButton();
        viewAllTasksButton();
    }


    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onResume() {
        super.onResume();
        tasks.clear();
        tasks.addAll(taskMasterDatabase.taskDao().findAllTasks());
        adapter.notifyDataSetChanged();


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

    public void setUpRecyclerView() {

        RecyclerView taskListRecyclerView = (RecyclerView) findViewById(R.id.homeTaskListRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(layoutManager);


         adapter = new TaskListRecyclerViewAdapter(tasks, this);
        taskListRecyclerView.setAdapter(adapter);
    }

    public void addTaskButton() {
        Button goToAllTasksButton = (Button) findViewById(R.id.homeAllTasksButton);

        goToAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToAddTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);

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