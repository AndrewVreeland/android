package com.example.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmaster.models.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insertATask(Task task);

    @Update public void updateTask(Task task);

    @Delete public void deleteATask(Task task);

    @Query("SELECT * FROM Task WHERE id =:id")
    public Task findTaskById(Long id);

    @Query("SELECT * FROM Task")
    List<Task> findAllTasks();

    @Query("SELECT * FROM TASK WHERE type = :type")
    List<Task> findAllTasksByType(Task.TaskTypeEnum type);
}
