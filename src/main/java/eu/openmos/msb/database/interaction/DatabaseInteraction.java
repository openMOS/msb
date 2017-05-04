/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.database.interaction;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ricardo Matos
 */
public class DatabaseInteraction {
    private static String dbURL = "jdbc:derby://localhost:1527/MSBDB;create=true;user=openmos;password=introsys";
    private static String DATABASE_CONNECTION_STRING_PARAMETER_NAME = "openmos.msb.database.connection.string";
    private static String deviceTable = "DEVICE";
    private static String topicTable = "TOPIC";
    private static String messageTable = "MESSAGE";
    private static String executeTable = "EXECUTE";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    PreparedStatement ps;
    
    private static void createConnection()
    {
        try
        {
            //Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            Class.forName("org.sqlite.JDBC");
            //Get a connection
            //conn = DriverManager.getConnection("jdbc:sqlite:C:\\OpenMOS\\code-git\\msb_db\\service_bus\\msb_db\\msbdb.db"); //DriverManager.getConnection(dbURL); 
            // conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\fabio.miranda\\Documents\\NetBeansProjects\\EJBDatabaseFabio\\msbdb.db"); //DriverManager.getConnection(dbURL); 
            // conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\fabio.miranda\\Documents\\NetBeansProjects\\EJBDatabaseFabio\\msbdb.db"); //DriverManager.getConnection(dbURL); 
            conn = DriverManager.getConnection(ConfigurationLoader.getMandatoryProperty(DATABASE_CONNECTION_STRING_PARAMETER_NAME));
            
            System.out.println("Opened database successfully");
        }
        catch (Exception except)
        {
            except.printStackTrace();
            System.err.println( except.getClass().getName() + ": " + except.getMessage() );
        }
        
    }
    
    
    public static boolean resetDB() {
        try {
            createConnection();
            //createConnection();
            stmt = conn.createStatement();
            stmt.execute("delete FROM TOPIC");
            stmt.execute("delete FROM DEVICE");
            stmt.execute("delete FROM MESSAGE");

          
            stmt.close();
            shutdown();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        shutdown();
        return false;
    }
    
    
     /*
     *BEGINNING OF DEVICES
     */
    
    public static boolean registerDevices(String name, String protocol, String short_descriptor, String long_descriptor, String ip_addr)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO " + deviceTable + 
                    "(NAME, SHORTDESCRIPTION, LONGDESCRIPTION, PROTOCOL, IPADDRESS)" + 
                            " values (" +
                    "'"+ name + "','" + short_descriptor + "','" + long_descriptor + "','" + protocol + "','" + ip_addr +"')");
            stmt.close();
            shutdown();
            return true;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return false;
    }
    
    
    
    public static ArrayList<String> listAllDevices()
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ID, NAME FROM " + deviceTable + ";");
            ArrayList<String> myresult = new ArrayList<>();

            while(results.next())
            {   
                myresult.add(results.getString(1));
                myresult.add(results.getString(2));
//                System.out.println(id + "\t\t" + results.getString(4) + "\t\t" + restName + "\t\t" + cityName);
            }
            results.close();
            stmt.close();
            shutdown();
            return myresult;   
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return null;
    }
    
    public static ArrayList<String> readDeviceInfo(String devicename)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ID, IPADDRESS, SHORTDESCRIPTION, LONGDESCRIPTION, PROTOCOL FROM DEVICE WHERE NAME = '" + devicename+ "'");
            ArrayList<String> myresult = new ArrayList<>();
            while(results.next())
            {   
                myresult.add(results.getString(1));
                myresult.add(results.getString(2));
                myresult.add(results.getString(3));
                myresult.add(results.getString(4));
                myresult.add(results.getString(5));
            }
            results.close();
            stmt.close();
            shutdown();
            return myresult;   
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return null;
    }
    
     public int deregisterDevice(String devicename){
        try {
            createConnection();
            stmt = conn.createStatement();
            int results = stmt.executeUpdate("DELETE FROM DEVICE WHERE NAME = '" + devicename + "'");
            //SELECT * FROM DEVICE where id = 'device123';
            //preparedStatement.setString(1, device_id);

            stmt.close();
            shutdown();
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        shutdown();
        return -999;
    } 
     
     public static ArrayList<String> getDeviceAddressProtocol(String deviceID)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT IPADDRESS, protocol FROM DEVICE WHERE NAME = '" + deviceID+ "'");
            ArrayList<String> myresult = new ArrayList<>();
            while(results.next())
            {   
                myresult.add(results.getString(1));
                myresult.add(results.getString(2));
            }
            results.close();
            stmt.close();
            shutdown();
            return myresult;   
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return null;
    }
     
     public static String getDeviceName(String ip_address)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT NAME FROM DEVICE WHERE IPADDRESS = '" + ip_address+ "'");
            results.close();
            String name=results.getString(1);
            /*ArrayList<String> myresult = new ArrayList<>();
            while(results.next())
            {   
                myresult.add(results.getString(1));
                myresult.add(results.getString(2));
            }*/
            
            stmt.close();
            shutdown();
            return name;   
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return null;
    }
     
         public boolean editDevice(String name, String protocol, String short_descriptor, String long_descriptor, String ip_addr) {
//        Update [DEVICE]
//        set    
//	   SHORTDESCRIPTION = 'bada',
//	   LONGDESCRIPTION = 'abadu',
//	   PROTOCOL = 'PRPRPRPR',
//	   IPADDRESS = '191919' 
//        Where  NAME = 'soap'
    try
        {
            createConnection();
            ps = conn.prepareStatement("UPDATE " + deviceTable + " SET SHORTDESCRIPTION = ?, LONGDESCRIPTION = ?, PROTOCOL = ?, IPADDRESS = ?"
                    + "WHERE NAME = ?");
            ps.setString(1, short_descriptor);
            ps.setString(2, long_descriptor);
            ps.setString(3, protocol);
            ps.setString(4, ip_addr);
            ps.setString(5, name);
           
            ps.executeUpdate();

            ps.close();
                    
            shutdown();
            return true;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return false;

    }
     
     /*
     *END OF DEVICES
     */
     /*
     *BEGINNING OF TOPICS
     */
     
    public static boolean registerTopics(int parent_device_id, String topic_name, String access_type, String data_type)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO " + topicTable + 
                    "(NAME, ACCESSTYPE, DATATYPE, DEVICEID)" + 
                            " values (" +
                    "'"+ topic_name + "','" + access_type + "','" + data_type + "','" + parent_device_id + "')");
            stmt.close();
            shutdown();
            return true;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return false;
    }
    
    public int DeregisterTopic(String topic_name){
        try {
            createConnection();
            stmt = conn.createStatement();
            int results = stmt.executeUpdate("DELETE FROM TOPIC WHERE NAME = '" + topic_name + "'");
            //preparedStatement.setString(1, device_id);

            stmt.close();
            shutdown();
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        shutdown();
        return -999;
    } 
    
    public static ArrayList<String> readTopicsFromDevice(int device_id)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM TOPIC WHERE DEVICEID = '" + device_id + "'");
            ArrayList<String> myresult = new ArrayList<>();
            while(results.next())
            {   
                myresult.add(results.getString(1));
                myresult.add(results.getString(2));
                myresult.add(results.getString(3));
                myresult.add(results.getString(4));
                myresult.add(results.getString(5));
            }
            results.close();
            stmt.close();
            shutdown();
            return myresult;   
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return null;
    }
    
    public static ArrayList<String> readTopicInfo(String topic_name)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM TOPIC WHERE NAME = '" + topic_name + "'");
            ArrayList<String> myresult = new ArrayList<>();
            while(results.next())
            {   
                myresult.add(results.getString(1));
                myresult.add(results.getString(2));
                myresult.add(results.getString(3));
                myresult.add(results.getString(4));
                myresult.add(results.getString(5));
            }
            results.close();
            stmt.close();
            shutdown();
            return myresult;   
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return null;
    }
    
    
    
    
    
    
    /*
    *END OF TOPICS
    */   
    
    /*
    *START OF MESSAGE
    */
    public static boolean registerMessage(int parent_topic_id, String target, String timestamp, String header, String content)
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO " + messageTable + 
                    " (TARGET, TIMESTAMP, TOPICID, HEADER, CONTENT)" + 
                            " values (" +
                    "'"+ target + "','" + timestamp + "','" + parent_topic_id + "','" + header + "','" + content + "')");
            stmt.close();
            shutdown();
            return true;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return false;
    }
    
    
    /*
    *END OF MESSAGE
    */
     /*
    *START OF EXECUTION
    */
    

     public static boolean registerExecutionInfo(String Workstation, String RecipeIDs, String AgentUniqueID, String ProductIDs, String MethodID, String ObjectID )
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO " + executeTable + 
                    "(WORKSTATION, RECIPEIDS, AGENTUNIQUEID, PRODUCTIDS, METHODID, OBJECTID)" + 
                            " values (" +
                    "'"+ Workstation + "','" + RecipeIDs + "','" + AgentUniqueID + "','" + ProductIDs + "','" + MethodID + "','" + ObjectID +"')");
            stmt.close();
            shutdown();
            return true;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return false;
    }
    
     public static ArrayList<String> listAllExecutionInfo()
    {
        try
        {
            createConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ID, WORKSTATION FROM " + executeTable + ";");
            ArrayList<String> myresult = new ArrayList<>();

            while(results.next())
            {   
                myresult.add(results.getString(1));
                myresult.add(results.getString(2));
//                System.out.println(id + "\t\t" + results.getString(4) + "\t\t" + restName + "\t\t" + cityName);
            }
            results.close();
            stmt.close();
            shutdown();
            return myresult;   
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return null;
    }
     
      public boolean editExecutionInfo(String Workstation, String RecipeIDs, String AgentUniqueID, String ProductIDs, String MethodID, String ObjectID) {
//        Update [DEVICE]
//        set    
//	   SHORTDESCRIPTION = 'bada',
//	   LONGDESCRIPTION = 'abadu',
//	   PROTOCOL = 'PRPRPRPR',
//	   IPADDRESS = '191919' 
//        Where  NAME = 'soap'
    try
        {
            createConnection();
            ps = conn.prepareStatement("UPDATE " + executeTable + " SET RECIPEIDS = ?, AGENTUNIQUEID = ?, PRODUCTIDS = ?, METHODID = ?, OBJECTID = ?"
                    + "WHERE WORKSTATION = ?");
            ps.setString(1, RecipeIDs);
            ps.setString(2, AgentUniqueID);
            ps.setString(3, ProductIDs);
            ps.setString(4, MethodID);
            ps.setString(5, ObjectID);
            ps.setString(6, Workstation);
           
            ps.executeUpdate();

            ps.close();
                    
            shutdown();
            return true;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();
        return false;

    }
    
    
    
    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                //DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
    

    
//    public static void main(String[] args)
//    {
//        createConnection();
//        resetDB();
//        registerDevices("devi230", "shortdescription", "Idothis, this, this and that", "OPCUA");
//        listAllDevices();
//        shutdown();
//    }


}
