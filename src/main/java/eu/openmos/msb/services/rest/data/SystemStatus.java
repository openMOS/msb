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
public class SystemStatus implements Serializable {
    private static final long serialVersionUID = 6529685098267757033L;
    
    private String status;

    public SystemStatus() {
    }    

    public SystemStatus(String status) {
        this.status = status;
    }    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }    
}
