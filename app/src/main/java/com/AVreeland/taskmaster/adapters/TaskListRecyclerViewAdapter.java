package com.AVreeland.taskmaster.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.AVreeland.taskmaster.MainActivity;
import com.AVreeland.taskmaster.R;
import com.AVreeland.taskmaster.activities.TaskDetailActivity;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

// step 1.4 sole purpose is to manage recyclerview (a recycler view adapter)

// 3.1 clean up recyclerviewadapter to actually use taskListRecyvlerViewAdapter
public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder>  {
    public static final String TAG = "TaskListRecyclerViewAdapter";

    // 2.3 at top of class add list of data items as a field
    private final List<Task> tasks;
    // 3.2 cont. add a calling activity context as field
    Context callingActivity;

    // 2-3 edit the recyclerview adapter to handle list of data items
    // 3-2 cont. change hte recyclerviewadapter's consturctor to take in a calling acticity context
    public TaskListRecyclerViewAdapter(List<Task> tasks, Context callingActivity){
        this.tasks = tasks;
        this.callingActivity = callingActivity;

    }


    // 1.8 make a viewholder class to hold a fragment
public static class TaskListViewHolder extends RecyclerView.ViewHolder {
    public TaskListViewHolder(View fragmentItemView){
        super(fragmentItemView);
    }
}


// 3.1 continued change on create view holder for the adapter
    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1.7 inflate fragment
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list2, parent,false);
        // 1.9 attatch the fragment to the view holder

        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        // 2.4 Bind the data items to the fragments
        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.task_fragment);

        DateFormat dateCreatedIso8601InputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        dateCreatedIso8601InputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        @SuppressLint("SimpleDateFormat") DateFormat dateCreatedOutputFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateCreatedOutputFormat.setTimeZone(TimeZone.getDefault());
        String dateCreatedCreateString = "";
        try{
            Date dateCreatedJavaDate = dateCreatedIso8601InputFormat.parse(tasks.get(position).getDateCreated().format());
            if(dateCreatedJavaDate != null){
                dateCreatedCreateString = dateCreatedOutputFormat.format(dateCreatedJavaDate);
            }
        } catch (ParseException e) {
            Log.e(TAG, "failed to parse date" + e);
            e.printStackTrace();
        }

        String taskName = tasks.get(position).getName();
        String taskBody = tasks.get(position).getDescription();
        String fragmentText = position + ". " + taskName + " Date Created: " + dateCreatedCreateString;
        taskFragmentTextView.setText(fragmentText);

        // 3-3: create onclick listener make an intent insidie it and call intect with an extra to go to details page

//        View taskViewHolder = holder.itemView;  NOT NEEDED
        taskFragmentTextView.setOnClickListener(v -> {
            Intent goToTaskDetailsIntent= new Intent (callingActivity, TaskDetailActivity.class);
            goToTaskDetailsIntent.putExtra(MainActivity.USER_TASK_TAG, taskName);
            goToTaskDetailsIntent.putExtra(MainActivity.USER_TASK_BODY_TAG, taskBody);
            callingActivity.startActivity(goToTaskDetailsIntent);
        });
    }

    @Override
    public int getItemCount() {
    // step 1.10 determine size of data set for testing use a big value
//        return 100;

        // 2-5
        return tasks.size();
    }



}
