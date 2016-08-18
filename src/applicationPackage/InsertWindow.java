package applicationPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import org.apache.poi.util.SystemOutLogger;

import com.mxrck.autocompleter.TextAutoCompleter;

import applicationPackage.ExcelFrame;

import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class InsertWindow {

    
    private static JTable testTable;
    //init Frame and springLayout
    public JFrame frmInsertAsset = new JFrame();
    private SpringLayout springLayout = new SpringLayout();
    private JPanel g1_Jpanel;
    private static Connection conn;
    //Global Variables
    private String     field3InputString;
    private String numSwap;
    private String autoIDString;
    private String deleteItemString;
    private TextAutoCompleter complete;
    int[] selection;
    String excelString;

    // instantiating textfields for each jlabel
    private JTextField field1 = new JTextField();
    private JTextField field2a = new JTextField();
    private JTextField field2b = new JTextField();

    private JComboBox field3 = new JComboBox();
    private JTextField field4 = new JTextField();
    private JTextField field5 = new JTextField();
    private JTextField field6 = new JTextField();    
    private JComboBox field7 = new JComboBox();

    private JButton updateBtn;
    private JButton clearBtn;
    private JButton excelBtn;
    private JScrollPane scrollPane;
    private JButton insertBtn;
    private JButton deleteBtn;
    private static JLabel totalLabel;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    	
        EventQueue.invokeLater(new Runnable() {
            public void run() {
            	
                try {
                	
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Segoe UI Semilight", Font.PLAIN, 20)));
                    InsertWindow window = new InsertWindow();
                    //    window.display();
                    window.frmInsertAsset.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public InsertWindow() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        super();
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Segoe UI Semilight", Font.PLAIN, 20)));
        initialize();
        //UpDateTable();

    }

    
    /**
     * @throws SQLException 
     * @wbp.parser.entryPoint
     */
    private void initialize() throws SQLException {
    	
    	field1.setFont(new Font("Arial", Font.BOLD, 14));
    	//init objects
    	conn= MySQLConnection.dbConnector();
	    complete=new TextAutoCompleter(field1);

        //Components
        JScrollPane scrollPane_1 = new JScrollPane();
        updateBtn = new JButton("Update");
        insertBtn = new JButton("Insert");
        clearBtn = new JButton("Clear Fields");
        excelBtn = new JButton("Export to Excel");
        deleteBtn = new JButton("Delete");
        springLayout.putConstraint(SpringLayout.WEST, deleteBtn, 70, SpringLayout.EAST, updateBtn);
        springLayout.putConstraint(SpringLayout.EAST, deleteBtn, -78, SpringLayout.WEST, scrollPane_1);

        //Setting up Gui's frame and components
        setupFrame();
        setUpFrameComponents(scrollPane_1,  updateBtn, clearBtn);
        gridLayoutSetup();
        setupTable(scrollPane_1);
        addTextBoxFields();
        addTypes();
        getTypes();
        getInputFromFields();
        actionPerformedBtn();
        autoComplete();

        frmInsertAsset.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    //System.out.println("Closed");
                    MainScreen.frame.setVisible(true);
                    frmInsertAsset.dispose();

                }
            });
        
    }
    private void actionPerformedBtn() {
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	String var = "update";
                    updateItems(var);
                    
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        excelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(selection != null)
            	{
	            	buildExcelString();
	            	ConvertExcel ec = new ConvertExcel();
	            	try {
						ec.exportExcel(excelString);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
            	else
            	{
                    JOptionPane.showMessageDialog(null, "You need to highlight the rows on the table, in order to export."  , "Error", JOptionPane.ERROR_MESSAGE);
            	}
            		
            	

            }

			private void buildExcelString() {
			    String getId = "";
            	ArrayList<String> IDs = new ArrayList<String>();

                if(selection != null)
            	{
            		for(int i=0; i<selection.length; i++){
            			getId = "'" + String.valueOf(testTable.getModel().getValueAt(selection[i], 8)) + "'";
            			IDs.add(getId);
            			//gets aisle,row, colum,depth

            			if(i==selection.length-1){
            			} else{
            				IDs.add(", ");
            			}

            		}
            		StringBuilder sb = new StringBuilder();
            		for (String s : IDs)
            		{
            			sb.append(s);
            			//sb.append(", ");
            		}

            		//System.out.println(sb.toString());
            		excelString = "SELECT `AssociationName`, `StartYear`, `EndYear`, `Type`, `Aisle`,`Column`, "
            				+ "`Row`, `Depth`, `ID`,`LastModified`"
            				+ " FROM new_schema.ResortManagement"
            				+ " WHERE `ID` IN (" + sb.toString() + ")";
            	 System.out.println(excelString);


            	}    
				
			}
        });
//        insertBtn.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                
//            }
//         
//        });
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	String var = "delete";
                    updateItems(var);
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }        
            }

         
        });
        
    }
    
    private void updateItems(String key) throws SQLException {

    	String defaultField1 = null;
    	String defaultField2a = null;
    	String defaultField2b = null;
    	String defaultField3 = null;
    	//delete

    
    	if(key.equals("delete"))
    	{
    		defaultField1 = "********";
    		defaultField2a = "0000";
    		defaultField2b = "0000";
    		defaultField3 = "";
    				
    	}
    	else
    	{
    		defaultField1 = field1.getText();
    		defaultField2a = field2a.getText();
    		defaultField2b = field2b.getText();
    		defaultField3 =  field3.getSelectedItem().toString();
    	}
    	String updateFieldsSQL;
    	String getId = "";
    	PreparedStatement prepareUpdate = null;
    	ArrayList<String> IDs = new ArrayList<String>();

    	if(selection != null)
    	{
    		for(int i=0; i<selection.length; i++){
    			getId = "'" + String.valueOf(testTable.getModel().getValueAt(selection[i], 8)) + "'";
    			IDs.add(getId);
    			//gets aisle,row, colum,depth

    			if(i==selection.length-1){
    			} else{
    				IDs.add(", ");
    			}

    		}
    		StringBuilder sb = new StringBuilder();
    		for (String s : IDs)
    		{
    			sb.append(s);
    			//sb.append(", ");
    		}

    		//System.out.println(sb.toString());
    			updateFieldsSQL = "UPDATE `new_schema`.`ResortManagement` SET `AssociationName`='" + defaultField1 +"', "
        				+ "`StartYear`='" + defaultField2a + "', "
        				+ "`EndYear`='" + defaultField2b + "', "
        				+ "`Type`='" + defaultField3 + "' "
        				+ " WHERE `ID` IN (" + sb.toString() + ")";
        		System.out.println(updateFieldsSQL);
        		
        		prepareUpdate = conn.prepareStatement(updateFieldsSQL);
        		prepareUpdate.executeUpdate();
        		prepareUpdate.getConnection().commit();
            	prepareUpdate.close();

    	}
    	//UpDateTable();
    	addTypes();
    	UpDateTable();
    	autoComplete();
    	//JOptionPane.showMessageDialog(null, "Successfully deleted item: " + field1.getText());
    	//conn.close();
    }
