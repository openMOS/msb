/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.starter;

import java.util.List;

/**
 *
 * @author fabio.miranda
 */
public interface DeviceInterface {
    void ExecutionData(String Function, List args); //function: RequestProduct(int pID, etc)
    void EquipmentData(String Function, List args);
    void EquipmentCommunication(String Function, List args);
    void EquipmentRegistration(String Function, List args);  
}
