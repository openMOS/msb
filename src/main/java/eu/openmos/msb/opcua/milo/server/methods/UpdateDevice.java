package eu.openmos.msb.opcua.milo.server.methods;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.model.Recipe;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
      //rebrowse namespace of targert DA
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);

      DeviceAdapter auxDA = rebrowseNamespace(da);

      if (auxDA != null)
      {
        da = auxDA;
        validateRecipes_in_DB(da);
        //MODULES
      } else
      {
        System.out.println("ERROR rebrownsing DA: " + da.getSubSystem().getName());
        //WHAT TO DO?
        //DA have corrupted data
      }
      result.set(1);
    } catch (Exception ex)
    {
      result.set(0);
    }
  }

  private DeviceAdapter rebrowseNamespace(DeviceAdapter da)
  {
    DeviceAdapterOPC da_OPC = (DeviceAdapterOPC) da;
    MSBClientSubscription msbClient = da_OPC.getClient();
    OpcUaClient client = msbClient.getClientObject();

    System.out.println("\n");
    System.out.println("***** Starting namespace browsing ***** \n");

    try
    {
      Element node = new Element("DeviceAdapter");
      Set<String> ignore = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
      ignore.addAll(Arrays.asList(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.ignore").split(",")));

      node.addContent(msbClient.browseNode(client,
              new NodeId(2, ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.namespace.dainstance")),
              Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level")),
              ignore));

      Element nSkills = new Element("Skills");
      nSkills.addContent(msbClient.browseNode(client,
              new NodeId(2, ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.namespace.skills")),
              Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level.skills")),
              ignore));

      // print to file the XML structure extracted from the browsing process             
      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());

      String XML_PATH = ConfigurationLoader.getMandatoryProperty("openmos.msb.xml.path");
      xmlOutput.output(node, new FileWriter(XML_PATH + "\\main_" + da.getSubSystem().getName() + ".xml", false));
      xmlOutput.output(nSkills, new FileWriter(XML_PATH + "\\skills_" + da.getSubSystem().getName() + ".xml", false));

      System.out.println("Starting DA Parser **********************");

      boolean ok = da.parseDNToObjects(node, nSkills);

      if (ok)
      {
        return da;
      } else
      {
        return null;
      }

    } catch (IOException | NumberFormatException ex)
    {

    }
    System.out.println("***** End namespace browsing ***** \n\n");
    return null;
  }

  private void validateRecipes_in_DB(DeviceAdapter da)
  {
    //RECIPES
    List<Recipe> auxRecipesDB = DatabaseInteraction.getInstance().getRecipesByDAName(da.getSubSystem().getName());

    List<Integer> indexFound = new ArrayList<>();
    for (int i = 0; i < auxRecipesDB.size(); i++)
    {
      Recipe recipeDB = auxRecipesDB.get(i);
      boolean notFound = true;
      for (int j = 0; j < da.getSubSystem().getRecipes().size(); j++)
      {
        Recipe recipe = da.getSubSystem().getRecipes().get(j);
        if (recipe.getUniqueId().equals(recipeDB.getUniqueId()))
        {
          notFound = false;
          indexFound.add(j);
          //update recipe fields?
        }
      }
      if (notFound)
      {
        DatabaseInteraction.getInstance().removeRecipeById(recipeDB.getUniqueId());
      }
    }

    for (int i = 0; i < da.getSubSystem().getRecipes().size(); i++)
    {
      if (!indexFound.contains(i))
      {
        Recipe recipe = da.getSubSystem().getRecipes().get(i);
        DACManager.getInstance().registerRecipe(da.getSubSystem().getName(), recipe.getUniqueId(), recipe.getSkill().getName(),
                "true", recipe.getName(), recipe.getInvokeObjectID(), recipe.getInvokeMethodID());
      }
    }

  }
  /*
  private void validateModules_in_DB(DeviceAdapter da)
  {
    //RECIPES
    List<Module> auxModulesDB = DatabaseInteraction.getInstance().get(da.getSubSystem().getName());

    List<Integer> indexFound = new ArrayList<>();
    for (int i = 0; i < auxModulesDB.size(); i++)
    {
      Module moduleDB = auxModulesDB.get(i);
      boolean notFound = true;
      for (int j = 0; j < da.getSubSystem().getRecipes().size(); j++)
      {
        Recipe recipe = da.getSubSystem().getRecipes().get(j);
        if (recipe.getUniqueId().equals(moduleDB.getUniqueId()))
        {
          notFound = false;
          indexFound.add(j);
        }
      }
      if (notFound)
      {
        DatabaseInteraction.getInstance().removeRecipeById(moduleDB.getUniqueId());
      }
    }

    for (int i = 0; i < da.getSubSystem().getRecipes().size(); i++)
    {
      if (!indexFound.contains(i))
      {
        Recipe recipe = da.getSubSystem().getRecipes().get(i);
        DACManager.getInstance().registerRecipe(da.getSubSystem().getName(), recipe.getUniqueId(), recipe.getSkill().getName(),
                "true", recipe.getName(), recipe.getInvokeObjectID(), recipe.getInvokeMethodID());
      }
    }

  }
*/
}
