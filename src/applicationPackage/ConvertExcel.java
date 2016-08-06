package applicationPackage;
//comment comment
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;


/**
 *  Converts Data from JTable to excel
 *  boolean tempalteState, when true only writes the column header for excel
 */
public class ConvertExcel {

    public static void exportExcel(boolean templateState)throws IOException
    {
        JTable table = new JTable();
        DateTime dt = new DateTime();
        UpDateTable(table);

        String excelName = excelName();
        File file = new File(excelName);
        Workbook wb = new XSSFWorkbook(); //Excel workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        Row row = sheet.createRow(1); //Row created at line 3
        TableModel model = table.getModel(); //Table model

        Row headerRow = sheet.createRow(0); //Create row at line 0
        String [] colName = new String[model.getColumnCount()];
        for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
            headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
            colName[headings] = table.getColumnName(headings);

        }

        if(templateState == true)
        {//When Template is chosen only print columns
             wb.write(new FileOutputStream(file.toString()));//Save the file
              openExcel(file);
            return;
        }
        for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
            for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
                if(table.getColumnName(cols).equals(colName[cols]) )
                {
                    String columnString = colName[cols];
                    //System.out.println("Col: " + columnString);
                    if(!isColumnIntType(columnString))
                    {//writes cell as float type to remove error checking in excel
                        XSSFCell cell = (XSSFCell) row.createCell(cols);//create a cell at the row,col location
                        cell.setCellValue(Float.parseFloat((String) (model.getValueAt(rows, cols))));
                       //row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
                    }
                    else//writes the cell as strings
                    {
                        XSSFCell cell = (XSSFCell) row.createCell(cols);//create a cell at the row,col location
                        String x = (String) (model.getValueAt(rows, cols));//get he  value from table
                        //cell.setCellType(Cell.CELL_TYPE_STRING);
                        DataFormatter dfTemp = new DataFormatter();
                        cell.setCellValue(x);
                        cell.setCellValue( dfTemp.formatCellValue(cell));
                    }
                }
            }
            //Set the row to the next one in the sequence
            row = sheet.createRow((rows + 1));
        }//end of row loop
        wb.write(new FileOutputStream(file.toString()));//Save the file
       openExcel(file);
    }//end of method

    //overloaded method for TestMain
    /**
     *
     *
     */
	public static void exportExcel(JTable table)throws IOException
    {

        String excelName = excelName();
        File file = new File(excelName);
        //UpDateTable(table);
        Workbook wb = new XSSFWorkbook(); //Excel workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        //Row row = sheet.createRow(1); //Row created at line 3
        
        

        TableModel model = table.getModel(); //Table model

        Row headerRow = sheet.createRow(0); //Create row at line 0
        String [] colName = new String[model.getColumnCount()];
        for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
            headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
            colName[headings] = table.getColumnName(headings);
        }
      
       
        
        Row row = sheet.createRow(1); //Row created at line 3


        for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
        	//Set the row to the next one in the sequence
            row = sheet.createRow((rows + 1));
            for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
                if(table.getColumnName(cols).equals(colName[cols]) )
                {
                	 String columnString = colName[cols];
                    if(!isColumnIntType(columnString))
                    {//writes cell as float type to remove error checking in excel
                        XSSFCell cell = (XSSFCell) (row).createCell(cols);//create a cell at the row,col location
                        cell.setCellValue(Float.parseFloat((String) (model.getValueAt(rows, cols))));

                    }
                    else//writes the cell as strings
                    {
                        XSSFCell cell = (XSSFCell) headerRow.createCell(cols);//create a cell at the row,col location
                        String x = (String) (model.getValueAt(rows, cols));//get he  value from table

                        DataFormatter dfTemp = new DataFormatter();
                        cell.setCellValue(x);
                        cell.setCellValue( dfTemp.formatCellValue(cell));
                        System.out.println(x);

                    }
                }
            }
            //Set the row to the next one in the sequence
            


        }//end of row loop
        wb.write(new FileOutputStream(file.toString()));//Save the file
        openExcel(file);
    }//end of method


    public static PreparedStatement initPrepare()
    {
        Connection conn = MySQLConnection.dbConnector();
        PreparedStatement prepare = null;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String query = "INSERT into `new_schema`.`ResortManagement` (`AssociationName`, `Year`, `Type`, `Aisle`, `Row`, `Column`, `Depth`) "
                + "values (?,?,?,?,?,?,?)";

        try {
            prepare = conn.prepareStatement(query);
        } catch (SQLException e) { e.printStackTrace(); }
        return prepare;
    }
    

    public static boolean isColumnIntType(String colName)
    {
        if(colName.equals("AssociationName")||(colName.equals("Type"))
                || (colName.equals("Depth")))
                {
                    return true;
                }
        return false;
    }

    public static void openExcel(File file)
    {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *  Verifies File import meets sqlite's standard by checking if the columns match
     */
    public static boolean validateExcel(File fs) throws SQLException
    {
        Connection conn = MySQLConnection.dbConnector();
        String testTable_String = "Select Select AssoicationName, Year, Type, Aisle, 'Row', 'Column', Depth from ResortManagement";
        PreparedStatement showTestTable = null;
        ResultSet rs = null;
        try {
            showTestTable = conn.prepareStatement(testTable_String);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs = showTestTable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ResultSetMetaData rsmd=rs.getMetaData();
        //Coding to get columns-
        int cols=rsmd.getColumnCount();
        String c[]=new String[cols];
        for(int i=0;i<cols;i++){
            System.out.println("Count: " + i + " Name: " +rsmd.getColumnName(i+1).toString() );
            c[i]=rsmd.getColumnName(i+1);
        }

        FileInputStream file = null;
        try {
            file = new FileInputStream(fs);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }

        //Get the workbook instance for XLS file
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e2) {
            e2.printStackTrace();
        }


        //Get first sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        //Iterate through each rows from first sheet
        Iterator<Row> rowIterator = sheet.iterator();
        Row row = sheet.getRow(0);
        int rowsCount = sheet.getLastRowNum();
        Cell cell; 
        String [] colHeader =  new String[rowsCount];
        FileWriter fw = null;
        Boolean flag = true;
        try {
            fw = new FileWriter("LogMissingColumns.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        } // needed so printwriter will not overwrite
        PrintWriter writer = new PrintWriter(fw);
        for(int count = 0; count < c.length; count++)
        {//get column headers from excel
            if(row.getCell(count) == null)
            {
//                int i = 1;
//                System.out.println((char)(i+'A'-1));
                writer.println("Column " +(c[count]) + " at  Excel column: " + (char)(count+'A'));
                flag = false;
        
            }
            else{
                cell = row.getCell(count);
//                System.out.println("Loop NAME: " + c[count]);

                if(!cell.getStringCellValue().equals(c[count]) || cell == null )
                {
//                    System.out.println("NOT Matched: " + cell.getStringCellValue());
                    //conn.close();
                    writer.println("Column " +(c[count]) + " at  Excel column: " + (char)(count+'A'));
                    flag = false;
                }
            }
        }
        

        conn.close();
        writer.close();
        try {
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;

    }

    //checks dir if fielname already, if it does output new filename
    public static String excelName()
    {
        String fileLocation  = System.getProperty("user.dir");

        File theDir = new File("Excel");

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            //System.out.println("creating directory: " + System.getProperty("user.dir") + "/Excel");
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
        File folder = new File(fileLocation + "/Excel/");
        File[] listOfFiles = folder.listFiles();

        File file = new File("Excel\\\\Form(1).xlsx");
        //File file2 = new File("Excel\\\\Apachi " + date + ".xlsx");

        String fileName = fileLocation + "\\\\" + file.toString();

        //loop will rename file if the filename exist already at the directory
        for(int i =0; i < listOfFiles.length;i++)
        {
            //System.out.println(listOfFiles[i]);

            if(fileName.equals(listOfFiles[i].toString()))
            {
                file = new File("Excel\\\\Form" + "(" + (i) +")" + ".xlsx");//need to change i to i plus 1
                fileName = (file.toString());
                break;
            }
        }

        return fileName;
    }//end of method

    public static String getDate()
    {
        DateTime dt = new DateTime();
        String b = dt.toString("MM-dd-yyyy");
        return b;
    }

    public static void UpDateTable(JTable table)
    {//Duplicate
        try
        {
            Connection conn = MySQLConnection.dbConnector();
            DefaultTableModel dm = new DefaultTableModel();
            //query and resultset
            String testTable_String = "Select AssociationName, Year, Type, Aisle, `Row`, `Column`, Depth from ResortManagement";
            PreparedStatement showTestTable = conn.prepareStatement(testTable_String);
            ResultSet rsTest = showTestTable.executeQuery();
            addRowsAndColumns(rsTest, dm);
            table.setModel(dm);
            table.revalidate();
            table.repaint();
            table.validate();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public static void addRowsAndColumns(ResultSet rs, DefaultTableModel dm) throws SQLException
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
        while(rs.next()){
             for(int i=0;i<cols;i++){
                    row[i]=rs.getString(i+1);
                }
            dm.addRow(row);
        }
    }

    private static Boolean isCellEmpty(Cell param)
    {
        if(param == null || param.getCellType() == Cell.CELL_TYPE_BLANK 
                ||param.getCellType() ==   Cell.CELL_TYPE_ERROR)
        {
            return true;
        }
        return false;
        
    }

    /**
     *  Inputs value from excel into preparedStamtent
     * @param row 
     */
    public static PreparedStatement getParepareValues(Cell[] cellArray, int j, PreparedStatement prepare, int row) throws SQLException
    {
        //Vars will be used multiple times
        int cellInt;
        double cellDoube;
        String cellTempString = null;
    
        if(cellArray[j].getCellType() == Cell.CELL_TYPE_STRING && (j==0 || j==2 || j==6) )
        {
            try {
                if(j==6 && !(cellArray[j].getStringCellValue().equals("F") 
                        ||(cellArray[j].getStringCellValue().equals("B"))
                        ||(cellArray[j].getStringCellValue().equals("f") 
                        ||(cellArray[j].getStringCellValue().equals("b"))))
                   )
                {
                    JOptionPane.showMessageDialog(null, "Column Depth(G) at row: " + row + " does not contain F or B , please fix this and reimport.", 
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    return prepare;
                }
                prepare.setString(j+1, cellArray[j].getStringCellValue());
                System.out.println(cellArray[j].getStringCellValue());
                return prepare;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        else if(cellArray[j].getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            try {
                
                cellDoube = cellArray[j].getNumericCellValue();
                cellInt = (int) cellDoube;

                if(String.valueOf(cellInt).length() !=4 && j==1)
                {
                    JOptionPane.showMessageDialog(null, "Column Year(B) at row: " + row + 
                            " Year must be ented in this format: YYYY "  , "Error", JOptionPane.ERROR_MESSAGE);
                    return prepare;
                }
                else if((cellInt <=0 || cellInt >6) && j == 3 )
                {
                    JOptionPane.showMessageDialog(null, "Column Aisle(D) at row: " + row + 
                            " Aisle must be between 1 and 6"  , "Error", JOptionPane.ERROR_MESSAGE);
                    return prepare;
                }
                
                else if((cellInt <=0 || cellInt >10) && j==4 )
                {
                    JOptionPane.showMessageDialog(null, "Column Row(E) at row: " + row + " .Row must be between 1 and 10"  , "Error", JOptionPane.ERROR_MESSAGE);
                    return prepare;
                }
                else if((cellInt <=0 || cellInt >30) && j ==5)
                {
                    JOptionPane.showMessageDialog(null, "Column(F) at row: " + row +  " must be between 1 and 30"  , "Error", JOptionPane.ERROR_MESSAGE);
                    return prepare;
                }
                else{
                    prepare.setInt(j+1, cellInt);
                    System.out.println(cellArray[j].getNumericCellValue());
                    return prepare;
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        //Never reaches
        return prepare;
    }//end of method

    //Overload for user to import excel
    public static boolean importExcel(File fs) throws SQLException
    {//uncomment
        PreparedStatement prepare = initPrepare();
        FileInputStream file = null;
        try {
            file = new FileInputStream(fs);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }

        //Get the workbook instance for XLS file
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        //Get first sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        //Iterate through each rows from first sheet
        Iterator<Row> rowIterator = sheet.iterator();
        XSSFRow row = sheet.getRow(1);
        int rowsCount = row.getRowNum();
//        String [] colHeader =  new String[size];
        int columnLength = 0;


        //System.out.println("Total Number of Rows: " + (rowsCount + 1));
        int size = 7; //num of  columns
        Cell [] cellArray = new Cell[size];    
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {//start at 1 to skip column
            row = sheet.getRow(i);//change colcounts to row
            int colCounts = columnLength;//assign colCounts to the length of max num of cols
            //System.out.println("COLUMN COUNT: "+ colCounts);        
            for (int j = 0; j < size; j++) {
                //System.out.println();
                cellArray[j] = row.getCell(j);
                if(!isCellEmpty(cellArray[j]))
                {
                    prepare = getParepareValues(cellArray,j,prepare,i);
                    if(j==size-1)//num of columns in database
                    {
                        prepare.executeUpdate();
                        prepare.getConnection().commit();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "There is an empty cell, please fill with"
                            + " the appropriate value and reimport.", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }


            }// end of j loop
            
        }//end of i loop
        try {
            file.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return true;


    }
}
    //end of method
    

//    public static void main(String args[]) throws IOException
//    {
//        //writeExcel();
//        long startTime = System.currentTimeMillis();
//        //JTable table = new JTable();
//        File  file = new File("C:\\Users\\Zelos\\Documents\\GitHub\\SpecsProject\\SpecsProject\\Excel\\ResortExcel.xlsx");
//        
//        try {
//            importExcel(file);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        long endTime   = System.currentTimeMillis();
//        long totalTime = endTime - startTime;
//        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
//        Date resultdate = new Date(totalTime);
//        //System.out.println(sdf.format(resultdate));
//        System.out.println("SUCCESS");
//
//    }

//}//End of ConvertExcel