//    private void updateItems() throws SQLException {
//        int ID = 0;
//        
//        PreparedStatement prepareID = conn.prepareStatement(autoIDString);
//
//
//        String defaultField1 = "EMPTY";
//        String defaultField2 = "0000";
//        String defaultField3 = "EMPTY";
//        PreparedStatement prepareUpdate;
//        
//        String updateFieldsSQL = "UPDATE `new_schema`.`ResortManagement` SET `AssociationName`='" + field1.getText() +"', "
//				+ "`StartYear`='" + field2a.getText()  + "', "
//				+ "`EndYear`='" + field2b.getText()  + "', "
//			    + "`Type`='" + field3.getSelectedItem().toString()   + "', "
//				+ "`Aisle`='" + field4.getText() + "', "
//				+ "`Row`='" + field5.getText() + "', "
//				+ "`Column`='" + field6.getText() + "', "
//				+ "`Depth`='" + field7.getSelectedItem().toString() + "' "
//				+ " WHERE `ID`='" + String.valueOf(testTable.getModel().getValueAt(selection[0], 8)) + "'";
//       prepareUpdate = conn.prepareStatement(updateFieldsSQL);
//        prepareUpdate.executeUpdate();
//        prepareUpdate.getConnection().commit();
//        System.out.println(updateFieldsSQL);
//        UpDateTable();
//        clearFields();
//        prepareUpdate.close();
//        autoComplete();
//        //conn.close();
//        JOptionPane.showMessageDialog(null, "Successfully updated item.", "Update" , JOptionPane.INFORMATION_MESSAGE);
//
//        
//    }

    private void getInputFromFields() throws NumberFormatException, SQLException {
        field1.addFocusListener(new FocusAdapter(){
        	public void focusGained(FocusEvent e) {
        		SwingUtilities.invokeLater(new Runnable(){
        			@Override
        			public void run() {
        				field1.selectAll();
        			}
        		});
        	}
        });
        field1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    System.out.println("DoctorV: " + field1.getText());

                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        
        //gets value from autocomplete selection
        field1.getDocument().addDocumentListener(new DocumentListener(){
        	@Override
        	public void changedUpdate(DocumentEvent e) {
               
        	}

			@Override
			public void insertUpdate(DocumentEvent e) {
				 try {
	                    System.out.println("DoctorO: " + field1.getText());

					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}});
        
        field2a.addFocusListener(new FocusAdapter(){
        	public void focusGained(FocusEvent e) {
        		SwingUtilities.invokeLater(new Runnable(){
        			@Override
        			public void run() {
        				field2a.selectAll();
        			}
        		});
        	}
        });
        field2a.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    getIntegerInput(field2a, e);
                
                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        
        field2b.addFocusListener(new FocusAdapter(){
        	public void focusGained(FocusEvent e) {
        		SwingUtilities.invokeLater(new Runnable(){
        			@Override
        			public void run() {
        				field2b.selectAll();
        			}
        		});
        	}
        });
        
        field2b.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    getIntegerInput(field2b, e);
                
                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        
  
        

       
        
    }

    /**
     *  Verifies that integers are only accept in textbox
     */
    public void getIntegerInput(JTextField jText, KeyEvent e){
    
        numSwap = null;
        String temp = jText.getText();
        //only accepts positives doubles
        
        String regex = "(?<![-.])\\b[0-9]+\\b(?!\\.[0-9])";
    
        //    (?<![-.])   # Assert that the previous character isn't a minus sign or a dot.
        //    \b          # Anchor the match to the start of a number.
        //    [0-9]+      # Match a number.
        //    \b          # Anchor the match to the end of the number.
        //    (?!\.[0-9]) # Assert that no decimal part follows.
    
        if(temp.matches(regex))
        {
            numSwap = temp;
            int round = (Integer.parseInt(jText.getText()));
        }
        else if((e.getKeyCode() == KeyEvent.VK_BACK_SPACE) || (e.getKeyCode() == KeyEvent.VK_DELETE) 
                && temp.length() == 0)
        {//deletes the element in textbox
            jText.setText("");
            numSwap="";
        }
        
        else{
            jText.setText(numSwap);
        }
    }

    private void getTypes() throws SQLException 
    {
        //field3

        field3.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                field3ItemListener();
            }}
        );

        field3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                field3ActionPerformed();
            }});
    }

    private void field3ItemListener()
    {
    	field3.addFocusListener(new FocusAdapter(){
        	public void focusGained(FocusEvent e) {
        		SwingUtilities.invokeLater(new Runnable(){
        			@Override
        			public void run() {
        				field3.getSelectedItem();
        			}
        		});
        	}
        });
        field3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                //after combobox is inserted with "", it will enable user to edit field
                if(e.getItem().equals("") || e.getItem().toString().equals("<New Type>"))
                {    
                    if(e.getStateChange() == ItemEvent.SELECTED)
                    {
                        field3ActionPerformed();
                        field3.getEditor().setItem("");
                        field3.setEditable(true);
                        //checks if the new Type is empty
                    }
                    else if(e.getStateChange() == ItemEvent.DESELECTED)
                    {
                        field3.setEditable(false);
                    }
                }
                else{//if not make all boxes uneditbale
                    field3.setSelectedItem(e.getItem());
                    field3.setEditable(false);
                }
            }
        });
    }
    
    private void field3ActionPerformed()
    {
        field3.addActionListener(new ActionListener(){
            @Override
            //new Type is clicked, remove it from combobox, insert a blank string and set it that item
            public void actionPerformed(ActionEvent e) {

                JComboBox cb = (JComboBox)e.getSource();
                if(cb.getSelectedIndex() == 0)
                {
                    if(cb.getSelectedItem() != null && !field3.getSelectedItem().equals(""))
                    {
                        field3.removeItem("<New Type>");
                        field3.insertItemAt("", 0);
                        field3.setSelectedItem("");
                    }
                }
                
                else{
                    //Gets Type from JComboBox Field3
                    if(field3.getSelectedItem() != null)
                    {
                        field3InputString = field3.getSelectedItem().toString();

                        //System.out.println(newTypeString);
                    }
                }

            }
        });
    }
    private static void UpDateTable() 
    {
        try 
        {
            DefaultTableModel dm = new DefaultTableModel(){
                @Override
                public Class getColumnClass(int c) {
                    //System.out.println(getValueAt(0, c).getClass().toString());
                    if(c == 1 || c==4 || c==5 || c==6  )
                    {
                        return Integer.class;
                    }
                    else
                        return String.class;
                }
            };
            //query and resultset
            String testTable_String = "Select * from ResortManagement";
            PreparedStatement showTestTable = conn.prepareStatement(testTable_String);
            ResultSet rsTest = showTestTable.executeQuery();
            addRowsAndColumns(rsTest, dm);

            testTable.setModel(dm);
            totalLabel.setText("Total number of items: " + testTable.getRowCount());

            refreshScreen();
            //conn.close();
        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private static void addRowsAndColumns(ResultSet rs, DefaultTableModel dm) throws SQLException
    {
        ResultSetMetaData rsmd=rs.getMetaData();
        //Coding to get columns-
        int cols=rsmd.getColumnCount();
        String c[]=new String[cols];
        for(int i=0;i<cols;i++){
            c[i]=rsmd.getColumnName(i+1);
            dm.addColumn(c[i]);
        }

        Object row[]=new Object[cols];
        int count = 0;
        while(rs.next()){
            for(int i=0;i<cols;i++){
                if(i == 1 || i==4 || i==5 || i==6)
                {
                    row[i]=Integer.parseInt(rs.getString(i+1));
                }
                else
                    row[i]=rs.getString(i+1);
            }
            count++;
            dm.addRow(row);
        }
    }
    

    /**
     * Refresh and resizes columns of testTable.
     */
    private static void refreshScreen()
    {
        testTable.revalidate();
        testTable.repaint();
        testTable.validate();
        testTable.getColumnModel().getColumn(0).setPreferredWidth(240);
        testTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        testTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        testTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        testTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        testTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        testTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        testTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        testTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        testTable.getColumnModel().getColumn(9).setPreferredWidth(240);

    }
    
    private void addTextBoxFields()
    {
        Object[] fields = {
                "Association Name:    ", field1,
                "Start Year:    ", field2a,
                "End Year:      ", field2b,
                "Type:    ", field3,
                "Aisle:    ", field4,
                "Row:    ", field5,
                "Column:    ", field6,
                "Depth:    ", field7
                
        };
        int i=0;
        while (i < fields.length) {
            JLabel label = new JLabel((String) fields[i++], JLabel.CENTER);
            label.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 20));
            g1_Jpanel.add(label);
            g1_Jpanel.add((Component) fields[i++]);
        }
	    
        field4.setEnabled(false);
        field5.setEnabled(false);
        field6.setEnabled(false);
        field7.setEnabled(false);
        
        field4.setDisabledTextColor(Color.BLACK);
        field5.setDisabledTextColor(Color.BLACK);
        field6.setDisabledTextColor(Color.BLACK);
       



        field7.addItem("B");
        field7.addItem("F");
        setTextFieldName();
        setFont();
    }//End of Method
    
    private void getResortID()
    {
        //Global Variables
        autoIDString = "SELECT  ID FROM new_schema.ResortManagement "
                + "WHERE `AssociationName` LIKE '" + field1.getText() +"%' "
                + "AND `StartYear` LIKE '" + field2a.getText() +"%' "
                + "AND `EndYear` LIKE '" + field2b.getText() +"%' "
                + "AND `Type` LIKE '" + field3.getSelectedItem().toString() +"%'"
                + "AND `Aisle` LIKE '" + field4.getText() +"%' "
                + "AND `Row`   LIKE '" + field5.getText() +"%' "
                + "AND `Column` LIKE '" + field6.getText() +"%' "
                + "AND `Depth` LIKE  '" + field7.getSelectedItem().toString() +"%'; ";
    }
    
    
    private void setupTable(JScrollPane scrollPane_1) {
        
    	//Set JTable editable to false  

		DefaultTableModel model = new DefaultTableModel();
		testTable = new JTable(model) {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false; // Disallow the editing of any cell
			}

		}; //end set JTable to false
        
        UpDateTable();
        testTable.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
        
        ((DefaultCellEditor) testTable.getDefaultEditor(Object.class))
        .getComponent().setFont(testTable.getFont());
        
        testTable.getTableHeader().setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
        testTable.setRowHeight(testTable.getRowHeight() + 20);
        testTable.putClientProperty("terminateEditOnFocusLost", true);
        scrollPane_1.setViewportView(testTable);
        //testTable.setAutoCreateColumnsFromModel(true);
        testTable.setAutoCreateRowSorter(true);
        testTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        testTable.getTableHeader().setReorderingAllowed(false);
        testTable.setDefaultEditor(Object.class, null);
        testTable.setDefaultEditor(Integer.class, null);
        testTable.addMouseListener(new MouseAdapter() {
            //
            //    // Program that when the mouse clicks a spot of the table autofills
            //    // the textboxes
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	//Grabs the indices form table
                selection = testTable.getSelectedRows();
                             
                int row = testTable.getSelectedRow();    
                //Gets text from row and fills jtext if cell is not empty

                if(testTable.getValueAt(row, 0) != null)
                {
                    field1.setText(testTable.getValueAt(row, 0).toString());
                }
                else
                    field1.setText("");
                if(testTable.getValueAt(row, 1) != null)
                {
                    field2a.setText(testTable.getValueAt(row, 1).toString());
                }
                else
                    field2a.setText("");
                if(testTable.getValueAt(row, 1) != null)
                {
                    field2b.setText(testTable.getValueAt(row, 2).toString());
                }
                else
                    field2b.setText("");
                
                if(testTable.getValueAt(row, 2) != null)
                {
                    field3.setSelectedItem(testTable.getValueAt(row, 3).toString());
                }
                else
                    field3.setSelectedIndex(0);
                if(testTable.getValueAt(row, 3) != null)
                {
                    field4.setText(testTable.getValueAt(row, 4).toString());
                }
                else
                    field4.setText("");
                if(testTable.getValueAt(row, 4) != null)
                {
                    field5.setText(testTable.getValueAt(row, 5).toString());
                }
                else
                    field5.setText("");
                if(testTable.getValueAt(row, 5) != null)
                {
                    field6.setText(testTable.getValueAt(row, 6).toString());
                }
                else
                    field6.setText("");
                if(testTable.getValueAt(row, 6) != null)
                {
                    field7.setSelectedItem((testTable.getValueAt(row, 7).toString()));
                    
                }
                else
                    field7.setSelectedIndex(0);
          
            
                //getInputFromFields();
            }
        });
        
    }//End of setupTable
    
  
    
    
    private void setupFrame()
    {
        frmInsertAsset.setVisible(true);
    	//frmInsertAsset.pack();

        Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();
        frmInsertAsset.setMaximumSize(DimMax);
    
        frmInsertAsset.setExtendedState(JFrame.MAXIMIZED_BOTH);        
        frmInsertAsset.getContentPane().setBackground(new Color(244, 244, 244));
        frmInsertAsset.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
        frmInsertAsset.setTitle("Insert Asset");
        frmInsertAsset.setBounds(100, 100, 1504, 793);
        frmInsertAsset.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmInsertAsset.setLocationRelativeTo(null);
        frmInsertAsset.getContentPane().setLayout(null);
        frmInsertAsset.getContentPane().setLayout(springLayout);
    
        ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/appIconImage-2.png"));
        frmInsertAsset.setIconImage(icon.getImage());
    }

    private void setUpFrameComponents(JScrollPane scrollPane_1, JButton btnUpdate, JButton btnClear) {
        scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane_1, 24, SpringLayout.NORTH, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane_1, -37, SpringLayout.SOUTH, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, scrollPane_1, -21, SpringLayout.EAST, frmInsertAsset.getContentPane());
        frmInsertAsset.getContentPane().add(scrollPane_1);    
        
        springLayout.putConstraint(SpringLayout.WEST, scrollPane_1, 677, SpringLayout.WEST, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, insertBtn, 163, SpringLayout.WEST, frmInsertAsset.getContentPane());
        insertBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        springLayout.putConstraint(SpringLayout.NORTH, updateBtn, 0, SpringLayout.NORTH, insertBtn);
        updateBtn.setSize(180, 38);
        clearBtn.setSize(448, 38);
        //Button Insert
        //btnInsert.setLocation(15, 662);
        //btnInsert.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
        //frmInsertAsset.getContentPane().add(btnInsert);
        btnUpdate.setLocation(275, 662);
        btnUpdate.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
        frmInsertAsset.getContentPane().add(btnUpdate);
        btnClear.setLocation(913, 653);
        btnClear.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
        frmInsertAsset.getContentPane().add(btnClear);
        springLayout.putConstraint(SpringLayout.WEST, updateBtn, 30, SpringLayout.WEST, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, updateBtn, 268, SpringLayout.WEST, frmInsertAsset.getContentPane());
        
        excelBtn.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
        frmInsertAsset.getContentPane().add(excelBtn);
        springLayout.putConstraint(SpringLayout.WEST, clearBtn, 30, SpringLayout.WEST, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, clearBtn, 16, SpringLayout.SOUTH, scrollPane_1);
        springLayout.putConstraint(SpringLayout.EAST, clearBtn, -409, SpringLayout.WEST, scrollPane_1);
        
        springLayout.putConstraint(SpringLayout.NORTH, clearBtn, 30, SpringLayout.SOUTH, insertBtn);
        
        springLayout.putConstraint(SpringLayout.NORTH, excelBtn, 30, SpringLayout.SOUTH, insertBtn);
        springLayout.putConstraint(SpringLayout.WEST, excelBtn, 70, SpringLayout.EAST, clearBtn);
        springLayout.putConstraint(SpringLayout.EAST, excelBtn, -78, SpringLayout.WEST, scrollPane_1);
        springLayout.putConstraint(SpringLayout.SOUTH, excelBtn, 16, SpringLayout.SOUTH, scrollPane_1);
        springLayout.putConstraint(SpringLayout.NORTH, deleteBtn, 0, SpringLayout.NORTH, insertBtn);
        deleteBtn.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
        frmInsertAsset.getContentPane().add(deleteBtn);
        
        totalLabel = new JLabel("Total number  of items: ");
        springLayout.putConstraint(SpringLayout.NORTH, totalLabel, 6, SpringLayout.SOUTH, scrollPane_1);
        springLayout.putConstraint(SpringLayout.WEST, totalLabel, 291, SpringLayout.EAST, excelBtn);
        springLayout.putConstraint(SpringLayout.SOUTH, totalLabel, -10, SpringLayout.SOUTH, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, totalLabel, 792, SpringLayout.EAST, clearBtn);
        totalLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 22));
        frmInsertAsset.getContentPane().add(totalLabel);
        
    }

    private void gridLayoutSetup() {
        GridLayout gl_panel = new GridLayout(0,2);
        gl_panel.setVgap(20);
        g1_Jpanel = new JPanel(gl_panel);
        g1_Jpanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        g1_Jpanel.setBackground(new Color(244, 244, 244));
        scrollPane = new JScrollPane();
        springLayout.putConstraint(SpringLayout.NORTH, insertBtn, 5, SpringLayout.SOUTH, scrollPane);
        springLayout.putConstraint(SpringLayout.WEST, insertBtn, 0, SpringLayout.WEST, scrollPane);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -127, SpringLayout.SOUTH, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, 616, SpringLayout.WEST, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 24, SpringLayout.NORTH, frmInsertAsset.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 15, SpringLayout.WEST, frmInsertAsset.getContentPane());
        scrollPane.setLocation(15, 80);
        //scrollPane.setSize(561, 610);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportView(g1_Jpanel);
        frmInsertAsset.getContentPane().add(scrollPane);    
    }

    private void addTypes() throws SQLException {

        //Declare local variables
        java.sql.Statement stmt;
        ArrayList<String> typeList = new ArrayList<String>();
        int count = 1;
        //remove items in combobox
        field3.removeAllItems();

        stmt = conn.createStatement(); // \"group\",price //\"group\",price
        ResultSet rs = stmt.executeQuery("SELECT Distinct Type From ResortManagement");
        String group = "";
        while (rs.next()) 
        {
            //get types from database
            group = rs.getString("Type");
            typeList.add(group);
        }
        
        //close connections
        rs.close();
        stmt.close();
        //conn.close();
        
        //sort the types add the editable field first then add the sorted types to combobox
        Collections.sort(typeList);
        field3.addItem("<New Type>");
        for(String str: typeList){
            field3.addItem(str);
            if(str.equals("Box"))
            {
                field3.setSelectedIndex(count);//sets the Type to ArtWork
            }
            count++;
        }

    }
    private void setTextFieldName()
    {
        field1.setName("1");
        field2a.setName("2");
        field2b.setName("3");
        field3.setName("4");
        field4.setName("5");
        field5.setName("6");
        field6.setName("7");
        field7.setName("8");
        
    }
    private void setFont(){
        Font font = new Font("Segoe UI Semilight", Font.PLAIN, 20);
        field1.setFont(font);
        field2a.setFont(font);
        field2b.setFont(font);
        field3.setFont(font);
        field4.setFont(font);
        field5.setFont(font);
        field6.setFont(font);
        field7.setFont(font); 
        
    }
    private boolean isFieldsEmpty()
    {
        if((field1.getText().equals("") || field2a.getText().equals("") 
        		|| field2b.getText().equals("") || field4.getText().equals("")
            ||field5.getText().equals("") || field6.getText().equals("")) 
                || isField3Empty() )
        {
            JOptionPane.showMessageDialog(null, "Please make sure there are no empty fields"  , "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;    
    }
    private boolean isField3Empty()
    {
        if(field3.getSelectedItem().toString().length() <= 0)
        {
            return true;
        }
        return false;
    }
    private void clearFields()
    {
        field1.setText("");
        field2a.setText("");
        field2b.setText("");
        field3.setSelectedIndex(0);
        /*field4.setText("");
        field5.setText("");
        field6.setText("");
        field7.setSelectedIndex(0);*/
    }
    private boolean validateFields()
    {
        if(field2a.getText().length() !=4 || field2b.getText().length() !=4)
        {
            JOptionPane.showMessageDialog(null, "Year must be ented in this format: YYYY "  , "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if(Integer.parseInt(field4.getText()) <=0 || Integer.parseInt(field4.getText()) >6)
        {
            JOptionPane.showMessageDialog(null, "Aisle must be between 1 and 6"  , "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if(Integer.parseInt(field5.getText()) <=0 || Integer.parseInt(field5.getText()) >10)
        {
            JOptionPane.showMessageDialog(null, "Row must be between 1 and 10"  , "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if(Integer.parseInt(field6.getText()) <=0 || Integer.parseInt(field6.getText()) >30)
        {
            JOptionPane.showMessageDialog(null, "Column must be between 1 and 30"  , "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        else
        {
            return true;    
        }
    }
   
    
    private void autoComplete() throws SQLException
    {
    	Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Distinct AssociationName From ResortManagement");
		complete.removeAllItems();
		while (rs.next()) {			
	        complete.addItem(rs.getString("AssociationName"));
		}
		rs.close();
		stmt.close();
    }

}