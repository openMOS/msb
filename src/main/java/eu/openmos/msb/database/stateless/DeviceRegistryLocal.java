/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.database.stateless;

import java.util.ArrayList;
//import javax.ejb.Local;

/**
 *
 * @author Ricardo Matos
 */
//@Local
public interface DeviceRegistryLocal {

    public String register_device(String device_name, 
        String short_info,  
        String long_info,
        String ip_addr,
        String device_protocol);
     
    public ArrayList<String> read_device_info(String device_name);
   
    public String edit_device(String device_name, 
        String short_info,  
        String long_info,
        String ip_addr,
        String device_protocol);
    
    public String register_topic(String topic_name, 
        String access_type,
        String data_type,  
        String device_name);
   
    public String register_message(String parent_topic_name, 
        String target,
        String timestamp,  
        String header,
        String content) ;
    
    public ArrayList<String> get_topics_from_device(String device_name);
    

    public ArrayList<String> list_all_devices() ;
    
    public ArrayList<String> get_device_address_protocol(String device_id) ;
    
    public boolean reset_DB();
    
    public int deregister_device(String device_id);

    public int deregister_topic(String topic_name);

}
