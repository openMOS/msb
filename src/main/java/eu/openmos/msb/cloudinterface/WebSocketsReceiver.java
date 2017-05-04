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
public class WebSocketsReceiver extends AbstractVerticle {

    private String name = null;

    public WebSocketsReceiver(String name) {
        this.name = name;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("5555", message -> {
            System.out.println(this.name
                    + " received message: "
                    + message.body());
        });
    }

}
