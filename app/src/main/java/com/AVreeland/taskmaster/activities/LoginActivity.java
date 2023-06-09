package com.AVreeland.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.AVreeland.taskmaster.MainActivity;
import com.AVreeland.taskmaster.R;
import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginAcivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpLoginButton();
        setUpSignUpButton();
    }

    public void setUpLoginButton() {
        Intent callingIntent = getIntent();
        String userEmail = callingIntent.getStringExtra(VerifyAccountsActivity.VERIFICATION_EMAIL_TAG);
        EditText userEmailEditText = findViewById(R.id.loginActivityUsernameEditText);
        if(userEmail != null) {
            userEmailEditText.setText(userEmail);
        }
        EditText userPasswordEditText = findViewById(R.id.loginActivityPasswordEditText);
        Button loginButton = findViewById(R.id.loginActivityLoginButton2);

        loginButton.setOnClickListener(v -> {
            // Amplify User Login code block
            Amplify.Auth.signIn(userEmailEditText.getText().toString(),
                    userPasswordEditText.getText().toString(),
                    success -> {
                        Log.i(TAG, "Login succeeded: " + success.toString());
                        Intent goToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(goToMainActivity);
                    },
                    failure -> {
                        Log.i(TAG, "Login failed: " + failure.toString());
                    });
        });
    }

    public void setUpSignUpButton() {
        Button signUpButton = findViewById(R.id.loginActivitySignUpButton);

        signUpButton.setOnClickListener(v -> {
            Intent goToSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(goToSignUpActivity);
        });
    }
}