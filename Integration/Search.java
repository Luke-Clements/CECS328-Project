/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Assignment;
import BackCode.Calculations;
import BackCode.ClassGrade;
import BackCode.GradingScale;
import BackCode.Settings;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 *
 * @author lukecjm
 */
public class Search 
{
    private ObservableList<ClassGrade> classGradeItem;
    private TableView<ClassGrade> classGradeTable = new TableView<ClassGrade>();
    private Label maxClassScore = new Label();
    private VBox searchPlusTable = new VBox();
    private TextField searchField = new TextField();
    private Label gpa;
    
    public TableView<ClassGrade> getSearchTable()
    {
        return classGradeTable;
    }
    
    //finished
    public ObservableList<ClassGrade> SetupSearchTableResults(String searchItem)
    {
        ArrayList<ClassGrade> tableResults = new ArrayList();
        
        if(searchItem == null || searchItem.equals(""))
        {
            return classGradeItem;
        }
        else if(classGradeItem != null)
        {
            for(int i = 0;i < classGradeItem.size();i++)
            {
                //may want to add String.toUpper() to make this more universal
                if(classGradeItem.get(i).getClassName().contains(searchItem) ||
                   classGradeItem.get(i).getGrade().contains(searchItem) ||
                   classGradeItem.get(i).getProfessorName().contains(searchItem) ||
                   classGradeItem.get(i).getSchoolName().contains(searchItem) ||
                   classGradeItem.get(i).getSemester().contains(searchItem))
                {
                    tableResults.add(classGradeItem.get(i));
                }
            }
            return FXCollections.observableList(tableResults);
        }
        return null;
    }
    
    public void SetupGradeInfo(Settings settings, StudentMod sm, Connection conn, Search search, VBox gradeInfo)
    {
        gradeInfo.getChildren().clear();
        int classID;
        int gsID;
        ObservableList<Assignment> assignments = null;
        HashMap<String, Integer> categoryWeights = null;
        if(classGradeTable.getSelectionModel().getSelectedItem() != null)
        {
            classID = classGradeTable.getSelectionModel().getSelectedItem().getClassID();
            gsID = CategoryWeightMod.getGradingScaleID(conn, classID);
            assignments = AssignmentMod.GetAssignmentValues(settings, sm, classID, conn);
            categoryWeights = CategoryWeightMod.GetCategoryWeightValues(gsID, conn);
        }
            
        gpa = new Label("The GPA of all items in the class table is " + Calculations.calculateGPA(classGradeItem));
        if(assignments != null)
        {
            maxClassScore.setText("Maximum Class Score: " + Calculations.calculateMaxClassScore(categoryWeights, assignments));
        }
        else
        {
            maxClassScore.setText("");
        }
        
        gradeInfo.getChildren().clear();
        gradeInfo.getChildren().addAll(maxClassScore, gpa);
    }
    public void SetupSearchTable(Settings settings, StudentMod sm, Connection conn, VBox searchTable,VBox gradeInfo)
    {
        searchTable.getChildren().clear();
        classGradeItem = getClassGradeItems(settings, sm, conn);
        
        classGradeTable.setItems(SetupSearchTableResults(searchField.getText()));
        
        TableColumn<ClassGrade,String> classNameCol = new TableColumn(ClassMod.CLASS_GRADE_INFO_CATEGORIES[0]);
        classNameCol.setCellValueFactory(new PropertyValueFactory("className"));
        TableColumn<ClassGrade,String> semesterCol = new TableColumn(ClassMod.CLASS_GRADE_INFO_CATEGORIES[1]);
        semesterCol.setCellValueFactory(new PropertyValueFactory("semester"));
        TableColumn<ClassGrade,String> gradeCol = new TableColumn(ClassMod.CLASS_GRADE_INFO_CATEGORIES[2]);
        gradeCol.setCellValueFactory(new PropertyValueFactory("grade"));
        TableColumn<ClassGrade,String> professorCol = new TableColumn(ClassMod.CLASS_GRADE_INFO_CATEGORIES[3]);
        professorCol.setCellValueFactory(new PropertyValueFactory("professorName"));
        TableColumn<ClassGrade,String> schoolCol = new TableColumn(ClassMod.CLASS_GRADE_INFO_CATEGORIES[4]);
        schoolCol.setCellValueFactory(new PropertyValueFactory("schoolName"));

        searchField.setOnKeyTyped(e ->{
            classGradeTable.setItems(SetupSearchTableResults(searchField.getText()));
            SetupGradeInfo(settings, sm, conn, this, gradeInfo);
        });
                
        classGradeTable.getSelectionModel().selectedIndexProperty().addListener(e -> {
            SetupGradeInfo(settings, sm, conn, this, gradeInfo);
        });
        
        classGradeTable.getColumns().setAll(classNameCol, semesterCol, gradeCol, professorCol, schoolCol);
        
        searchTable.getChildren().addAll(searchField, classGradeTable);
    }
    
    public static ArrayList<String> getGrades(Search search)
    {
        ArrayList<String> grades = new ArrayList();
        for(ClassGrade cg:search.getSearchTable().getItems())
        {
            grades.add(cg.getGrade());
        }
        return grades;
    }
    
    public static ObservableList<ClassGrade> getClassGradeItems(Settings settings, StudentMod sm, Connection conn)
    {
        ObservableList<BackCode.Class> classes = ClassMod.GetClassTableValues(conn);
        int gsID;
        int classID;
        ObservableList<Assignment> assignments;
        HashMap<String, Integer> categoryWeights;
        ArrayList<ClassGrade> classGradeItems = new ArrayList();
        ClassGrade cg;
        GradingScale gs;
        
        for(BackCode.Class c: classes)
        {
            cg = new ClassGrade();
            classID = c.getCID();
            gsID = CategoryWeightMod.getGradingScaleID(conn, classID);
            gs = GradingScaleMod.getGradingScale(conn, gsID);
            assignments = AssignmentMod.GetAssignmentValues(settings, sm, classID, conn);
            categoryWeights = CategoryWeightMod.GetCategoryWeightValues(classID, conn);
            float currentGrade = Calculations.calculateCurrentClassScore(categoryWeights, assignments);
            
            cg.setClassID(new SimpleIntegerProperty(classID));
            cg.setName(new SimpleStringProperty(c.getClassName()));
            cg.setProfName(new SimpleStringProperty(c.getClassTeacherName()));
            cg.setSchoolName(new SimpleStringProperty(c.getClassSchool()));
            cg.setSemester(new SimpleStringProperty(c.getClassSemester()));
            cg.setGrade(new SimpleStringProperty(Calculations.getFinalLetterGrade(currentGrade, gs)));
            classGradeItems.add(cg);
        }
        return FXCollections.observableArrayList(classGradeItems);
    }
}
