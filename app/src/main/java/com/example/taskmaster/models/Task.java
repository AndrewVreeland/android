package com.example.taskmaster.models;

// make a dta class
public class Task {
    private String name;
    private String body;

    private TaskState taskState;

    public Task(String name, String body, TaskState taskState) {
        this.name = name;
        this.body = body;
        this.taskState = taskState;

    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", taskState=" + taskState +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }
}
