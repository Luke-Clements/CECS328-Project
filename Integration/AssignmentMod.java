/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Assignment;
import BackCode.Settings;
import BackCode.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.beans.property.SimpleFloatProperty;
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
    public TableView<Assignment> SetupAssignmentTable(StudentMod sm, Settings settings, Connection conn, VBox assignmentModBox, ClassMod cm)
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
        assignmentItems = GetAssignmentTableValues(settings, sm, classID, conn);
        
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
            SetupAssignmentMod(sm, settings, cm, conn, assignmentModBox, assignmentInfo);
        });

        assignmentTable.getColumns().setAll(aNameCol, aCategoryCol, aScoreCol, aMaxScoreCol);
        return assignmentTable;
    }
    
    public void SetupAssignmentMod(StudentMod sm, Settings settings, ClassMod cm, Connection conn, VBox assignmentMod, String[] assignmentInfo)
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
        
        Button saveAssignment;
        if(settings.getUserMode().get() == Settings.STUDENT ||
           (settings.getUserMode().get() == Settings.TEACHER && sm.getTable().getSelectionModel().getSelectedItem() == null))
        {
            saveAssignment = new Button("Save Class Assignment");
        }
        else
        {
            saveAssignment = new Button("Save Student Assignment");
        }
        saveAssignment.setOnMouseClicked(e -> 
        {
            try
            {
                Assignment a = new Assignment();
                a.setName(new SimpleStringProperty(nameField.getText()));
                a.setCategory(new SimpleStringProperty(categoryField.getText()));
                a.setMaxScore(new SimpleFloatProperty(Float.parseFloat(maxScoreField.getText())));
                a.setScore(new SimpleFloatProperty(Float.parseFloat(scoreField.getText())));

                int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
                if(CategoryWeightMod.isValidCategory(a.getAssignmentCategory(), conn, classID))
                {
                    insertUpdateAssignment(settings, sm, classID, a, conn);
                }
            }
            catch(NumberFormatException nfe)
            {
                SettingsInfo.Notification("The score and max score fields must contain an integer.");
            }
        });
        
        Button deleteAssignment;
        if(settings.getUserMode().get() == Settings.STUDENT ||
           (settings.getUserMode().get() == Settings.TEACHER && sm.getTable().getSelectionModel().getSelectedItem() == null))
        {
            deleteAssignment = new Button("Delete Class Assignment");
        }
        else
        {
            deleteAssignment = new Button("Delete Student Assignment");
        }
        deleteAssignment.setOnMouseClicked(e -> {
            int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
            deleteAssignment(settings, sm, classID, assignmentTable.getSelectionModel().getSelectedItem(), conn);
            assignmentItems = GetAssignmentTableValues(settings, sm, classID, conn);
            assignmentTable.setItems(assignmentItems);
        });
        
        assignmentMod.getChildren().addAll(getAssignmentName, getCategory, getScore, getMaxScore, saveAssignment, deleteAssignment);
    }
    //finished
    public void insertUpdateAssignment(Settings settings, StudentMod sm, int classID, Assignment a, Connection conn)
    {
        int sID;
        Student s = null;
        String aName = a.getAssignmentName();
        String aCategory = a.getAssignmentCategory();
        float score = a.getAssignmentScore();
        float maxScore = a.getAssignmentMaxScore();
        String stmt;
        String updateStmt;
        String testStmt;
        if(sm.getTable() != null)
        {
            s = sm.getTable().getSelectionModel().getSelectedItem();
        }
        
        if(s == null)
        {
            sID = -1;
        }
        else
        {
            sID = s.getSID();
        }
        if(settings.getUserMode().get() == Settings.STUDENT)
        {
            //for inserting values into assignment table when the student ID is unknown (or when program is in student mode)
            stmt = "INSERT INTO Assignment(cID, aName, aCategory, score, maxScore) "+
                    "VALUES(?, ?, ?, ?, ?)";
            updateStmt = "UPDATE Assignment SET score=" + score + ", maxScore=" + maxScore + 
                            " WHERE cID=" + classID + " and aName='" + aName + 
                                    "' and aCategory='" + aCategory + "'";
            testStmt = "SELECT cID, aName, aCategory FROM Assignment WHERE cID=" + classID + " and aName='" +
                                        aName + "' and aCategory='" + aCategory + "'";
        }
        else
        {
            //for inserting values into assignment table when studeitn ID is known in teacher mode
            stmt = "INSERT INTO Assignment(cID, aName, aCategory, score, maxScore, sID) "+
                    "VALUES(?, ?, ?, ?, ?, ?)";
            updateStmt = "UPDATE Assignment SET score=" + score + ", maxScore=" + maxScore +
                            " WHERE cID=" + classID + " and aName='" + aName + 
                                    "' and aCategory='" + aCategory + "' and sID=" + s.getSID();
            testStmt = "SELECT cID, sID, aName, aCategory FROM Assignment WHERE cID=" + classID + " and aName='" +
                                aName + "' and aCategory='" + aCategory + "' and sID=" + s.getSID();
        }

        try
        {
            if(aName.equals("") || aCategory.equals("") || (score+"").equals("") || (maxScore+"").equals(""))
            {
                throw new NullPointerException("Cannot have empty fields.");
            }
            PreparedStatement pStmt = conn.prepareStatement(testStmt);
            ResultSet rs = pStmt.executeQuery();
            
            if(rs.next() && rs.getString("aName").equals(aName) && rs.getString("aCategory").equals(aCategory) &&
                    rs.getInt("cID") == classID && rs.getInt("sID") == sID)
            {
                pStmt = conn.prepareStatement(updateStmt);
                    
                pStmt.executeUpdate();
            }
            else
            {
                pStmt = conn.prepareStatement(stmt);
                pStmt.setInt(1,classID);
                pStmt.setString(2,aName);
                pStmt.setString(3,aCategory);
                pStmt.setFloat(4,score);
                pStmt.setFloat(5,maxScore);
                if(settings.getUserMode().get() != Settings.STUDENT && s != null)
                {
                    pStmt.setInt(6, sID);
                }

                pStmt.executeUpdate();
            }
        }
        catch(SQLException se)
        {
            SettingsInfo.Notification(se.getMessage());
        }

        assignmentItems = GetAssignmentTableValues(settings, sm, classID, conn);
        assignmentTable.setItems(assignmentItems);     
    }
    //finished
    public static void deleteAssignment(Settings settings, StudentMod sm, int classID, Assignment a, Connection conn)
    {
        int sID;
        Student s = null;
        if(sm.getTable() != null)
        {
            s = sm.getTable().getSelectionModel().getSelectedItem();
        }
        if(settings.getUserMode().get() == Settings.STUDENT || s == null)
        {
            sID = -1;
        }
        else
        {
            sID = s.getSID();
        }
        String stmt;
        
        if(settings.getUserMode().get() == Settings.STUDENT || s == null)
        {
            stmt = "DELETE FROM Assignment WHERE cID=" + classID + 
                                            " and aName='" + a.getAssignmentName() +
                                            "' and aCategory='" + a.getAssignmentCategory() + "'";
        }
        else
        {
            stmt = "DELETE FROM Assignment WHERE cID=" + classID + 
                                            " and sID=" + sID +
                                            " and aName='" + a.getAssignmentName() +
                                            "' and aCategory='" + a.getAssignmentCategory() + "'";
        }
        
        try
        {
            PreparedStatement pStmt = conn.prepareStatement(stmt);
            pStmt.executeUpdate();
        }
        catch(SQLException se)
        {
            SettingsInfo.Notification(se.getMessage());
        }
    }
    //finished
    public static ObservableList<Assignment> GetAssignmentTableValues(Settings settings, StudentMod sm, int classID, Connection conn)
    {
        ArrayList<Assignment> aa = new ArrayList();
        
        Assignment a = new Assignment();

        aa.add(a);

        aa.addAll(GetAssignmentValues(settings, sm, classID, conn));
        return FXCollections.observableArrayList(aa);
    }
    //finished
    public static ObservableList<Assignment> GetAssignmentValues(Settings settings, StudentMod sm, int classID, Connection conn)
    {
        int sID;
        Student s = null;
        String stmt;
        if(sm != null && sm.getTable() != null)
        {
            s = sm.getTable().getSelectionModel().getSelectedItem();
        }
        if(s == null)
        {
            sID = -1;
        }
        else
        {
            sID = s.getSID();
        }
        if(settings.getUserMode().get() == Settings.STUDENT)
        {
            stmt = "SELECT * FROM Assignment WHERE cID=" + classID;
        }
        else
        {
            stmt = "SELECT * FROM Assignment WHERE cID=" + classID +
                                                " and sID=" + sID;
        }
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
                    aa.add(a);
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            if(sID != -1)
            {
                SettingsInfo.Notification(se.getMessage());
            }
        }
        return FXCollections.observableArrayList(aa);
    }
}
