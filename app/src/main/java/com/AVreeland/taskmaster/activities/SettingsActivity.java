package com.AVreeland.taskmaster.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskOwner;
import com.AVreeland.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SettingsActivity extends AppCompatActivity {
    public static final String USER_NICKNAME_TAG = "userNickname";

    SharedPreferences preferences;
    Spinner taskTypeSpinner;
    Spinner taskOwnerSpinner;
    ArrayList<String> ownerNames;
    ArrayList<TaskOwner> taskOwners;
    CompletableFuture<List<TaskOwner>> taskOwnersFuture = new CompletableFuture<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        taskOwnerSpinner = findViewById(R.id.settingsTaskOwnerSpinner);
        ownerNames = new ArrayList<>();
        taskOwners = new ArrayList<>();


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

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

        populateNameEditText();


        setUpSaveButton(preferences);
    }


    public void populateNameEditText(){
        String userName = preferences.getString(USER_NICKNAME_TAG, "");
        ((EditText) findViewById(R.id.userNameActivityEditText)).setText(userName);
    }

    public void saveSelectedTaskOwner(){}

    public void setUpSaveButton(SharedPreferences preferences){
        Button saveButton = findViewById(R.id.userSettingsSaveButton);
        saveButton.setOnClickListener(v -> {

            //creating an editor because SharedPreferences is read-only
           SharedPreferences.Editor preferenceEditor = preferences.edit();

           // grabbing string to save from user input
            EditText userNameEditText = findViewById(R.id.userNameActivityEditText);
            String userNameString = userNameEditText.getText().toString();
            String selectedTaskOwnerStringName = taskOwnerSpinner.getSelectedItem().toString();
            // save teh string to shared preferences
            preferenceEditor.putString(USER_NICKNAME_TAG, userNameString);
            preferenceEditor.apply();


//            Snackbar.make(findViewById(R.id.userSettingsActivity), "Settings saved!", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
        });
    }

    public void setupSpinners() {
        taskOwnerSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ownerNames
        ));
    }

}