package com.AVreeland.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    Spinner taskTypeSpinner;
    Spinner taskOwnerSpinner;
    private String s3Key;
    private String lat;
    private String lon;
    private FusedLocationProviderClient fusedLocationProviderClient;
    CompletableFuture<List<TaskOwner>> taskOwnersFuture = new CompletableFuture<>();
    ActivityResultLauncher<Intent> activityResultLauncher;

ArrayList<String> ownerNames;
ArrayList<TaskOwner> taskOwners;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskTypeSpinner = findViewById(R.id.AddTaskEnumTypeSpinner);
        taskOwnerSpinner = findViewById(R.id.addTaskEnumOwnerTypeSpinner);
        ownerNames = new ArrayList<>();
        taskOwners = new ArrayList<>();
        activityResultLauncher = getImagePickingActivityResult();


        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());


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

        fusedLocationProviderClient.flushLocations(); // <- try this is you are not seeing the location update
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "Application does not have access to either ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION!");
            return;
        }

        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            if(location == null) {
                Log.e(TAG, "Location callback was null");
            } else {
                String currentLatitude = Double.toString(location.getLatitude());
                String currentLongitude = Double.toString(location.getLongitude());
                Log.i(TAG, "Our current latitude: " + currentLatitude);
                Log.i(TAG, "Our current longitude: " + currentLongitude);
            }
        });

        // TODO: Subscription example for many updates (requestLocationUpdates)
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                try {
                    String address = geocoder.getFromLocation(
                                    locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude(),
                                    1) // gives us 1 best guess for the location
                            .get(0) // grab the best guess from the returned "List"
                            .getAddressLine(0); // get first address line

                    Log.i(TAG, "Repeating  current location is: " + address);
                } catch (IOException ioe) {
                    Log.e(TAG, "Could not get subscribed location: " + ioe.getMessage());
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
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
            // TODO: Class-39: Step 4: Return (leave the click method) if we don't have access to the appropriate location permissions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.e(TAG, "Application does not have access to either ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION!");
                return;
            }


            String taskName = ((EditText) findViewById(R.id.AddTaskETTitle)).getText().toString();
            String selectedTaskOwnerStringName = taskOwnerSpinner.getSelectedItem().toString();

            try{
                taskOwners = (ArrayList<TaskOwner>) taskOwnersFuture.get();
            } catch(InterruptedException | ExecutionException ie){
                ie.printStackTrace();
            }

            TaskOwner selectedOwner = taskOwners.stream().filter(owner -> owner.getName().equals(selectedTaskOwnerStringName)).findAny().orElseThrow(RuntimeException::new);

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                // "location" here should be null if no one has ever requested location prior!
                // Try running Google Maps first and clicking your current location button if you have a null callback here, or a null object when getting lat/long
                if(location == null) {
                    Log.e(TAG, "Location callback was null!");
                } else {
                    String currentLatitude = Double.toString(location.getLatitude());
                    String currentLongitude = Double.toString(location.getLongitude());
                    Log.i(TAG, "Our latitude: " + location.getLatitude());
                    Log.i(TAG, "Our longitude: " + location.getLongitude());
                    // TODO: For lab: Add latitude and longitude to product (task) model (schema update)
                    // TODO: For lab: Move logic to create and add product (task) to DynamoDB database in this callback!
                    Task newTask = Task.builder()
                            .name(taskName)
                            .description("simple task description")
                            .dateCreated(new Temporal.DateTime(new Date(), 0))
                            .taskCategory((TaskCategoryEnum) taskTypeSpinner.getSelectedItem())
                            .taskOwner(selectedOwner)
                            .currentLatitude(currentLatitude)
                            .currentLongitude(currentLongitude)
                            .s3Key(s3Key)
                            .build();

                    Amplify.API.mutate(
                            ModelMutation.create(newTask),
                            successResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): made a new task successfully"),
                            failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate().setUpSaveButton(): failed with this response " + failureResponse)
                    );
                }
            });


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
        Amplify.Storage.uploadInputStream(
                imageName,
                pickedImageInputStream,
                success -> {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    s3Key = success.getKey();  // non-empty s3ImageKey globally indicates there is an image picked in this activity currently
                    ImageView productImageView = findViewById(R.id.addTaskActivityImageSelection);
                    InputStream pickedImageInputStreamCopy = null;  // need to make a copy because InputStreams cannot be reused!
                    try
                    {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    productImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure -> Log.e(TAG, "Upload failed", failure)
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

