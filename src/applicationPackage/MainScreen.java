
package applicationPackage;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import javax.swing.*;

import applicationPackage.ExcelFrame;
import applicationPackage.InsertWindow;
import applicationPackage.reportsFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.WindowListener;
import java.awt.Toolkit;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Map;
import java.util.TreeMap;

public class MainScreen extends JApplet {

	public static JFrame frame;

	Connection connection = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				new MainScreen();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public MainScreen() throws URISyntaxException {
		super();
		connection = MySQLConnection.dbConnector();
		init();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@Override
	public void init() {


		frame = new JFrame();
		ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/appIconImage-2.png"));
		frame.setIconImage(icon.getImage());
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.getContentPane().setBackground(new Color(244, 244, 244));
		frame.setBackground(Color.WHITE);
		frame.setTitle("Archive Finder");
		frame.setSize(1440, 810);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		JLabel Logo_MainScreen = new JLabel("");
		Logo_MainScreen.setBounds(560, 56, 300, 165);
		Logo_MainScreen.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/Logo_Alt_5.png")));
		frame.getContentPane().add(Logo_MainScreen);

		JLabel reportsIcon = new JLabel("");
		reportsIcon.setHorizontalAlignment(SwingConstants.CENTER);
		reportsIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/fileFinderIcon.jpg")));
		reportsIcon.setBounds(59, 250, 394, 310);
		frame.getContentPane().add(reportsIcon);
		reportsIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// helpIconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				reportsIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/fileFinderIcon_Hover.jpg")));

			}

			@Override
			public void mouseExited(MouseEvent e) {
				reportsIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/fileFinderIcon.jpg")));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {

					new reportsFrame();
					frame.setVisible(false);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		JLabel insertIcon = new JLabel("");
		insertIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/addFileIcon.jpg")));
		insertIcon.setHorizontalAlignment(SwingConstants.CENTER);
		insertIcon.setBounds(512, 250, 394, 301);
		frame.getContentPane().add(insertIcon);
		insertIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// helpIconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				insertIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/addFileIcon_Hover.jpg")));

			}

			@Override
			public void mouseExited(MouseEvent e) {
				insertIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/addFileIcon.jpg")));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {

					InsertWindow insertFrame = new InsertWindow();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		JLabel infoGraphIcon = new JLabel("");
		infoGraphIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/excelIcon.jpg")));
		infoGraphIcon.setHorizontalAlignment(SwingConstants.CENTER);
		infoGraphIcon.setBounds(965, 250, 394, 301);
		frame.getContentPane().add(infoGraphIcon);
		
		
		infoGraphIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// helpIconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				infoGraphIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/excelIcon_Hover.jpg")));

			}

			@Override
			public void mouseExited(MouseEvent e) {
				infoGraphIcon.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/excelIcon.jpg")));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					ExcelFrame excelFrame = new ExcelFrame();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(MainScreen.class.getResource("/Resources/bottomBorder.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setBounds(0, 533, 1418, 240);
		frame.getContentPane().add(lblNewLabel);

	}

}
