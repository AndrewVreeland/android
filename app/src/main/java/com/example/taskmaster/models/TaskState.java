package com.example.taskmaster.models;

public enum TaskState {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("complete");

    private String assigned;
    TaskState(String assigned) {
        this.assigned = assigned;
    }

}
