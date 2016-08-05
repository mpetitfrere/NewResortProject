package applicationPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

//import org.apache.poi.util.SystemOutLogger;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import java.awt.LayoutManager;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

public class reportsFrame {

	private JFrame reportFrame;
	private static JTable reportsTable;

	// instantiating textfields for each jlabel
	JTextField resortName = new JTextField();
	JTextField year = new JTextField();
	JComboBox type = new JComboBox();
	public static JLabel lblNoOfResults = new JLabel();
	private String numswap;

	JLabel empty = new JLabel(" ");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					reportsFrame window = new reportsFrame();
					// window.display();
					window.reportFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public reportsFrame() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		Connection conn = MySQLConnection.dbConnector();
		initialize();
		UpDateTable();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		reportFrame = new JFrame();
		
//		reportFrame.addWindowListener(new WindowAdapter()
//        {
//            @Override
//            public void windowClosing(WindowEvent e)
//            {
//                //System.out.println("Closed");
//                MainScreen.frame.setVisible(true);
//                reportFrame.dispose();
//
//            }
//        });
		
		reportFrame.setVisible(true);
		reportFrame.setResizable(false);
		reportFrame.getContentPane().setBackground(new Color(244, 244, 244));
		reportFrame.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		reportFrame.setTitle("File Finder");
		reportFrame.setBounds(100, 100, 1920, 900);
		reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		reportFrame.setLocationRelativeTo(null);
		reportFrame.getContentPane().setLayout(null);
		//reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		SpringLayout springLayout = new SpringLayout();
		reportFrame.getContentPane().setLayout(springLayout);

		ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/appIconImage-2.png"));
		reportFrame.setIconImage(icon.getImage());

