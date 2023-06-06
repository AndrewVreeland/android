package com.AVreeland.taskmaster.model;

import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.util.Immutable;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelProvider;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/**
 *  Contains the set of model classes that implement {@link Model}
 * interface.
 */

public final class AmplifyModelProvider implements ModelProvider {
  private static final String AMPLIFY_MODEL_VERSION = "6ad23f8165a9537f1e8e17ac879f7b54";
  private static AmplifyModelProvider amplifyGeneratedModelInstance;
  private AmplifyModelProvider() {
    
  }
  
  public static AmplifyModelProvider getInstance() {
    if (amplifyGeneratedModelInstance == null) {
      amplifyGeneratedModelInstance = new AmplifyModelProvider();
    }
    return amplifyGeneratedModelInstance;
  }
  
  /**
   * Get a set of the model classes.
   *
   * @return a set of the model classes.
   */
  @Override
   public Set<Class<? extends Model>> models() {
    final Set<Class<? extends Model>> modifiableSet = new HashSet<>(
            Collections.<Class<? extends Model>>singletonList(Task.class)
        );
    
        return Immutable.of(modifiableSet);
        
  }
  
  /**
   * Get the version of the models.
   *
   * @return the version string of the models.
   */
  @Override
   public String version() {
    return AMPLIFY_MODEL_VERSION;
  }
}
