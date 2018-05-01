/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Assignment;
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
    
    //sets up Assignment table
    public static TableView<Assignment> SetupAssignmentTable(int classID)
    {
        TableView<Assignment> assignmentTable = new TableView<>();
        
        TableColumn<Assignment,String> aNameCol = new TableColumn(ASSIGNMENT_INFO_CATEGORIES[0]);
        aNameCol.setCellValueFactory(new PropertyValueFactory("assignmentName"));
        TableColumn<Assignment,String> aCategoryCol = new TableColumn(ASSIGNMENT_INFO_CATEGORIES[1]);
        aCategoryCol.setCellValueFactory(new PropertyValueFactory("assignmentCategory"));
        TableColumn<Assignment,String> aScoreCol = new TableColumn(ASSIGNMENT_INFO_CATEGORIES[2]);
        aScoreCol.setCellValueFactory(new PropertyValueFactory("assignmentGrade"));

        assignmentTable.getColumns().setAll(aNameCol, aCategoryCol, aScoreCol);
        return assignmentTable;
    }
    
    public static void SetupAssignmentMod(VBox assignmentMod, String[] assignmentInfo)
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
            //need to execute a update statement in the database
        });
        
        assignmentMod.getChildren().addAll(getAssignmentName, getCategory, getScore, getMaxScore, saveAssignment);
    }
}
