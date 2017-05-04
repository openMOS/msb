/*
 * Copyright (c) 2016 Kevin Herron
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.html.
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
public class SumMethod {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @UaMethod
    public void invoke(
        AnnotationBasedInvocationHandler.InvocationContext context,

        

        @UaOutputArgument(
            name = "xy_SUM",
            description = "The sum result of two values")
            AnnotationBasedInvocationHandler.Out<Double> xySum) {

        //System.out.println("sum " + y.toString() + x.toString() + ")");
        logger.debug("Invoking sum() method of Object '{}'", context.getObjectNode().getBrowseName().getName());

        xySum.set(1.1);
    }
    
}
