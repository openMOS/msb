/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest.data;

import java.io.Serializable;

/**
 *
 * @author valerio.gentile
 */
public class SystemStage implements Serializable {
    private static final long serialVersionUID = 6529685098267757033L;
    
    private String stage;

    public SystemStage() {
    }    

    public SystemStage(String stage) {
        this.stage = stage;
    }    

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }    
}
