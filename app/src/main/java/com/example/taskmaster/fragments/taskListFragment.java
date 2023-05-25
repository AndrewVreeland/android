package com.example.taskmaster.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmaster.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link taskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


// step 1.6 create a fragment for RecyclerView viewHolders and update ui xml file
public class taskListFragment extends Fragment {






    public taskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment taskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static taskListFragment newInstance(String param1, String param2) {
        taskListFragment fragment = new taskListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list2, container, false);
    }
}