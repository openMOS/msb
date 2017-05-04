/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.utils;

/**
 *
 * @author Admin
 */
public interface DevicesDynamic {
    
    void on_participant_disappear(String id, String reason);
    void on_participant_appear(String id, String description);
    void on_new_readonly_topic(String device_id, String topic_name);
    void on_new_writeonly_topic(String device_id, String topic_name);               
}
