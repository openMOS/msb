/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.utils;

import eu.openmos.msb.starter.MSB_MiloClientSubscription;
import static java.lang.Thread.sleep;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.nodes.objects.ServerNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.variables.ServerStatusNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fabio.miranda
 */
public class CheckOPCServers {
    
    private final int polling_period = 10; /*30 seconds*/
    private final Map<String, Object> clientObjectsList;
    private Map<String, String> ServersDisconectedList;
    
    public CheckOPCServers(Map<String, Object> clientObjects){
        clientObjectsList=clientObjects;
    }
    
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    
    public Map<String, String> run() {
        
        //while(true){
             // Get a typed reference to the Server object: ServerNode
             for(String key: clientObjectsList.keySet()){
                 try {
                     System.out.println("a tentar sacar o clientsubscription do hashMAP...");
                     MSB_MiloClientSubscription TempSubscription = (MSB_MiloClientSubscription)clientObjectsList.get(key);;
                     
                     System.out.println("a tentar sacar o cliente do clientsubscription: " + TempSubscription);
                     //OpcUaClient client=TempSubscription.milo_client_instanceMSB;
                     if(TempSubscription.getClientObject()!=null){
                        OpcUaClient client= TempSubscription.getClientObject();
                        
                        ServerNode  serverNode = client.getAddressSpace().getObjectNode(Identifiers.Server,ServerNode.class).get();
                        
                        // Read properties of the Server object...
                        String[] serverArray = serverNode.getServerArray().get();
                        String[] namespaceArray = serverNode.getNamespaceArray().get();

                        logger.info("ServerArray={}", Arrays.toString(serverArray));
                        logger.info("NamespaceArray={}", Arrays.toString(namespaceArray));

                        // Read the value of attribute the ServerStatus variable component
                        ServerStatusDataType serverStatus = serverNode.getServerStatus().get();

                        logger.info("ServerStatus={}", serverStatus);

                        // Get a typed reference to the ServerStatus variable
                        // component and read value attributes individually
                        ServerStatusNode serverStatusNode = serverNode.serverStatus().get();
                        BuildInfo buildInfo = serverStatusNode.getBuildInfo().get();
                        DateTime startTime = serverStatusNode.getStartTime().get();
                        DateTime currentTime = serverStatusNode.getCurrentTime().get();
                        ServerState state = serverStatusNode.getState().get();

                        logger.info("ServerStatus.BuildInfo={}", buildInfo);
                        logger.info("ServerStatus.StartTime={}", startTime);
                        logger.info("ServerStatus.CurrentTime={}", currentTime);
                        logger.info("ServerStatus.State={}", state);

                        LocalDateTime MSBTime=LocalDateTime.now();
                        Date MSBDate=new Date();
                        long MSB_DateUTC=LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

                        Date ServerTime=currentTime.getJavaDate();
                        long ServerDateUTC=ServerTime.getTime();

                        System.out.println("SERVERTIME: "+ ServerTime + " MSBTIME: "+ MSBTime + " MSBDate: " + MSBDate);
                        System.out.println("MSB_DateUTC: "+ MSB_DateUTC + " ServerDateUTC: "+ ServerDateUTC);

                     }else{
                         System.out.println("null :c");
                     }
                 } catch (InterruptedException ex) {
                     Logger.getLogger(CheckOPCServers.class.getName()).log(Level.SEVERE, null, ex);
                     System.out.println("what is the problem? interrupt "+ex.getMessage());
                     //return ex.getMessage();
                     //ServersDisconectedList.put(key, ex.getMessage());
                 } catch (ExecutionException ex) {
                     Logger.getLogger(CheckOPCServers.class.getName()).log(Level.SEVERE, null, ex);
                     System.out.println("what is the problem? execution "+ex.getMessage() +" name: "+ key);
                     ServersDisconectedList.put(key, ex.getMessage());
                     return ServersDisconectedList;
                 }
            }

            try {
                sleep(polling_period * 1000);
                System.out.println("Sleeping...");
            } catch (InterruptedException ex) {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
        return ServersDisconectedList;
    }
    
}
