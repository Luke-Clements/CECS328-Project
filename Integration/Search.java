/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.ClassGrade;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author lukecjm
 */
public class Search 
{
    private ObservableList<ClassGrade> classGradeItem;
    
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
    
    public void SetupSearchPane(GridPane searchPane)
    {
        ArrayList<ClassGrade> cgi = new ArrayList();
        //implement function to return an arraylist of classGrade items
        cgi.add(new ClassGrade());
        classGradeItem = FXCollections.observableList(cgi);
        VBox searchPlusTable = new VBox();
        TableView<ClassGrade> classGradeTable = new TableView();
        TextField searchField = new TextField();
        
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

        classGradeTable.getColumns().setAll(classNameCol, semesterCol, gradeCol, professorCol, schoolCol);
        
        searchField.setOnKeyTyped(e ->{
            classGradeTable.setItems(SetupSearchTableResults(searchField.getText()));
        });
        
        //setup search field/button and table
        searchPlusTable.getChildren().addAll(searchField, classGradeTable);
        searchPlusTable.setFillWidth(true);
        searchPane.getChildren().add(searchPlusTable);
    }
    
}
