package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import eu.openmos.model.utilities.ListsToString;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Describes workstation and transport:
 
 into the cloud, the agent that arrives to the cloud from the Manufacturing
 Service Bus to be created (the old SubSystem class);
 
 into the MSB, the device adapter information (the old RegFile class)
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *      
 */
@XmlRootElement(name = "deviceAdapter")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubSystem extends Equipment implements Serializable {    
    private static final Logger logger = Logger.getLogger(SubSystem.class.getName());
    private static final long serialVersionUID = 6529685098267757698L;

    /**
     * Agent execution table.
    */
    private ExecutionTable executionTable;
    
    /**
     * The recipes the agent can apply (Resource/Transport Agent).
     */
    @XmlElement(name = "recipes")
    private List<Recipe> recipes;

    /**
     * The Physical Location of the device abstracted by this agent.
     */
    @XmlElement(name = "physicalLocation")
    private PhysicalLocation physicalLocation;

    /**
     * The Logical location of the agent.
     * TBV if we need it or not
     */
    @XmlElement(name = "logicalLocation")
    private LogicalLocation logicalLocation;

    /**
     * Agent's type.
     * TBV if we need it or not
     * 
     * TODO: check the value if "resource", "transport", "df_resource..." 
     */
    @XmlElement(name = "type")    
    private String type;
    
    /**
     * List of internal modules.
     */
    protected List<Module> internalModules;

    public List<Module> getInternalModules() {
        return internalModules;
    }

    public void setInternaleModules(List<Module> internalModules) {
        this.internalModules = internalModules;
    }
    
//    private static final int FIELDS_COUNT = 15;

    /**
     * Default constructor, for reflection purpose.
     */
    public SubSystem() { super(); }

    /**
     * Parameterized constructor.
     * 
   * @param uniqueId
   * @param name
   * @param description
   * @param executionTable
   * @param connected
     * @param equipmentId - the equipment id, aka the agent unique identifier
     * @param skills - The skills the agent is capable of performing (Resource/
     * Transport Agent).
   * @param internalModules
   * @param address
   * @param status
   * @param manufacturer
     * @param recipes - The recipes the agent can apply (Resource/Transport Agent).
     * @param equipments - The inner equipment in case of workstation.
     * @param physicalLocation - The Physical Location of the device abstracted 
     * by this agent.
     * @param logicalLocation - The skill requirements of the agent (Product Agent).
     * @param agentClass - Agent's class - TBV if necessary.
     * @param type - Agent's type - TBV if necessary.
     * @param registeredTimestamp - the agent creation time
     */
//    public SubSystem(String name, 
//            ExecutionTable executionTable, 
//            List<Skill> skills, 
//            List<Recipe> recipes, 
//            List<Equipment> equipments, 
//            PhysicalLocation physicalLocation, 
//            LogicalLocation logicalLocation, 
////            String agentClass, 
//            String type,
//            Date registered
//            ) {
////        this.equipmentId = equipmentId;
//        this.name = name;
//        this.registered = registered;
//        this.executionTable = executionTable;
//        this.skills = skills;
//        this.recipes = recipes;
//        this.equipments = equipments;
//        this.physicalLocation = physicalLocation;
//        this.logicalLocation = logicalLocation;
////        this.agentClass = agentClass;
//        this.type = type;
//    }
    public SubSystem(
            String uniqueId, 
            String name, 
            String description, 
            ExecutionTable executionTable,
            boolean connected,
            List<Skill> skills,
            List<Recipe> recipes, 
            List<Module> internalModules,
            String address,
            String status,
            String manufacturer,
            PhysicalLocation physicalLocation, 
            LogicalLocation logicalLocation, 
            String type,
            Date registeredTimestamp
    ) {
        super(uniqueId, name, description, connected, skills, 
                address, status, manufacturer, registeredTimestamp);

        this.executionTable = executionTable;
        this.recipes = recipes;
        this.physicalLocation = physicalLocation;
        this.logicalLocation = logicalLocation;
        this.type = type;
    }

    public ExecutionTable getExecutionTable() {
        return executionTable;
    }

    public void setExecutionTable(ExecutionTable executionTable) {
        this.executionTable = executionTable;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public PhysicalLocation getPhysicalLocation() {
        return physicalLocation;
    }

    public void setPhysicalLocation(PhysicalLocation physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    public LogicalLocation getLogicalLocation() {
        return logicalLocation;
    }

    public void setLogicalLocation(LogicalLocation logicalLocation) {
        this.logicalLocation = logicalLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * equipment id,
     * execution table,
     * skills,
     * recipes,
     * equipments,
     * physical location,
     * logical location,
     * agent java class,
     * agent type,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    public String toString_OLD() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(description);
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(executionTable.toString());
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(connected);
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(ListsToString.writeSkills(skills));
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(ListsToString.writeRecipes(recipes));
//
////        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
////        builder.append(ListsToString.writeInternalModuleIds(internalModuleIds));
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(address);
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(status);
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(manufacturer);
//
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(physicalLocation.toString());
//        
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(logicalLocation.toString());
//        
////        builder.append(Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
////        builder.append(agentClass);
////        
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(type);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(stringRegisteredTimestamp);
//        
//        return builder.toString();
//    }
    
//    @Override
//    public String toString() {
//        String outString = null;
//    
//        try {
//         ByteArrayOutputStream bOut = new ByteArrayOutputStream();         
//         ObjectOutputStream out = new ObjectOutputStream(bOut);
//         out.writeObject(this);
//         out.close();
//         bOut.close();
//         outString = bOut.toString();
//      }catch(IOException i) {
//         i.printStackTrace();
//      }     
//        return outString;
//    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * equipment id,
     * execution table,
     * skills,
     * recipes,
     * equipments,
     * physical location,
     * logical location,
     * agent java class,
     * agent type,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static SubSystem fromString_OLD(String object) throws ParseException {        
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("CyberPhysicalAgentDescription - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        String uniqueId = tokenizer.nextToken();
//        String name = tokenizer.nextToken();
//        String description = tokenizer.nextToken();
//        ExecutionTable executionTable = ExecutionTable.fromString(tokenizer.nextToken());              // execution table
//        boolean connected = Boolean.valueOf(tokenizer.nextToken());
//        List<Skill> skills = StringToLists.readSkills(tokenizer.nextToken());
//        List<Recipe> recipes = StringToLists.readRecipes(tokenizer.nextToken());
//        List<String> internalModuleIds = StringToLists.readInternalModuleIds(tokenizer.nextToken());
//        String address = tokenizer.nextToken();
//        String status = tokenizer.nextToken();
//        String manufacturer = tokenizer.nextToken();
//        PhysicalLocation physicalLocation = PhysicalLocation.fromString(tokenizer.nextToken());
//        LogicalLocation logicalLocation = LogicalLocation.fromString(tokenizer.nextToken());
//        String type = tokenizer.nextToken();
//        Date registered = sdf.parse(tokenizer.nextToken());
//        
//        return new SubSystem(
//             uniqueId, 
//             name, 
//             description, 
//             executionTable,
//             connected,
//             skills,
//             recipes, 
//             internalModuleIds,
//             address,
//             status,
//             manufacturer,
//             physicalLocation, 
//             logicalLocation, 
////            String agentClass, 
//             type,
//             registered
//        );
//    }

//    public static SubSystem fromString(String object) throws ParseException {        
//        SubSystem ss = null;
//      try {
//         ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//         ObjectInputStream in = new ObjectInputStream(bIn);
//         ss = (SubSystem) in.readObject();
//         in.close();
//         bIn.close();
//         return ss;
//      }catch(IOException i) {
//         i.printStackTrace();
//         return null;
//      }catch(ClassNotFoundException c) {
//         System.out.println("SubSystem class not found");
//         c.printStackTrace();
//         return null;
//      }
//    }
    
    /**
     * To check if the object is valid.... for now it returns always "true"
     * Used into the optimizeragent.
     * 
     * @return always true
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 equipment id,
 execution table,
 skills,
 recipes,
 equipments,
 physical location,
 logical location,
 agent java class,
 agent type,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();

        List<String> skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());        
        List<String> recipeIds = recipes.stream().map(recipe -> recipe.getUniqueId()).collect(Collectors.toList());
        List<String> internalModuleIds = skills.stream().map(module -> module.getUniqueId()).collect(Collectors.toList());        
        
        doc.append("id", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("executionTableId", executionTable.getUniqueId());
        doc.append("connected", connected);
        doc.append("skillIds", skillIds);
        doc.append("recipeIds", recipeIds);
        doc.append("internalModuleIds", internalModuleIds);  
        doc.append("address", address);
        doc.append("status", status);
        doc.append("manufacturer", manufacturer);
        doc.append("physicalLocation", physicalLocation.toBSON());
        doc.append("logicalLocation", logicalLocation.toBSON());
        doc.append("type", type);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
                
        return doc;
    }
    
//    public static SubSystem getTestObject() {
//
//        SubSystem cpad;
//
//        String equipmentId = "CyberPhysicalAgentDescriptionEquipmentId";
////        String productId = "productId";
////        String modelId = "modelId";
////        String recipeId = "recipeId";        
//        String agentClass = "CyberPhysicalAgentDescriptionAgentClass";
//        String type = "CyberPhysicalAgentDescriptionType";
//        
//        Date registered = new Date();
//        
////        List<String> parameters = new LinkedList<>(Arrays.asList("parameters"));
//
//        List<Parameter> skillParameters = ParameterTest.getTestList();
//        List<KPISetting> kpiSettings = KPISettingTest.getTestList();
//        List<ParameterSetting> parameterSettings = ParameterSettingTest.getTestList();
//        List<SkillRequirement> skillRequirements = SkillRequirementTest.getTestList();
//        List<Recipe> recipes = RecipeTest.getTestList();
//        List<KPI> kpis = KPITest.getTestList();
//        List<Skill> skills = SkillTest.getTestList();
//        
//        PhysicalLocation physicalLocation = PhysicalLocationTest.getTestObject();
//        LogicalLocation logicalLocation = LogicalLocationTest.getTestObject();
//        
//        List<Equipment> equipments = EquipmentTest.getTestList(equipmentId);
//        ExecutionTable executionTable = ExecutionTableTest.getTestObject(equipmentId, 6);
//
//        cpad = new CyberPhysicalAgentDescription(
//            equipmentId, 
//            executionTable, 
//            skills, 
//            recipes, 
//            equipments, 
//            physicalLocation, 
//            logicalLocation, 
//            //agentClass, 
//            type,
//            registered
//            );
//        return cpad;        
//    }    
    

//    public static SubSystem deserialize(String object) 
//    {        
//        SubSystem objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (SubSystem) in.readObject();
//            in.close();
//            bIn.close();
//        }
//        catch (IOException i) 
//        {
//            logger.error(i);
//        }
//        catch (ClassNotFoundException c) 
//        {
//            logger.error(c);
//        }
//        return objectToReturn;
//    }
//    public static SubSystem deserialize(byte[] objectBytes) 
//    {        
//        SubSystem objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(objectBytes);
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (SubSystem) in.readObject();
//            in.close();
//            bIn.close();
//        }
//        catch (IOException i) 
//        {
//            logger.error(i);
//        }
//        catch (ClassNotFoundException c) 
//        {
//            logger.error(c);
//        }
//        return objectToReturn;
//    }

//    public String serialize() throws IOException {
//        String outString = null;
//    
//        try 
//        {
//            ByteArrayOutputStream bOut = new ByteArrayOutputStream();         
//            ObjectOutputStream out = new ObjectOutputStream(bOut);
//            out.writeObject(this);
//            out.close();
//            bOut.close();
//            outString = bOut.toString();
//            return outString;
//        }
//        catch(IOException i) 
//        {
//            logger.error(i);
//            throw i;
//        }     
//    }
//    
//    public String toString()
//    {
//        try
//        {
//            return serialize();
//        }
//        catch (IOException e)
//        {
//            logger.equals(e);
//            return "Object serialization really not possible, please check error log";
//        }
//    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SubSystem = " + uniqueId).append("\n");
        builder.append(name).append("\n");
        builder.append(description).append("\n");
        builder.append(executionTable.toString()).append("\n");
        builder.append(connected).append("\n");
        builder.append(ListsToString.writeSkills(skills)).append("\n");
        builder.append(ListsToString.writeRecipes(recipes)).append("\n");
        builder.append(address).append("\n");
        builder.append(status).append("\n");
        builder.append(manufacturer).append("\n");
        builder.append(physicalLocation.toString()).append("\n");
        builder.append(logicalLocation.toString()).append("\n");
        builder.append(type).append("\n");
        builder.append(registered).append("\n");
        
        return builder.toString();
    }
}
