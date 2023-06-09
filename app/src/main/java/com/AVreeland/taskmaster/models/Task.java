package com.AVreeland.taskmaster.models;




import java.util.Date;

// make a dta class

public class Task  {

    private String name;
    private String body;
    private final java.util.Date dateCreated;
   private TaskTypeEnum type;


    public Task(String name, String body, Date dateCreated, TaskTypeEnum type) {
        this.name = name;
        this.body = body;
        this.dateCreated = dateCreated;
        this.type = TaskTypeEnum.NEW;
    }

    public enum TaskTypeEnum {
        NEW("New"),
        ASSIGNED("Assigned"),
        IN_PROGRESS("In Progress"),
        COMPLETE("Complete");

        private final String taskType;

        TaskTypeEnum(String taskType) {
            this.taskType = taskType;
        }

        public static TaskTypeEnum fromString(String possibleTaskType) {
            for (TaskTypeEnum type : TaskTypeEnum.values()) {
                if (type.taskType.equals(possibleTaskType)) {
                    return type;
                }
            }
            return null;
        }
        @Override
        public String toString() {
            return "TaskTypeEnum{" +
                    "taskType='" + taskType + '\'' +
                    '}';
        }

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public TaskTypeEnum getType() {
        return type;
    }

    public void setType(TaskTypeEnum type) {
        this.type = type;
    }
}