		JScrollPane scrollPane_1 = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, scrollPane_1, 720, SpringLayout.WEST,
				reportFrame.getContentPane());
		scrollPane_1.setSize(792, 616);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane_1, 24, SpringLayout.NORTH,
				reportFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane_1, -98, SpringLayout.SOUTH,
				reportFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane_1, -21, SpringLayout.EAST,
				reportFrame.getContentPane());
		reportFrame.getContentPane().add(scrollPane_1);

		// Font styling for TextFields
		Font font = new Font("Segoe UI Semilight", Font.PLAIN, 20);

		addCategoriesToJCombo();
		
		// Setting custom font to text fields and comboBoxe
		resortName.setFont(font);
		year.setFont(font);
		type.setFont(font);

		JButton btnRunFileQuery = new JButton("Run");
		btnRunFileQuery.setFont(font);
		getYearInput();
		btnRunFileQuery.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {

					int Year = 0;
					String Name = resortName.getText().trim();

					if (!(year.getText().isEmpty()))
						Year = Integer.parseInt(year.getText().trim());

					String Container = (String) type.getSelectedItem();

					// Returns All Package Types By Year For All Associations
					if (resortName.getText().isEmpty() && !(year.getText().isEmpty())
							&& type.getSelectedItem().equals("All")) {

						Everything_By_Year(Year);
						
					}

					// Returns Specific Package Type By Year Without Association
					// Name
					else if (resortName.getText().isEmpty() && !(year.getText().isEmpty())
							&& !(type.getSelectedItem().equals("All"))) {

						All_Associations_By_Year(Container, Year);

					}

					// Returns All Package Types For All Years For A Specified
					// Association
					else if (!(resortName.getText().isEmpty()) && year.getText().isEmpty()
							&& type.getSelectedItem().equals("All")) {

						Association_Name(Name);

					}

					// Returns Specified Package Type For All Years For A
					// Specified Association
					else if (!(resortName.getText().isEmpty()) && year.getText().isEmpty()
							&& !(type.getSelectedItem().equals("All"))) {

						Association_By_Type_And_Name(Name, Container);
						
					}

					// Returns All Packages Types For Association For A
					// Specified Year
					else if (!(resortName.getText().isEmpty()) && !(year.getText().isEmpty())
							&& type.getSelectedItem().equals("All")) {

						All_Associations_By_Name_And_Year(Name, Year);

					}

					// Returns Specified Package Type For Association For A
					// Specified Year
					else if (!(resortName.getText().isEmpty()) && !(year.getText().isEmpty())
							&& !(type.getSelectedItem().equals("All"))) {

						Association_By_Type_Name_And_Year(Name, Container, Year);
						
					}

					// Returns All Package Types For All Associations For All
					// Years
					else if (resortName.getText().isEmpty() && year.getText().isEmpty()
							&& type.getSelectedItem().equals("All")) {

						UpDateTable();
						
					}

					// Returns Specified Package Type For All Associations For
					// All Years
					else if (resortName.getText().isEmpty() && year.getText().isEmpty()
							&& !(type.getSelectedItem().equals("All"))) {

						Association_Type(Container);
						
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});        
        
		// Reset Button to reset table after a query has been performed
        JButton btnResetTable = new JButton("Reset Table");
        btnResetTable.setFont(font);
        
        btnResetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {

                    UpDateTable();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });

		
		
		//Left outermost panel
		JPanel compPanel = new JPanel();
		
		compPanel.setBorder(new EmptyBorder(25, 0, 0, 25));
		compPanel.setBackground(new Color(244, 244, 244));
		//compPanel.
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 24, SpringLayout.NORTH,
				reportFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 15, SpringLayout.WEST, reportFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, scrollPane_1);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.WEST, scrollPane_1);
		scrollPane.setLocation(15, 80);
		scrollPane.setSize(695, 651);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setViewportView(compPanel);
		compPanel.setLayout(new BoxLayout(compPanel, BoxLayout.X_AXIS));

		//Inner left panel to contain text labels 
		JPanel labelPanel = new JPanel((LayoutManager) null);
		labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		labelPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		labelPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		labelPanel.setBackground(new Color(244, 244, 244));
		labelPanel.setPreferredSize(new Dimension (200, 200));
		labelPanel.setMaximumSize(new Dimension (200,400));
		compPanel.add(labelPanel);
		GridLayout gl_labelPanel = new GridLayout(0, 1);
		gl_labelPanel.setVgap(20);
		labelPanel.setLayout(gl_labelPanel);

		//Inner right panel to contain text fields
		JPanel textPanel = new JPanel((LayoutManager) null);
		textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		textPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		textPanel.setBackground(new Color(244, 244, 244));
		textPanel.setPreferredSize(new Dimension (400, 200));
		textPanel.setMaximumSize(new Dimension (600,400));
		compPanel.add(textPanel);
		GridLayout gl_textPanel = new GridLayout(0, 1);
		gl_textPanel.setVgap(20);
		textPanel.setLayout(gl_textPanel);
		reportFrame.getContentPane().add(scrollPane);

		// Array of labels and corresponding textFields to 
				Object[] fields = {

						"Find A File   ", 			empty, // Spacer

						"Resort Name:    ",			resortName, 
						"Year:    ", 				year, 
						"Container Type:    ", 			type,
						"    ", 						btnRunFileQuery, //Run Button
						"    ", 						btnResetTable, //Run Button

						};
		
		//While loop to add labels and text fields from object array above
		int i = 0;
		while (i < fields.length) {
			JLabel label = new JLabel((String) fields[i++], JLabel.RIGHT);
			label.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 20));
			label.setForeground(new Color(98, 98, 98));
			labelPanel.add(label);
			textPanel.add((Component) fields[i++]);
		}

		//Reports JTable
		reportsTable = new JTable();
		{
			
		}
		reportsTable.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
		((DefaultCellEditor) reportsTable.getDefaultEditor(Object.class)).getComponent().setFont(reportsTable.getFont());
		reportsTable.getTableHeader().setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
		reportsTable.setRowHeight(reportsTable.getRowHeight() + 20);
		reportsTable.putClientProperty("terminateEditOnFocusLost", true);
		scrollPane_1.setViewportView(reportsTable);
		reportsTable.setAutoCreateRowSorter(true);
		reportsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		lblNoOfResults = new JLabel("No. of Results: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblNoOfResults, 621, SpringLayout.NORTH, scrollPane);
		lblNoOfResults.setForeground(new Color(0, 155, 167));
		lblNoOfResults.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
		lblNoOfResults.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.WEST, lblNoOfResults, 10, SpringLayout.WEST, scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, lblNoOfResults, 953, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, lblNoOfResults, -10, SpringLayout.EAST, scrollPane);
		lblNoOfResults.setLocation(400, 0);
		reportFrame.getContentPane().add(lblNoOfResults);

		//JPanel to hold buttons for horizontal scaling purposes
		JPanel buttonPanel = new JPanel(); 
		springLayout.putConstraint(SpringLayout.WEST, buttonPanel, 720, SpringLayout.WEST, reportFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, buttonPanel, -20, SpringLayout.EAST, reportFrame.getContentPane());
		buttonPanel.setBackground(new Color(244, 244, 244));
		buttonPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		springLayout.putConstraint(SpringLayout.SOUTH, buttonPanel, -10, SpringLayout.SOUTH,
				reportFrame.getContentPane());
		buttonPanel.setSize(10, 50);
		buttonPanel.setLocation(719, 645);
		springLayout.putConstraint(SpringLayout.NORTH, buttonPanel, 11, SpringLayout.SOUTH, scrollPane_1);
		reportFrame.getContentPane().add(buttonPanel);
		buttonPanel.setLayout(new GridLayout(0, 2, 20, 0));

		// Print button for reports
		JButton btnPrint = new JButton("Print");
		buttonPanel.add(btnPrint);
		springLayout.putConstraint(SpringLayout.WEST, btnPrint, 750, SpringLayout.WEST, reportFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnPrint, 23, SpringLayout.SOUTH, scrollPane_1);
		springLayout.putConstraint(SpringLayout.SOUTH, btnPrint, -28, SpringLayout.SOUTH, reportFrame.getContentPane());
		btnPrint.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));



		//Export to Excel Button
		JButton btnExportToExcel = new JButton("Export to Excel");
		btnExportToExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ConvertExcel.exportExcel(reportsTable);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e1) {
					if (e1.toString().contains("java.lang.NullPointerException")) {
						Scanner input = null;
						String line;
						try {
							input = new Scanner(new File("LogReportFrameNullPointer.txt"));
						} catch (FileNotFoundException e2) {
							e2.printStackTrace();
						}
						line = input.nextLine();
						JOptionPane.showMessageDialog(null, "Check ID_Tag: " + line + "'s rows for errors");
					}
				}

			}
		});
		buttonPanel.add(btnExportToExcel);
		springLayout.putConstraint(SpringLayout.WEST, btnExportToExcel, (int) ((scrollPane_1.getWidth() / 2) + 15),
				SpringLayout.WEST, scrollPane_1);
		springLayout.putConstraint(SpringLayout.EAST, btnExportToExcel, -50, SpringLayout.EAST,
				reportFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnExportToExcel, 23, SpringLayout.SOUTH, scrollPane_1);
		springLayout.putConstraint(SpringLayout.SOUTH, btnExportToExcel, -28, SpringLayout.SOUTH,
				reportFrame.getContentPane());
		btnExportToExcel.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 22));
		springLayout.putConstraint(SpringLayout.EAST, btnPrint, -80, SpringLayout.WEST, btnExportToExcel);

	} //end initialize
	
	// Adds categories to category drop down boxes
	public void addCategoriesToJCombo() {

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {

			stmt = conn2.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Distinct Type From ResortManagement");

			String group = "All";
			type.addItem(group);
			while (rs.next()) {
				group = rs.getString("Type");
				type.addItem(group);
				// categoryDeactivated.addItem(group);

			}
		} catch (SQLException e) {
			System.out.println("sql exception caught");
			e.printStackTrace();
		}
	}

	// Populates JTable and names columns
	public static void addRowsAndColumns(ResultSet rs, DefaultTableModel dm) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		// Coding to get columns-
		int cols = rsmd.getColumnCount();
		String c[] = new String[cols];
		for (int i = 0; i < cols; i++) {
			c[i] = rsmd.getColumnName(i + 1);
			dm.addColumn(c[i]);
		}

		Object row[] = new Object[cols];
		while (rs.next()) {
			for (int i = 0; i < cols; i++) {
				row[i] = rs.getString(i + 1);
			}
			dm.addRow(row);
		}
	}

	// update table from most recent data in database
	public static void UpDateTable() {
		try {
			Connection conn = MySQLConnection.dbConnector();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = "Select * from ResortManagement";
			PreparedStatement showTestTable = conn.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();
			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();

			conn.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	// Repaints the paints the Jtable
	public static void refreshScreen() {
		reportsTable.revalidate();
		reportsTable.repaint();
		reportsTable.validate();

		// setting column widths in reports table
		reportsTable.getColumnModel().getColumn(0).setPreferredWidth(420);
		reportsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
		reportsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
		reportsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		reportsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		reportsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		reportsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		// reportsTable.getColumnModel().getColumn(7).setPreferredWidth(100);
		countColumns();
	}
	
	public static void countColumns() {
		int rows = reportsTable.getRowCount();
		lblNoOfResults.setText("No. of Results: " + rows);
	}

	public static void All_Associations_By_Name(String category, int year) {

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("SELECT *"
					+ " From ResortManagement Where ResortManagement.AssociationName Like '" + category + "%' "
					+ "AND ResortManagement.Year = " + year + ";"); // + "AND
																	// Date_Acquired
																	// LIKE '" +
																	// year +
																	// "%';'");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void Association_By_Type_And_Name(String name, String type) { // NEEDED
																				// BIG
																				// TIME

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth, ID"
					+ " From ResortManagement Where new_schema.ResortManagement.AssociationName Like '" + name + "%' "
					+ "AND new_schema.ResortManagement.Type LIKE '" + type + "%';"); // +
																						// "AND
																						// Date_Acquired
																						// LIKE
																						// '"
																						// +
																						// year
																						// +
																						// "%';'");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void All_Associations_By_Name_And_Year(String name, int year) { // NEEDED
																					// BIG
																					// TIME

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth, ID"
					+ " From ResortManagement Where ResortManagement.AssociationName Like '" + name + "%' "
					+ "AND ResortManagement.Year = " + year + ";"); // + "AND
																	// Date_Acquired
																	// LIKE '" +
																	// year +
																	// "%';'");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void Association_Name(String name) { // NEEDED BIG TIME

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth, ID"
					+ " From new_schema.ResortManagement Where new_schema.ResortManagement.AssociationName LIKE '"
					+ name + "%';");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void Everything_By_Year(int year) { // NEEDED BIG TIME

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth, ID"
					+ " From ResortManagement Where ResortManagement.Year = " + year + ";");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void All_Associations_By_Year(String type, int year) { // NEEDED
																			// BIG
																			// TIME

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth, ID"
					+ " From ResortManagement Where ResortManagement.Type Like '" + type + "%' "
					+ "AND ResortManagement.Year = " + year + ";"); // + "AND
																	// Date_Acquired
																	// LIKE '" +
																	// year +
																	// "%';'");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void Association_Type(String type) { // NEEDED BIG TIME

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth, ID"
					+ " From new_schema.ResortManagement Where new_schema.ResortManagement.Type LIKE '" + type + "%';");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	// NEEDED BIG TIME
	public static void Association_By_Type_Name_And_Year(String name, String type, int year) {

		Connection conn2 = MySQLConnection.dbConnector();
		java.sql.Statement stmt;

		try {
			stmt = conn2.createStatement();
			DefaultTableModel dm = new DefaultTableModel();
			String testTable_String = ("Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth, ID"
					+ " From ResortManagement Where ResortManagement.AssociationName Like '" + name + "%' "
					+ "AND ResortManagement.Type Like '" + type + "%' " + "AND ResortManagement.Year = " + year + ";");

			PreparedStatement showTestTable = conn2.prepareStatement(testTable_String);
			ResultSet rsTest = showTestTable.executeQuery();

			addRowsAndColumns(rsTest, dm);
			reportsTable.setModel(dm);
			refreshScreen();
			conn2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	public void getYearInput()
	{
		year.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    getIntegerInput(year, e);

                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
	}
	
	public void getIntegerInput(JTextField jText, KeyEvent e){
	    
        numswap = null;
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
            numswap = temp;

        }
        else if((e.getKeyCode() == KeyEvent.VK_BACK_SPACE) || (e.getKeyCode() == KeyEvent.VK_DELETE) 
                && temp.length() == 0)
        {//deletes the element in textbox
            jText.setText("");
            numswap="";
        }
        
        else{
            jText.setText(numswap);
        }
    }

} // end of ReportsFrame