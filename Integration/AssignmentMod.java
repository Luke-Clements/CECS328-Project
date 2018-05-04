/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Assignment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author lukecjm
 */
public class AssignmentMod 
{
    private static final String[] ASSIGNMENT_INFO_CATEGORIES = {"Assignment Name", "Category", "Score", "Max Score"};
    public static final String[] ASSIGNMENT_INFO_EMPTY = {"", "", "", ""};
    private ObservableList<Assignment> assignmentItems;
    private TableView<Assignment> assignmentTable = new TableView();
    
    //sets up Assignment table
    public TableView<Assignment> SetupAssignmentTable(Connection conn, VBox assignmentModBox, ClassMod cm)
    {
        int classID;
        if(!(cm.getClassTable() == null) && !(cm.getClassTable().getSelectionModel().getSelectedItem() == null))
        {
            classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
        }
        else
        {
            classID = -1;
        }
        assignmentItems = GetAssignmentTableValues(classID, conn);
        
        assignmentTable.setItems(assignmentItems);
        
        TableColumn<Assignment,String> aNameCol = new TableColumn(ASSIGNMENT_INFO_CATEGORIES[0]);
        aNameCol.setCellValueFactory(new PropertyValueFactory("assignmentName"));
        TableColumn<Assignment,String> aCategoryCol = new TableColumn(ASSIGNMENT_INFO_CATEGORIES[1]);
        aCategoryCol.setCellValueFactory(new PropertyValueFactory("assignmentCategory"));
        TableColumn<Assignment,Float> aScoreCol = new TableColumn(ASSIGNMENT_INFO_CATEGORIES[2]);
        aScoreCol.setCellValueFactory(new PropertyValueFactory("assignmentScore"));
        TableColumn<Assignment,Float> aMaxScoreCol = new TableColumn(ASSIGNMENT_INFO_CATEGORIES[3]);
        aMaxScoreCol.setCellValueFactory(new PropertyValueFactory("assignmentMaxScore"));
        
        assignmentTable.getSelectionModel().selectedIndexProperty().addListener(e -> {
            Assignment a = assignmentTable.getSelectionModel().getSelectedItem();
            String[] assignmentInfo;
            if(a != null)
            {
                assignmentInfo = a.getAssignmentInfoArray();
            }
            else
            {
                assignmentInfo = AssignmentMod.ASSIGNMENT_INFO_EMPTY;
            }
            SetupAssignmentMod(cm, conn, assignmentModBox, assignmentInfo);
        });

        assignmentTable.getColumns().setAll(aNameCol, aCategoryCol, aScoreCol, aMaxScoreCol);
        return assignmentTable;
    }
    
