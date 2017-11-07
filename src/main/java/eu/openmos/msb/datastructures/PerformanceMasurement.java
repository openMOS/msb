/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private final List<Long> NamespaceParsing;
  
  private final Map<String, Long> agentCreationTimers;
  private final Map<String, Long> agentRemovalTimers;
   
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
    NamespaceParsing = new ArrayList<>();
    agentCreationTimers = new HashMap<>();
    agentRemovalTimers = new HashMap<>();
    
  }
  
    public Map<String, Long> getAgentRemovalTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.agentRemovalTimers;
  }

  public Map<String, Long> getAgentCreationTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.agentCreationTimers;
  }
  
  public List<Long> getNameSpaceParsingTimers()
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();
    return aux.NamespaceParsing;
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
  
  public void exportTimers() throws IOException
  {
    PerformanceMasurement aux = PerformanceMasurement.getInstance();

    try (FileWriter writer = new FileWriter("outputMSBPerformanceTimers.txt"))
    {
      writer.write("\n");
      writer.write("AdapterReadyTillRecipeCall: ");
      for (Long time : aux.getAdapterReadyTillRecipeCallTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("AgentCreationTillAgentConfirmation: ");
      for (Long time : aux.getAgentCreationTillAgentConfirmationTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("ChangeStateTillNextRecipeCall: ");
      for (Long time : aux.getChangeStateTillNextRecipeCallTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("DatabaseQuery: ");
      for (Long time : aux.getDatabaseQueryTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("HMISubsystemUpdate: ");
      for (Long time : aux.getHMISubsystemUpdateTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("NameSpaceParsing: ");
      for (Long time : aux.getNameSpaceParsingTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("OrderTillOrderInstanceCreation: ");
      for (Long time : aux.getOrderTillOrderInstanceCreationTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("OrderTillRecipeCall: ");
      for (Long time : aux.getOrderTillRecipeCallTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");
      writer.write("RecipeCallMethodTillResult: ");
      for (Long time : aux.getRecipeCallMethodTillResultTimers())
      {
        writer.write(time.toString());
        writer.write("ms ");
      }
      writer.write("\n");

    }
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
