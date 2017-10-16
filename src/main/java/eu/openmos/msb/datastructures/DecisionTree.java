/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Introsys
 */
public class DecisionTree {
     /* ------------------------------- */
    /*                                 */
    /*              FIELDS             */
    /*                                 */
    /* ------------------------------- */

    /* NESTED CLASS */

    private class BinTree {
    	
	/* FIELDS */
	
	private String     nodeID;
    	private String  questOrAns = null;
    	private List<BinTree> yesBranch  = null;
    	private BinTree noBranch   = null;
	
	/* CONSTRUCTOR */
	
	public BinTree(String newNodeID, String newQuestAns) {
	    nodeID     = newNodeID;
	    questOrAns = newQuestAns;
            }
	}

    /* OTHER FIELDS */

    static BufferedReader    keyboardInput = new
                           BufferedReader(new InputStreamReader(System.in));
    BinTree rootNode = null;

    /* ------------------------------------ */
    /*                                      */
    /*              CONSTRUCTORS            */
    /*                                      */
    /* ------------------------------------ */

    /* Default Constructor */

    public DecisionTree() {
	}

    /* ----------------------------------------------- */
    /*                                                 */
    /*               TREE BUILDING METHODS             */
    /*                                                 */
    /* ----------------------------------------------- */

    /* CREATE ROOT NODE */

    public void createRoot(String newNodeID, String newQuestAns) {
	rootNode = new BinTree(newNodeID,newQuestAns);	
	System.out.println("Created root node " + newNodeID);	
	}
			
    /* ADD YES NODE */

    public void addYesNode(String existingNodeID, String newNodeID, String newQuestAns) {
	// If no root node do nothing
	
	if (rootNode == null) {
	    System.out.println("ERROR: No root node!");
	    return;
	    }
	
	// Search tree
	
	if (searchTreeAndAddYesNode(rootNode,existingNodeID,newNodeID,newQuestAns)) {
	    System.out.println("Added node " + newNodeID +
	    		" onto \"yes\" branch of node " + existingNodeID);
	    }
	else System.out.println("Node " + existingNodeID + " not found");
	}

    /* SEARCH TREE AND ADD YES NODE */

    private boolean searchTreeAndAddYesNode(BinTree currentNode,
    			String existingNodeID, String newNodeID, String newQuestAns) {
    	if (currentNode.nodeID == existingNodeID) {
	    // Found node
            if (currentNode.yesBranch == null)
                currentNode.yesBranch = new ArrayList<>();
	    currentNode.yesBranch.add(new BinTree(newNodeID,newQuestAns));
		
    	    return(true);
	    }
        return false;

   	} 	
    		
    /* ADD NO NODE */

    public void addNoNode(String existingNodeID, String newNodeID, String newQuestAns) {
	// If no root node do nothing
	
	if (rootNode == null) {
	    System.out.println("ERROR: No root node!");
	    return;
	    }
	
	// Search tree
	
	if (searchTreeAndAddNoNode(rootNode,existingNodeID,newNodeID,newQuestAns)) {
	    System.out.println("Added node " + newNodeID +
	    		" onto \"no\" branch of node " + existingNodeID);
	    }
	else System.out.println("Node " + existingNodeID + " not found");
	}
	
    /* SEARCH TREE AND ADD NO NODE */

    private boolean searchTreeAndAddNoNode(BinTree currentNode,
    			String existingNodeID, String newNodeID, String newQuestAns) {
    	if (currentNode.nodeID == existingNodeID) {
	    // Found node
	    if (currentNode.noBranch == null) currentNode.noBranch = new
	    		BinTree(newNodeID,newQuestAns);
	    else {
	        System.out.println("WARNING: Overwriting previous node " +
			"(id = " + currentNode.noBranch.nodeID +
			") linked to yes branch of node " +
			existingNodeID);
		currentNode.noBranch = new BinTree(newNodeID,newQuestAns);
		}		
    	    return(true);
	    }
        
        return false;

   	} 	

    /* --------------------------------------------- */
    /*                                               */
    /*               TREE QUERY METHODS             */
    /*                                               */
    /* --------------------------------------------- */

    public void queryBinTree() throws IOException {
        queryBinTree(rootNode);
        }

    private void queryBinTree(BinTree currentNode) throws IOException {

        // Test for leaf node (answer) and missing branches

        if (currentNode.yesBranch==null) {
            if (currentNode.noBranch==null) System.out.println(currentNode.questOrAns);
            else System.out.println("Error: Missing \"Yes\" branch at \"" +
            		currentNode.questOrAns + "\" question");
            return;
            }
        if (currentNode.noBranch==null) {
            System.out.println("Error: Missing \"No\" branch at \"" +
            		currentNode.questOrAns + "\" question");
            return;
            }

        // Question

        askQuestion(currentNode);
        }

    private void askQuestion(BinTree currentNode) throws IOException {
        System.out.println(currentNode.questOrAns + " (enter \"Yes\" or \"No\")");
        String answer = keyboardInput.readLine();
       /* if (answer.equals("Yes")) queryBinTree(currentNode.yesBranch);
        else {
            if (answer.equals("No")) queryBinTree(currentNode.noBranch);
            else {
                System.out.println("ERROR: Must answer \"Yes\" or \"No\"");
                askQuestion(currentNode);
                }
            }*/
        }

    /* ----------------------------------------------- */
    /*                                                 */
    /*               TREE OUTPUT METHODS               */
    /*                                                 */
    /* ----------------------------------------------- */

    /* OUTPUT BIN TREE */

    public void outputBinTree() {

        outputBinTree("1",rootNode);
        }

    private void outputBinTree(String tag, BinTree currentNode) {

        // Check for empty node

        if (currentNode == null) return;

        // Output

        System.out.println("[" + tag + "] nodeID = " + currentNode.nodeID +
        		", question/answer = " + currentNode.questOrAns);
        		
        // Go down yes branch
        if (currentNode.yesBranch != null)
            for(int i=0; i<currentNode.yesBranch.size();i++){
                outputBinTree(tag + ".1",currentNode.yesBranch.get(i));
            }
        

        // Go down no branch

        outputBinTree(tag + ".2",currentNode.noBranch);
	}      		
}
