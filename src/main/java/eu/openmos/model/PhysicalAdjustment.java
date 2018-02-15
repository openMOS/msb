package eu.openmos.model;

import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * Concrete class for equipment physical adjustments.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class PhysicalAdjustment extends EquipmentAdjustment implements Serializable {
    private static final Logger logger = Logger.getLogger(PhysicalAdjustment.class.getName());    
    private static final long serialVersionUID = 6529685098267757992L;
    
}