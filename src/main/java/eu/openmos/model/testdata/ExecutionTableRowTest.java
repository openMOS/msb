/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.testdata;

import eu.openmos.model.ExecutionTableRow;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class ExecutionTableRowTest {
    
    public static ExecutionTableRow getTestObject(String parent, int currentRowPosition)
    {
        ExecutionTableRow executionTableRow = new ExecutionTableRow(
                parent + "ExecutionTableRowUniqueId - row " + currentRowPosition, 
                "ExecutionTableRowProductId", 
                "ExecutionTableRowRecipeId", 
/*                
                true, 
                true, 
                "ExecutionTableRowFrequency"
*/
                "ExecutionTableRowNextRecipeId",
                new LinkedList<String>()
        );
        
        return executionTableRow;
    }
    
    public static List<ExecutionTableRow> getTestList(String parent, int rowsCount)
    {
        List<ExecutionTableRow> letr = new LinkedList<ExecutionTableRow>();
        for (int i = 0; i < rowsCount; i++)
        {
            ExecutionTableRow etr = getTestObject(parent, i);
            letr.add(etr);
        }
        return letr;
    }

    public static ExecutionTableRow getTestObject()
    {
        ExecutionTableRow executionTableRow = new ExecutionTableRow(
                "uniqueId", 
                "productId", 
                "recipeId", 
/*                
                true, 
                true, 
                "frequency"
*/
                "nextRecipeId",
                new LinkedList<String>()
        );
        
        return executionTableRow;
    }
    
    public static List<ExecutionTableRow> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
