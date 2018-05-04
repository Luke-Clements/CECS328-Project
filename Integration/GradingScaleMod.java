/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.GradingScale;
import BackCode.Settings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author lukecjm
 */
public class GradingScaleMod 
{
    private static final String[] GRADING_SCALE_INFO_CATEGORIES = {"A", "B", "C", "D", "F", "Pass or Fail"};
    public static final String[] GRADING_SCALE_INFO_EMPTY = {"", "", "", "", "", ""};
    
    private static TextField aText;
    private static TextField bText;
    private static TextField cText;
    private static TextField dText;
    private static TextField fText;
    private static ComboBox passFailBox;
    public static void SetupGradingScaleMod(VBox gradingScaleMod, String[] gsi)
    {
        gradingScaleMod.getChildren().clear();
        HBox a = new HBox();
        aText = new TextField();
        Label promptA = new Label(GRADING_SCALE_INFO_CATEGORIES[0] + ":");
        aText.setText(gsi[0]);
        a.getChildren().addAll(promptA, aText);
        
        HBox b = new HBox();
        bText = new TextField();
        Label promptB = new Label(GRADING_SCALE_INFO_CATEGORIES[1] + ":");
        bText.setText(gsi[1]);
        b.getChildren().addAll(promptB, bText);
        
        HBox c = new HBox();
        cText = new TextField();
        Label promptC = new Label(GRADING_SCALE_INFO_CATEGORIES[2] + ":");
        cText.setText(gsi[2]);
        c.getChildren().addAll(promptC, cText);
        
        HBox d = new HBox();
        dText = new TextField();
        Label promptD = new Label(GRADING_SCALE_INFO_CATEGORIES[3] + ":");
        dText.setText(gsi[3]);
        d.getChildren().addAll(promptD, dText);
        
        HBox f = new HBox();
        fText = new TextField();
        Label promptF = new Label(GRADING_SCALE_INFO_CATEGORIES[4] + ":");
        fText.setText(gsi[4]);
        f.getChildren().addAll(promptF, fText);
        
        HBox passFail = new HBox();
        passFailBox = new ComboBox();
        String[] trueFalse = {"True", "False"};
        Label promptpassFail = new Label(GRADING_SCALE_INFO_CATEGORIES[5] + ":");
        passFailBox.setItems(FXCollections.observableArrayList(trueFalse));
        passFailBox.setValue(gsi[5]);
        passFail.getChildren().addAll(promptpassFail, passFailBox);
        
        gradingScaleMod.getChildren().addAll(a, b, c, d, f, passFail);
    }
    
    public static String[] getGradingScaleInfo(int gsID, Connection conn)
    {
        String stmt = "SELECT * FROM GradingScale WHERE gsID=" + gsID;
        
        try
        {
            PreparedStatement pStmt = conn.prepareStatement(stmt);
            ResultSet rs = pStmt.executeQuery();

            if(rs.next())
            {
                String[] gradingScaleInfo = new String[6];
                gradingScaleInfo[0] = rs.getInt("A") + "";
                gradingScaleInfo[1] = rs.getInt("B") + "";
                gradingScaleInfo[2] = rs.getInt("C") + "";
                gradingScaleInfo[3] = rs.getInt("D") + "";
                gradingScaleInfo[4] = rs.getInt("F") + "";
                gradingScaleInfo[5] = rs.getBoolean("passFail") == true ? "True" : "False";
                return gradingScaleInfo;
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return null;
    }
    public static String getGradingScaleID(Settings settings, Connection conn)
    {
        String stmt = "SELECT gsID FROM GradingScale where A= " + Float.parseFloat(aText.getText()) +
                                                     " and B= " + Float.parseFloat(bText.getText()) +
                                                     " and C= " + Float.parseFloat(cText.getText()) +
                                                     " and D= " + Float.parseFloat(dText.getText()) +
                                                     " and F= " + Float.parseFloat(fText.getText()) +
                                                     " and passFail = " + passFailBox.getSelectionModel().getSelectedItem();
        
        ResultSet rs = null;
        
        System.out.println(stmt);
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            
            rs = ps.executeQuery();
            
            rs.next();
            System.out.println(rs.getString("gsID"));
            return rs.getString("gsID");
        }
        catch(SQLException se)
        {
            if(se.getMessage().equals("Invalid cursor state - no current row."))
            {
                InsertNewGradingScale(settings, conn);
                return getGradingScaleID(settings, conn);
            }
//            se.printStackTrace();
        }
        return null;
    }
    
    public static void InsertNewGradingScale(Settings settings, Connection conn)
    {
        String Stmt = "INSERT INTO GradingScale(gsID, A, B, C, D, F, passFail) "+
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        //implement find grading scale here
        float a = Float.parseFloat(aText.getText());
        float b = Float.parseFloat(bText.getText());
        float c = Float.parseFloat(cText.getText());
        float d = Float.parseFloat(dText.getText());
        float f = Float.parseFloat(fText.getText());
        boolean passFail = false;

        try
        {
            if(aText.getText().equals("") || bText.getText().equals("") || cText.getText().equals("") || dText.getText().equals("") || fText.getText().equals(""))
            {
                throw new NullPointerException("Cannot have empty fields.");
            }
            PreparedStatement pStmt = conn.prepareStatement(Stmt);
            pStmt.setInt(1, settings.getGradingScaleCounter());
            pStmt.setFloat(2,a);
            pStmt.setFloat(3,b);
            pStmt.setFloat(4,c);
            pStmt.setFloat(5,d);
            pStmt.setFloat(6,f);
            pStmt.setBoolean(7, passFail);

            pStmt.executeUpdate();
            System.out.println("executed update");
            settings.incrementGradingScaleCounter();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(NullPointerException npe)
        {
            System.out.println("null somewhere");
            npe.printStackTrace();
            //notification window
        }
    }
    
    public static GradingScale getGradingScale(Connection conn, int gsID)
    {
        String stmt = "SELECT * FROM GradingScale WHERE gsID=" + gsID;
        GradingScale gs;
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs;
            rs = ps.executeQuery();
            
            rs.next();
            gs = new GradingScale(rs.getInt("A"), rs.getInt("B"), rs.getInt("C"), 
                    rs.getInt("D"), rs.getInt("F"), rs.getBoolean("passFail"));
            return gs;
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return null;
    }
}
