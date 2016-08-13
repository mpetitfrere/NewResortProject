import java.awt.EventQueue;

import javax.swing.JFrame;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TestAutoiFill {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestAutoiFill window = new TestAutoiFill();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestAutoiFill() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JComboBox comboBox = new JComboBox();
	
		
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 28));
		comboBox.setBounds(183, 94, 134, 48);
		frame.getContentPane().add(comboBox);
		Object[] elements = new Object[] {"Cat", "Dog", "Lion", "Mouse","Acer","Apple","Cars","Car"};
		AutoCompleteSupport.install(comboBox, GlazedLists.eventListOf(elements));
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				comboBox.getEditor().setItem(textField.getText());
				
			}
		});
		textField.setBounds(183, 42, 134, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		
		comboBox.setEditable(true);
		comboBox.getEditor().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(comboBox.getEditor().getItem());
				System.out.println(comboBox.getSelectedItem().toString());
				 
				

			}
		});
		
		comboBox.addItemListener(new ItemListener() 
		{
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				//System.out.println(e.getItem().toString());
				
				
			}}
		);
		

		comboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
            	//System.out.println("ITEM: " + comboBox.getSelectedItem());
            	//System.out.println(e.getItem());
    
            }}
        );
	
		comboBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println(comboBox.getSelectedItem());
			}
		});

	}
}
