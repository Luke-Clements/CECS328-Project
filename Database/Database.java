/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author lukecjm
 */
public class Database 
{
    private String DB_URL;
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    /*
    SELECT 
    */
    
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
        ResultSet rs;
        try {
            rs = conn.getMetaData().getTables(null, null, "%", null);
            
            if(rs.next())
            {
                do
                {
                    //will exit if a table that is non system-generated is found
                    if(!rs.getString(3).contains("SYS"))
                    {
                        System.out.println(rs.getString(3));
                        return false;
                    }
                }
                while(rs.next());
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public void initializeDatabase(Connection conn, String filepath)
    {
        String[] statements = getSQLTableDDL(filepath);
        try
        {
            PreparedStatement ps = null;
            for(int i = 0;i < statements.length;i++)
            {
                ps = conn.prepareStatement(statements[i]);
                System.out.println(statements[i]);
                
                ps.executeUpdate();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String[] getSQLTableDDL(String filepath)
    {
        JSONParser parser = new JSONParser();
        String[] SQLTableDDLStrings = null;
        
        try
        {
            Object object = parser.parse(new FileReader(filepath));
            JSONObject jsonObj = (JSONObject)object;

            JSONArray SQLTableDDL = (JSONArray)jsonObj.get("SQLTableDDL");
            SQLTableDDLStrings = new String[SQLTableDDL.size()];
            for (int i = 0;i < SQLTableDDL.size();i++) 
            {
                SQLTableDDLStrings[i] = (String)SQLTableDDL.get(i);
            }
        } catch (FileNotFoundException e) {
            System.out.println("The selected file at " + filepath + " does not exist.");
        } catch (IOException ex) {
            System.out.println("There was an error reading the file.");
        } catch (ParseException ex) {
            System.out.println("The database table creation data has been corrupted.");
        }
        return SQLTableDDLStrings;
    }
    
    //only used to help create the json files that hold the databases DDL.
    //  never used in the execution of the full program
    private void saveSQLTableDDL(String[] statements, String filepath)
    {
        JSONObject obj = new JSONObject();
        JSONArray jstatements = new JSONArray();
        
        for(String statement: statements)
        {
            jstatements.add(statement);
        }
        
        obj.put("SQLTableDDL", jstatements);

        try {
            FileWriter fw = new FileWriter(filepath);

            fw.write(obj.toJSONString());
            fw.close();
        } catch (Exception ex) 
        {

        }
    }
}
