/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.milo.server;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import io.vertx.core.Vertx;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.agentcloud.data.LogicalLocation;
import eu.openmos.agentcloud.data.PhysicalLocation;
import eu.openmos.model.KPI;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Parameter;
import eu.openmos.model.ParameterSetting;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.Equipment;
import eu.openmos.model.SubSystem;
import eu.openmos.msb.cloud.cloudinterface.testdata.CyberPhysicalAgentDescriptionTest;
import eu.openmos.msb.cloudinterface.WebSocketsReceiver;
import eu.openmos.msb.cloudinterface.WebSocketsSender;
import eu.openmos.msb.cloudinterface.WebSocketsSenderDraft;
import eu.openmos.msb.messages.ChangedState;
import eu.openmos.msb.messages.RegFile;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.starter.MSB_gui;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.messages.DaRecipe;
import eu.openmos.msb.messages.DaSkill;
import eu.openmos.agentcloud.cloudinterface.ServiceCallStatus;
import javax.xml.transform.TransformerException;



/**
 * @deprecated 
 * @author fabio.miranda
 */
public class OPCDeviceHelper extends Observable
{

  Vertx vertx = Vertx.vertx();
  Boolean withAGENTCloud = false;
  Boolean fromFile = true;

  /**
   *
   * @param Function
   * @param args
   * @return
   * @deprecated This will no longer be used
   */
  public String allCases(String Function, String args)
  {
    String productID;
    String WorkstationID;
    String EntityID;
    String ProductID;
    String State = "";
    String Recipe;
    switch (Function.toLowerCase())
    {
      case "deviceregistration":
        // from workstation to msb. from msb to equipment module, deployment agent and data listener
        // params: ID, EquipmentType, SelfDescriptionServiceCall
        // active for the 1st demonstrator
        return workStationRegistration(args);

      case "deployrecipe":
        //from resource agent to msb. from msb to workstation. 
        //param: workstation ID, recipe  
        WorkstationID = (String) args; //args will be a list of xml elements. use XML parser
        Recipe = (String) args; //args will be a list of xml elements. use XML parser
        deployRecipe(WorkstationID, Recipe);
        break;

      case "updaterecipe":
        //from workstation to msb. from msb to datalistener
        //param: workstation ID, OldRecipeID, NewRecipe 
        EntityID = (String) args; //args will be a list of xml elements. use XML parser
        String OldRecipeID = (String) args; //args will be a list of xml elements. use XML parser
        String NewRecipe = (String) args; //args will be a list of xml elements. use XML parser
        //UpdateRecipe(WorkstationID,OldRecipeID,NewRecipe);
        break;

      case "executereciperampupmode":
        //from workstation to msb. from msb to datalistener
        //param: workstation ID, OldRecipeID, NewRecipe 
        WorkstationID = (String) args; //args will be a list of xml elements. use XML parser
        String RecipeID = (String) args; //args will be a list of xml elements. use XML parser
        executeRecipeRampUpMode(WorkstationID, RecipeID);
        break;

      case "uploadexecutiondata":
        //from workstation to msb. from msb to datalistener
        //param: Module ID, Data 
        String ModuleID = args; //args will be a list of xml elements. use XML parser
        String Data = (String) args; //args will be a list of xml elements. use XML parser
        uploadExecutionData(ModuleID, Data);
        break;

      case "statusupdate":
        //from workstation to msb. from msb to datalistener
        //param: State   
        //args will be a list of xml elements. use XML parser
        State = (String) args;
        return statusUpdate(State);

      case "changerecipestate":
        //from workstation to msb. from msb to datalistener
        //param: WorkstationID,RecipeID, State   
        WorkstationID = args;
        RecipeID = (String) args;
        State = (String) args; //args will be a list of xml elements. use XML parser
        changeRecipeState(WorkstationID, RecipeID, State);
        break;

      case "requestproduct":
        //from workstation to msb. from msb to datalistener
        //param: ProductID  
        ProductID = args;
        requestProduct(ProductID);
        break;

      case "deploynewrecipe":
        //from workstation to msb. from msb to datalistener
        //param: EntityID, Recipe, ProductID  
        EntityID = args;
        Recipe = (String) args;
        ProductID = args;
        deployNewRecipe(EntityID, Recipe, ProductID);
        break;

      case "requestmoduledetails":
        //from resource agents to msb. from msb to workstation. from datalistener to msb. from msb to workstation
        requestModuleDetails();
        break;

      case "selfdescription":
        //from workstation to msb. from msb to datalistener
        productID = (String) args; //args will be a list of xml elements. use XML parser
        break;

      case "requestworkstationinformation":
        //from skill agreggator to msb. from msb to workstation
        // TODO
        break;

      case "provideselfdescription":
        //from workstation to msb. from msb to skill agreggator
        // TODO
        //enviar requestworkstationinformation para workstation e esperar ProvideSelfDescription
        break;

      case "executeskill":
        ProductID = args;
        //args will be a list of xml elements. use XML parser
        // TODO
        break;

      case "changedstate":
        return changedState(args);

      case "recipeexecutiondone":
        //active for the 1st demonstrator
        return recipeExecutionDone(args);

      case "executetransportskill":
        // TODO
        break;

      case "productcreated":
        // TODO
        break;

      case "resquestlocalModulesregistration":
        //from workstation to msb. from msb to equipment module
        // TODO
        break;

      case "broadcastpresence":
        //from workstation to msb. from msb to equipment module, deployment agent and data listener
        //params: ID, EquipmentType, SelfDescriptionServiceCall
        String ID = args;
        String EquipmentType = (String) args;
        String SelfDescriptionServiceCall = (String) args;
        broadcastPresence(ID, EquipmentType, SelfDescriptionServiceCall);
        break;

      case "sendRecipe":
        //active for the 1st demonstrator
        return sendRecipe(args);

      default:
        return "Function not found!";
    }
    return null;
  } // end

