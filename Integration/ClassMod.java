/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author lukecjm
 */
public class ClassMod 
{
    public static final String[] CLASS_GRADE_INFO_CATEGORIES = {"Class Name", "Semester", "Grade", "Professor", "School"};
    private static final String[] CLASS_INFO_CATEGORIES = {"Class ID", "Class Name", "Semester", "Grade"};
    public static final String[] CLASS_INFO_EMPTY = {"", "", "", "", ""}; //empty strings to replace anything in the textFields when a new class is selected
    private ObservableList<BackCode.Class> classItems;
    private TableView<BackCode.Class> classTable;
    
    public TableView<BackCode.Class> getClassTable()
    {
        return classTable;
    }
    
    public VBox SetupClassTable(VBox classMod, VBox assignmentMod, VBox gradingScaleMod, VBox categoryWeightMod)
    {
        VBox classTableSearch = new VBox();
        classTable = new TableView<>();
        ArrayList<BackCode.Class> ci = new ArrayList();
        //implement function to return an arraylist of class items
        ci.add(new BackCode.Class());
        classItems = FXCollections.observableList(ci);
        
        TableColumn<BackCode.Class,String> classIDCol = new TableColumn(CLASS_INFO_CATEGORIES[0]);
        classIDCol.setCellValueFactory(new PropertyValueFactory("cID"));
        TableColumn<BackCode.Class,String> classNameCol = new TableColumn(CLASS_INFO_CATEGORIES[1]);
        classNameCol.setCellValueFactory(new PropertyValueFactory("className"));
        TableColumn<BackCode.Class,String> semesterCol = new TableColumn(CLASS_INFO_CATEGORIES[2]);
        semesterCol.setCellValueFactory(new PropertyValueFactory("classSemester"));
        
        TextField search = new TextField();
        search.setOnKeyTyped(e ->{
            classTable.setItems(SetupClassTableResults(search.getText()));
            AssignmentMod.SetupAssignmentTable(0);
            SetupClassMod(classMod, CLASS_INFO_EMPTY);
            AssignmentMod.SetupAssignmentMod(assignmentMod, AssignmentMod.ASSIGNMENT_INFO_EMPTY);
            GradingScaleMod.SetupGradingScaleMod(gradingScaleMod, GradingScaleMod.GRADING_SCALE_INFO_EMPTY);
            CategoryWeightMod.SetupCategoryWeightMod(categoryWeightMod, CategoryWeightMod.CATEGORYWEIGHT_INFO_EMPTY);
        });

        classTable.getColumns().setAll(classIDCol, classNameCol, semesterCol);
        classTableSearch.getChildren().addAll(search, classTable);
        return classTableSearch;
    }
    public void SetupClassMod(VBox classMod, String[] classInfo)
    {
        classMod.getChildren().clear();
        HBox getClassName = new HBox();
        TextField nameField = new TextField();
        Label promptName = new Label(CLASS_GRADE_INFO_CATEGORIES[0] + ":");
        nameField.setText(classInfo[0]);
        getClassName.getChildren().addAll(promptName, nameField);
        
        HBox getSemesterName = new HBox();
        TextField semesterField = new TextField();
        Label promptSemester = new Label(CLASS_GRADE_INFO_CATEGORIES[1] + ":");
        semesterField.setText(classInfo[1]);
        getSemesterName.getChildren().addAll(promptSemester, semesterField);
        
        HBox getGrade = new HBox();
        TextField gradeField = new TextField();
        Label promptGrade = new Label(CLASS_GRADE_INFO_CATEGORIES[2] + ":");
        getGrade.getChildren().addAll(promptGrade, gradeField);
        
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
        GridPane.setConstraints(getSchoolName, 0, 4);
        
        Button saveClass = new Button("Save Class Changes");

        saveClass.setOnMouseClicked(e -> 
        {
            //need to execute a update statement in the database
        });
        
        classMod.getChildren().addAll(getClassName, getSemesterName, getGrade, getProfessorName, getSchoolName, saveClass);
    }
    
    //finished
    public ObservableList<BackCode.Class> SetupClassTableResults(String searchItem)
    {
        ArrayList<BackCode.Class> tableResults = new ArrayList();
        
        if(searchItem == null || searchItem.equals(""))
        {
            return classItems;
        }
        else if(classItems != null)
        {
            for(int i = 0;i < classItems.size();i++)
            {
                //may want to add String.toUpper() to make this more universal
                if(classItems.get(i).getName().get().contains(searchItem) ||
                   (classItems.get(i).getID().get() + "").contains(searchItem) ||
                   classItems.get(i).getSemester().get().contains(searchItem))
                {
                    tableResults.add(classItems.get(i));
                }
            }
            return FXCollections.observableList(tableResults);
        }
        return null;
    }
}
