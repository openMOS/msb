/*
 * TODO - Add licence
 */
package eu.openmos.msb.starter;

// IMOPORTS
import eu.openmos.msb.opcua.milo.client.MSB_MiloClientSubscription;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.opcua.utils.OPCDeviceDiscoveryItf;
import eu.openmos.msb.dds.instance.DDSDeviceManager;
import eu.openmos.msb.dds.instance.DDSDevice;
import eu.openmos.msb.opcua.milo.server.opcuaServerMSB;
import eu.openmos.msb.opcua.utils.OPCDeviceItf;
import eu.openmos.msb.opcua.utils.OpcUaServersDiscoverySnippet;
import eu.openmos.msb.recipesmanagement.RecipesDeployerImpl;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultCaret;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.ws.Endpoint;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.xml.sax.SAXException;
import DDS.DataWriter;
import DDS.HANDLE_NIL;
import MSB2ADAPTER.StringMessage;
import MSB2ADAPTER.StringMessageDataWriter;
import MSB2ADAPTER.StringMessageDataWriterHelper;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.EProtocol;
import eu.openmos.msb.dds.instance.DDSErrorHandler;
import eu.openmos.msb.messages.DaDevice;
import eu.openmos.msb.messages.DaRecipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;

/**
 * *********************************************************************************************************************
 *
 * @author fabio.miranda
 */
public class MSB_gui extends javax.swing.JFrame implements Observer
{

    // GLOBAL VARIABLES
    private static String MSB_OPCUA_SERVER_ADDRESS = null;
    private static eu.openmos.msb.opcua.milo.server.opcuaServerMSB opcuaServerInstanceMILO;

    private static DefaultTableModel adaptersTableModel;
    private static DefaultTableModel recipesTableModel;
    private static DefaultTableModel devicesTableModel;
    private static ImageIcon redLight;
    private static ImageIcon greenLight;
    private static ImageIcon greyLight;
    private static JLabel labelLDS;
    private static JLabel labelServer;
    private static JLabel labelWS;
    private static JLabel labelRegister;
    private static OPCDeviceItf DeviceITF;
    private boolean isDDSRunning;

    /**
     *
     * @throws Exception
     */
    public MSB_gui() throws Exception
    {
        initComponents();

        this.isDDSRunning = false;

        adaptersTableModel = (DefaultTableModel) TableServers.getModel();
        recipesTableModel = (DefaultTableModel) recipesTable.getModel();
        devicesTableModel = (DefaultTableModel) devicesTable.getModel();
        pb_produceProduct.hide();

        recipesTable.getModel().addTableModelListener(new CheckBoxModelListener());

        OnOffServerPanel.setLayout(new FlowLayout());
        OnOffRegister.setLayout(new FlowLayout());
        OnOffWSPanel.setLayout(new FlowLayout());
        OnOffLDS.setLayout(new FlowLayout());

        //faz o setup das imagens e dimensiona para o tamanho pretendido
        setImage();
        labelServer = new JLabel(redLight);
        OnOffServerPanel.add(labelServer);
        OnOffServerPanel.setMaximumSize(new Dimension(34, 31));

        labelLDS = new JLabel(redLight);
        OnOffLDS.add(labelLDS);
        OnOffLDS.setMaximumSize(new Dimension(34, 31));

        labelRegister = new JLabel(redLight);
        OnOffRegister.add(labelRegister);
        OnOffRegister.setMaximumSize(new Dimension(34, 31));

        labelWS = new JLabel(redLight);
        OnOffWSPanel.add(labelWS);
        OnOffWSPanel.setMaximumSize(new Dimension(34, 31));

        DeviceITF = new OPCDeviceItf(); //inputs? endpoints, MAP<ID, OPCclientObject> ?

//    ImageIcon imageIcon;
//    imageIcon = new ImageIcon(new ImageIcon(".\\main\\resources\\eu\\openmos\\msb\\icons\\OpenMos-logo-RGB-colours.png").getImage().getScaledInstance(l_openmosLogo.getWidth(), l_openmosLogo.getHeight(), Image.SCALE_DEFAULT));
//    l_openmosLogo.setIcon(imageIcon);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        opc_comms_log = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableServers = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        recipesTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        choice1 = new java.awt.Choice();
        choice2 = new java.awt.Choice();
        choice3 = new java.awt.Choice();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        devicesTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        msb_opcua_servername = new javax.swing.JTextField();
        StartMSBServer = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        LDSserverAddress = new javax.swing.JTextField();
        LDSRegisterserver = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btn_start_discovery = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        OnOffRegister = new javax.swing.JPanel();
        OnOffServerPanel = new javax.swing.JPanel();
        OnOffLDS = new javax.swing.JPanel();
        comboServers = new javax.swing.JComboBox<>();
        btn_SendURL = new javax.swing.JButton();
        btn_RequestProduct = new javax.swing.JButton();
        btn_sendRecipe = new javax.swing.JButton();
        btn_invoqueMethod = new javax.swing.JButton();
        ComboMSB = new javax.swing.JComboBox<>();
        btn_DeviceRegistration = new javax.swing.JButton();
        btn_RecipeExecutionDone = new javax.swing.JButton();
        btn_ChangedState = new javax.swing.JButton();
        btn_sendrecipe2 = new javax.swing.JButton();
        btn_updatestatus = new javax.swing.JButton();
        textToSend = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        p_dds = new javax.swing.JPanel();
        l_ddsDomain = new javax.swing.JLabel();
        b_startMSBDDS = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        cb_DDSDevice = new javax.swing.JComboBox<>();
        cb_DDSRecipeList = new javax.swing.JComboBox<>();
        b_DDSCallRecipe = new javax.swing.JButton();
        l_testDDS = new javax.swing.JLabel();
        l_DDSDevice = new javax.swing.JLabel();
        l_DDSRecipe = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        StartWebService = new javax.swing.JButton();
        WebServiceAddress = new javax.swing.JTextField();
        OnOffWSPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        l_openmosLogo = new javax.swing.JLabel();
        p_productExecution = new javax.swing.JPanel();
        pb_produceProduct = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cb_productSelection = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        opc_comms_log.setEditable(false);
        opc_comms_log.setColumns(20);
        opc_comms_log.setRows(5);
        opc_comms_log.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane1.setViewportView(opc_comms_log);

        TableServers.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TableServers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Server Name", "Protocol", "URL"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TableServers);

        recipesTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        recipesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Recipe", "Status", "Device Adapter"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(recipesTable);

        jLabel3.setText("Recipes Table");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Adapters Table");

        jList1.setModel(new javax.swing.AbstractListModel<String>()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        devicesTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        devicesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Name", "Status", "Endpoint", "Device Adapter"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(devicesTable);

        jLabel5.setText("Devices Table");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 600));

