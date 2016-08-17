import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import applicationPackage.MySQLConnection;


public class newTable {

	public static void main(String[] args) throws SQLException {
		Connection conn = MySQLConnection.dbConnector();
	 
	String query = "INSERT into `new_schema`.`ResortManagement` (`AssociationName`, `Year`, `Type`, `Aisle`, `Row`, `Column`, `Depth`) "
			+ "values (?,?,?,?,?,?,?)";

	PreparedStatement  prepare = conn.prepareStatement(query);
	String associationName = "EMPTY";
	int year = 0000;
	String type = "EMPTY";
	int count = 0;
	//Aisle
		for (int a = 1; a<=5; a++)
		{
			

			//Row
			for (int b = 1; b<=10; b++)
			{
				//Column
				
				if(a==1){
					for (int c = 1; c<=35; c++)
					{
						//Depth
						for (int d = 1; d<=2; d++)
						{

							if(d == 2)
							{
								prepare.setString(1, associationName);
								prepare.setInt(2, year);
								prepare.setString(3, type);
								prepare.setInt(4, a);
								prepare.setInt(5, b);
								prepare.setInt(6, c);
								prepare.setString(7, "B");
								prepare.executeUpdate();

								//System.out.println("Aisle: " + a + " Row: " + b + " Columm: " + c + " Depth: " +  " B" );
								count += 1;

							}
							else{
								prepare.setString(1, associationName);
								prepare.setInt(2, year);
								prepare.setString(3, type);
								prepare.setInt(4, a);
								prepare.setInt(5, b);
								prepare.setInt(6, c);
								prepare.setString(7, "F");
								prepare.executeUpdate();

								//System.out.println("Aisle: " + a + " Row: " + b + " Columm: " + c + " Depth: " +  " F" );
								count += 1;

							}
						}

					}
					
				}
				else{
					for (int c = 1; c<=30; c++)
					{
						//Depth
						for (int d = 1; d<=2; d++)
						{

							if(d == 2)
							{
								prepare.setString(1, associationName);
								prepare.setInt(2, year);
								prepare.setString(3, type);
								prepare.setInt(4, a);
								prepare.setInt(5, b);
								prepare.setInt(6, c);
								prepare.setString(7, "B");

								//System.out.println("Aisle: " + a + " Row: " + b + " Columm: " + c + " Depth: " +  " B" );
								count += 1;
								prepare.executeUpdate();

							}
							else{
								prepare.setString(1, associationName);
								prepare.setInt(2, year);
								prepare.setString(3, type);
								prepare.setInt(4, a);
								prepare.setInt(5, b);
								prepare.setInt(6, c);
								prepare.setString(7, "F");
								prepare.executeUpdate();

								//System.out.println("Aisle: " + a + " Row: " + b + " Columm: " + c + " Depth: " +  " F" );
								count += 1;

							}
						}

					}
				}
			}

			System.out.println("Total was: " + count);

		}
	}
}

