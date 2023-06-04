package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskCategoryEnum;
import com.amplifyframework.datastore.generated.model.TaskOwner;
import com.example.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    Spinner taskTypeSpinner;
    Spinner taskOwnerSpinner;

CompletableFuture<List<TaskOwner>> taskOwnersFuture = new CompletableFuture<>();
ArrayList<String> ownerNames;
ArrayList<TaskOwner> taskOwners;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskTypeSpinner = findViewById(R.id.AddTaskEnumTypeSpinner);
        taskOwnerSpinner = findViewById(R.id.addTaskEnumOwnerTypeSpinner);
        ownerNames = new ArrayList<>();
        taskOwners = new ArrayList<>();
//        TaskOwner taskOwner2 = TaskOwner.builder()
//                .name("TaskOwner2")
//                .build();



Amplify.API.query(
        ModelQuery.list(TaskOwner.class),
        success -> {
            Log.i(TAG, "Read task owners successfully");
            for (TaskOwner databaseTaskOwners : success.getData()){
                ownerNames.add(databaseTaskOwners.getName());
                taskOwners.add(databaseTaskOwners);
            }
        taskOwnersFuture.complete(taskOwners);
            runOnUiThread(this::setupSpinners);
        },
        failure ->{
            taskOwnersFuture.complete(null);
            Log.e(TAG, "FAILED to read task owners" + failure);
        }
);

        setupAddTaskButton();
        setupSpinners();
    }

    public void setupSpinners() {
        taskTypeSpinner.setAdapter(new ArrayAdapter<>(

                AddTaskActivity.this,
                android.R.layout.simple_spinner_item,
                TaskCategoryEnum.values()
        ));
        taskOwnerSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ownerNames
        ));
    }

    public void setupAddTaskButton() {
        findViewById(R.id.add_task_activity_button).setOnClickListener(view -> {
            String taskName = ((EditText) findViewById(R.id.AddTaskETTitle)).getText().toString();
            String selectedTaskOwnerStringName = taskOwnerSpinner.getSelectedItem().toString();

            try{
                taskOwners = (ArrayList<TaskOwner>) taskOwnersFuture.get();
            } catch(InterruptedException | ExecutionException ie){
                ie.printStackTrace();
            }

            TaskOwner selectedOwner = taskOwners.stream().filter(owner -> owner.getName().equals(selectedTaskOwnerStringName)).findAny().orElseThrow(RuntimeException::new);
            Task newTask = Task.builder()
                    .name(taskName)
                    .description("simple task description")
                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .taskCategory((TaskCategoryEnum) taskTypeSpinner.getSelectedItem())
                    .taskOwner(selectedOwner)
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

