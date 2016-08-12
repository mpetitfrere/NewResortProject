package applicationPackage;

//Imports
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

class SelectTableExample
		extends 	JFrame
		implements	ListSelectionListener
{
	// Instance attributes used in this example
	private	JPanel		topPanel;
	private	JTable		table;
	private	JScrollPane scrollPane;

	// Constructor of main frame
	public SelectTableExample()
	{
		// Set the frame characteristics
		setTitle( "Simple Table Application" );
		setSize( 300, 200 );
		setBackground( Color.gray );

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		// Create columns names
		String columnNames[] = { "Column 1", "Column 2", "Column 3" };

		// Create some data
		String dataValues[][] =
		{
			{ "12", "234", "67" },
			{ "-123", "43", "853" },
			{ "93", "89.2", "109" },
			{ "279", "9033", "3092" }
		};

		// Create a new table instance
		table = new JTable( dataValues, columnNames );

		// Handle the listener
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener( this );

		// Add the table to a scrolling pane
		scrollPane = new JScrollPane( table );
		topPanel.add( scrollPane, BorderLayout.CENTER );
	}

	// Handler for list selection changes
	public void valueChanged( ListSelectionEvent event )
	{
		// See if this is a valid table selection
		if( event.getSource() == table.getSelectionModel()
						&& event.getFirstIndex() >= 0 )
		{
			// Get the data model for this table
			TableModel model = (TableModel)table.getModel();

			// Determine the selected item
			String string = (String)model.getValueAt(
									table.getSelectedRow(),
									table.getSelectedColumn() );

			// Display the selected item
			System.out.println( "Value selected = " + string );
		}
	}

	// Main entry point for this example
	public static void main( String args[] )
	{
		// Create an instance of the test application
		SelectTableExample mainFrame	= new SelectTableExample();
		mainFrame.setVisible( true );
	}
}