        jLabel1.setText("MSB OPCUA Server");

        msb_opcua_servername.setText("opc.tcp://172.18.1.174:12637/MSB-OPCUA-SERVER");
        msb_opcua_servername.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                msb_opcua_servernameActionPerformed(evt);
            }
        });

        StartMSBServer.setText("Start");
        StartMSBServer.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                StartMSBServerMouseClicked(evt);
            }
        });
        StartMSBServer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                StartMSBServerActionPerformed(evt);
            }
        });

        jLabel7.setText("LDS Server");

        LDSserverAddress.setText("opc.tcp://localhost:4840");
        LDSserverAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                LDSserverAddressActionPerformed(evt);
            }
        });

        LDSRegisterserver.setText("Register");
        LDSRegisterserver.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                LDSRegisterserverActionPerformed(evt);
            }
        });

        jLabel6.setText("Discovery Service");

        btn_start_discovery.setText("Start");
        btn_start_discovery.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_start_discoveryActionPerformed(evt);
            }
        });

        OnOffRegister.setPreferredSize(new java.awt.Dimension(34, 31));

        javax.swing.GroupLayout OnOffRegisterLayout = new javax.swing.GroupLayout(OnOffRegister);
        OnOffRegister.setLayout(OnOffRegisterLayout);
        OnOffRegisterLayout.setHorizontalGroup(
            OnOffRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );
        OnOffRegisterLayout.setVerticalGroup(
            OnOffRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        OnOffServerPanel.setPreferredSize(new java.awt.Dimension(34, 31));

        javax.swing.GroupLayout OnOffServerPanelLayout = new javax.swing.GroupLayout(OnOffServerPanel);
        OnOffServerPanel.setLayout(OnOffServerPanelLayout);
        OnOffServerPanelLayout.setHorizontalGroup(
            OnOffServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 41, Short.MAX_VALUE)
        );
        OnOffServerPanelLayout.setVerticalGroup(
            OnOffServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        OnOffLDS.setPreferredSize(new java.awt.Dimension(34, 31));

        javax.swing.GroupLayout OnOffLDSLayout = new javax.swing.GroupLayout(OnOffLDS);
        OnOffLDS.setLayout(OnOffLDSLayout);
        OnOffLDSLayout.setHorizontalGroup(
            OnOffLDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );
        OnOffLDSLayout.setVerticalGroup(
            OnOffLDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        comboServers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No device adapters available!"}));
        comboServers.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                comboServersActionPerformed(evt);
            }
        });

        btn_SendURL.setText("Call SendURL");
        btn_SendURL.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_SendURLActionPerformed(evt);
            }
        });

        btn_RequestProduct.setText("Call RequestProduct");
        btn_RequestProduct.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_RequestProductActionPerformed(evt);
            }
        });

        btn_sendRecipe.setText("Call SendRecipe");
        btn_sendRecipe.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_sendRecipeActionPerformed(evt);
            }
        });

        btn_invoqueMethod.setText("Call InvoqueMethod");
        btn_invoqueMethod.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_invoqueMethodActionPerformed(evt);
            }
        });

        ComboMSB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No MSB available!"}));

        btn_DeviceRegistration.setText("Call DeviceRegistration");
        btn_DeviceRegistration.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_DeviceRegistrationActionPerformed(evt);
            }
        });

        btn_RecipeExecutionDone.setText("Call RecipeExecutionDone");
        btn_RecipeExecutionDone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_RecipeExecutionDoneActionPerformed(evt);
            }
        });

        btn_ChangedState.setText("Call ChangedState");
        btn_ChangedState.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_ChangedStateActionPerformed(evt);
            }
        });

        btn_sendrecipe2.setText("Call SendRecipe");
        btn_sendrecipe2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_sendrecipe2ActionPerformed(evt);
            }
        });

        btn_updatestatus.setText("Call StatusUpdate");
        btn_updatestatus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_updatestatusActionPerformed(evt);
            }
        });

        textToSend.setText("insert data to send");
        textToSend.setPreferredSize(new java.awt.Dimension(100, 22));
        textToSend.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                textToSendActionPerformed(evt);
            }
        });

        jLabel2.setText("OPC-UA Test Bed");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(LDSRegisterserver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OnOffRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(textToSend, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btn_SendURL, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_RequestProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_sendRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_invoqueMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboServers, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(27, 27, 27)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btn_updatestatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_sendrecipe2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_ChangedState, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_RecipeExecutionDone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ComboMSB, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_DeviceRegistration, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_start_discovery, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OnOffLDS, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(msb_opcua_servername)
                                .addComponent(StartMSBServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(OnOffServerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(LDSserverAddress))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {LDSRegisterserver, LDSserverAddress, StartMSBServer, btn_start_discovery, msb_opcua_servername});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msb_opcua_servername, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(OnOffServerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(StartMSBServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LDSserverAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LDSRegisterserver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(OnOffRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(OnOffLDS, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(btn_start_discovery, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboMSB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboServers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_DeviceRegistration)
                    .addComponent(btn_SendURL))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_RecipeExecutionDone)
                    .addComponent(btn_RequestProduct))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ChangedState)
                    .addComponent(btn_sendRecipe))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_sendrecipe2)
                    .addComponent(btn_invoqueMethod))
                .addGap(6, 6, 6)
                .addComponent(btn_updatestatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textToSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {LDSRegisterserver, StartMSBServer, btn_start_discovery});

        jTabbedPane1.addTab("OPCUA", jPanel1);

        l_ddsDomain.setText("DDS Configuration");

        b_startMSBDDS.setText("Start Domain");
        b_startMSBDDS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                b_startMSBDDSActionPerformed(evt);
            }
        });

        cb_DDSDevice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No devices available!"}));
        cb_DDSDevice.setEnabled(false);
        cb_DDSDevice.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cb_DDSDeviceActionPerformed(evt);
            }
        });

        cb_DDSRecipeList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No recipes available!"}));
        cb_DDSRecipeList.setEnabled(false);
        cb_DDSRecipeList.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cb_DDSRecipeListActionPerformed(evt);
            }
        });

        b_DDSCallRecipe.setText("Call");
        b_DDSCallRecipe.setEnabled(false);
        b_DDSCallRecipe.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                b_DDSCallRecipeActionPerformed(evt);
            }
        });

        l_testDDS.setText("Test DDS Call's");

        l_DDSDevice.setText("Device");

        l_DDSRecipe.setText("Recipe");

        javax.swing.GroupLayout p_ddsLayout = new javax.swing.GroupLayout(p_dds);
        p_dds.setLayout(p_ddsLayout);
        p_ddsLayout.setHorizontalGroup(
            p_ddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ddsLayout.createSequentialGroup()
                .addGroup(p_ddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_ddsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(p_ddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b_startMSBDDS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator3)))
                    .addGroup(p_ddsLayout.createSequentialGroup()
                        .addGroup(p_ddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(p_ddsLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(l_ddsDomain))
                            .addGroup(p_ddsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(l_testDDS, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(p_ddsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(l_DDSDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(l_DDSRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(p_ddsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cb_DDSDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cb_DDSRecipeList, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(b_DDSCallRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        p_ddsLayout.setVerticalGroup(
            p_ddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ddsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_ddsDomain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b_startMSBDDS)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_testDDS)
                .addGap(24, 24, 24)
                .addGroup(p_ddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_DDSDevice)
                    .addComponent(l_DDSRecipe))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_ddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_DDSDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_DDSRecipeList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_DDSCallRecipe))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("DDS", p_dds);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("MQTT", jPanel4);

        StartWebService.setText("Start");
        StartWebService.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                StartWebServiceActionPerformed(evt);
            }
        });

        WebServiceAddress.setText("http://0.0.0.0:9997/wsRecipesDeployer");
        WebServiceAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                WebServiceAddressActionPerformed(evt);
            }
        });

        OnOffWSPanel.setPreferredSize(new java.awt.Dimension(34, 31));

        javax.swing.GroupLayout OnOffWSPanelLayout = new javax.swing.GroupLayout(OnOffWSPanel);
        OnOffWSPanel.setLayout(OnOffWSPanelLayout);
        OnOffWSPanelLayout.setHorizontalGroup(
            OnOffWSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );
        OnOffWSPanelLayout.setVerticalGroup(
            OnOffWSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 31, Short.MAX_VALUE)
        );

        jLabel10.setText("Server Address:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(StartWebService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(WebServiceAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OnOffWSPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(OnOffWSPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(WebServiceAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StartWebService)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("WebService", jPanel5);

        jTabbedPane1.setSelectedComponent(jPanel1);

        l_openmosLogo.setSize(400, 100);
        l_openmosLogo.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/eu/openmos/msb/icons/OpenMos-logo-RGB-colours.png")).getImage().getScaledInstance(l_openmosLogo.getWidth(), l_openmosLogo.getHeight(), Image.SCALE_DEFAULT)) );

        p_productExecution.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pb_produceProduct.setText("Produce Product");
        pb_produceProduct.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pb_produceProductActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Product Execution");

        cb_productSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No recipes available!"}));
        cb_productSelection.setMaximumSize(new java.awt.Dimension(100, 30));

        javax.swing.GroupLayout p_productExecutionLayout = new javax.swing.GroupLayout(p_productExecution);
        p_productExecution.setLayout(p_productExecutionLayout);
        p_productExecutionLayout.setHorizontalGroup(
            p_productExecutionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_productExecutionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_productExecutionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_productExecutionLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(p_productExecutionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cb_productSelection, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pb_produceProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        p_productExecutionLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cb_productSelection, pb_produceProduct});

        p_productExecutionLayout.setVerticalGroup(
            p_productExecutionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_productExecutionLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_productSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pb_produceProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        p_productExecutionLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cb_productSelection, pb_produceProduct});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator4)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(145, 145, 145)
                                .addComponent(l_openmosLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1))
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 38, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addGap(302, 302, 302))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(p_productExecution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))))
            .addGroup(layout.createSequentialGroup()
                .addGap(493, 493, 493)
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(l_openmosLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_productExecution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void StartWebServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartWebServiceActionPerformed
        // TODO add your handling code here:
        String address = WebServiceAddress.getText();

        //myRecipeWS.publish(address, new RecipesDeployerImpl());
        //System.out.println("Listening: " + address);
        //opc_comms_log.setText("Starting WebServer...\n Listening on: "+ address);
        Thread wsThread = new Thread()
        {
            @Override
            public void run()
            {

                Endpoint myRecipeWS = Endpoint.publish(address, new RecipesDeployerImpl());
                System.out.println("Listening: " + address);
                opc_comms_log.append("Starting WebServer...\nListening on: " + address + "\n");
                Boolean status = myRecipeWS.isPublished();
                if (status)
                {
                    setConnectionColor(true, false, OnOffWSPanel, labelWS);
                    opc_comms_log.append("Recipes WebServer successfully published\n");
                } else
                {
                    setConnectionColor(false, true, OnOffWSPanel, labelWS);
                    opc_comms_log.append("Failed publishing Recipes WebServer\n");
                }
            }
        };

        wsThread.start();
    }//GEN-LAST:event_StartWebServiceActionPerformed

    /**
     *
     * @param evt
     */
    private void WebServiceAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WebServiceAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_WebServiceAddressActionPerformed


  private void b_startMSBDDSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_b_startMSBDDSActionPerformed
  {//GEN-HEADEREND:event_b_startMSBDDSActionPerformed

      DDSDeviceManager dm = DDSDeviceManager.getInstance();
      if (!this.isDDSRunning)
      {
          dm.addDevice("msb");
          dm.getDevice("msb").crateTopicReader("generalmethod", DDSDevice.TopicType.GENERALMETHODMESSAGE);
          dm.getDevice("msb").crateTopicReader("registo_fabio", DDSDevice.TopicType.STRINGMESSAGE);

          this.b_startMSBDDS.setText("Stop");
          this.p_dds.repaint();
          this.isDDSRunning = true;

      } else if (this.isDDSRunning)
      {
          this.b_startMSBDDS.setText("Start");
          this.isDDSRunning = false;
          dm.removeDevice("msb");

          cb_DDSDevice.removeAllItems();
          cb_DDSDevice.addItem("No devices available!");
          cb_DDSRecipeList.removeAllItems();
          cb_DDSRecipeList.addItem("No recipes available!");

          cb_DDSDevice.setEnabled(false);
          cb_DDSRecipeList.setEnabled(false);

          b_DDSCallRecipe.setEnabled(false);

          p_dds.repaint();
      }

  }//GEN-LAST:event_b_startMSBDDSActionPerformed

  private void b_DDSCallRecipeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_b_DDSCallRecipeActionPerformed
  {//GEN-HEADEREND:event_b_DDSCallRecipeActionPerformed
      DDSDeviceManager dm = DDSDeviceManager.getInstance();
      //dm.getDevice(cb_DDSDevice.getModel().getElementAt(cb_DDSDevice.getSelectedIndex()));
      try
      {
          int status;
          String deviceName = cb_DDSDevice.getSelectedItem().toString();
          String topic = cb_DDSRecipeList.getSelectedItem().toString();
          DDSDevice d = dm.getDevice(deviceName);
          d.createTopicWriter(topic, DDSDevice.TopicType.STRINGMESSAGE);
          DataWriter dwriter = d.getWriter(topic);
          StringMessageDataWriter listenerWriter = StringMessageDataWriterHelper.narrow(dwriter);
          StringMessage msgSample = new StringMessage();

          msgSample.device = deviceName;
          msgSample.args = "0";
          status = listenerWriter.write(msgSample, HANDLE_NIL.value);
          DDSErrorHandler.checkStatus(status, "StringMessageDataWriter.write");

      } catch (Exception ex)
      {
          System.out.println("à meu deus" + ex.toString());
      }

  }//GEN-LAST:event_b_DDSCallRecipeActionPerformed

  private void cb_DDSRecipeListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cb_DDSRecipeListActionPerformed
  {//GEN-HEADEREND:event_cb_DDSRecipeListActionPerformed
      // TODO add your handling code here:
  }//GEN-LAST:event_cb_DDSRecipeListActionPerformed

  private void cb_DDSDeviceActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cb_DDSDeviceActionPerformed
  {//GEN-HEADEREND:event_cb_DDSDeviceActionPerformed
      // TODO add your handling code here:
  }//GEN-LAST:event_cb_DDSDeviceActionPerformed

    /**
     *
     * @param evt
     */
  private void pb_produceProductActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pb_produceProductActionPerformed
  {//GEN-HEADEREND:event_pb_produceProductActionPerformed

  }//GEN-LAST:event_pb_produceProductActionPerformed

  private void textToSendActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_textToSendActionPerformed
  {//GEN-HEADEREND:event_textToSendActionPerformed
      // TODO add your handling code here:
  }//GEN-LAST:event_textToSendActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_updatestatusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_updatestatusActionPerformed
  {//GEN-HEADEREND:event_btn_updatestatusActionPerformed
      String ret = "null";
      //OPCDeviceItf DeviceITF = new OPCDeviceItf();
      try
      {
          ret = DeviceITF.AllCases("statusupdate", textToSend.getText()); //simulate a device registration

      } catch (ParserConfigurationException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call statusupdate method: " + ex + "\n");
      } catch (SAXException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call statusupdate method: " + ex + "\n");
      } catch (IOException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call statusupdate method: " + ex + "\n");
      } catch (JAXBException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call statusupdate method: " + ex + "\n");
      } catch (TransformerException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
      }
      opc_comms_log.append("statusupdate method called. Returned: " + ret + "\n");
  }//GEN-LAST:event_btn_updatestatusActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_sendrecipe2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_sendrecipe2ActionPerformed
  {//GEN-HEADEREND:event_btn_sendrecipe2ActionPerformed
      String ret = "null";
      //OPCDeviceItf DeviceITF = new OPCDeviceItf();
      try
      {
          ret = DeviceITF.AllCases("sendRecipe", textToSend.getText()); //simulate a sendRecipe

      } catch (ParserConfigurationException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call sendRecipe method: " + ex + "\n");
      } catch (SAXException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call sendRecipe method: " + ex + "\n");
      } catch (IOException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call sendRecipe method: " + ex + "\n");
      } catch (JAXBException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call sendRecipe method: " + ex + "\n");
      } catch (TransformerException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
      }
      opc_comms_log.append("sendRecipe method called. Returned: " + ret + "\n");
  }//GEN-LAST:event_btn_sendrecipe2ActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_ChangedStateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_ChangedStateActionPerformed
  {//GEN-HEADEREND:event_btn_ChangedStateActionPerformed
      String ret = "null";
      try
      {
          ret = DeviceITF.AllCases("changedstate", textToSend.getText()); //simulate a device registration
      } catch (ParserConfigurationException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call changed state method: " + ex + "\n");
      } catch (SAXException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call changed state method: " + ex + "\n");
      } catch (IOException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call changed state method: " + ex + "\n");
      } catch (JAXBException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call changed state method: " + ex + "\n");
      } catch (TransformerException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
      }
      opc_comms_log.append("Changed State method called. Returned: " + ret + "\n");
  }//GEN-LAST:event_btn_ChangedStateActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_RecipeExecutionDoneActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_RecipeExecutionDoneActionPerformed
  {//GEN-HEADEREND:event_btn_RecipeExecutionDoneActionPerformed
      String ret = "null";
      //OPCDeviceItf DeviceITF = new OPCDeviceItf();
      try
      {
          ret = DeviceITF.AllCases("recipeexecutiondone", textToSend.getText()); //simulate a device registration

      } catch (ParserConfigurationException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call RecipeExecutionDone method: " + ex + "\n");
      } catch (SAXException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call RecipeExecutionDone method: " + ex + "\n");
      } catch (IOException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call RecipeExecutionDone method: " + ex + "\n");
      } catch (JAXBException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call RecipeExecutionDone method: " + ex + "\n");
      } catch (TransformerException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
      }
      opc_comms_log.append("RecipeExecutionDone method called. Returned: " + ret + "\n");
  }//GEN-LAST:event_btn_RecipeExecutionDoneActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_DeviceRegistrationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_DeviceRegistrationActionPerformed
  {//GEN-HEADEREND:event_btn_DeviceRegistrationActionPerformed
      String ret = "null";
      //OPCDeviceItf DeviceITF = new OPCDeviceItf();
      try
      {
          ret = DeviceITF.AllCases("deviceregistration", textToSend.getText()); //simulate a device registration

      } catch (ParserConfigurationException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call device registration method: " + ex + "\n");
      } catch (SAXException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call device registration method: " + ex + "\n");
      } catch (IOException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call device registration method: " + ex + "\n");
      } catch (JAXBException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
          opc_comms_log.append("Failed to call device registration method: " + ex + "\n");
      } catch (TransformerException ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
      }
      opc_comms_log.append("Device registration method called. Returned: " + ret + "\n");
  }//GEN-LAST:event_btn_DeviceRegistrationActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_invoqueMethodActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_invoqueMethodActionPerformed
  {//GEN-HEADEREND:event_btn_invoqueMethodActionPerformed
      /*
     * String deviceName = String.valueOf(comboServers.getSelectedItem()); //ver hashmap e chamar o metodo DACManager
     * myOpcUaClientsMap = DACManager.getInstance(); //singleton to access client objects in other classes
     * MSB_MiloClientSubscription MSBcs = myOpcUaClientsMap.getOPCclientIDMaps().get(deviceName);
     *
     * //TESTE INVOQUESKILL NodeId objectId = NodeId.parse("ns=2;s=Pre-Demonstrator_InstanceHierarchy/" +
     * "AssemblySystem/WorkStation/SC1:Task_slow_Recipe"); NodeId methodId =
     * NodeId.parse("ns=2;s=InvokeSkill/SC1:Task_slow_Recipe");
     *
     * MSBcs.InvoqueDeviceSkill(MSBcs.milo_client_instanceMSB, objectId, methodId).exceptionally(ex -> {
     * System.out.println("error invoking Call InvoqueMethod() for server: " + ex); //logger.error("error invoking
     * SendServerURL()", ex); return "-1.0"; }).thenAccept(v -> { //logger.info("SendServerURL(cenas)={}", v);
     * System.out.println("Call InvoqueMethod(uri)={}\n" + v); //future.complete(client); });
       */
  }//GEN-LAST:event_btn_invoqueMethodActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_sendRecipeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_sendRecipeActionPerformed
  {//GEN-HEADEREND:event_btn_sendRecipeActionPerformed
      /*
     * String deviceName = String.valueOf(comboServers.getSelectedItem()); //ver hashmap e chamar o metodo DACManager
     * myOpcUaClientsMap = DACManager.getInstance(); //singleton to access client objects in other classes
     * MSB_MiloClientSubscription MSBcs = myOpcUaClientsMap.getOPCclientIDMaps().get(deviceName);
     *
     * MSBcs.SendRecipetoDevice(MSBcs.milo_client_instanceMSB, textToSend.getText()).exceptionally(ex -> {
     * System.out.println("error invoking SendRecipetoDevice() for server: " + deviceName + "\n" + ex);
     * //logger.error("error invoking SendServerURL()", ex); opc_comms_log.append("error invoking SendRecipetoDevice()
     * for server: " + deviceName + "\n" + ex + "\n"); return "-1.0"; }).thenAccept(v -> {
     * //logger.info("SendServerURL(cenas)={}", v); System.out.println("SendRecipetoDevice(uri)={}\n" + v);
     * opc_comms_log.append("SendRecipetoDevice(uri)={}\n" + v); //future.complete(client); });
       */
  }//GEN-LAST:event_btn_sendRecipeActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_RequestProductActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_RequestProductActionPerformed
  {//GEN-HEADEREND:event_btn_RequestProductActionPerformed

  }//GEN-LAST:event_btn_RequestProductActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_SendURLActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_SendURLActionPerformed
  {//GEN-HEADEREND:event_btn_SendURLActionPerformed
      /*
     * String deviceName = String.valueOf(comboServers.getSelectedItem()); //ver hashmap e chamar o metodo DACManager
     * myOpcUaClientsMap = DACManager.getInstance(); //singleton to access client objects in other classes
     * MSB_MiloClientSubscription MSBcs = myOpcUaClientsMap.getOPCclientIDMaps().get(deviceName);
     *
     * MSBcs.SendServerURL(MSBcs.milo_client_instanceMSB, MSB_OPCUA_SERVER_ADDRESS).exceptionally(ex -> {
     * System.out.println("error invoking SendServerURL() for server: " + deviceName + "\n" + ex); //logger.error("error
     * invoking SendServerURL()", ex); opc_comms_log.append("error invoking SendServerURL() for server: " + deviceName +
     * "\n" + ex + "\n"); return "-1.0"; }).thenAccept(v -> { //logger.info("SendServerURL(cenas)={}", v);
     * System.out.println("SendServerURL(uri)={}\n" + v); opc_comms_log.append("SendServerURL(uri)={}\n" + v);
     * //future.complete(client); });
     *
       */
  }//GEN-LAST:event_btn_SendURLActionPerformed

    /**
     *
     * @param evt
     */
  private void comboServersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_comboServersActionPerformed
  {//GEN-HEADEREND:event_comboServersActionPerformed
      // TODO add your handling code here:
  }//GEN-LAST:event_comboServersActionPerformed

    /**
     *
     * @param evt
     */
  private void btn_start_discoveryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_start_discoveryActionPerformed
  {//GEN-HEADEREND:event_btn_start_discoveryActionPerformed
      //launch Discovery Service to search for other devices (OPCUA servers from devices)

      OPCDeviceDiscoveryItf OPCdevDiscItf;
      OPCdevDiscItf = new OPCDeviceDiscoveryItf()
      {

          /**
           *
           * @param name
           * @param app_uri
           */
          @Override
          public void on_new_server(String name, String app_uri)
          {
              System.out.println("server found: " + app_uri);

              labelLDS.setIcon(greenLight); //blink status LED every polling cycle
              try
              {
                  Thread.sleep(1000);
              } catch (InterruptedException ex)
              {
                  Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
              }
              labelLDS.setIcon(greyLight);
          }

          /**
           *
           * @param app_uri Device Adapter address
           */
          @Override
          public void on_new_endpoint(String name, String app_uri)
          {
              // every time there is a "polling" cycle in discovery, check OPC client/server connections
              System.out.println("[on_new_endpoint]  POLLING CYCLE  ");
              System.out.println("NAME: " + name + " URL: " + app_uri);

              DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(name);

              // if the device name doesn't exist in the database, register
              if (da == null)
              {
                  try
                  {
                      DACManager.getInstance().addDeviceAdapter(name, EProtocol.OPC, "", "");

                      // visual
                      addToTableAdapters(name, "opcua", app_uri);
                      opc_comms_log.append("New Server found and registered: " + name + " " + app_uri + "\n");
                      System.out.println("onNewServer Registered Server: " + name + " " + app_uri);

                      if (name.contains("MSB"))
                      {
                          MSB_OPCUA_SERVER_ADDRESS = app_uri;
                          // visual
                          ComboMSB.addItem(name);
                      }

                      // TODO af-silva validate this
                      ApplicationDescription[] serverList = UaTcpStackClient.findServers(app_uri).get(); //new MSB            
                      if (serverList[0].getApplicationType() != ApplicationType.DiscoveryServer)
                      {
                          System.out.println("olas");
                          DACManager manager = DACManager.getInstance();
                          DeviceAdapterOPC opc = (DeviceAdapterOPC) manager.getDeviceAdapter(name);
                          MSB_MiloClientSubscription instance = opc.getClient();

                          //start connection after inserting on the hashmap!
                          instance.startConnection(app_uri);

                          // Iterate over all values, using the keySet method.
                          // call SendServerURL() method from device
                          OpcUaClient client = instance.getClientObject();
                          if (!name.contains("MSB"))
                          {
                              // visual
                              comboServers.addItem(name);

                              // send server url to Device Adatper
                              instance.SendServerURL(client, MSB_OPCUA_SERVER_ADDRESS).exceptionally(ex
                                      ->
                              {
                                  System.out.println("error invoking SendServerURL() for server: " + name + "\n" + ex);
                                  //logger.error("error invoking SendServerURL()", ex);
                                  return "-1.0";
                              }).thenAccept(v
                                      ->
                              {
                                  //logger.info("SendServerURL(cenas)={}", v);
                                  System.out.println("SendServerURL(uri)={}\n" + v);
                                  //future.complete(client);
                              });

                          }

                          if (client == null)
                          {
                              System.out.println("Client = null?");
                          }

                      }
                  } catch (InterruptedException ex)
                  {
                      Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (ExecutionException ex)
                  {
                      Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);

                  } catch (Exception ex)
                  {
                      Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
                  }
              } else
              {
                  // TODO af-silva 
                  // if the device name exist in the database, check if it is still available
                  System.out.println("Server : " + name + " " + app_uri + " Already registered in the Database!");
              }
          }

          /**
           *
           */
          @Override
          public void on_endpoint_dissapeared()
          {
              //To change body of generated methods, choose Tools | Templates.
              throw new UnsupportedOperationException("Not supported yet.");
          }

          /**
           *
           */
          @Override
          public void reset_tables()
          {
              // dropServersAndEndpoints(); //ver
              System.out.println("reset endpoint and servers tables...");
          }

          /**
           *
           * @param name
           * @param app_uri
           */
          @Override
          public void on_server_dissapeared(String name, String app_uri)
          {
              System.out.println("This server has disapeared: " + name);
              if (removeDownServer(name))
              {
                  opc_comms_log.append("The server: " + name + " has disapeared and has been successfully removed from database.\n");
                  //deleteFromTableServers(name, "", app_uri);
                  CleanTablesFromWorkstation(name);
                  //wait for completion? warning
                  try
                  {
                      Thread.sleep(1000);
                  } catch (InterruptedException ex)
                  {
                      Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
                  }
              } else
              {
                  opc_comms_log.append("The server: " + name + " has disapeared and has NOT been removed from database.\n");
              }
          }
      };

      try
      {
          // String LDS_uri="opc.tcp://localhost:4840";
          String LDS_uri = LDSserverAddress.getText();

          OpcUaServersDiscoverySnippet OPCDiscoverySnippet = new OpcUaServersDiscoverySnippet(LDS_uri, OPCdevDiscItf);
          OPCDiscoverySnippet.start();
      } catch (Exception ex)
      {
          System.out.println("boda");
      }

      //TODO
      //create a new OPCDevice with structure methods - common to all OPCcommunication?
  }//GEN-LAST:event_btn_start_discoveryActionPerformed

    /**
     *
     * @param evt
     */
  private void LDSRegisterserverActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_LDSRegisterserverActionPerformed
  {//GEN-HEADEREND:event_LDSRegisterserverActionPerformed
      int ret = 0;
      try
      {
          ret = opcuaServerInstanceMILO.register(LDSserverAddress.getText());
          opc_comms_log.append("Registering MSB OPCUA Milo Server on the LDS server...\n");
          if (ret == 1)
          {
              opc_comms_log.append("Success\n");
              setConnectionColor(true, false, OnOffRegister, labelRegister);
          } else
          {
              opc_comms_log.append("Failed to register MSB OPCUA Milo Server on the LDS server!\n");
              setConnectionColor(false, true, OnOffRegister, labelRegister);
          }
      } catch (Exception ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
      }
  }//GEN-LAST:event_LDSRegisterserverActionPerformed

    /**
     *
     * @param evt
     */
  private void LDSserverAddressActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_LDSserverAddressActionPerformed
  {//GEN-HEADEREND:event_LDSserverAddressActionPerformed
      // TODO add your handling code here:
  }//GEN-LAST:event_LDSserverAddressActionPerformed

    /**
     *
     * @param evt
     */
  private void StartMSBServerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_StartMSBServerActionPerformed
  {//GEN-HEADEREND:event_StartMSBServerActionPerformed
      try
      {
          opc_comms_log.append("Starting MSB OPCUA Milo Server...\n");

          opcuaServerInstanceMILO = new opcuaServerMSB(msb_opcua_servername.getText()); //new MSB Milo server

          //launch MILO MSB OPCUA Server endpoint
          if (opcuaServerInstanceMILO.control == false)
          {
              try
              {
                  opcuaServerInstanceMILO.startup().get();
                  opc_comms_log.append("Server created. \n");
                  setConnectionColor(true, false, OnOffServerPanel, labelServer);
              } catch (InterruptedException | ExecutionException ex)
              {
                  Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, "[StartMSBServerActionPerformed] Exception: " + ex);
                  opc_comms_log.append("[StartMSBServerActionPerformed] Exception: " + ex);
                  setConnectionColor(false, true, OnOffServerPanel, labelServer);
              }
          } else
          {
              System.out.println("MSB Server already created!\n");
              opc_comms_log.append("MSB Server already created!\n");
          }

          try
          {//test to see if the server is running at the first discovery
              Thread.sleep(500);
          } catch (InterruptedException ex)
          {
              Thread.currentThread().interrupt();
          }

      } catch (Exception ex)
      {
          Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, "[StartMSBServerActionPerformed] Exception: " + ex);
      }
  }//GEN-LAST:event_StartMSBServerActionPerformed

    /**
     *
     * @param evt
     */
  private void StartMSBServerMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_StartMSBServerMouseClicked
  {//GEN-HEADEREND:event_StartMSBServerMouseClicked

  }//GEN-LAST:event_StartMSBServerMouseClicked

    /**
     *
     * @param evt
     */
  private void msb_opcua_servernameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_msb_opcua_servernameActionPerformed
  {//GEN-HEADEREND:event_msb_opcua_servernameActionPerformed
      // TODO add your handling code here:
  }//GEN-LAST:event_msb_opcua_servernameActionPerformed

    /**
     *
     * @param ServerName
     * @return
     */
    public boolean removeDownServer(String ServerName)
    {
        return DACManager.getInstance().deleteDeviceAdapter(ServerName);
    }

    /**
     *
     */
    public static void fillRecipesTable()
    {
        DACManager instance = DACManager.getInstance(); //singleton to access hashmaps in other classes
        List<String> devices = instance.getDeviceAdapters();
        for (String daName : devices)
        {
            ArrayList<DaRecipe> list = instance.getRecipesFromDevice(daName);
            for (DaRecipe r : list)
            {
                addToTableRecipes(r.getName(), Boolean.parseBoolean(r.getValid()), daName); //add each product from the list for each workstation
            }
        }
    }

    /**
     * [TODO] Test af-silva
     */
    public static void fillDDSCombos()
    {
//    DACManager instance = DACManager.getInstance(); //singleton to access hashmaps in other classes
//
//    ArrayList<String> devices = instance.listDevicesByProtocol(Protocol.DDS);
//    cb_DDSDevice.removeAllItems();
//    cb_DDSRecipeList.removeAllItems();
//    for (String key : devices)
//    {
//      cb_DDSDevice.addItem(key);
//      Map<String, String> list = instance.getRecipesFromDevice(key);
//      for (String recipe : list.keySet())
//      {
//        cb_DDSRecipeList.addItem(list.get(key));
//      }
//    }
//
//    cb_DDSRecipeList.setEnabled(true);
//    cb_DDSDevice.setEnabled(true);
//    b_DDSCallRecipe.setEnabled(true);
//    p_dds.repaint();
    }

    /**
     *
     */
    public static void fillDevicesTable()
    {
        DACManager instance = DACManager.getInstance();
        List<String> adapters = instance.getDeviceAdapters();
        for (String adapter : adapters)
        {
            List<DaDevice> devices = instance.getDevicesFromDeviceAdapter(adapter);
            for (DaDevice device : devices)
            {
                addToTableDevice(device.getName(), (device.getStatus().equals("1")), device.getAddress(), adapter);
            }
        }
    }

    /**
     *
     * @param serverName
     * @param protocol
     * @param serverUri
     */
    private void addToTableAdapters(String serverName, String protocol, String serverUri)
    {
        adaptersTableModel.addRow(new Object[]
        {
            serverName, protocol, serverUri
        });
        Object[] rowData = new Object[adaptersTableModel.getColumnCount()];
        for (int i = 0; i < adaptersTableModel.getColumnCount(); i++)
        {
            rowData[i] = adaptersTableModel.getValueAt(0, i);
        }
        opc_comms_log.append("Server successfully added to table. Name: " + serverName + "URL: " + serverUri + "\n");
    }

    /**
     * add a row to tableProduct
     *
     * @param recipeName
     * @param status
     * @param deviceAdapterName
     */
    public static void addToTableRecipes(String recipeName, Boolean status, String deviceAdapterName)
    {
        recipesTableModel.addRow(new Object[]
        {
            recipeName, status, deviceAdapterName
        });
    }

    /**
     * add a row to DeviceTable
     *
     * @param DeviceName
     * @param Status
     * @param Endpoint
     * @param WorkStation
     */
    public static void addToTableDevice(String DeviceName, Boolean Status, String Endpoint, String WorkStation)
    {
        devicesTableModel.addRow(new Object[]
        {
            DeviceName, Status, Endpoint, WorkStation
        });
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object[] rowData = new Object[devicesTableModel.getColumnCount()];
        for (int i = 0; i < devicesTableModel.getColumnCount(); i++)
        {
            rowData[i] = devicesTableModel.getValueAt(0, i);
            System.out.println("Devices NA TABELA: " + rowData[i].toString());
        }
        opc_comms_log.append("Devices successfully added to table. Name: " + DeviceName + "\n");
    }

    /**
     * @todo this is to use??????
     * @param serverName
     * @param protocol
     * @param serverUri
     */
    private void deleteFromTableAdapters(String serverName, String protocol, String serverUri)
    {
        int indexToRemove = -1;
        for (int i = 0; i < adaptersTableModel.getRowCount(); i++)
        {//For each row
            for (int j = 0; j < adaptersTableModel.getColumnCount(); j++)
            {//For each column in that row
                if (adaptersTableModel.getValueAt(i, j).equals(serverName))
                {//Search the model
                    System.out.println("FOUND SERVER TO DELETE from table: " + adaptersTableModel.getValueAt(i, j));//Print if found string
                    indexToRemove = i;
                    adaptersTableModel.removeRow(indexToRemove);
                    i--;
                }
            }//For inner loop 
        }//For outer loop 

        if (indexToRemove != -1)
        {
            opc_comms_log.append("SERVER DELETED from table: " + serverName + "\n");
        } else
        {
            opc_comms_log.append("SERVER TO DELETE NOT FOUND from table: " + serverName + "\n");
        }
        comboServers.removeItem(serverName);
    }

    /**
     * @todo this is to use??????
     * @param productID
     * @param recipeID
     * @param workstationName
     */
    private static void deleteFromTableRecipes(String productID, String recipeID, String workstationName)
    {
        int indexToRemove = -1;
        for (int i = 0; i < recipesTableModel.getRowCount(); i++)
        {//For each row
            for (int j = 0; j < recipesTableModel.getColumnCount(); j++)
            {//For each column in that row
                if (recipesTableModel.getValueAt(i, j).equals(workstationName))
                {//Search the model
                    indexToRemove = i;
                    recipesTableModel.removeRow(indexToRemove);
                    i--;
                }
            }//For inner loop 
        }//For outer loop 

        if (indexToRemove != -1)
        {
            opc_comms_log.append("RECIPE DELETED from table: " + productID + "\n");
        } else
        {
            opc_comms_log.append("RECIPE TO DELETE NOT FOUND from table: " + productID + "\n");
        }
    }

    /**
     * @todo this is to use??????
     * @param DeviceName
     * @param status
     * @param endpoint
     * @param workstationName
     */
    private static void deleteFromTableDevices(String DeviceName, Boolean status, String endpoint, String workstationName)
    {
        //delete from DeviceName? status? or endpoint? or all?
        //TODO

        int indexToRemove = -1;
        for (int i = 0; i < devicesTableModel.getRowCount(); i++)
        {//For each row
            for (int j = 0; j < devicesTableModel.getColumnCount(); j++)
            {//For each column in that row
                if (devicesTableModel.getValueAt(i, j).equals(workstationName))
                {//Search the model
                    System.out.println("FOUND PRODUCT TO DELETE from table: " + devicesTableModel.getValueAt(i, j) + " at row:" + i + " col:" + j);//Print if found string
                    indexToRemove = i;
                    devicesTableModel.removeRow(indexToRemove);
                    i--;
                }
            }//For inner loop 
        }//For outer loop 

        if (indexToRemove != -1)
        {
            System.out.println("DEVICE DELETED from table: " + workstationName);//Print if server found 
            opc_comms_log.append("DEVICE DELETED from table: " + workstationName + "\n");
        } else
        {
            System.out.println("DEVICE TO DELETE NOT FOUND from table: " + workstationName);//Print if server not found 
            //opc_comms_log.append("DEVICE TO DELETE NOT FOUND from table: " + DeviceName + "\n");
        }

    }

    /**
     *
     */
    private void setImage()
    {
        //faz o set das images e dimensiona se acordo com o tamanho do panel
        BufferedImage img = null;
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try
        {
            img = ImageIO.read(new File("images/green-circle.png"));
            img1 = ImageIO.read(new File("images/red.png"));
            img2 = ImageIO.read(new File("images/glossy-gray.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Image dimg = img.getScaledInstance(34, 31, Image.SCALE_SMOOTH);
        Image dimg2 = img1.getScaledInstance(34, 31, Image.SCALE_SMOOTH);
        Image dimg3 = img2.getScaledInstance(34, 31, Image.SCALE_SMOOTH);
        greenLight = new ImageIcon(dimg);
        redLight = new ImageIcon(dimg2);
        greyLight = new ImageIcon(dimg3);
    }

    /**
     *
     * @param con
     * @param lastState
     * @param panel
     * @param label
     * @return
     */
    private boolean setConnectionColor(boolean con, boolean lastState, JPanel panel, JLabel label)
    {
        // verifica o estado da ligaçao ao servidor e altera a imagem de acordo com a mesma
        if (con != lastState)
        {

            if (con == true)
            {

                panel.removeAll();
                label.setIcon(greenLight);
                panel.add(label);
                panel.revalidate();
                panel.repaint();
            } else if (con == false)
            {

                panel.removeAll();
                label.setIcon(redLight);
                panel.add(label);
                panel.revalidate();
                panel.repaint();
            }
            return con;
        }
        return lastState;
    }

    /**
     *
     */
    private void threadCheckServersConnection()
    {
        //thread da verificaçao da ligaçao ao servidor do plc
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                /*
         * boolean aux1 = setConnectionColor(SubscriptionPLC.Connection, SubscriptionPLC.lastState, OnOffPLCPanel,
         * labelPLC); if (aux1 == SubscriptionPLC.Connection) { SubscriptionPLC.lastState = aux1; }
                 */
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void update(java.util.Observable o, Object arg)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * reimplementation of modellistener for checkbox value checking
     */
    public class CheckBoxModelListener implements TableModelListener
    {

        public void tableChanged(TableModelEvent e)
        {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 2)
            { //column of the checkbox
                TableModel model = (TableModel) e.getSource();
                String columnName = model.getColumnName(column);
                Boolean checked = (Boolean) model.getValueAt(row, column);
                String recipeID = (String) model.getValueAt(row, 1);
                if (checked)
                {
                    System.out.println(columnName + ": " + true);
                    opc_comms_log.append("SendChanges:" + columnName + " of " + recipeID + " " + true + "\n");
                } else
                {
                    System.out.println(columnName + ": " + false);
                    opc_comms_log.append("SendChanges:" + columnName + " of " + recipeID + " " + false + "\n");
                }
            }
        }
    }

    /**
     *
     * @param text
     */
    public void setLogText(String text)
    {
        opc_comms_log.append(text + '\n');
    }

    /**
     *
     * @param WorkstationName
     * @return
     */
    public String CleanTablesFromWorkstation(String WorkstationName)
    {
        deleteFromTableDevices(null, null, null, WorkstationName);
        deleteFromTableRecipes(null, null, WorkstationName);
        deleteFromTableAdapters(WorkstationName, null, null);

        return "OK";
    }

    /**
     *
     * @param ServerName
     * @return
     */
    public static int RemoveDownServer(String ServerName)
    {
        if (DACManager.getInstance().deleteDeviceAdapter(ServerName))
        {
            System.out.println("DownServer successfully deleted from DB!");
            return 1;
        } else
        {
            System.out.println("ERROR deleting DownServer from DB!");
            return -1;
        }
    }

    // **************************************************************************************************************** //
    // **************************************************************************************************************** //
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {

        //Set the Nimbus look and feel
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(MSB_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(MSB_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(MSB_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MSB_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /*
     * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            /**
             *
             */
            public void run()
            {

                try
                {
                    new MSB_gui().setVisible(true);
                } catch (Exception ex)
                {
                    Logger.getLogger(MSB_gui.class.getName()).log(Level.SEVERE, null, ex);
                }

                jScrollPane1.getVerticalScrollBar().setAutoscrolls(true);

                DefaultCaret caret = (DefaultCaret) opc_comms_log.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboMSB;
    private javax.swing.JButton LDSRegisterserver;
    private javax.swing.JTextField LDSserverAddress;
    private javax.swing.JPanel OnOffLDS;
    private javax.swing.JPanel OnOffRegister;
    private javax.swing.JPanel OnOffServerPanel;
    private javax.swing.JPanel OnOffWSPanel;
    private javax.swing.JButton StartMSBServer;
    private javax.swing.JButton StartWebService;
    private javax.swing.JTable TableServers;
    private javax.swing.JTextField WebServiceAddress;
    private static javax.swing.JButton b_DDSCallRecipe;
    private javax.swing.JButton b_startMSBDDS;
    private javax.swing.JButton btn_ChangedState;
    private javax.swing.JButton btn_DeviceRegistration;
    private javax.swing.JButton btn_RecipeExecutionDone;
    private javax.swing.JButton btn_RequestProduct;
    private javax.swing.JButton btn_SendURL;
    private javax.swing.JButton btn_invoqueMethod;
    private javax.swing.JButton btn_sendRecipe;
    private javax.swing.JButton btn_sendrecipe2;
    private javax.swing.JButton btn_start_discovery;
    private javax.swing.JButton btn_updatestatus;
    private static javax.swing.JComboBox<String> cb_DDSDevice;
    private static javax.swing.JComboBox<String> cb_DDSRecipeList;
    private javax.swing.JComboBox<String> cb_productSelection;
    private java.awt.Choice choice1;
    private java.awt.Choice choice2;
    private java.awt.Choice choice3;
    private javax.swing.JComboBox<String> comboServers;
    private javax.swing.JTable devicesTable;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private static javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel l_DDSDevice;
    private javax.swing.JLabel l_DDSRecipe;
    private javax.swing.JLabel l_ddsDomain;
    private static javax.swing.JLabel l_openmosLogo;
    private javax.swing.JLabel l_testDDS;
    private javax.swing.JTextField msb_opcua_servername;
    private static javax.swing.JTextArea opc_comms_log;
    private static javax.swing.JPanel p_dds;
    private javax.swing.JPanel p_productExecution;
    private static javax.swing.JButton pb_produceProduct;
    private javax.swing.JTable recipesTable;
    private javax.swing.JTextField textToSend;
    // End of variables declaration//GEN-END:variables
}
//EOF
