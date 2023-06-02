package com.example.taskmaster.activities;

import static com.example.taskmaster.MainActivity.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.core.model.temporal.Temporal;
import com.example.taskmaster.R;
import com.example.taskmaster.model.Task;
import com.example.taskmaster.model.TaskCategoryEnum;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;



import java.util.Date;


public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    Spinner taskTypeSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskTypeSpinner = findViewById(R.id.AddTaskEnumTypeSpinner);



        setupAddTaskButton();
        setupTypeSpinner();
    }
    public void setupTypeSpinner() {
        taskTypeSpinner.setAdapter(new ArrayAdapter<>(

                AddTaskActivity.this,
                android.R.layout.simple_spinner_item,
                TaskCategoryEnum.values()
        ));
    }

    public void setupAddTaskButton() {
        findViewById(R.id.add_task_activity_button).setOnClickListener(view -> {
            String taskName = ((EditText) findViewById(R.id.AddTaskETTitle)).getText().toString();
//
            Task newTask = Task.builder()
                    .name(taskName)
                            .description("simple task description")
            .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .taskCategory((TaskCategoryEnum) taskTypeSpinner.getSelectedItem())
                            .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    successResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): made a new task successfully"),
                    failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): failed with this response " + failureResponse)
            );
//            Amplify.API.mutate(
//                    ModelMutation.update(newTask),
//                    successResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): made a new task successfully"),
//                    failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): failed with this response " + failureResponse)
//            );
//            Amplify.API.mutate(
//                    ModelMutation.delete(newTask),
//                    successResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): made a new task successfully"),
//                    failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): failed with this response " + failureResponse)
//            );


            // TODO FIX THE DATABASE SAVE!
//            taskMasterDatabase.taskDao().insertATask(newTask);
            Toast.makeText(this, "Task added to the database!", Toast.LENGTH_SHORT).show();
        });
    }

    }

