package eu.openmos.msb.opcua.milo.server.methods;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.QueuedAction;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import static eu.openmos.msb.opcua.milo.server.OPCServersDiscoverySnippet.browseInstaceHierarchyNode;
import eu.openmos.msb.starter.MSB_gui;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.annotations.UaInputArgument;
import org.eclipse.milo.opcua.sdk.server.annotations.UaMethod;
import org.eclipse.milo.opcua.sdk.server.annotations.UaOutputArgument;
import org.eclipse.milo.opcua.sdk.server.util.AnnotationBasedInvocationHandler;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fabio.miranda
 */
public class UpdateDevice
{

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @UaMethod
  public void invoke(
          AnnotationBasedInvocationHandler.InvocationContext context,
          @UaInputArgument(
                  name = "da_id",
                  description = "Device Adapter to re-browse") String da_id,
          @UaOutputArgument(
                  name = "result",
                  description = "The result") AnnotationBasedInvocationHandler.Out<Integer> result)
  {
    try
    {
      logger.debug("Update Device invoked! '{}'", context.getObjectNode().getBrowseName().getName());
      //rebrowse namespace of targert DA
      Thread threadUpdateDevice = new Thread()
      {
        @Override
        public synchronized void run()
        {
          int count = 0;
          DeviceAdapter da = null;
          while (da == null && count < 30)
          {
            try
            {
              da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
              
              if (da == null)
                Thread.sleep(1000);
              count++;
            } catch (InterruptedException ex)
            {
              java.util.logging.Logger.getLogger(UpdateDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
          }

          Thread threadRebrowseNamespace = DACManager.getInstance().update_device_threadMap.get(da_id);
          try
          {
            if (threadRebrowseNamespace != null)
            {
              threadRebrowseNamespace.interrupt();
              threadRebrowseNamespace.stop();
              logger.debug("UpdateDevice INTERRUPTED! - " + threadRebrowseNamespace.getState().toString());
              logger.debug("" + threadRebrowseNamespace.getName());
            }
          } catch (Exception ex)
          {

          }
          
          final DeviceAdapter test_da = da;
          threadRebrowseNamespace = new Thread()
          {
            @Override
            public synchronized void run()
            {
              
              DeviceAdapter auxDA = rebrowseNamespace(test_da);

              if (auxDA != null)
              {
                try
                {
                  DatabaseInteraction.write_stuff.acquire();
                  validateModules_in_DB(test_da);
                  validateRecipes_in_DB(test_da);
                  DatabaseInteraction.write_stuff.release();

                  //update tables
                  MSB_gui.fillModulesTable();
                  MSB_gui.fillRecipesTable();
                  
                  QueuedAction qa = DACManager.getInstance().QueuedActionMap.get(da_id);
                  if (qa != null)
                  {
                    DACManager.getInstance().VerifyQueuedActions(test_da, qa);
                  }
                } catch (InterruptedException ex)
                {
                  java.util.logging.Logger.getLogger(UpdateDevice.class.getName()).log(Level.SEVERE, null, ex);
                }

              } else
              {
                logger.error("ERROR rebrownsing DA: " + da_id);
                //WHAT TO DO?
                //DA have corrupted data
              }
              DACManager.getInstance().update_device_threadMap.remove(da_id);
            }
          };
          DACManager.getInstance().update_device_threadMap.put(da_id, threadRebrowseNamespace);

          logger.debug("" + threadRebrowseNamespace.getName());
          threadRebrowseNamespace.start();
        }
      };
      threadUpdateDevice.start();
      logger.debug("Update Device FINISHED!");
      result.set(1);
    } catch (Exception ex)
    {
      logger.debug("Update Device ERROR! '{}'", ex.getMessage());
      result.set(0);
    }

  }

  private DeviceAdapter rebrowseNamespace(DeviceAdapter da)
  {
    DeviceAdapterOPC da_OPC = (DeviceAdapterOPC) da;
    MSBClientSubscription msbClient = da_OPC.getClient();
    OpcUaClient client = msbClient.getClientObject();

    logger.info("***** Starting namespace re-browsing ***** \n");

    try
    {
      Element node = new Element("DeviceAdapter");
      Set<String> ignore = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
      ignore.addAll(Arrays.asList(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.ignore").split(",")));

      logger.info("Browse instance Hierarchy started");
      NodeId InstaceHierarchyNode = browseInstaceHierarchyNode("", client, new NodeId(0, 84));
      if (InstaceHierarchyNode != null)
      {
        logger.info("Browse instance Hierarchy ended with: " + InstaceHierarchyNode.getIdentifier().toString());
        node.addContent(msbClient.browseNode(client,
                InstaceHierarchyNode,
                Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level")),
                ignore));
      } else
      {
        return null;
      }

      Element nSkills = new Element("Skills");
      nSkills.addContent(msbClient.browseNode(client,
              new NodeId(2, ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.namespace.skills")),
              Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level.skills")),
              ignore));

      // print to file the XML structure extracted from the browsing process             
      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());

      //xmlOutput.output(node, new FileWriter(XML_PATH + "\\main_" + da.getSubSystem().getName() + ".xml", false));
      //xmlOutput.output(nSkills, new FileWriter(XML_PATH + "\\skills_" + da.getSubSystem().getName() + ".xml", false));
      logger.info("Starting DA Parser **********************");
      boolean ok = da.parseDNToObjects(client, node, nSkills, false);
      logger.info("***** End namespace browsing ***** \n\n");

      if (ok)
      {
        return da;
      } else
      {
        return null;
      }

    } catch (NumberFormatException ex)
    {
      System.out.println("***** End namespace browsing with error ***** \n\n");
    }

    return null;
  }

  private void validateRecipes_in_DB(DeviceAdapter da)
  {
    //RECIPES
    List<String> auxRecipesDB = DatabaseInteraction.getInstance().getRecipesIDByDAName(da.getSubSystem().getName());
    List<String> idsFound = new ArrayList<>();

    List<Recipe> tempRepList = new ArrayList<>(da.getListOfRecipes());
    for (Module auxMod : da.getListOfModules())
    {
      tempRepList.addAll(auxMod.getRecipes());
    }

    for (String recipeID_DB : auxRecipesDB)
    {
      boolean notFound = true;
      for (Recipe recipe : tempRepList)
      {
        if (recipe.getUniqueId().equals(recipeID_DB))
        {
          notFound = false;
          idsFound.add(recipe.getUniqueId());
          //update recipe fields?
          break;
        }
      }
      if (notFound)
      {
        Boolean a = DatabaseInteraction.getInstance().remove_recipe_from_SR(recipeID_DB);
        int aux = DatabaseInteraction.getInstance().removeRecipeById(recipeID_DB);
        logger.info("" + aux);
      }
    }

    for (Recipe recipe : tempRepList)
    {
      if (!idsFound.contains(recipe.getUniqueId()))
      {
        DACManager.getInstance().registerRecipe(da.getSubSystem().getName(), recipe.getUniqueId(), recipe.getSkill().getName(),
                "true", recipe.getName(), recipe.getInvokeObjectID(), recipe.getInvokeMethodID());
      }
      DACManager.getInstance().AssociateRecipeToSR(recipe.getSkill());
    }

  }

  private void validateModules_in_DB(DeviceAdapter da)
  {
    //RECIPES
    String da_id_db = DatabaseInteraction.getInstance().getDA_DB_IDbyAML_ID(da.getSubSystem().getUniqueId());
    List<String> auxModulesAML_DB_ID = DatabaseInteraction.getInstance().getModulesAML_ID_ByDA_DB_ID(da_id_db);

    List<String> idsFound = new ArrayList<>();
    for (String moduleAML_DB_ID : auxModulesAML_DB_ID)
    {
      boolean notFound = true;

      for (Module module : da.getSubSystem().getModules())
      {
        if (module.getUniqueId().equals(moduleAML_DB_ID))
        {
          notFound = false;
          idsFound.add(module.getUniqueId());
          break;
        }
      }
      if (notFound)
      {
        DatabaseInteraction.getInstance().removeModuleByID(moduleAML_DB_ID);
      }
    }

    for (Module module : da.getSubSystem().getModules())
    {
      if (!idsFound.contains(module.getUniqueId()))
      {
        DACManager.getInstance().registerModule(da.getSubSystem().getName(), module.getName(),
                module.getUniqueId(), module.getStatus(), module.getAddress());
      }
    }

  }

}
