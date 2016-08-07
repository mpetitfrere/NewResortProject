package applicationPackage;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import applicationPackage.MySQLConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ExcelFrame extends JFrame {

    private JPanel contentPane;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ExcelFrame frame = new ExcelFrame();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ExcelFrame() {
    	
    	UIManager.put("ToolTip.foreground", new ColorUIResource(Color.WHITE));
		UIManager.put("ToolTip.background", new ColorUIResource(new Color(0, 155, 167)));
		UIManager.put("ToolTip.font", new FontUIResource("Segoe UI Semilight", Font.BOLD, 20));

    	addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                //System.out.println("Closed");
                MainScreen.frame.setVisible(true);
                dispose();

            }
        });
    	setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ExcelFrame.class.getResource("/Resources/appIconImage-2.png")));
        setTitle("Excel Options");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setBounds(100, 100, 905, 330);
        setSize(905,330);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(244, 244, 244));
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        //ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/appIconImage.png"));

		


        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ExcelFilter());


        JLabel btnImportExcel = new JLabel("");
        btnImportExcel.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/importIcon.jpg")));
        btnImportExcel.setToolTipText("Inserts Excel files to database.");
        btnImportExcel.addFocusListener(new FocusAdapter() {

        });
        btnImportExcel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent arg0) {
                //btnImportExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnImportExcel.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/importIcon_Hover.jpg")));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnImportExcel.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/importIcon.jpg")));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {

                //Handle open button action.
                if (e.getSource() == btnImportExcel) {
                    int returnVal = fc.showOpenDialog(btnImportExcel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        try {
                            long startTime = System.currentTimeMillis();
                            if(ConvertExcel.importExcel(file))
                            {
                                
                                long endTime   = System.currentTimeMillis();
                                long totalTime = endTime - startTime;
                                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                                Date resultdate = new Date(totalTime);
                                JOptionPane.showMessageDialog(contentPane,
                                        "Successfully imported " + file.getName() +" to database."
                                                +"\nTime: " + sdf.format(resultdate) ,
                                                "Import",
                                                JOptionPane.INFORMATION_MESSAGE
                                        ); 
                            }
                        } catch (SQLException e1) {
                        
                            if(e1.getMessage().contains("key 'LOCATION'"))
                            {
                                String duplicateItem = e1.getMessage();
                                JOptionPane.showMessageDialog(contentPane, duplicateItem + "\nWhere Aisle-Row-Column-Depth", 
                                        "Duplicated Location", JOptionPane.ERROR_MESSAGE);    
                            }
                            else{
                                JOptionPane.showMessageDialog(contentPane, "Import failed. Please fix your file and reimport.", 
                                        "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            
                            e1.printStackTrace();
                        }
                        
                    }

                  
            }          

        }});

        JLabel btnExportExcel = new JLabel("");
        btnExportExcel.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/exportIcon.jpg")));
        btnExportExcel.setToolTipText("Exports all data from database into an excel file.");
        btnExportExcel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent arg0) {
                //btnImportExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnExportExcel.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/exportIcon_Hover.jpg")));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnExportExcel.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/exportIcon.jpg")));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ConvertExcel.exportExcel(false);




                } catch (IOException e1) {e1.printStackTrace();
                

                
                }
            }
        });

        JLabel buttonExcelTemplate = new JLabel("");
        buttonExcelTemplate.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/templateIcon.jpg")));
        buttonExcelTemplate.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent arg0) {
                //btnImportExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                buttonExcelTemplate.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/templateIcon_Hover.jpg")));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                buttonExcelTemplate.setIcon(new ImageIcon(ExcelFrame.class.getResource("/Resources/templateIcon.jpg")));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ConvertExcel.exportExcel(true);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        buttonExcelTemplate.setToolTipText("Opens a template in Excel to import file.");
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        contentPane.add(btnExportExcel);
        contentPane.add(buttonExcelTemplate);
        contentPane.add(btnImportExcel);        
    }
}