package com.AVreeland.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import com.AVreeland.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    Spinner taskTypeSpinner;
    Spinner taskOwnerSpinner;
    private String s3Key;
CompletableFuture<List<TaskOwner>> taskOwnersFuture = new CompletableFuture<>();
ActivityResultLauncher<Intent> activityResultLauncher;
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
        activityResultLauncher = getImagePickingActivityResult();


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

        saveTask();
        setupSpinners();
        setupImageButton();
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

    public void saveTask() {
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
                    .s3Key(s3Key)
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

    public void setupImageButton(){
    findViewById(R.id.addTaskActivityImageSelection).setOnClickListener(view ->{
        launchImageSelectionIntent();
    });
    }

    public void launchImageSelectionIntent(){
    Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
    imageFilePickingIntent.setType("*/*");
    imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image.jpg"});

    activityResultLauncher.launch(imageFilePickingIntent);
    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResult(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            public void onActivityResult(ActivityResult result) {
                                // Uri of image -> the path
                                Uri pickedImageFileUri = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                    String pickedImageFileName = getFileNameFromUri(pickedImageFileUri);
                                    Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is:" + pickedImageFileName);
                                    uploadInputStreamToS3(pickedImageInputStream, pickedImageFileName, pickedImageFileUri);
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }


    public void uploadInputStreamToS3(InputStream pickedImageInputStream, String imageName, Uri pickedImageFileUri){
        s3Key = imageName;
        Amplify.Storage.uploadInputStream(
                imageName,
                pickedImageInputStream,
                success -> Log.i(TAG, "Successfully uploaded: " + success.getKey()),
                failure -> Log.e("MyAmplifyApp", "Upload Failed", failure)

        );
    }

    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}