    public void SetupAssignmentMod(ClassMod cm, Connection conn, VBox assignmentMod, String[] assignmentInfo)
    {
        assignmentMod.getChildren().clear();
        HBox getAssignmentName = new HBox();
        TextField nameField = new TextField();
        Label promptName = new Label(ASSIGNMENT_INFO_CATEGORIES[0] + ":");
        nameField.setText(assignmentInfo[0]);
        getAssignmentName.getChildren().addAll(promptName, nameField);
        
        HBox getCategory = new HBox();
        TextField categoryField = new TextField();
        Label promptCategory = new Label(ASSIGNMENT_INFO_CATEGORIES[1] + ":");
        categoryField.setText(assignmentInfo[1]);
        getCategory.getChildren().addAll(promptCategory, categoryField);
        
        HBox getScore = new HBox();
        TextField scoreField = new TextField();
        Label promptScore = new Label(ASSIGNMENT_INFO_CATEGORIES[2] + ":");
        scoreField.setText(assignmentInfo[2]);
        getScore.getChildren().addAll(promptScore, scoreField);
        
        HBox getMaxScore = new HBox();
        TextField maxScoreField = new TextField();
        Label promptMaxScore = new Label(ASSIGNMENT_INFO_CATEGORIES[3] + ":");
        maxScoreField.setText(assignmentInfo[3]);
        getMaxScore.getChildren().addAll(promptMaxScore, maxScoreField);
        
        Button saveAssignment = new Button("Save Assignment Changes");
        saveAssignment.setOnMouseClicked(e -> 
        {
            String Stmt = "INSERT INTO Assignment(cID, aName, aCategory, score, maxScore) "+
                "VALUES(?, ?, ?, ?, ?)";
           
            int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
            //perform category check in categoryweight
            String aName = nameField.getText();
            String aCategory = categoryField.getText();
            float score = Float.parseFloat(scoreField.getText());
            float maxScore = Float.parseFloat(maxScoreField.getText());

            try
            {
                if(aName.equals("") || aCategory.equals("") || scoreField.getText().equals("") || maxScoreField.getText().toString().equals(""))
                {
                    throw new NullPointerException("Cannot have empty fields.");
                }
                PreparedStatement pStmt = conn.prepareStatement(Stmt);
                pStmt.setInt(1, classID);
                pStmt.setString(2,aName);
                pStmt.setString(3,aCategory);
                pStmt.setFloat(4,score);
                pStmt.setFloat(5,maxScore);

                pStmt.executeUpdate();
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
            assignmentItems = GetAssignmentTableValues(classID, conn);
            for(Assignment a:assignmentItems)
            {
                System.out.println(a.getAssignmentName());
            }
            assignmentTable.setItems(assignmentItems);            
        });
        
        Button deleteAssignment = new Button("Delete Assignment");
        deleteAssignment.setOnMouseClicked(e -> {
            int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
            deleteAssignment(classID, assignmentTable.getSelectionModel().getSelectedItem(), conn);
            System.out.println(assignmentTable.getSelectionModel().getSelectedItem().getAssignmentName());
            System.out.println(assignmentTable.getSelectionModel().getSelectedItem().getAssignmentCategory());
            assignmentItems = GetAssignmentTableValues(classID, conn);
            assignmentTable.setItems(assignmentItems);
        });
        
        assignmentMod.getChildren().addAll(getAssignmentName, getCategory, getScore, getMaxScore, saveAssignment, deleteAssignment);
    }
    
    public static void deleteAssignment(int classID, Assignment a, Connection conn)
    {
        String stmt = "DELETE FROM Assignment WHERE cID=" + classID + 
                                            " and aName='" + a.getAssignmentName() +
                                            "' and aCategory='" + a.getAssignmentCategory() + "'";
        
        System.out.println(stmt);
        try
        {
            PreparedStatement pStmt = conn.prepareStatement(stmt);
            pStmt.executeUpdate();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
    }
    
    public static ObservableList<Assignment> GetAssignmentTableValues(int classID, Connection conn)
    {
        String stmt = "SELECT * FROM Assignment WHERE cID=" + classID;
        ArrayList<Assignment> aa = new ArrayList();
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            Assignment a = new Assignment();
            
            aa.add(a);

            if(rs.next())
            {
                do
                {
                    a = new Assignment();
                    a.setName(new SimpleStringProperty(rs.getString("aName")));
                    a.setCategory(new SimpleStringProperty(rs.getString("aCategory")));
                    a.setScore(new SimpleFloatProperty(rs.getFloat("score")));
                    a.setMaxScore(new SimpleFloatProperty(rs.getFloat("maxScore")));
                    aa.add(a);
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return FXCollections.observableArrayList(aa);
    }
    
    public static ObservableList<Assignment> GetAssignmentValues(int classID, Connection conn)
    {
        String stmt = "SELECT * FROM Assignment WHERE cID=" + classID;
        ArrayList<Assignment> aa = new ArrayList();
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            Assignment a;

            if(rs.next())
            {
                do
                {
                    a = new Assignment();
                    a.setName(new SimpleStringProperty(rs.getString("aName")));
                    a.setCategory(new SimpleStringProperty(rs.getString("aCategory")));
                    a.setScore(new SimpleFloatProperty(rs.getFloat("score")));
                    a.setMaxScore(new SimpleFloatProperty(rs.getFloat("maxScore")));
                    System.out.println(a.getAssignmentCategory());
                    System.out.println(a.getAssignmentName());
                    aa.add(a);
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return FXCollections.observableArrayList(aa);
    }
}
