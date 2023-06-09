package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.annotations.BelongsTo;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byOwners", fields = {"taskOwnerId","name"})
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField NAME = field("Task", "name");
  public static final QueryField CURRENT_LATITUDE = field("Task", "currentLatitude");
  public static final QueryField CURRENT_LONGITUDE = field("Task", "currentLongitude");
  public static final QueryField DESCRIPTION = field("Task", "description");
  public static final QueryField DATE_CREATED = field("Task", "dateCreated");
  public static final QueryField TASK_CATEGORY = field("Task", "taskCategory");
  public static final QueryField TASK_OWNER = field("Task", "taskOwnerId");
  public static final QueryField S3_KEY = field("Task", "s3Key");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String") String currentLatitude;
  private final @ModelField(targetType="String") String currentLongitude;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime dateCreated;
  private final @ModelField(targetType="TaskCategoryEnum") TaskCategoryEnum taskCategory;
  private final @ModelField(targetType="TaskOwner") @BelongsTo(targetName = "taskOwnerId", targetNames = {"taskOwnerId"}, type = TaskOwner.class) TaskOwner taskOwner;
  private final @ModelField(targetType="String") String s3Key;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public String getCurrentLatitude() {
      return currentLatitude;
  }
  
  public String getCurrentLongitude() {
      return currentLongitude;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Temporal.DateTime getDateCreated() {
      return dateCreated;
  }
  
  public TaskCategoryEnum getTaskCategory() {
      return taskCategory;
  }
  
  public TaskOwner getTaskOwner() {
      return taskOwner;
  }
  
  public String getS3Key() {
      return s3Key;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Task(String id, String name, String currentLatitude, String currentLongitude, String description, Temporal.DateTime dateCreated, TaskCategoryEnum taskCategory, TaskOwner taskOwner, String s3Key) {
    this.id = id;
    this.name = name;
    this.currentLatitude = currentLatitude;
    this.currentLongitude = currentLongitude;
    this.description = description;
    this.dateCreated = dateCreated;
    this.taskCategory = taskCategory;
    this.taskOwner = taskOwner;
    this.s3Key = s3Key;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getName(), task.getName()) &&
              ObjectsCompat.equals(getCurrentLatitude(), task.getCurrentLatitude()) &&
              ObjectsCompat.equals(getCurrentLongitude(), task.getCurrentLongitude()) &&
              ObjectsCompat.equals(getDescription(), task.getDescription()) &&
              ObjectsCompat.equals(getDateCreated(), task.getDateCreated()) &&
              ObjectsCompat.equals(getTaskCategory(), task.getTaskCategory()) &&
              ObjectsCompat.equals(getTaskOwner(), task.getTaskOwner()) &&
              ObjectsCompat.equals(getS3Key(), task.getS3Key()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getCurrentLatitude())
      .append(getCurrentLongitude())
      .append(getDescription())
      .append(getDateCreated())
      .append(getTaskCategory())
      .append(getTaskOwner())
      .append(getS3Key())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("currentLatitude=" + String.valueOf(getCurrentLatitude()) + ", ")
      .append("currentLongitude=" + String.valueOf(getCurrentLongitude()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("dateCreated=" + String.valueOf(getDateCreated()) + ", ")
      .append("taskCategory=" + String.valueOf(getTaskCategory()) + ", ")
      .append("taskOwner=" + String.valueOf(getTaskOwner()) + ", ")
      .append("s3Key=" + String.valueOf(getS3Key()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NameStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      currentLatitude,
      currentLongitude,
      description,
      dateCreated,
      taskCategory,
      taskOwner,
      s3Key);
  }
  public interface NameStep {
    BuildStep name(String name);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep currentLatitude(String currentLatitude);
    BuildStep currentLongitude(String currentLongitude);
    BuildStep description(String description);
    BuildStep dateCreated(Temporal.DateTime dateCreated);
    BuildStep taskCategory(TaskCategoryEnum taskCategory);
    BuildStep taskOwner(TaskOwner taskOwner);
    BuildStep s3Key(String s3Key);
  }
  

  public static class Builder implements NameStep, BuildStep {
    private String id;
    private String name;
    private String currentLatitude;
    private String currentLongitude;
    private String description;
    private Temporal.DateTime dateCreated;
    private TaskCategoryEnum taskCategory;
    private TaskOwner taskOwner;
    private String s3Key;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          name,
          currentLatitude,
          currentLongitude,
          description,
          dateCreated,
          taskCategory,
          taskOwner,
          s3Key);
    }
    
    @Override
     public BuildStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep currentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
        return this;
    }
    
    @Override
     public BuildStep currentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep dateCreated(Temporal.DateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
    
    @Override
     public BuildStep taskCategory(TaskCategoryEnum taskCategory) {
        this.taskCategory = taskCategory;
        return this;
    }
    
    @Override
     public BuildStep taskOwner(TaskOwner taskOwner) {
        this.taskOwner = taskOwner;
        return this;
    }
    
    @Override
     public BuildStep s3Key(String s3Key) {
        this.s3Key = s3Key;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, String currentLatitude, String currentLongitude, String description, Temporal.DateTime dateCreated, TaskCategoryEnum taskCategory, TaskOwner taskOwner, String s3Key) {
      super.id(id);
      super.name(name)
        .currentLatitude(currentLatitude)
        .currentLongitude(currentLongitude)
        .description(description)
        .dateCreated(dateCreated)
        .taskCategory(taskCategory)
        .taskOwner(taskOwner)
        .s3Key(s3Key);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder currentLatitude(String currentLatitude) {
      return (CopyOfBuilder) super.currentLatitude(currentLatitude);
    }
    
    @Override
     public CopyOfBuilder currentLongitude(String currentLongitude) {
      return (CopyOfBuilder) super.currentLongitude(currentLongitude);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder dateCreated(Temporal.DateTime dateCreated) {
      return (CopyOfBuilder) super.dateCreated(dateCreated);
    }
    
    @Override
     public CopyOfBuilder taskCategory(TaskCategoryEnum taskCategory) {
      return (CopyOfBuilder) super.taskCategory(taskCategory);
    }
    
    @Override
     public CopyOfBuilder taskOwner(TaskOwner taskOwner) {
      return (CopyOfBuilder) super.taskOwner(taskOwner);
    }
    
    @Override
     public CopyOfBuilder s3Key(String s3Key) {
      return (CopyOfBuilder) super.s3Key(s3Key);
    }
  }
  
}
