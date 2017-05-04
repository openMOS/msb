/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloudinterface;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 *
 * @author fabio.miranda
 */
public class WebSocketsSenderDraft extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().publish("5555", "message 2");
        vertx.eventBus().send("5555", "message 1");
    }

}
