package com.AVreeland.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.AVreeland.taskmaster.MainActivity;
import com.AVreeland.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.predictions.models.LanguageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskDetailActivity extends AppCompatActivity {
private String taskId;
public final String TAG = "TaskDetailActivity";
    private final MediaPlayer mp = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail2);
        setUpTaskTitleName();
        setUpTaskBodyContent();
        translateText();
        setupAudioButton();



    }

    public void translateText(){
        Intent callingIntent = getIntent();
        String taskBodyContent = null;

        if(callingIntent != null){
            taskBodyContent = callingIntent.getStringExtra(MainActivity.USER_TASK_BODY_TAG);
        }

        TextView taskBodyTranslatedTextView = (TextView) findViewById(R.id.task_detail_translated_text);

        if(taskBodyContent != null){
            Amplify.Predictions.translateText(
                    taskBodyContent, LanguageType.ENGLISH, LanguageType.RUSSIAN,
                    result -> taskBodyTranslatedTextView.setText(result.getTranslatedText()),
                    error -> Log.e("MyAmplifyApp", "Translation failed", error)
            );

        } else{
            taskBodyTranslatedTextView.setText(R.string.no_body);
        }
    }



    public Void setupAudioButton(){
        Button audioButton = findViewById(R.id.taskFormActivityAudioButton);
        audioButton.setOnClickListener(v ->{
            String taskName;
            taskName = String.valueOf((TextView)findViewById(R.id.task_detail_task_body));
            Amplify.Predictions.convertTextToSpeech(
                    taskName,
                    success -> playAudio(success.getAudioData()),
                    failure -> Log.e(TAG, "Audio conversion of task, " + taskName + ", failed", failure)
            );
        });
        return null;
    }

        private void playAudio(InputStream data){
            File mp3File = new File(getCacheDir(), "audio.mp3");

            try(OutputStream out = new FileOutputStream(mp3File)){
                byte[] buffer = new byte[8* 1024];
                int bytesRead;
                while((bytesRead = data.read(buffer)) != -1){
                    out.write(buffer, 0, bytesRead);
                }
                mp.reset();
                mp.setOnPreparedListener(MediaPlayer::start);
                mp.setDataSource(new FileInputStream(mp3File).getFD());
                mp.prepareAsync();

            }catch(IOException error){
                Log.e(TAG, "Error writing audio file");
            }
        }

    public void setUpTaskTitleName(){
        Intent callingIntent = getIntent();
        String taskTitleName = null;

        if(callingIntent != null){
            taskTitleName = callingIntent.getStringExtra(MainActivity.USER_TASK_TAG);
        }

        TextView taskTitleNameTextView = (TextView) findViewById(R.id.task_detail_task_name);

        if(taskTitleName != null){
            taskTitleNameTextView.setText(taskTitleName);
        } else{
            taskTitleNameTextView.setText(R.string.no_task_name);
        }
    }
    public void setUpTaskBodyContent(){
        Intent callingIntent = getIntent();
        String taskBodyContent = null;


        if(callingIntent != null){
            taskBodyContent = callingIntent.getStringExtra(MainActivity.USER_TASK_BODY_TAG);
        }

        TextView taskBodyTextView = (TextView) findViewById(R.id.task_detail_task_body);

        if(taskBodyContent != null){

            taskBodyTextView.setText(taskBodyContent);
        } else{
            taskBodyTextView.setText(R.string.no_body);
        }
    }

}