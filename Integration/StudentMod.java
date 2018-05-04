/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Settings;
import BackCode.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
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
public class StudentMod 
{
    private static final String[] STUDENT_INFO_CATEGORIES = {"Student Name", "Student ID", "Student Email"};
    public static final String[] STUDENT_INFO_EMPTY = {"", "", ""};
    private ObservableList<Student> studentItems;
    private TableView<Student> studentTable = new TableView();
    
    public TableView<Student> getTable()
    {
        return studentTable;
    }
    
    //sets up Assignment table
    public TableView<Student> SetupStudentTable(Settings settings, Connection conn, VBox studentModBox, VBox assignmentModBox, ClassMod cm, AssignmentMod am)
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
        studentItems = GetStudentTableValues(classID, conn);
        
        studentTable.setItems(studentItems);
        
        TableColumn<Student,String> sNameCol = new TableColumn(STUDENT_INFO_CATEGORIES[0]);
        sNameCol.setCellValueFactory(new PropertyValueFactory("sName"));
        TableColumn<Student,Integer> idCol = new TableColumn(STUDENT_INFO_CATEGORIES[1]);
        idCol.setCellValueFactory(new PropertyValueFactory("sID"));

        
        studentTable.getSelectionModel().selectedIndexProperty().addListener(e -> {
            Student s = studentTable.getSelectionModel().getSelectedItem();
            String[] studentInfo;
            if(s != null)
            {
                studentInfo = s.getStudentInfoArray();
            }
            else
            {
                studentInfo = StudentMod.STUDENT_INFO_EMPTY;
            }
            am.SetupAssignmentTable(this, settings, conn, assignmentModBox, cm);
            SetupStudentMod(cm, conn, studentModBox, studentInfo);
        });

