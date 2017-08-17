/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model;



/**
 * Abstract classes that distinguishes locations based on their serialized format.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public abstract class Location {
    
    public static final int PHYSICAL_LOCATION = 0;
    public static final int LOGICAL_LOCATION = 1;
    public static final int ERROR = -1;
    
    /**
     * Method to distinguish locations objecs.
     * 
     * @param location serialized form of a Location object
     * @return 0 if Physical, 1 if Logical, -1 if Error. 
     */
    public static int checkLocationType(String location) {
        if(location.contains(eu.openmos.model.utilities.SerializationConstants.TOKEN_PHYSICAL_LOCATION))
            return PHYSICAL_LOCATION;
        if(location.contains(eu.openmos.model.utilities.SerializationConstants.TOKEN_LOGICAL_LOCATION))
            return LOGICAL_LOCATION;
        return ERROR;
    }
}
