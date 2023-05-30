package com.example.taskmaster.activities;

import static com.example.taskmaster.MainActivity.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taskmaster.R;
import com.example.taskmaster.database.TaskMasterDatabase;
import com.example.taskmaster.models.Task;

import java.util.Date;


public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    Spinner taskTypeSpinner;
    TaskMasterDatabase taskMasterDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskTypeSpinner = findViewById(R.id.AddTaskEnumTypeSpinner);


        taskMasterDatabase = Room.databaseBuilder(

                        getApplicationContext(),
                        TaskMasterDatabase.class,
                        DATABASE_NAME
                ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        setupAddTaskButton();
        setupTypeSpinner();
    }
    public void setupTypeSpinner() {
        taskTypeSpinner.setAdapter(new ArrayAdapter<>(

                AddTaskActivity.this,
                android.R.layout.simple_spinner_item,
                Task.TaskTypeEnum.values()
        ));
    }

    public void setupAddTaskButton() {
        findViewById(R.id.add_task_activity_button).setOnClickListener(view -> {
            Task newTask = new Task(
                    ((EditText) findViewById(R.id.AddTaskETTitle)).getText().toString(),
                    ((EditText) findViewById(R.id.AddTaskEDTitle)).getText().toString(),
                    new Date(),
                    Task.TaskTypeEnum.fromString(taskTypeSpinner.getSelectedItem().toString())
            );

            taskMasterDatabase.taskDao().insertATask(newTask);
            Toast.makeText(this, "Task added to the database!", Toast.LENGTH_SHORT).show();
        });
    }

    }

