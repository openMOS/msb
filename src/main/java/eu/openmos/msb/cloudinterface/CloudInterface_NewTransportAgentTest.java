/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloudinterface;

import eu.openmos.agentcloud.data.recipe.KPI;
import eu.openmos.agentcloud.data.recipe.KPISetting;
import eu.openmos.agentcloud.data.recipe.Parameter;
import eu.openmos.agentcloud.data.recipe.ParameterSetting;
import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.cloudinterface.AgentStatus;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.data.LogicalLocation;
import eu.openmos.agentcloud.data.PhysicalLocation;
import eu.openmos.agentcloud.utilities.Constants;
import io.vertx.core.Vertx;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class CloudInterface_NewTransportAgentTest {
    
    private static final Logger logger = Logger.getLogger(CloudInterface_NewTransportAgentTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New Transport Agent Test main start");
        
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
	SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();

/////////////////////////////
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");            

        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(
              // BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://192.168.15.5:9999/wsSystemConfigurator");
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
//////////////////////////////
        
        // CyberPhysicalAgentDescription cpad = new CyberPhysicalAgentDescription();
        String myAgentId = "Transport_" + Calendar.getInstance().getTimeInMillis();
        CyberPhysicalAgentDescription cpad = getTestObject(myAgentId);
        // cpad.setType("transport_df_service");
        cpad.setType(Constants.DF_TRANSPORT);
        cpad.setUniqueName(myAgentId);

        AgentStatus agentStatus = systemConfigurator.createNewAgent(cpad);
        
        logger.info(agentStatus.getCode());
        logger.info(agentStatus.getDescription());

        // 
        Vertx.vertx().deployVerticle(new WebSocketsSender(myAgentId));
        
        
        
        logger.info("New Transport Agent Test main end");
    }

    public static CyberPhysicalAgentDescription getTestObject(String toString) {
CyberPhysicalAgentDescription cpad;
        String cpadUniqueName = "asd";
        String cpadAgentClass = "asdsa";
        String cpadType = "asdas";
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
                    // kpi = new Kpi(kpiDescription, kpiUniqueId, kpiName, kpiDefaultUpperBound, kpiDefaultLowerBound, kpiCurrentValue, kpiUnit);
                    // kpi = new Kpi(kpiDescription, kpiUniqueId, kpiName, kpiDefaultUpperBound, kpiDefaultLowerBound, kpiCurrentValue, kpiUnit);
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
                    // p = new Parameter(parameterDefaultValue, parameterDescription, parameterUniqueId,                             parameterLowerBound, parameterUpperBound, parameterName, parameterUnit);
                    p = new Parameter();
                    p.setDefaultValue(parameterDefaultValue);
                    p.setDescription(parameterDescription);
                    p.setUniqueId(parameterUniqueId);
                    p.setLowerBound(parameterLowerBound);
                    p.setUpperBound(parameterUpperBound);
                    p.setName(parameterName);
                    p.setUnit(parameterUnit);
                    
                    
                    
                List<Parameter> skillParameters = new LinkedList<>(Arrays.asList(p));
                int skillType = 0;
            // s = new Skill(skillDescription, skillUniqueId, skillKpis, skillName, skillParameters, skillType);
            s = new Skill();
            s.setDescription(skillDescription);
            s.setUniqueId(skillUniqueId);
            // s.getKpis().addAll(skillKpis);
            s.setKpis(skillKpis);
            s.setName(skillName);
            // s.getParameters().addAll(skillParameters);
            s.setParameters(skillParameters);
            s.setType(skillType);
            
            
            
            
        List<Skill> cpadSkills = new LinkedList<>(Arrays.asList(s));
            Recipe r;
                String recipeDescription = "asdsad";
                String recipeUniqueId = "asda";
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
                    recipePS.setId(parameterSettingId);
                    recipePS.setName(parameterSettingName);
                    recipePS.setValue(parameterSettingValue);
                    
                List<ParameterSetting> recipeParameterSettings = new LinkedList<>(Arrays.asList(recipePS));
                String recipeUniqueAgentName = "asdsad";
                    SkillRequirement recipeSR;
                    String skillRequirementDescription = "asdsad";
                    String skillRequirementUniqueId = "asda";
                    String skillRequirementName = "asdsa";
                    int skillRequirementType = 0;
                    // recipeSR = new SkillRequirement(skillRequirementDescription, skillRequirementUniqueId, skillRequirementName, skillRequirementType);
                    recipeSR = new SkillRequirement();
                    recipeSR.setDescription(skillRequirementDescription);
                    recipeSR.setUniqueId(skillRequirementUniqueId);
                    recipeSR.setName(skillRequirementName);
                    recipeSR.setType(skillRequirementType);
                    
                List<SkillRequirement> recipeSkillRequirements = new LinkedList<>(Arrays.asList(recipeSR));
            // r = new Recipe(recipeDescription, recipeUniqueId, recipeKpiSettings, recipeName, recipeParameterSettings, recipeUniqueAgentName, recipeSkillRequirements);
            r = new Recipe();
            r.setDescription(recipeDescription);
            r.setName(recipeName);
            r.setUniqueAgentName(recipeUniqueAgentName);
            r.setUniqueId(recipeUniqueId);
            // r.getKpisSetting().addAll(recipeKpiSettings);
            r.setKpisSetting(recipeKpiSettings);
            // r.getParametersSetting().addAll(recipeParameterSettings);
            r.setParametersSetting(recipeParameterSettings);
            // r.getSkillRequirements().addAll(recipeSkillRequirements);
            r.setSkillRequirements(recipeSkillRequirements);
            
        List<Recipe> cpadRecipes = new LinkedList<>(Arrays.asList(r));
        PhysicalLocation cpadPl;
           String physicalLocationReferenceFrameName = "asdas"; 
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
        cpadLl.setLocation("asdad");
        
            SkillRequirement cpadSR;
                String cpadSkillRequirementDescription = "asdsad";
                String cpadSkillRequirementUniqueId = "asda";
                String cpadSkillRequirementName = "asdsa";
                int cpadSkillRequirementType = 0;
            // cpadSR = new SkillRequirement(cpadSkillRequirementDescription, cpadSkillRequirementUniqueId, cpadSkillRequirementName, cpadSkillRequirementType);
            cpadSR = new SkillRequirement();
            cpadSR.setDescription(cpadSkillRequirementDescription);
            cpadSR.setName(cpadSkillRequirementName);
            cpadSR.setType(cpadSkillRequirementType);
            cpadSR.setUniqueId(cpadSkillRequirementUniqueId);
            
        List<SkillRequirement> cpadSkillRequirements = new LinkedList<>(Arrays.asList(recipeSR));
        // cpad = new CyberPhysicalAgentDescription(cpadUniqueName, cpadAgentClass,
        // cpadType, cpadParameters, cpadSkills, cpadRecipes, cpadPl, cpadLl, cpadSkillRequirements);

        cpad = new CyberPhysicalAgentDescription();
        cpad.setAgentClass(cpadAgentClass);
        cpad.setLogicalLocation(cpadLl);
        cpad.setPhysicalLocation(cpadPl);
        cpad.setType(cpadType);
        cpad.setUniqueName(cpadUniqueName);
        // cpad.getRecipes().addAll(cpadRecipes);
        cpad.setRecipes(cpadRecipes);
        // cpad.getSkillRequirements().addAll(cpadSkillRequirements);
        cpad.setSkillRequirements(cpadSkillRequirements);        
        // cpad.getSkills().addAll(cpadSkills);
        cpad.setSkills(cpadSkills);
        // cpad.getRecipes().addAll(cpadRecipes);
        cpad.setRecipes(cpadRecipes);
        // cpad.getAgentParameters().addAll(cpadParameters);
        cpad.setAgentParameters(cpadParameters);
        
        
        return cpad;        
    }

}
