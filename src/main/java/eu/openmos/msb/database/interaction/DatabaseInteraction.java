/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.database.interaction;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.msb.messages.DaDevice;
import eu.openmos.msb.messages.HelperDevicesInfo;
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
import java.util.HashMap;
import java.util.Map;
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
    private final String DBSQLPATH = "openmos.msb.database.creation.file.path.string";
    private final String DB_CONNECTION_PARAMETER = "openmos.msb.database.connection.string";
    private Connection conn = null;
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

            StringBuilder sb = new StringBuilder();
            try
            {
                System.out.println("caminho da bd  " + ConfigurationLoader.getMandatoryProperty(DBSQLPATH));
                FileReader file = new FileReader(new File(ConfigurationLoader.getMandatoryProperty(DBSQLPATH)));
                try (BufferedReader fileReader = new BufferedReader(file))
                {
                    String command = new String();
                    while ((command = fileReader.readLine()) != null)
                    {
                        sb.append(command);
                    }
                } catch (FileNotFoundException ex)
                {
                    System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                    Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
                    return dbCreated;
                }
            } catch (FileNotFoundException ex)
            {
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
                return dbCreated;
            } catch (IOException ex)
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
            st.closeOnCompletion();
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.INFO, null, "Opened database successfully.");
            System.out.println("Opened database successfully.");
            dbCreated = true;
        } catch (ClassNotFoundException | SQLException ex)
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
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO DeviceAdapter"
                    + "(name, short_description, long_description, protocol)"
                    + " VALUES ("
                    + "'" + device_name + "','" + short_descriptor + "','" + long_descriptor + "','" + protocol + "')");
            ResultSet r = stmt.getGeneratedKeys();
            ok_id = r.getInt(1);
            stmt.close();

        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            ArrayList<String> myresult;
            try (
                    ResultSet query = stmt.executeQuery("SELECT id, name FROM DeviceAdapter;"))
            {
                myresult = new ArrayList<>();
                while (query.next())
                {

                    myresult.add(query.getString(2));
                }
            }
            stmt.close();
            return myresult;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            ResultSet query = stmt.executeQuery("SELECT name FROM DeviceAdapter WHERE protocol = '" + protocol + "';");
            ArrayList<String> myresult = new ArrayList<>();
            while (query.next())
            {
                myresult.add(query.getString(1));
            }
            query.close();
            stmt.close();
            return myresult;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            ResultSet query = stmt.executeQuery("SELECT id, short_description, long_description, protocol FROM DeviceAdapter WHERE name = '" + deviceName + "'");
            ArrayList<String> myresult = new ArrayList<>();
            while (query.next())
            {
                myresult.add(query.getString(1));
                myresult.add(query.getString(2));
                myresult.add(query.getString(3));
                myresult.add(query.getString(4));
                myresult.add(query.getString(5));
                myresult.add(query.getString(6));
            }
            query.close();
            stmt.close();

            return myresult;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            int query = stmt.executeUpdate("DELETE FROM DeviceAdapter WHERE name = '" + deviceName + "'");
            stmt.close();

            return query;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            int results = stmt.executeUpdate("DELETE FROM DeviceAdapter WHERE id = '" + deviceId + "'");
            stmt.close();
            return results;
        } catch (SQLException ex)
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
    public ArrayList<String> getDeviceAddressProtocolById(String deviceId)
    {
        try
        {
            Statement stmt = conn.createStatement();
            ArrayList<String> myresult;
            try (ResultSet query = stmt.executeQuery("SELECT address, protocol FROM DeviceAdapter WHERE id = '" + deviceId + "'"))
            {
                myresult = new ArrayList<>();
                while (query.next())
                {
                    myresult.add(query.getString(1));
                    myresult.add(query.getString(2));
                }
            }
            stmt.close();

            return myresult;
        } catch (SQLException ex)
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

            Statement stmt = conn.createStatement();
            ResultSet query = stmt.executeQuery("SELECT name FROM DeviceAdapter WHERE address = '" + address + "'");
            query.close();
            String name = query.getString(1);

            stmt.close();

            return name;
        } catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public ArrayList<String> getDeviceAdapters()
    {
        return DatabaseInteraction.getInstance().getDeviceAdapters();
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
            ps = conn.prepareStatement("UPDATE DeviceAdapter "
                    + "SET short_description = ?, long_description = ?, protocol = ?, client_id = ?, agent_id = ?"
                    + "WHERE name = ?");
            ps.setString(1, short_descriptor);
            ps.setString(2, long_descriptor);
            ps.setString(3, protocol);
            ps.setString(4, client_id);
            ps.setString(4, agent_id);
            ps.setString(5, name);
            ps.executeUpdate();
            ps.close();

            return true;
        } catch (SQLException ex)
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

        int id = -1;
        String sql = "SELECT id FROM DeviceAdapter WHERE name = '" + deviceName + "'";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql))
        {

            while (rs.next())
            {
                id = rs.getInt("id");
                System.out.println("Found divce with id:  " + id);
                break;
            }

        } catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
        /**
     *
     * @param deviceName
     * @return
     */
    public int getSkillIdByName(String skillName)
    {

        int id = -1;
        String sql = "SELECT id FROM Skill WHERE name = '" + skillName + "'";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql))
        {

            while (rs.next())
            {
                id = rs.getInt("id");
                System.out.println("Found skill with id:  " + id);
                break;
            }

        } catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    // **************************************************************************************************************** //
    // **************************************************************************************************************** //
    /**
     *
     * @param device_name
     * @param aml_id
     * @param skill_name
     * @param description
     * @return
     */
    public boolean registerSkill(String device_name, String aml_id, String skill_name, String description)
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
            Statement stmt = conn.createStatement();
            {
            String sql = "INSERT INTO Skill"
                    + "(aml_id, name, description)"
                    + " VALUES ("
                    + "'" + aml_id +"','" + skill_name + "','" + description + "')";
            skill_id = stmt.executeUpdate(sql);
            }
            {
            String sql = "INSERT INTO DAS"
                    + "(sk_id, da_id)"
                    + " VALUES ("
                    + "'" + skill_id + "','" + device_id + "')";
            stmt.execute(sql);
            }
            stmt.close();

        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            int query = stmt.executeUpdate("DELETE FROM Skill WHERE name = '" + skill_name + "'");
            stmt.close();

            return query;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            ArrayList<String> myresult;
            ResultSet query = stmt.executeQuery("SELECT DeviceAdapter.id, DeviceAdapter.name, Skill.name "
                    + "FROM Skill, DeviceAdapter, DAS "
                    + "WHERE DeviceAdapter.id = DAS.da_id AND Skill.id = DAS.sk_id AND DeviceAdapter.name =" + device_name + ";");
            myresult = new ArrayList<>();
            while (query.next())
            {
                myresult.add(query.getString(1));
                myresult.add(query.getString(2));
                myresult.add(query.getString(3));
            }
            stmt.close();
            return myresult;
        } catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // **************************************************************************************************************** //
    // **************************************************************************************************************** //
    /**
     *
     * @param da_id
     * @param aml_id
     * @param name
     * @param endpoint
     * @return
     */
    public boolean registerRecipe(String aml_id, int da_id, int sk_id, boolean valid, String name)
    {
        try
        {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) "
                    + "VALUES(" + aml_id + "," + da_id + "," + sk_id + "," + valid + "," + name + ");";
            stmt.execute(sql);
            stmt.close();

            return true;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            int query = stmt.executeUpdate("DELETE FROM Recipe WHERE name = '" + recipe_name + "'");
            stmt.close();

            return query;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            int query = stmt.executeUpdate("DELETE FROM Recipe WHERE id = '" + recipe_id + "'");
            stmt.close();

            return query;
        } catch (SQLException ex)
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
            Statement stmt = conn.createStatement();
            ResultSet query = stmt.executeQuery("Select Recipe.id FROM Recipe WHERE name = '" + recipe_name + "'");
            stmt.close();
            return Integer.parseInt(query.getString(1));
        } catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public Map<String, String> getRecipesByDAName(String deviceAdapterName)
    {
        try
        {
            Map<String, String> resultArray;
            Statement stmt = conn.createStatement();
            ResultSet query = stmt.executeQuery("SELECT Recipe.aml_id, Recipe.name "
                    + "FROM Recipe, DeviceAdapter "
                    + "WHERE Recipe.da_id = DeviceAdapter.id AND DeviceAdapter.name = '" + deviceAdapterName + "';");
            stmt.close();
            resultArray = new HashMap<String, String>();
            while (query.next())
            {
                resultArray.put(query.getString(1), query.getString(2));
            }
            return resultArray;
        } catch (SQLException ex)
        {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
            Statement stmt = conn.createStatement();
            ResultSet query = stmt.executeQuery("SELECT Recipe.endpoint FROM Recipe WHERE name = '" + recipe_name + "'");
            stmt.close();
            return query.getString(1);
        } catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param devcieAdapterName
     * @return
     */
    public ArrayList<HelperDevicesInfo> getDevicesFromDeviceAdapter(String devcieAdapterName)
    {
        try
        {
            ArrayList<HelperDevicesInfo> devicesInfo = new ArrayList<HelperDevicesInfo>();
            Statement stmt = conn.createStatement();
            ResultSet query = stmt.executeQuery("SELECT Device.name, Device.status, Device.address\n"
                    + "FROM Device, DeviceAdapter\n"
                    + "WHERE Device.da = DeviceAdapter.id AND DeviceAdapter.name = '" + devcieAdapterName + "';");
            stmt.close();
            while (query.next())
            {
                HelperDevicesInfo temp = new HelperDevicesInfo();
                temp.setDeviceAdapter(devcieAdapterName);
                temp.setName(query.getString(1));
                temp.setStatus(Integer.parseInt(query.getString(2)));
                temp.setAddress(query.getString(3));
                devicesInfo.add(temp);
            }
            return devicesInfo;
        } catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // **************************************************************************************************************** //
}
