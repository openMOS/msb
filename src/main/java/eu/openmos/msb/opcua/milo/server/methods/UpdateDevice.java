package eu.openmos.msb.opcua.milo.server.methods;

import org.eclipse.milo.opcua.sdk.server.annotations.UaInputArgument;
import org.eclipse.milo.opcua.sdk.server.annotations.UaMethod;
import org.eclipse.milo.opcua.sdk.server.annotations.UaOutputArgument;
import org.eclipse.milo.opcua.sdk.server.util.AnnotationBasedInvocationHandler;
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
                  name = "device_id",
                  description = "State of the device adapter") String device_id,
          @UaInputArgument(
                  name = "node",
                  description = "The ndoe to update") String node,
          @UaOutputArgument(
                  name = "result",
                  description = "The result") AnnotationBasedInvocationHandler.Out<Integer> result)
  {

    logger.debug("Not implemented yet! '{}'", context.getObjectNode().getBrowseName().getName());
    // TODO add handler code af-silva
  }

}
