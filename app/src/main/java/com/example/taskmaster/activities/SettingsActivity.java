package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmaster.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.prefs.Preferences;

public class SettingsActivity extends AppCompatActivity {
    public static final String USER_NICKNAME_TAG = "userNickname";

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        populateNameEditText();


        setUpSaveButton(preferences);
    }


    public void populateNameEditText(){
        String userName = preferences.getString(USER_NICKNAME_TAG, "");
        ((EditText) findViewById(R.id.userNameActivityEditText)).setText(userName);
    }
    public void setUpSaveButton(SharedPreferences preferences){
        Button saveButton = findViewById(R.id.userSettingsSaveButton);
        saveButton.setOnClickListener(v -> {

            //creating an editor because SharedPreferences is read-only
           SharedPreferences.Editor preferenceEditor = preferences.edit();

           // grabbing string to save from user input
            EditText userNameEditText = findViewById(R.id.userNameActivityEditText);
            String userNameString = userNameEditText.getText().toString();

            // save teh string to shared preferences
            preferenceEditor.putString(USER_NICKNAME_TAG, userNameString);
            preferenceEditor.apply();


//            Snackbar.make(findViewById(R.id.userSettingsActivity), "Settings saved!", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
        });
    }



}