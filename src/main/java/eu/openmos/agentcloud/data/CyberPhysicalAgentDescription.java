/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

// import eu.openmos.agentcloud.data.recipe.Equipment;
// import eu.openmos.agentcloud.data.recipe.ExecutionTable;
//import eu.openmos.agentcloud.data.recipe.Skill;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import eu.openmos.model.*;

/**
 * Describes the agent that arrives to the cloud from the Manufacturing Service Bus to be created.
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class CyberPhysicalAgentDescription extends SubSystem
{

  public static CyberPhysicalAgentDescription fromString(String object) throws ParseException
  {
//        return (CyberPhysicalAgentDescription)SubSystem.fromString(object);
    SubSystem da = SubSystem.fromString(object);
    return new CyberPhysicalAgentDescription(
            da.getName(), da.getExecutionTable(),
            da.getSkills(), da.getRecipes(), da.getEquipments(),
            da.getPhysicalLocation(), da.getLogicalLocation(), da.getType(), da.getRegisteredTimestamp());
  }

  public CyberPhysicalAgentDescription()
  {
    super();
  }

  public CyberPhysicalAgentDescription(String equipmentId,
          ExecutionTable executionTable,
          List<Skill> skills,
          List<Recipe> recipes,
          List<Equipment> equipments,
          PhysicalLocation physicalLocation,
          LogicalLocation logicalLocation,
          String type,
          Date registeredTimestamp)
  {

    super(equipmentId, executionTable, skills, recipes, equipments, physicalLocation,
            logicalLocation, type, registeredTimestamp
    );
  }

  public String getEquipmentId()
  {
    return super.getName();
  }

  public void setEquipmentId(String equipmentId)
  {
    super.setName(equipmentId);
  }

}