        studentTable.getColumns().setAll(sNameCol, idCol);
        return studentTable;
    }
    
    public void SetupStudentMod(ClassMod cm, Connection conn, VBox studentMod, String[] studentInfo)
    {
        studentMod.getChildren().clear();
        HBox getStudentName = new HBox();
        TextField nameField = new TextField();
        Label promptName = new Label(STUDENT_INFO_CATEGORIES[0] + ":");
        nameField.setText(studentInfo[0]);
        getStudentName.getChildren().addAll(promptName, nameField);
        
        HBox getEmail = new HBox();
        TextField emailField = new TextField();
        Label promptEmail = new Label(STUDENT_INFO_CATEGORIES[2] + ":");
        emailField.setText(studentInfo[1]);
        getEmail.getChildren().addAll(promptEmail, emailField);
        
        HBox getStudentID = new HBox();
        TextField idField = new TextField();
        Label promptId = new Label(STUDENT_INFO_CATEGORIES[1] + ":");
        idField.setText(studentInfo[2]);
        getStudentID.getChildren().addAll(promptId, idField);
        
        Button saveStudent = new Button("Save Student Changes");
        saveStudent.setOnMouseClicked(e -> 
        {
            try
            {
                Student s = new Student();
                s.setSName(new SimpleStringProperty(nameField.getText()));
                s.setSEmail(new SimpleStringProperty(emailField.getText()));
                s.setSID(new SimpleIntegerProperty(Integer.parseInt(idField.getText())));
                int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
                this.insertUpdateStudent(classID, s, conn);
            }
            catch(NumberFormatException nfe)
            {
                SettingsInfo.Notification("The id field must contain an integer.");
            }
        });
        
        Button removeStudentFromClass = new Button("Remove Student From Class");
        removeStudentFromClass.setOnMouseClicked(e -> {
            int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
            removeStudentFromClass(classID, studentTable.getSelectionModel().getSelectedItem(), conn);
            studentItems = GetStudentTableValues(classID, conn);
            studentTable.setItems(studentItems);
        });
        
        Button removeStudent = new Button("Remove Student");
        removeStudent.setOnMouseClicked(e -> {
            int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
            removeStudent(classID, studentTable.getSelectionModel().getSelectedItem(), conn);
            studentItems = GetStudentTableValues(classID, conn);
            studentTable.setItems(studentItems);
        });
        
        studentMod.getChildren().addAll(getStudentName, getEmail, getStudentID, saveStudent, removeStudentFromClass, removeStudent);
    }
    
    public void insertUpdateStudent(int classID, Student s, Connection conn)
    {
        String sName = s.getSName();
        String sEmail = s.getSEmail();
        int sID = s.getSID();
        boolean studentExists = false;
        
        String studentStmt = "INSERT INTO Student(sID, sName, sEmail) "+
                "VALUES(?, ?, ?)";
        String studentClassStmt = "INSERT INTO StudentClass(sID, cID) "+
                "VALUES(?, ?)";
        String updateStmt = "UPDATE Student SET sName='" + sName +
                        "' WHERE sID=" + sID;
        String testStmt = "SELECT * FROM Student WHERE sID=" + sID;
        String testStmt2 = "SELECT * FROM StudentClass WHERE sID=" + sID + " and cID=" + classID;

        try
        {
            if(sName.equals("") || sEmail.equals(""))
            {
                throw new NullPointerException("Cannot have empty fields.");
            }
            PreparedStatement pStmt = conn.prepareStatement(testStmt);
            ResultSet rs = pStmt.executeQuery();
            
            if(rs.next() && rs.getString("sName").equals(sName) && rs.getString("sEmail").equals(sEmail) && rs.getInt("sID") == sID)
            {
                studentExists = true;
            }
            pStmt = conn.prepareStatement(testStmt2);
            rs = pStmt.executeQuery();
            
            if(studentExists && rs.next() && rs.getInt("sID") == sID && rs.getInt("cID") == classID)
            {
                pStmt = conn.prepareStatement(updateStmt);
                    
                pStmt.executeUpdate();
            }
            else
            {
                if(!studentExists)
                {
                    pStmt = conn.prepareStatement(studentStmt);
                    pStmt.setInt(1, sID);
                    pStmt.setString(2, sName);
                    pStmt.setString(3, sEmail);
                    pStmt.executeUpdate();
                }
                
                pStmt = conn.prepareStatement(studentClassStmt);
                pStmt.setInt(1, sID);
                pStmt.setInt(2, classID);
                pStmt.executeUpdate();
            }
        }
        catch(SQLException se)
        {
            SettingsInfo.Notification(se.getMessage());
        }
        
        studentItems = GetStudentTableValues(classID, conn);
        studentTable.setItems(studentItems);     
    }
    
    //finished
    public static void removeStudentFromClass(int classID, Student s, Connection conn)
    {
        int sID = s.getSID();
        String assignmentStmt = "DELETE FROM Assignment WHERE cID=" + classID + 
                                            " and sID=" + sID;
        String studentClassStmt = "DELETE FROM StudentClass WHERE cID=" + classID + 
                                            " and sID=" + sID;

        try
        {
            PreparedStatement pStmt = conn.prepareStatement(assignmentStmt);
            pStmt.executeUpdate();
            pStmt = conn.prepareStatement(studentClassStmt);
            pStmt.executeUpdate();
        }
        catch(SQLException se)
        {
            SettingsInfo.Notification(se.getMessage());
        }
    }
    
    public static void removeStudent(int classID, Student s, Connection conn)
    {
        int sID = s.getSID();
        String assignmentStmt = "DELETE FROM Assignment WHERE sID=" + sID;
        String studentClassStmt = "DELETE FROM StudentClass WHERE sID=" + sID;
        String studentStmt = "DELETE FROM Student WHERE sID=" + sID;

        try
        {
            PreparedStatement pStmt = conn.prepareStatement(assignmentStmt);
            pStmt.executeUpdate();
            pStmt = conn.prepareStatement(studentClassStmt);
            pStmt.executeUpdate();
            pStmt = conn.prepareStatement(studentStmt);
            pStmt.executeUpdate();
        }
        catch(SQLException se)
        {
            SettingsInfo.Notification(se.getMessage());
        }
    }
    
    public static ObservableList<Student> GetStudentTableValues(int classID, Connection conn)
    {
        String stmt = "SELECT * FROM Student natural join StudentClass WHERE cID=" + classID;
        ArrayList<Student> sa = new ArrayList();
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            Student s = new Student();
            
            sa.add(s);

            sa.addAll(GetStudentValues(classID, conn));
        }
        catch(SQLException se)
        {
            SettingsInfo.Notification(se.getMessage());
        }
        return FXCollections.observableArrayList(sa);
    }
    
    //finished
    public static ObservableList<Student> GetStudentValues(int classID, Connection conn)
    {
        String stmt = "SELECT * FROM Student natural join StudentClass WHERE cID=" + classID;
        ArrayList<Student> sa = new ArrayList();
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            Student s;

            if(rs.next())
            {
                do
                {
                    s = new Student();
                    s.setSName(new SimpleStringProperty(rs.getString("sName")));
                    s.setSEmail(new SimpleStringProperty(rs.getString("sEmail")));
                    s.setSID(new SimpleIntegerProperty(rs.getInt("sID")));
                    sa.add(s);
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            SettingsInfo.Notification(se.getMessage());
        }
        return FXCollections.observableArrayList(sa);
    }
}
