/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Introsys
 */
public class PerformanceMasurement
{
  // Singleton specific objects
  private static final Object lock = new Object();
  private static volatile PerformanceMasurement instance = null;
  
  private final List<Long> OrderTillRecipeCallTimers;
  private final List<Long> OrderTillOrderInstanceCreationTimers;
  private final List<Long> RecipeCallMethodTillResultTimers;
  private final List<Long> ChangeStateTillNextRecipeCallTimers;
  private final List<Long> AdapterReadyTillRecipeCallTimers;
  private final List<Long> AgentCreationTillAgentConfirmationTimers;
  private final List<Long> HMISubsystemUpdateTimers;
  private final List<Long> DatabaseQueryTimers;

  public PerformanceMasurement()
  {    
    OrderTillRecipeCallTimers = new ArrayList<>();
    OrderTillOrderInstanceCreationTimers = new ArrayList<>();
    RecipeCallMethodTillResultTimers = new ArrayList<>();
    ChangeStateTillNextRecipeCallTimers = new ArrayList<>();
    AdapterReadyTillRecipeCallTimers = new ArrayList<>();
    AgentCreationTillAgentConfirmationTimers = new ArrayList<>();
    HMISubsystemUpdateTimers = new ArrayList<>();
    DatabaseQueryTimers = new ArrayList<>();
  }



  public List<Long> getOrderTillRecipeCallTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.OrderTillRecipeCallTimers;
  }

  public List<Long> getOrderTillOrderInstanceCreationTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.OrderTillOrderInstanceCreationTimers;
  }

  public List<Long> getRecipeCallMethodTillResultTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.RecipeCallMethodTillResultTimers;
  }

  public List<Long> getChangeStateTillNextRecipeCallTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.ChangeStateTillNextRecipeCallTimers;
  }

  public List<Long> getAdapterReadyTillRecipeCallTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.AdapterReadyTillRecipeCallTimers;
  }

  public List<Long> getAgentCreationTillAgentConfirmationTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.AgentCreationTillAgentConfirmationTimers;
  }

  public List<Long> getHMISubsystemUpdateTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.HMISubsystemUpdateTimers;
  }

  public List<Long> getDatabaseQueryTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.DatabaseQueryTimers;
  }
  

  /**
   * @brief obtain the Device Adapter Clients Manager unique instance
   * @return
   */
  public static PerformanceMasurement getInstance()
  {
    PerformanceMasurement i = instance;
    if (i == null)
    {
      synchronized (lock)
      {
        // While we were waiting for the lock, another 
        i = instance; // thread may have instantiated the object.
        if (i == null)
        {
          i = new PerformanceMasurement();
          instance = i;
        }
      }
    }
    return i;
  }
  
 
}
