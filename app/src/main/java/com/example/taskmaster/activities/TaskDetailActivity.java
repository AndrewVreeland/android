package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.taskmaster.MainActivity;
import com.example.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail2);
        setUpTaskTitleName();
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


}