import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import com.mysql.jdbc.ResultSetMetaData;

public class DConnector {
	public static void main(String[] args) {

		new Thread(new Runnable() {
			public void run() {
				runData();
			}
		},"thread1").start();
		new Thread(new Runnable() {
			public void run() {
				runData();
			}
		},"thread2").start();
		}
		
	public static void runData(){
		while(true){
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"Cannot find the driver in the classpath!", e);
			}

			String url = "jdbc:mysql://localhost:3306/DATABASENAME";
			String username = "USER";
			String password = "PASSWORD";
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, username,
						password);
				connection.setAutoCommit(false);
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				Statement stmt = (Statement) connection.createStatement();
				ResultSet seta = null;
				stmt.executeUpdate("DELETE FROM product WHERE name = 'videokaart' OR  name = 'harde schijf'");
				stmt.executeUpdate("INSERT INTO product VALUES ('videokaart', 100)");
				stmt.executeQuery("START TRANSACTION");
				Thread.sleep(1000);
				seta = stmt.executeQuery("SELECT * FROM product");
				
				ResultSetMetaData rsmd = (ResultSetMetaData) seta
						.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				while (seta.next()) {

					for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1)
							System.out.print(",  ");
						String columnValue = seta.getString(i);
						System.out.print(columnValue + "\n " + rsmd.getColumnName(i));
					}
					
				}
				
				int queue = (int) (Math.random() * 11);
				System.out.println("\n" + Thread.currentThread().getName() + ": Sleep " +
				queue + " sec");
				 // sleep some seconds
				
				 Thread.sleep(queue * 1000);
				stmt.executeUpdate("UPDATE product SET amount = 125");
connection.commit();
			} catch (SQLException e) {
				throw new RuntimeException("Cannot connect the database!",
						e);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
		
}
