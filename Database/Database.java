/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author lukecjm
 */
public class Database 
{
    private String DB_URL;
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    
    public Database(String dbURL, String dbPath)
    {
        this.DB_URL = dbURL;
        this.setDBDir(dbPath);
    }
    
    public Connection getConnection()
    {
        try
        {
            Class.forName(driver);
            return DriverManager.getConnection(this.DB_URL);
        }
        catch(SQLException se)
        {
            System.out.println("Database access error");
        } 
        catch (ClassNotFoundException ex) {
            System.out.println("Class not found exception");
        }
        return null;
    }
    
    private void setDBDir(String dbPath) 
    {
        // Set the db system directory.
        System.setProperty("derby.system.home", dbPath);
    }
    
    public boolean IsEmpty(Connection conn)
    {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM GPTTest.Customers");
            ps.executeQuery();
        } catch (SQLException ex) {
            return true;
        }
        return false;
    }
    
    public void initializeDatabase(Connection conn, String tables[])
    {
        try
        {
            PreparedStatement ps = null;
            for(int i = 0;i < tables.length;i++)
            {
                ps = conn.prepareStatement(tables[i]);
                System.out.println(tables[i]);
                
                ps.executeUpdate();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
