package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    public static final String USER_TASK_TAG = "taskName";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Create and trigger intent; grab the button
        setUpSettingsButton();
        addTaskButton();
        viewAllTasksButton();
        task1Button();
        task2Button();
        task3Button();

    }

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

    public void task1Button() {
        Button goToTask1Button = (Button) findViewById(R.id.homeTaskOneButton);
        goToTask1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTask1Intent = new Intent(MainActivity.this, TaskDetailActivity.class);

                String taskName = goToTask1Button.getText().toString();

                goToTask1Intent.putExtra(USER_TASK_TAG, taskName );

                startActivity(goToTask1Intent);
            }
        });

    }

    public void task2Button() {
        Button goToTask2Button = (Button) findViewById(R.id.homeTaskTwoButton);
        goToTask2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTask2Intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                String taskName = goToTask2Button.getText().toString();

                goToTask2Intent.putExtra(USER_TASK_TAG, taskName );
                startActivity(goToTask2Intent);
            }
        });

    }

    public void task3Button() {
        Button goToTask3Button = (Button) findViewById(R.id.homeTask3Button);
        goToTask3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTask3Intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                String taskName = goToTask3Button.getText().toString();

                goToTask3Intent.putExtra(USER_TASK_TAG, taskName );
                startActivity(goToTask3Intent);
            }
        });

    }
}