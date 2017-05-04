/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class DeviceXMLmethod {
      private final Logger logger = LoggerFactory.getLogger(getClass());

    @UaMethod
    public void invoke(
        AnnotationBasedInvocationHandler.InvocationContext context,

        @UaInputArgument(
            name = "XML_info",
            description = "input a XML containing device info to MSB")
            String x,

        @UaOutputArgument(
            name = "Feedback",
            description = "Feedback of the operation")
            AnnotationBasedInvocationHandler.Out<String> feedback) {

        System.out.println("DeviceXMLmethod(" + x + ")");
        logger.debug("Invoking DeviceXMLmethod() method of Object '{}'", context.getObjectNode().getBrowseName().getName());

        feedback.set("OK");
    }
}
