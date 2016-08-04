package applicationPackage;
//new

//EventQueue is a platform-independent class that queues events
import java.awt.EventQueue;
// Used to create the areas for most of the tabs, buttons, and frames
import java.awt.Rectangle;
// Used to find when actions occur during the program
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
// Used to communicate with the SQLite database
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
// Used to change the icon from the default Java
import javax.swing.ImageIcon;
// Used to create button in the program
import javax.swing.JButton;
// The whole program is ran on a JFrame
import javax.swing.JFrame;
// Used to label certian areas for better readability
import javax.swing.JLabel;
// Used to throw error messages
import javax.swing.JOptionPane;
// Used to create a secure login area
import javax.swing.JPasswordField;
// Used to enter the User name
import javax.swing.JTextField;
// Used for positioning and orienting components on the screen
import javax.swing.SwingConstants;

//import appPackage.MainScreen;
//import appPackage.sqliteConnection;

//Used to create fonts for the displayed texts
import java.awt.Font;
// Used to change the color of buttons
import java.awt.Color;

public class loginGUI extends JFrame {

	// Declaration of the Username textField
	private static JTextField userNameLabel;
	// clicker is used so when the program is first opened the Username
	// textField
	// isnt already clicked
	private final JTextField clicker;
	// Declaration of the password field
	private final JPasswordField passwordLabel;
	// Declaration of the button used to login
	private JButton blogin;

	/**
	 * Launch the application.
	 */
	// Main for the program
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			// run() is used to initialize the program
			public void run() {
				try {

					loginGUI frame = new loginGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection connection = null;

	/**
	 * Create the frame.
	 */
	public loginGUI() {
		// Titles the JFrame as the "Login"
		setTitle("Login");
		// The user can't resize the JFrame
		setResizable(false);
		// Sets the static size of the frame
		setSize(1440, 810);
		// center JFrame on screen
		setLocationRelativeTo(null);
		// removes frame around window
		setUndecorated(false);
		// Gives the default close option
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Makes the Frame Viewable
		setVisible(true);

		// background image
		setContentPane(new JLabel(new ImageIcon(getClass().getResource("/Resources/Login-Screen-BG-3.jpg"))));
		getContentPane().setLayout(null);

		// Icon Image
		ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/appIconImage-2.png"));
		setIconImage(icon.getImage());
		// Used to hide cursor when first opened
		clicker = new JTextField("");
		getContentPane().add(clicker);
		// Displays "User Name" in the TextField
		userNameLabel = new JTextField("User Name");
		// When the username textfield is clicked it will become blank for the
		// user
		// to type and when clicked away while empty it will display "User name"
		userNameLabel.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				userNameLabel.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (userNameLabel.getText().trim().isEmpty()) {
					userNameLabel.setText("User Name");
				}
			}
		});
		// The label is not set as Opaque meaning the component may not paint
		// some
		// or all of its pixels, allowing the underlying pixels to show through.
		userNameLabel.setOpaque(false);
		// Creates an empty border that gives no space
		userNameLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		// Centers the Label
		userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		// Sets font
		userNameLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		userNameLabel.setForeground(new Color(0, 0, 0));
		// Positions the label
		userNameLabel.setBounds(new Rectangle(508, 331, 442, 65));
		// Displays "Password" in the pawwordField
		passwordLabel = new JPasswordField("Password");
		// When the password textfield is clicked it will become blank for the
		// user
		// to type and when clicked away while empty it will display "Password"
		passwordLabel.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				passwordLabel.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (passwordLabel.getPassword().length == 0) {
					passwordLabel.setText("password");
				}
			}
		});
		// The label is not set as Opaque meaning the component may not paint
		// some
		// or all of its pixels, allowing the underlying pixels to show through.
		passwordLabel.setOpaque(false);
		// Creates an empty border that gives no space
		passwordLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		// Center the field horizontaly
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		// Sets the font of the textfield
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		passwordLabel.setForeground(new Color(0, 0, 0));
		// Set the bounds of the etxtField
		passwordLabel.setBounds(new Rectangle(508, 431, 442, 65));
		// Displays "Login" on the JButton
		blogin = new JButton("Login");
		// sets the color of the button
		blogin.setForeground(Color.WHITE);
		blogin.setBackground(new Color(0, 155, 167));
		// sets the font for the button
		blogin.setFont(new Font("Tahoma", Font.BOLD, 18));
		// sets the Bounds for the button
		blogin.setBounds(new Rectangle(560, 528, 350, 65));
		// adds everything the the JFrame
		getContentPane().add(userNameLabel);
		getContentPane().add(passwordLabel);
		getContentPane().add(blogin);
		// actionlogin() is used to check for the correct username and password
		actionlogin();
		// conects to the SQLite database
		connection = MySQLConnection.dbConnector();
	}

	// Return the test entered in the Username feild
	public static String returnUsername() {
		String userName = userNameLabel.getText();

		return userName;
	}

	// The actionlogin method
	public void actionlogin() {
		// The Action interface provides a useful extension to the
		// ActionListener
		// interface in cases where the same functionality may be accessed by
		// several controls.
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// the String query is used to hold the username and
					// password
					String query = "select * from UsernameInfo where username = ? and password = ?";
					// A SQL statement is precompiled and stored in a
					// PreparedStatement
					// object. This object can then be used to efficiently
					// execute this
					// statement multiple times.
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setString(1, userNameLabel.getText());
					pst.setString(2, passwordLabel.getText());
					// A ResultSet object maintains a cursor pointing to its
					// current row
					// of data. Initially the cursor is positioned before the
					// first row.
					// The next method moves the cursor to the next row
					ResultSet rs = pst.executeQuery();
					int count = 0;
					while (rs.next()) {

						count++;

					}
					// Opens the Dashboard if the Login is correct
					if (count == 1) {
						MainScreen mainScreen = new MainScreen();
						setVisible(false);
						//dispose();
						// If user and pass are incorrect
					} else if (count == 2) {
						JOptionPane.showMessageDialog(null, "Duplicate Username and Password");
						// If user or pass is incorrect
					} else {
						JOptionPane.showMessageDialog(null, "Username and/or Password is not correct!");
					}

					rs.close();
					pst.close();
				} catch (Exception event) {
					JOptionPane.showMessageDialog(null, event);
				}
			}
		};
		// This whole thing is identical to the previous this is triggered when
		// someone has pressed the enter key in the password textField
		passwordLabel.addActionListener(action);
		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					String query = "select * from UsernameInfo where username = ? and password = ?";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setString(1, userNameLabel.getText());
					pst.setString(2, passwordLabel.getText());

					ResultSet rs = pst.executeQuery();
					int count = 0;
					while (rs.next()) {

						count++;

					}
					if (count == 1) {
						MainScreen regFace = new MainScreen();
						dispose();
					} else if (count == 2) {
						JOptionPane.showMessageDialog(null, "Duplicate Username and Password");
					} else {
						JOptionPane.showMessageDialog(null, "Username and/or Password is not correct!");
					}

					rs.close();
					pst.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});

	}

}