  /**
   *
   * @param args
   * @return
   * @deprecated 
   */
  private String workStationRegistration(String args)
  {
    // global instance for managing device adapterss
    DACManager instance = DACManager.getInstance();

    // for performance monitoring
    System.out.println("\n\n WorkStation Registration message has arrived \n\n");
    long startTime = System.nanoTime();

    //parse xml SelfDescriptionInformation
    String senderName = args.split("\\:")[0]; //get sender name
    String Alldata = args.substring(args.indexOf(':') + 1); //get XML data
    RegFile parsedClass = null;

    try
    {
      if (fromFile)
      {
        // simulated that the device has sent the regfile
        // this overwrites the regfile send from th DA
        parsedClass = xmlMsgToObject("RegFile.xml");
      } else
      {
        // Pass the XML file (RegFile) to java classes
        DocumentBuilderFactory dfctr = DocumentBuilderFactory.newInstance();
        DocumentBuilder bldr = null;
        bldr = dfctr.newDocumentBuilder();
        InputSource insrc = new InputSource();
        insrc.setCharacterStream(new StringReader(Alldata));
        org.w3c.dom.Document docres = null;
        docres = bldr.parse(insrc);
        JAXBContext jaxbContext = JAXBContext.newInstance(RegFile.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        parsedClass = (RegFile) unmarshaller.unmarshal(docres);
      }

      // --------------------------------------------------------------------------------------------------------------
      DeviceAdapter da = instance.getDeviceAdapter(senderName);

      if (parsedClass.getDevicesTable().size() > 0)
      {
        for (String key : parsedClass.getDevicesTable().keySet())
        {
          Equipment device = (Equipment) parsedClass.getDevicesTable().get(key);
          da.addEquipmentModule(device);
        }
        MSB_gui.fillDevicesTable();
      }

      if (parsedClass.getSkillsTable().size() > 0)
      {
        for (String key : parsedClass.getSkillsTable().keySet())
        {
          DaSkill skill = parsedClass.getSkillsTable().get(key);
          instance.registerSkill(senderName, skill.getAmlId(), skill.getName(), skill.getDescription());
        }
      }

      if (parsedClass.getRecipesTable().size() > 0)
      {
        for (String key : parsedClass.getRecipesTable().keySet())
        {
          DaRecipe r = parsedClass.getRecipesTable().get(key);
          instance.registerRecipe(senderName, r.getAmlId(), r.getSkill(), r.getValid(), r.getName());
        }
        MSB_gui.fillRecipesTable();
      }

      // --------------------------------------------------------------------------------------------------------------
      // create new agent
      if (withAGENTCloud)
      {
        // THIS CODE IS WORKING!! 
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
        SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
        // SubSystem cpad = dummySubSystemGeneration(parsedClass);
        CyberPhysicalAgentDescription cpad = CyberPhysicalAgentDescriptionTest.getTestObject();

        ServiceCallStatus agentStatus = systemConfigurator.createNewResourceAgent(cpad);
        System.out.println("\n\n Creating Resource or Transport Agent... \n\n");
        String msgToSend = Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA + "anything";
        //Vertx.vertx().deployVerticle(new WebSocketsSender(cpad.getUniqueName())); // TODO - DELETE THIS

        //add the sender client object to the respective agentID
        da.setSubSystem(cpad);
        return agentStatus.getCode(); //OK? ou KO?

      } else
      {
        //call mainwindow filltables
        MSB_gui.fillRecipesTable();
        vertx.deployVerticle(new WebSocketsReceiver("R1"));
        vertx.deployVerticle(new WebSocketsReceiver("R2"));

        Thread.sleep(3000);

        vertx.deployVerticle(new WebSocketsSenderDraft());

        return "OK - No AgentPlatform";

      }
    } catch (SAXException | IOException | ParserConfigurationException | JAXBException | DOMException ex)
    {
      Logger.getLogger(OPCDeviceHelper.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("Problems parsing the RegFile");
    } catch (InterruptedException ex)
    {
      Logger.getLogger(OPCDeviceHelper.class.getName()).log(Level.SEVERE, null, ex);
    } catch (Exception ex)
    {
      System.out.println("[ERROR]   " + ex.getMessage());
    } finally
    {
      long endTime = System.nanoTime();
      long elapsedTime = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
      Logger.getLogger(OPCDeviceHelper.class.getName()).log(Level.INFO, null, "ELAPSED TIME: " + elapsedTime + "ms");
      System.out.println("\n\n ELAPSED TIME: " + elapsedTime + "ms");
    }
    return null;
  }


  /**
   * Send ChangedState to the AgentCloud
   *
   * @param ExecutionState
   * @param productID
   * @param RecipeID
   * @param KPIs
   */
  private String changedState(String args)
  {
    //from workstation

    //parse xml SelfDescriptionInformation
    String senderName = args.split("\\:")[0]; //get sender name
    String Alldata = args.substring(args.indexOf(':') + 1); //get XML data
    ChangedState parsedClass = null;

    //STRING TO XML
    DocumentBuilderFactory dfctr = DocumentBuilderFactory.newInstance();
    DocumentBuilder bldr = null;
    try
    {
      bldr = dfctr.newDocumentBuilder();
      InputSource insrc = new InputSource();
      insrc.setCharacterStream(new StringReader(Alldata));
      org.w3c.dom.Document docres = null;
      docres = bldr.parse(insrc);
      System.out.println("\n\nThis is the XML received: " + docres.getDocumentElement().getTextContent() + "\n\n");
      JAXBContext jaxbContext = JAXBContext.newInstance(ChangedState.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      parsedClass = (ChangedState) unmarshaller.unmarshal(docres);
    } catch (ParserConfigurationException | SAXException | IOException ex)
    {
      Logger.getLogger(OPCDeviceHelper.class.getName()).log(Level.SEVERE, null, ex);
    } catch (JAXBException | DOMException ex)
    {
      System.out.println("\n Problem parsing class: " + ex.toString());
    }

    DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(senderName);
    //get the opcdevice objectID for the given Server Name
    MSBClientSubscription MiloClientID = (MSBClientSubscription) da.getClient();

    if (withAGENTCloud)
    {

      SubSystem agentObj = da.getSubSystem(); //get the  agentID for the respective sender client object

      //already deployed when agent is created ?
      Vertx.vertx().deployVerticle(new WebSocketsSender("5555")); //test! delete
      List<String> ReplyMsg = new ArrayList<String>();

      //Send message on the respective AgentID topic/websocket
      String msgToSend = Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA + ";" + parsedClass.recipeID + ";" + parsedClass.productID + ";";

      // TODO - talk to pedro aboud the naming of this parameter it sould be something like adapter_id or other thing
      // not Equipment
      // missing uniqueId in the SubsSystem Class defenition
      vertx.eventBus().send(agentObj.getName(), msgToSend, reply
              ->
      {
        if (reply.succeeded())
        {
          System.out.println("vertX Received reply: " + reply.result().body());
          ReplyMsg.add(reply.result().body().toString());
        } else
        {
          System.out.println("vertX No reply");
          ReplyMsg.add("vertX No reply");
        }
      });
      return ReplyMsg.get(0); //ADD VERTX MESSAGE REPLY
    } else
    {
      return "OK - No AgentCloud";
    }

  }

  // ------------------------------------------------------------------------------------------------------------------
  /**
   *
   * @param WorkstationID
   * @param Recipe
   */
  public void deployRecipe(String WorkstationID, String Recipe)
  {
    //send to workstation
    //DeployRecipe(Recipe) //send to workstation opc
    //wait for ConfirmRecipeDeployment(Recipe)
    //return Recipe //send back to Resource Agent
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param WorkstationID
   * @param RecipeID
   */
  private void executeRecipeRampUpMode(String WorkstationID, String RecipeID)
  {
    //from skill aggregator
    //call ExecuteRecipeRampUpMode(RecipeID) //to workstation
    //wait for ExecuteSkill(ModuleID,RecipeID)
    //change this??
    //call ExecuteSkill(RecipeID)
    //wait for CompletedSkillExecution(RecipeID)
    //call CompletedSkillExecution(RecipeID)
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param ModuleID
   * @param Data
   */
  private void uploadExecutionData(String ModuleID, String Data)
  {
    //from EquipmentModule or Workstation
    //leitura de nós especificos, do cliente msb para o servidor do device
    //call UploadExecutionData(ModuleID, Data) //colocar na Base De Dados
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param State
   * @return
   */
  private String statusUpdate(String State)
  {

    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    //from workstation
    //call StatusUpdate(State,...) //send to ResourceAgent
    //what to send?
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param WorkstationID
   * @param RecipeID
   * @param State
   */
  private void changeRecipeState(String WorkstationID, String RecipeID, String State)
  {
    //from skill aggregator
    //call ChangeRecipeState(RecipeID, state) //send to workstation
    //wait for ConfirmRecipestate(RecipeID, State)
    //return ConfirmRecipestate(RecipeID, State) //to skillaggregator
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param ProductID
   * @return [TODO] if from production Optimizer Agent - webservice? call RequestProduct(ProductID) //to lauch order
   * service wait for return RequestProduct(ProductID) //from launch order service if launch order service - opcua call
   * ExecuteSkill(ProductID) //to Workstation wait for return ExecuteSkill(Recipe,EquipmentID) //from Workstation or
   * wait for NoRecipe(Recipe,ProductID //from workstation call ExecuteSkill(Recipe) //to equipment?? change this wait
   * for RecipeExecutionDone(RecipeID) //change this call RecipeExecutionDone(RecipeID) //change this
   */
  private String requestProduct(String ProductID)
  {

    /*
     * //check the sender opcClient object from its Name DACManager myMaps = DACManager.getInstance(); //singleton to
     * access hashmaps in other classes Map<String, MSB_MiloClientSubscription> ProductAdapterHashMap =
     * myMaps.getProductIDAdapterMaps(); //get opcdevice name and object map MSB_MiloClientSubscription MiloClientID =
     * ProductAdapterHashMap.get(ProductID); //get the opcdevice objectID for the given ProductID
     * CompletableFuture<String> RequestProductResponse =
     * MiloClientID.RequestProduct(MiloClientID.milo_client_instanceMSB, ProductID); //call the method on the respective
     * Device
     *
     *
     * try { return RequestProductResponse.get(); } catch (InterruptedException | ExecutionException ex) {
     * Logger.getLogger(OPCDeviceItf.class.getName()).log(Level.SEVERE, null, ex); return ex.getMessage(); }
     */
    return null;
  }

  /**
   *
   * @param EntityID
   * @param Recipe
   * @param ProductID from resource Agent or transport Agent call DeployNewRecipe(Recipe,ProductID) //to EntityID -
   * workstation or transport equipment wait for return RecipeDeployed(Recipe,ProductID) return
   * RecipeDeployed(WorkstationID, Recipe,ProductID) //back to sender.
   */
  private void deployNewRecipe(String EntityID, String Recipe, String ProductID)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param ID
   * @param EquipmentType
   * @param SelfDescriptionServiceCall
   */
  private void broadcastPresence(String ID, String EquipmentType, String SelfDescriptionServiceCall)
  {
    //from workstation to msb. from msb to equipment module, deployment agent and data listener

    //call BroadcastPresence(ID, EquipmentType, SelfDescriptionServiceCall) //to equipment module
    //call BroadcastPresence(ID, EquipmentType, SelfDescriptionServiceCall) //to deployment Agent
    //call BroadcastPresence(ID, EquipmentType, SelfDescriptionServiceCall) //to DataListener (DB)
    //dbMSB.register_device(DevName, ShortInfo, LongInfo, Ip_addr, Protocol);
    //ArrayList Arrays= dbMSB.get_device_address_protocol(ID); //get device address and protocol
    //System.out.println(Arrays.toString());
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   */
  private void requestModuleDetails()
  {
    throw new UnsupportedOperationException("Not supported yet.");
    //from Resource Agent or Data Listener (DB)
    //call RequestModuleDetails
  }

  /**
   *
   * @param agentID
   * @return
   */
  public ServiceCallStatus removeAgent(String agentID)
  {

    SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
    SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();

    String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");

    BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
    bindingProvider.getRequestContext().put(
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);

    ServiceCallStatus agentStatus = systemConfigurator.removeAgent(agentID);

    return agentStatus;

  }

  /**
   *
   * @param datafromWorkStation
   * @return
   */
  private SubSystem workStationDataToSubSystem(RegFile datafromWorkStation)
  {

    String myAgentId = "Resource_" + Calendar.getInstance().getTimeInMillis(); //or Transport_...
    String cpadUniqueName = myAgentId;

    String cpadAgentClass = "asdsa"; //what is this?

    String cpadType = null; //select the agent type according to the workstation info
    if (datafromWorkStation.getType().equals("resource"))
    {
      cpadType = "resource_df_service";  //or transport_df_service
    } else if (datafromWorkStation.getType().equals("transport"))
    {
      cpadType = "transport_df_service";  //or transport_df_service
    }

    List<String> cpadParameters = new LinkedList<>(Arrays.asList("asdsad"));
    Skill s;
    String skillDescription = "SkillDescription";
    String skillUniqueId = "SkillUniqueId";
    KPI kpi;
    String kpiDescription = "SkillKpiDescription";
    String kpiUniqueId = "SkillKpiUniqueId";
    String kpiName = "SkillKpiName";
    String kpiDefaultUpperBound = "SkillKpiDefaultUpperBound";
    String kpiDefaultLowerBound = "SkillKpiDefaultLowerBound";
    String kpiCurrentValue = "SkillKpiCurrentValue";
    String kpiUnit = "SkillKpiUnit";
    kpi = new KPI();
    kpi.setDescription(kpiDescription);
    kpi.setUniqueId(kpiUniqueId);
    kpi.setName(kpiName);
    kpi.setDefaultUpperBound(kpiDefaultUpperBound);
    kpi.setDefaultLowerBound(kpiDefaultLowerBound);
    kpi.setCurrentValue(kpiCurrentValue);
    kpi.setUnit(kpiUnit);

    List<KPI> skillKpis = new LinkedList<>(Arrays.asList(kpi));
    String skillName = "SkillName";
    Parameter p;
    String parameterDefaultValue = "SkillParameterDefaultValue";
    String parameterDescription = "SkillParameterDescription";
    String parameterUniqueId = "SkillParameterUniqueId";
    String parameterLowerBound = "SkillParameterLowerBound";
    String parameterUpperBound = "SkillParameterUpperBound";
    String parameterName = "SkillParameterName";
    String parameterUnit = "SkillParameterUnit";

    p = new Parameter();
    p.setDefaultValue(parameterDefaultValue);
    p.setDescription(parameterDescription);
    p.setUniqueId(parameterUniqueId);
    p.setLowerBound(parameterLowerBound);
    p.setUpperBound(parameterUpperBound);
    p.setName(parameterName);
    p.setUnit(parameterUnit);

    List<Parameter> skillParameters = new LinkedList<>(Arrays.asList(p));
    String skillType = "test";
    s = new Skill();
    s.setDescription(skillDescription);
    s.setUniqueId(skillUniqueId);
    s.setKpis(skillKpis);
    s.setName(skillName);
    s.setParameters(skillParameters);
    s.setType(skillType);

    List<Skill> cpadSkills = new LinkedList<>(Arrays.asList(s));
    List<Recipe> cpadRecipes = new LinkedList<>();
    List<SkillRequirement> cpadSkillRequirements = new LinkedList<>();

    // af-silva changes made here, this should not work
    for (String key : datafromWorkStation.getRecipesTable().keySet())
    {
      Recipe recipe_inst = new Recipe();
      DaRecipe temp = datafromWorkStation.getRecipesTable().get(key);

      if (temp.getSkillRequirements() != null)
      {
        for (int i = 0; i < temp.getSkillRequirements().size(); i++)
        {
          // PROBLEM! TO SOLVE 
          // The class contains different data from the agents representation
          //cpadSkillRequirements.add(temp.getSkillRequirements().get(i));
        }
      }

      //populate KPIsettings as they aren't available yet
      KPISetting recipeKpiSetting;
      String kpiSettingDescription = "asdsad";
      String kpiSettingId = "asda";
      String kpiSettingName = "asdsa";
      String kpiSettingValue = "asds";
      // recipeKpiSetting = new KPISetting(kpiSettingDescription, 
      // kpiSettingId, kpiSettingName, kpiSettingValue);
      recipeKpiSetting = new KPISetting();
      recipeKpiSetting.setDescription(kpiSettingDescription);
      recipeKpiSetting.setId(kpiSettingId);
      recipeKpiSetting.setName(kpiSettingName);
      recipeKpiSetting.setValue(kpiSettingValue);

      List<KPISetting> recipeKpiSettings = new LinkedList<>(Arrays.asList(recipeKpiSetting));
      String recipeName = "asda";
      ParameterSetting recipePS;
      String parameterSettingDescription = "asdsad";
      String parameterSettingId = "asda";
      String parameterSettingName = "asdsa";
      String parameterSettingValue = "asds";
      // recipePS = new ParameterSetting(parameterSettingDescription, parameterSettingId, parameterSettingName, parameterSettingValue);
      recipePS = new ParameterSetting();
      recipePS.setDescription(parameterSettingDescription);
      recipePS.setUniqueId(parameterSettingId);
      recipePS.setName(parameterSettingName);
      recipePS.setValue(parameterSettingValue);
      List<ParameterSetting> recipeParameterSettings = new LinkedList<>(Arrays.asList(recipePS));

      recipe_inst.setKpiSettings(recipeKpiSettings);
      recipe_inst.setParameterSettings(recipeParameterSettings);
      cpadRecipes.add(recipe_inst); //add the received recipe to the list

    }

    //TODO when the workstation provide this info
    PhysicalLocation cpadPl;
    String physicalLocationReferenceFrameName = "Not yet implemented";
    long physicalLocationX = 0;
    long physicalLocationY = 0;
    long physicalLocationZ = 0;
    long physicalLocationAlpha = 0;
    long physicalLocationBeta = 0;
    long physicalLocationGamma = 0;
    // cpadPl = new PhysicalLocation(physicalLocationReferenceFrameName, physicalLocationX, physicalLocationY, physicalLocationZ, physicalLocationAlpha, physicalLocationBeta, physicalLocationGamma);
    cpadPl = new PhysicalLocation();
    cpadPl.setAlpha(physicalLocationAlpha);
    cpadPl.setBeta(physicalLocationBeta);
    cpadPl.setGamma(physicalLocationGamma);
    cpadPl.setReferenceFrameName(physicalLocationReferenceFrameName);
    cpadPl.setX(physicalLocationX);
    cpadPl.setY(physicalLocationY);
    cpadPl.setZ(physicalLocationZ);

    // LogicalLocation cpadLl = new LogicalLocation("asdad");
    LogicalLocation cpadLl = new LogicalLocation();
    cpadLl.setLocation(datafromWorkStation.getLogicalLocation());

    SkillRequirement cpadSR;
    String cpadSkillRequirementDescription = "asdsad";
    String cpadSkillRequirementUniqueId = "asda";
    String cpadSkillRequirementName = "asdsa";
    String cpadSkillRequirementType = "test";
    // cpadSR = new SkillRequirement(cpadSkillRequirementDescription, cpadSkillRequirementUniqueId, cpadSkillRequirementName, cpadSkillRequirementType);
    cpadSR = new SkillRequirement();
    cpadSR.setDescription(cpadSkillRequirementDescription);
    cpadSR.setName(cpadSkillRequirementName);
    cpadSR.setType(cpadSkillRequirementType);
    //cpadSR.setUniqueId(cpadSkillRequirementUniqueId);

    // cpad = new CyberPhysicalAgentDescription(cpadUniqueName, cpadAgentClass,
    // cpadType, cpadParameters, cpadSkills, cpadRecipes, cpadPl, cpadLl, cpadSkillRequirements);
    SubSystem cpad = new SubSystem();

    cpad.setLogicalLocation(cpadLl);
    cpad.setPhysicalLocation(cpadPl);
    cpad.setType(cpadType);
    //cpad.setEquipmentId(cpadUniqueName);
    cpad.setRecipes(cpadRecipes);
    cpad.setSkills(cpadSkills);

    return cpad;
  }

  /**
   * Send RecipeExecutionDone message with the ProductID to the AgentCloud using the respective VertX topic
   *
   * @param args
   */
  private String recipeExecutionDone(String args)
  {
    //check ProductID
    String ProductID = "1111"; //replace with productID in the received arguments
    //check which workstation sent
    String SenderName = "someAdaptor"; //replace with workstation name in the received arguments

    DACManager instance = DACManager.getInstance();
    SubSystem agentObj = instance.getDeviceAdapter(SenderName).getSubSystem();

    if (withAGENTCloud)
    {
      //Send message on the respective AgentID topic/websocket
      String msgToSend = Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA + ProductID;
      List<String> ReplyMsg = new ArrayList<String>();

      vertx.eventBus().send(/*
         * agentObj.getUniqueName()
               */"5555", msgToSend, reply
              ->
      {
        if (reply.succeeded())
        {
          System.out.println("vertX Received reply: " + reply.result().body());
          String replyMessage = (String) reply.result().body().toString();
          ReplyMsg.add(reply.result().body().toString());
        } else
        {
          System.out.println("vertX No reply");
          ReplyMsg.add("vertX No reply");
        }
      });

      return ReplyMsg.get(0);

    } else
    {
      return "OK  - with no AgentCloud";
    }

  }

  /**
   *
   * @param filepath
   * @return
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   * @throws JAXBException
   */
  public RegFile xmlMsgToObject(String filepath) throws ParserConfigurationException, SAXException, IOException, JAXBException
  {
    File fXmlFile = new File(filepath);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(fXmlFile);

    //read this:
    // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    // http://howtodoinjava.com/jaxb/jaxb-example-marshalling-and-unmarshalling-hashmap-in-java/
    doc.getDocumentElement().normalize();
    String message = doc.getDocumentElement().getTextContent();

    System.out.println(message);

    JAXBContext jc = JAXBContext.newInstance(RegFile.class);
    Unmarshaller unmar = jc.createUnmarshaller();
    RegFile aux = (RegFile) unmar.unmarshal(doc);

    return aux;
  }

  /**
   *
   * @param args
   * @return
   */
  private String sendRecipe(String args)
  {

    //check which workstation sent
    String SenderName = "someAdaptor"; //replace with workstation name in the received arguments
    DACManager instance = DACManager.getInstance();

    SubSystem agentObj = instance.getDeviceAdapter(SenderName).getSubSystem();

    //already deployed when agent is created ?
    //Vertx.vertx().deployVerticle(new WebSocketsSender("5555")); //test! delete
    //vertx.deployVerticle(new WebSocketsSender(agentObj.getUniqueName()));
    //get agentID
    //get recipes sent by the workstation
    //send them to the respective agentID by publishing on its topic
    if (withAGENTCloud)
    {
      //Send message on the respective AgentID topic/websocket
      String msgToSend = Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES + args;
      List<String> ReplyMsg = new ArrayList<String>();

      vertx.eventBus().send(/*
         * agentObj.getUniqueName()
               */"5555", msgToSend, reply
              ->
      {
        if (reply.succeeded())
        {
          System.out.println("vertX Received reply: " + reply.result().body());
          String replyMessage = (String) reply.result().body().toString();
          ReplyMsg.add(reply.result().body().toString());
        } else
        {
          System.out.println("vertX No reply");
          ReplyMsg.add("vertX No reply");
        }
      });

      return ReplyMsg.get(0);

    } else
    {
      return "OK  - with no AgentCloud";
    }

  }

}

//EOF
