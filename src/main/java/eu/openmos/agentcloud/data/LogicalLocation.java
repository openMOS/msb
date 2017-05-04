/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

/**
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class LogicalLocation extends Location {
    String location;

    public LogicalLocation() {}
    
    public LogicalLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    } 

    /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        return location;
    }
    
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static LogicalLocation fromString(String object){
        return new LogicalLocation(object);
    }
}
