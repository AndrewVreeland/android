package com.AVreeland.taskmaster.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthSignOutOptions;
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
    AuthUser authUser;
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
                    for (TaskOwner databaseTaskOwners : success.getData()) {
                        ownerNames.add(databaseTaskOwners.getName());
                        taskOwners.add(databaseTaskOwners);
                    }
                    taskOwnersFuture.complete(taskOwners);
                    runOnUiThread(this::setupSpinners);
                },
                failure -> {
                    taskOwnersFuture.complete(null);
                    Log.e(TAG, "FAILED to read task owners" + failure);
                }
        );

        populateNameEditText();
        setUpLoginButton();
        setUpLogoutButton();

        setUpSaveButton(preferences);
    }


    public void populateNameEditText() {
        String userName = preferences.getString(USER_NICKNAME_TAG, "");
        ((EditText) findViewById(R.id.userNameActivityEditText)).setText(userName);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Class-36 Follow up: The call below kicks off the check for an auth user used to determine which buttons to display
        checkForAuthUser();
    }
    public void checkForAuthUser() {
        Amplify.Auth.getCurrentUser(
                success -> {
                    Log.i(TAG, "User authenticated with username: " + success.getUsername());
                    authUser = success;
                    runOnUiThread(this::renderButtons);
                },
                failure -> {
                    Log.i(TAG, "There is no current authenticated user");
                    authUser = null;
                    runOnUiThread(this::renderButtons);
                }
        );
    }
    public void renderButtons() {
        Button loginButton = findViewById(R.id.userSettingsLoginButton);
        if (authUser == null) {
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton = findViewById(R.id.userSettingsLogoutButton);
            logoutButton.setVisibility(View.INVISIBLE);
        } else {
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton = findViewById(R.id.userSettingsLogoutButton);
            logoutButton.setVisibility(View.VISIBLE);
        }
    }

    public void setUpSaveButton(SharedPreferences preferences) {
        Button saveButton = findViewById(R.id.userSettingsSaveButton2);
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

    public void setUpLoginButton() {
        Button loginButton = findViewById(R.id.userSettingsLoginButton);
        loginButton.setOnClickListener(v -> {
            Intent goToLoginActivity = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(goToLoginActivity);
        });
    }

    public void setUpLogoutButton() {
        Button logoutButton = findViewById(R.id.userSettingsLogoutButton);
        logoutButton.setOnClickListener(v -> {
            // Amplify User Logout code block
            AuthSignOutOptions options = AuthSignOutOptions.builder()
                    .globalSignOut(true)
                    .build();

            Amplify.Auth.signOut(options, signOutResult -> {
                if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                    Log.i(TAG, "Global logout successful!");
                } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
                    Log.i(TAG, "Partial logout successful!");
                } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                    Log.i(TAG, "Logout failed: " + signOutResult.toString());
                }
            });
        });
    }
}