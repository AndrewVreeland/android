package com.AVreeland.taskmaster;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.AVreeland.taskmaster.activities.AddTaskActivity;
import com.AVreeland.taskmaster.activities.AllTasksActivity;
import com.AVreeland.taskmaster.activities.SettingsActivity;
import com.AVreeland.taskmaster.adapters.TaskListRecyclerViewAdapter;
import com.AVreeland.taskmaster.R;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String USER_TASK_TAG = "taskName";
    public static final String USER_TASK_BODY_TAG = "taskBody";
    SharedPreferences preferences;



    public static final String DATABASE_NAME = "tasks_database";
    List<Task> tasks;
    TaskListRecyclerViewAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();


        setUpSettingsButton();
        setUpRecyclerView();
        addTaskButton();
        viewAllTasksButton();
        uploadFile();
    }

    private void uploadFile() {
        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append("Example file contents");
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }

        Amplify.Storage.uploadFile(
                "ExampleKey",
                exampleFile,
                result -> Log.i("MyAmplifyApp", "Successfully created file: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "file creation failed", storageFailure)
        );
    }


    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onResume() {
        super.onResume();

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read products successfully!");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        tasks.add(databaseTask);
                    }

                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                },
                failure -> Log.i(TAG, "Did not read tasks successfully")
        );
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