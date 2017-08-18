/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

import eu.openmos.agentcloud.data.RawEquipmentData;
import java.util.Date;

/**
 *
 * @author valerio.gentile
 */
public class RawEquipmentDataTest
{

  public static RawEquipmentData getTestObject(String agentUniqueIdentifier /* equipmentId */)
  {
    String[] sensorValues = new String[1];
    sensorValues[0] = "RawEquipmentDataSensorValue1";

    return new RawEquipmentData(
            agentUniqueIdentifier,
            sensorValues,
            "RawEquipmentDataValueType",
            "RawEquipmentDataMeasurementUnit",
            new Date());
  }

}
