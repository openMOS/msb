/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.database.interaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Fabio Miranda (1st)
 * @author af-silva (2nd)
 */
public class DatabaseInteraction
{

  private static final Object lock = new Object();
  private static volatile DatabaseInteraction instance;
  private final String DBINMEMORY = "jdbc:sqlite::memory:";
  private final String DBSQLPATH = "openmos.msb.database.creation.file.path";
  private final String DB_CONNECTION_PARAMETER = "openmos.msb.database.connection.string";
  private Connection conn = null;
  private Statement stmt = null;
  private PreparedStatement ps = null;
  
  
  /**
   * @brief private constructor from the singleton implementation
   */
  private DatabaseInteraction()
  {
    createInMemDatabase();
  }


  /**
   *
   * @return
   */
  public static DatabaseInteraction getInstance()
  {
    DatabaseInteraction i = instance;
    if (i == null)
    {
      synchronized (lock)
      {
        // While we were waiting for the lock, another 
        i = instance; // thread may have instantiated the object.
        if (i == null)
        {
          i = new DatabaseInteraction();
          instance = i;
        }
      }
    }
    return i;
  }


  /**
   *
   * @return
   */
  private boolean createInMemDatabase()
  {
    boolean dbCreated = false;
    try
    {
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection(DBINMEMORY);
      stmt = conn.createStatement();
      StringBuilder sb = new StringBuilder();
      try
      {
        FileReader file = new FileReader(new File(DBSQLPATH));
        try (BufferedReader fileReader = new BufferedReader(file))
        {
          String command = new String();
          while ((command = fileReader.readLine()) != null)
          {
            sb.append(command);
          }
        }
        catch (FileNotFoundException ex)
        {
          System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
          Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
          return dbCreated;
        }
      }
      catch (FileNotFoundException ex)
      {
        System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        return dbCreated;
      }
      catch (IOException ex)
      {
        System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        return dbCreated;
      }
      // each command is split by a ";"
      String[] commands = sb.toString().split(";");
      Statement st = conn.createStatement();
      for (String command : commands)
      {
        // avoid empty statements
        if (!command.trim().equals(""))
        {
          st.executeUpdate(command);
          System.out.println(">>" + command);
        }
      }
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.INFO, null, "Opened database successfully.");
      System.out.println("Opened database successfully.");
      dbCreated = true;
    }
    catch (ClassNotFoundException | SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
      return dbCreated;
    }
    return dbCreated;
  }


  /**
   *
   * @return
   */
  public boolean resetDB()
  {
    return createInMemDatabase();
  }


  /**
   *
   * @param device_name
   * @param protocol
   * @param short_descriptor
   * @param long_descriptor
   * @return
   */
  public int createDevice(String device_name, String protocol, String short_descriptor, String long_descriptor)
  {
    int ok_id = -1;
    try
    {
      stmt = conn.createStatement();
      ok_id = stmt.executeUpdate("INSERT INTO DeviceAdapter"
        + "(name, short_description, long_description, protocol)"
        + " VALUES ("
        + "'" + device_name + "','" + short_descriptor + "','" + long_descriptor + "','" + protocol + "')");
      stmt.close();
      conn.commit();
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
      ok_id = -1; // make sure to return the -1 value
    }
    return ok_id;
  }


  /**
   *
   * @return
   */
  public ArrayList<String> listAllDevices()
  {
    try
    {
      stmt = conn.createStatement();
      ArrayList<String> myresult;
      try (ResultSet results = stmt.executeQuery("SELECT id, name FROM DeviceAdapter;"))
      {
        myresult = new ArrayList<>();
        while (results.next())
        {
          myresult.add(results.getString(1));
          myresult.add(results.getString(2));
        }
      }
      stmt.close();
      return myresult;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }


  /**
   * @param protocol
   * @return
   */
  public ArrayList<String> listDevicesByProtocol(String protocol)
  {
    try
    {
      stmt = conn.createStatement();
      ResultSet results;
      results = stmt.executeQuery("SELECT name FROM DeviceAdapter WHERE protocol = '" + protocol + "';");
      ArrayList<String> myresult = new ArrayList<>();
      while (results.next())
      {
        myresult.add(results.getString(1));
      }
      results.close();
      stmt.close();
      return myresult;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }


  /**
   *
   * @param deviceName
   * @return
   */
  public ArrayList<String> readDeviceInfoByName(String deviceName)
  {
    try
    {
      stmt = conn.createStatement();
      ResultSet results = stmt.executeQuery("SELECT id, client_id, agent_id, short_description, long_description, protocol FROM DeviceAdapter WHERE name = '" + deviceName + "'");
      ArrayList<String> myresult = new ArrayList<>();
      while (results.next())
      {
        myresult.add(results.getString(1));
        myresult.add(results.getString(2));
        myresult.add(results.getString(3));
        myresult.add(results.getString(4));
        myresult.add(results.getString(5));
        myresult.add(results.getString(6));
      }
      results.close();
      stmt.close();

      return myresult;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }


  /**
   *
   * @param deviceName
   * @return
   */
  public int removeDeviceByName(String deviceName)
  {
    try
    {
      stmt = conn.createStatement();
      int results = stmt.executeUpdate("DELETE FROM DeviceAdapter WHERE name = '" + deviceName + "'");
      stmt.close();
      conn.commit();
      return results;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return -999;
  }


  /**
   *
   * @param deviceId
   * @return
   */
  public int removeDeviceById(String deviceId)
  {
    try
    {
      stmt = conn.createStatement();
      int results = stmt.executeUpdate("DELETE FROM DeviceAdapter WHERE id = '" + deviceId + "'");
      stmt.close();
      conn.commit();
      return results;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return -999;
  }


  /**
   *
   * @param deviceName in the database is represented as name
   * @return
   */
  public ArrayList<String> getDeviceAddressProtocolByName(String deviceName)
  {
    try
    {
      stmt = conn.createStatement();
      ResultSet results = stmt.executeQuery("SELECT address, protocol FROM DeviceAdapter WHERE name = '" + deviceName + "'");
      ArrayList<String> myresult = new ArrayList<>();
      while (results.next())
      {
        myresult.add(results.getString(1));
        myresult.add(results.getString(2));
      }
      results.close();
      stmt.close();

      return myresult;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }


  /**
   *
   * @param deviceId
   * @return
   */
  public ArrayList<String> getDeviceAddressProtocolById(String deviceId)
  {
    try
    {
      stmt = conn.createStatement();
      ArrayList<String> myresult;
      try (ResultSet results = stmt.executeQuery("SELECT address, protocol FROM DeviceAdapter WHERE id = '" + deviceId + "'"))
      {
        myresult = new ArrayList<>();
        while (results.next())
        {
          myresult.add(results.getString(1));
          myresult.add(results.getString(2));
        }
      }
      stmt.close();

      return myresult;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }


  /**
   *
   * @param address
   * @return
   */
  public String getDeviceName(String address)
  {
    try
    {

      stmt = conn.createStatement();
      ResultSet results = stmt.executeQuery("SELECT name FROM DeviceAdapter WHERE address = '" + address + "'");
      results.close();
      String name = results.getString(1);

      stmt.close();

      return name;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }


  /**
   *
   * @param name
   * @param protocol
   * @param short_descriptor
   * @param long_descriptor
   * @param client_id
   * @param agent_id
   * @return
   */
  public boolean updateDevice(String name, String protocol, String short_descriptor, String long_descriptor, String client_id, String agent_id)
  {
    try
    {
      ps = conn.prepareStatement("UPDATE DeviceAdapter SET short_description = ?, long_description = ?, protocol = ?, client_id = ?, agent_id = ?"
        + "WHERE name = ?");
      ps.setString(1, short_descriptor);
      ps.setString(2, long_descriptor);
      ps.setString(3, protocol);
      ps.setString(4, client_id);
      ps.setString(4, agent_id);
      ps.setString(5, name);
      ps.executeUpdate();
      ps.close();
      conn.commit();
      return true;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }


  /**
   *
   * @param deviceName
   * @return
   */
  public int getDeviceIdByName(String deviceName)
  {
    try
    {

      stmt = conn.createStatement();
      ResultSet results = stmt.executeQuery("SELECT id FROM DeviceAdapter WHERE name = '" + deviceName + "'");
      results.close();
      int id = Integer.valueOf(results.getString(1));
      stmt.close();

      return id;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }


  // **************************************************************************************************************** //
  /**
   *
   * @param device_name
   * @param skill_name
   * @param description
   * @return
   */
  public boolean registerSkill(String device_name, String skill_name, String description)
  {

    int device_id;
    int skill_id;
    device_id = getDeviceIdByName(device_name);
    if (device_id == -1)
    {
      return false;
    }

    try
    {
      stmt = conn.createStatement();
      skill_id = stmt.executeUpdate("INSERT INTO Skill"
        + "(name, description)"
        + " VALUES ("
        + "'" + skill_name + "','" + description + "')");

      stmt.execute("INSERT INTO DAS"
        + "(sk_id, da_id)"
        + " VALUES ("
        + "'" + skill_id + "','" + device_id + "')");
      stmt.close();
      conn.commit();
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }


  /**
   *
   * @param skill_name
   * @return
   */
  public int removeSkill(String skill_name)
  {
    try
    {
      stmt = conn.createStatement();
      int results = stmt.executeUpdate("DELETE FROM Skill WHERE name = '" + skill_name + "'");
      stmt.close();
      conn.commit();
      return results;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }


  /**
   *
   * @param device_name
   * @return
   */
  public ArrayList<String> getSkillsByDevice(String device_name)
  {
    try
    {
      stmt = conn.createStatement();
      ArrayList<String> myresult;
      ResultSet results = stmt.executeQuery("SELECT DeviceAdapter.id, DeviceAdapter.name, Skill.name "
        + "FROM Skill, DeviceAdapter, DAS WHERE DeviceAdapter.id = DAS.da_id AND Skill.id = DAS.sk_id AND DeviceAdapter.name =" + device_name + ";");
      myresult = new ArrayList<>();
      while (results.next())
      {
        myresult.add(results.getString(1));
        myresult.add(results.getString(2));
        myresult.add(results.getString(3));
      }
      stmt.close();
      return myresult;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }


  // **************************************************************************************************************** //
  /**
   *
   * @param da_id
   * @param aml_id
   * @param name
   * @param endpoint
   * @return
   */
  public boolean registerRecipe(int da_id, String aml_id, String name, String endpoint)
  {
    try
    {
      stmt = conn.createStatement();
      stmt.execute("INSERT INTO Recipe (aml_id, da_id, name, endpoint) "
        + "VALUES(" + aml_id + "," + da_id + "," + name + ", " + endpoint + ");");
      stmt.close();
      conn.commit();
      return true;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }


  /**
   *
   * @param recipe_name
   * @return
   */
  public int removeRecipeByName(String recipe_name)
  {
    try
    {
      stmt = conn.createStatement();
      int results = stmt.executeUpdate("DELETE FROM Recipe WHERE name = '" + recipe_name + "'");
      stmt.close();
      conn.commit();
      return results;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }


  /**
   *
   * @param recipe_id
   * @return
   */
  public int removeRecipeById(String recipe_id)
  {
    try
    {
      stmt = conn.createStatement();
      int results = stmt.executeUpdate("DELETE FROM Recipe WHERE id = '" + recipe_id + "'");
      stmt.close();
      conn.commit();
      return results;
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }


  /**
   *
   * @param recipe_name
   * @return
   */
  public int getRecipeId(String recipe_name)
  {
    try
    {
      stmt = conn.createStatement();
      ResultSet result = stmt.executeQuery("Select Recipe.id FROM Recipe WHERE name = '" + recipe_name + "'");
      stmt.close();
      return Integer.parseInt(result.getString(1));
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }


  /**
   *
   * @param recipe_name
   * @return
   */
  public String getRecipeEndpoint(String recipe_name)
  {
    try
    {
      stmt = conn.createStatement();
      ResultSet result = stmt.executeQuery("Select Recipe.endpoint FROM Recipe WHERE name = '" + recipe_name + "'");
      stmt.close();
      return result.getString(1);
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
      Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  // **************************************************************************************************************** //
}
