/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Settings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Map;
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
public class ClassMod 
{
    public static final String[] CLASS_GRADE_INFO_CATEGORIES = {"Class Name", "Semester", "Grade", "Professor", "School", "Time"};
    private static final String[] CLASS_INFO_CATEGORIES = {"Class ID", "Class Name", "Semester", "Professor", "School", "Time"};
    public static final String[] CLASS_INFO_EMPTY = {"", "", "", "", "", ""}; //empty strings to replace anything in the textFields when a new class is selected
    private ObservableList<BackCode.Class> classItems;
    private TableView<BackCode.Class> classTable;
    
    public TableView<BackCode.Class> getClassTable()
    {
        return classTable;
    }
    //finished
    public void SetupClassTable(Settings settings, Connection conn, VBox classTableSearch, VBox classMod, VBox assignmentMod, VBox gradingScaleModBox, VBox categoryWeightMod, TableView<Map.Entry<String, Integer>> categoryWeightTable)
    {
        classTable = new TableView<>();
        TextField search = new TextField();
        classItems = GetClassTableValues(conn);
        
        classTable.setItems(classItems);
        
        classTable.setItems(SetupClassTableResults(search.getText()));
        
        TableColumn<BackCode.Class,Integer> classIDCol = new TableColumn(CLASS_INFO_CATEGORIES[0]);
        classIDCol.setCellValueFactory(new PropertyValueFactory("cID"));
        TableColumn<BackCode.Class,String> classNameCol = new TableColumn(CLASS_INFO_CATEGORIES[1]);
        classNameCol.setCellValueFactory(new PropertyValueFactory("className"));
        TableColumn<BackCode.Class,String> semesterCol = new TableColumn(CLASS_INFO_CATEGORIES[2]);
        semesterCol.setCellValueFactory(new PropertyValueFactory("classSemester"));
        TableColumn<BackCode.Class,String> professorCol = new TableColumn(CLASS_INFO_CATEGORIES[3]);
        professorCol.setCellValueFactory(new PropertyValueFactory("classTeacherName"));
        TableColumn<BackCode.Class,String> schoolCol = new TableColumn(CLASS_INFO_CATEGORIES[4]);
        schoolCol.setCellValueFactory(new PropertyValueFactory("classSchool"));
        TableColumn<BackCode.Class,Long> timeCol = new TableColumn(CLASS_INFO_CATEGORIES[5]);
        timeCol.setCellValueFactory(new PropertyValueFactory("classTime"));
        
        classTable.getColumns().setAll(classIDCol, classNameCol, semesterCol, professorCol, schoolCol, timeCol);
        
        classTable.getSelectionModel().selectedIndexProperty().addListener(e -> {
            BackCode.Class c = classTable.getSelectionModel().getSelectedItem();
            String[] classInfo = c.getClassInfoArray(); //ignore errors from this line, as they do not affect the program
            AssignmentMod.SetupAssignmentTable(assignmentMod, c.getCID());
            SetupClassMod(settings, search, classMod, classInfo, conn);
            BackCode.GradingScale gs = new BackCode.GradingScale();
            GradingScaleMod.SetupGradingScaleMod(gradingScaleModBox, gs.getGradingScaleInfoArray());
            BackCode.CategoryWeight cw = new BackCode.CategoryWeight();
            categoryWeightTable.setItems(FXCollections.observableArrayList(cw.getCategoryWeight().entrySet()));
        });
        
        search.setOnKeyTyped(e ->{
            classTable.setItems(SetupClassTableResults(search.getText()));
            AssignmentMod.SetupAssignmentTable(assignmentMod, -1);
            SetupClassMod(settings, search, classMod, CLASS_INFO_EMPTY, conn);
            AssignmentMod.SetupAssignmentMod(assignmentMod, AssignmentMod.ASSIGNMENT_INFO_EMPTY);
            GradingScaleMod.SetupGradingScaleMod(gradingScaleModBox, GradingScaleMod.GRADING_SCALE_INFO_EMPTY);
            CategoryWeightMod.SetupCategoryWeightMod(categoryWeightTable, categoryWeightMod, CategoryWeightMod.CATEGORYWEIGHT_INFO_EMPTY);
        });

        classTableSearch.getChildren().addAll(search, classTable);
    }
    //finished
    public void SetupClassMod(Settings settings, TextField searchField, VBox classMod, String[] classInfo, Connection conn)
    {
        classMod.getChildren().clear();
        HBox getClassName = new HBox();
        TextField nameField = new TextField();
        Label promptName = new Label(CLASS_GRADE_INFO_CATEGORIES[0] + ":");
        nameField.setText(classInfo[1]);
        getClassName.getChildren().addAll(promptName, nameField);
        
        HBox getSemesterName = new HBox();
        TextField semesterField = new TextField();
        Label promptSemester = new Label(CLASS_GRADE_INFO_CATEGORIES[1] + ":");
        semesterField.setText(classInfo[2]);
        getSemesterName.getChildren().addAll(promptSemester, semesterField);
        
        HBox getProfessorName = new HBox();
        TextField professorNameField = new TextField();
        Label promptProfessorName = new Label(CLASS_GRADE_INFO_CATEGORIES[3] + ":");
        professorNameField.setText(classInfo[3]);
        getProfessorName.getChildren().addAll(promptProfessorName, professorNameField);
        
        HBox getSchoolName = new HBox();
        TextField schoolNameField = new TextField();
        Label promptSchoolName = new Label(CLASS_GRADE_INFO_CATEGORIES[4] + ":");
        schoolNameField.setText(classInfo[4]);
        getSchoolName.getChildren().addAll(promptSchoolName, schoolNameField);
        
        HBox getTime = new HBox();
        TextField timeField = new TextField();
        Label promptClassTime = new Label(CLASS_GRADE_INFO_CATEGORIES[5] + ":");
        timeField.setText(classInfo[5]);
        getTime.getChildren().addAll(promptClassTime, timeField);
        
        Button saveClass = new Button("Save Class Changes");

        saveClass.setOnMouseClicked(e -> 
        {
            String Stmt = "INSERT INTO Class(cID, cName, teacherName, semester, schoolName, cTime, gsID) "+
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
           
            String className = nameField.getText();
            String classSemester = semesterField.getText();
            String classSchool = schoolNameField.getText();
            String classTeacherName = professorNameField.getText();

            try
            {
                Time classTime = new Time(Long.parseLong(timeField.getText()));
                if(className.equals("") || classSemester.equals("") || classSchool.equals("") || classTime.toString().equals(""))
                {
                    throw new NullPointerException("Cannot have empty fields.");
                }
                PreparedStatement pStmt = conn.prepareStatement(Stmt);
                pStmt.setInt(1, settings.getClassCounter());
                pStmt.setString(2,className);
                pStmt.setString(3,classTeacherName);
                pStmt.setString(4,classSemester);
                pStmt.setString(5,classSchool);
                pStmt.setTime(6, classTime);
                pStmt.setInt(7, Integer.parseInt(GradingScaleMod.getGradingScaleID(settings, conn)));

                pStmt.executeUpdate();
                settings.incrementClassCounter(); //increments class counter IF the class was added to the database
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
            classItems = GetClassTableValues(conn);
            classTable.setItems(classItems);
            searchField.setText("");
            
        });
        
        classMod.getChildren().addAll(getClassName, getSemesterName, getProfessorName, getSchoolName, getTime, saveClass);
    }
    //finished
    public ObservableList<BackCode.Class> GetClassTableValues(Connection conn)
    {
        String stmt = "SELECT * FROM Class";
        ArrayList<BackCode.Class> ac = new ArrayList();
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            BackCode.Class c = new BackCode.Class();
            
            ac.add(c);

            if(rs.next())
            {
                do
                {
                    c = new BackCode.Class();
                    c.setID(new SimpleIntegerProperty(rs.getInt("cID")));
                    c.setName(new SimpleStringProperty(rs.getString("cName")));
                    c.setTeacher(new SimpleStringProperty(rs.getString("teacherName")));
                    c.setSemester(new SimpleStringProperty(rs.getString("semester")));
                    c.setSchool(new SimpleStringProperty(rs.getString("schoolName")));
                    c.setTime(new SimpleLongProperty(rs.getTime("cTime").getTime()));
                    ac.add(c);
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            
        }
        return FXCollections.observableArrayList(ac);
    }
    //finished
    public ObservableList<BackCode.Class> SetupClassTableResults(String searchItem)
    {
        ArrayList<BackCode.Class> tableResults = new ArrayList();
        
        //Will add one empty entry to the table for adding a new class
        if(searchItem == null || searchItem.equals(""))
        {
            return classItems;
        }
        else if(classItems != null)
        {
            for(int i = 0;i < classItems.size();i++)
            {
                //may want to add String.toUpper() to make this more universal
                if(classItems.get(i).getClassName().contains(searchItem) ||
                   (classItems.get(i).getCID() + "").contains(searchItem) ||
                    classItems.get(i).getClassSemester().contains(searchItem) ||
                    classItems.get(i).getClassSchool().contains(searchItem) ||
                    classItems.get(i).getClassTeacherName().contains(searchItem) ||
                   (classItems.get(i).getClassTime() + "").contains(searchItem))
                {
                    tableResults.add(classItems.get(i));
                }
            }
            return FXCollections.observableList(tableResults);
        }
        return null;
    }
}
