/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import BackCode.*;
import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author lukecjm
 */
public class GradeProgressTracker extends Application {
    private final String[] classInfoCategories = {"Class Name", "Semester", "Grade", "Professor", "School", "Assignment Name"};
    // need to have filepath here and check it before anything else
    
    @Override
    public void start(Stage primaryStage) 
    {    
        StackPane root = new StackPane();
        TabPane mainPane = new TabPane();
        GridPane searchPane = new GridPane();
        GridPane classModificationsPane = new GridPane();
        GridPane calculatePane = new GridPane();
        GridPane settingsPane = new GridPane();
        Tab classModificationsTab = new Tab("Update Classes");
        Tab searchTab = new Tab("Search");
        Tab calculateGradeTab = new Tab("Calculations");
        Tab settingsTab = new Tab("Settings");
        
        //SetupSearchPane
        // Name(of class), semester(date), grade, GPA by semester, Professor, SchoolName
        SetupSearchPane(searchPane);
        
        //SetupClassInputPane
        // name(of class), semester, grade, professor, schoolName
        SetupClassModificationsPane(classModificationsPane);
        //end classInputPane setup
        
        //SetupCalculatePane
        // calculates GPA based on (very similar to search Pane)
        
        classModificationsTab.setContent(classModificationsPane);
        searchTab.setContent(searchPane);
        calculateGradeTab.setContent(calculatePane);
        settingsTab.setContent(settingsPane);
        
        mainPane.getTabs().addAll(classModificationsTab, searchTab, calculateGradeTab, settingsTab);
        mainPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        root.getChildren().add(mainPane);
        
        Scene scene = new Scene(root, 500, 425);
        
        primaryStage.setTitle("Grade Progress Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    //incomplete
    public void InitialStartup()
    {
        //setup action on save button click
        FileChooser fileChooser = new FileChooser();
        //only updates the filepath for the save window if it has been initialized
        if((new Settings()).getFilePathToDataBaseFile() != null)
        {
            fileChooser.setInitialDirectory(new File((new Settings()).getFilePathToDataBaseFile().get()));
        }
        Stage stage = new Stage();
        fileChooser.setTitle("Select Save Location");
        fileChooser.showSaveDialog(stage);
    }
    
    //incomplete
    public void SetFileSavePath()
    {
        
    }
    
    public void SetupSearchPane(GridPane searchPane)
    {
        ArrayList<ClassGrade> cg = new ArrayList();
        cg.add(new ClassGrade());
        TableView<ClassGrade> classGradeTable = new TableView();
        ObservableList<ClassGrade> classGradeItem = FXCollections.observableList(cg);
        classGradeTable.setItems(classGradeItem);
        
        TableColumn<ClassGrade,String> classNameCol = new TableColumn(classInfoCategories[0]);
        classNameCol.setCellValueFactory(new PropertyValueFactory("className"));
        TableColumn<ClassGrade,String> semesterCol = new TableColumn(classInfoCategories[1]);
        semesterCol.setCellValueFactory(new PropertyValueFactory("semester"));
        TableColumn<ClassGrade,String> gradeCol = new TableColumn(classInfoCategories[2]);
        gradeCol.setCellValueFactory(new PropertyValueFactory("grade"));
        TableColumn<ClassGrade,String> professorCol = new TableColumn(classInfoCategories[3]);
        professorCol.setCellValueFactory(new PropertyValueFactory("professorName"));
        TableColumn<ClassGrade,String> schoolCol = new TableColumn(classInfoCategories[4]);
        schoolCol.setCellValueFactory(new PropertyValueFactory("schoolName"));
        //need completed assignment class before testing this, 
        //  don't forgot to modify the line below to add this to the table
//        TableColumn<ClassGrade,String> assignmentCol = new TableColumn(classInfoCategories[3]);
//        assignmentCol.setCellValueFactory(new PropertyValueFactory("assignmentName"));

        classGradeTable.getColumns().setAll(classNameCol, semesterCol, gradeCol, professorCol, schoolCol);
        
        searchPane.getChildren().add(classGradeTable);
    }
    public void SetupClassModificationsPane(GridPane classModificationsPane)
    {
        HBox getClassName = new HBox();
        TextField nameField = new TextField();
        Label promptName = new Label(classInfoCategories[0] + ":");
        getClassName.getChildren().addAll(promptName, nameField);
        GridPane.setConstraints(getClassName, 0, 0);
        
        HBox getSemesterName = new HBox();
        TextField semesterField = new TextField();
        Label promptSemester = new Label(classInfoCategories[1] + ":");
        getSemesterName.getChildren().addAll(promptSemester, semesterField);
        GridPane.setConstraints(getSemesterName, 0, 1);
        
        HBox getGrade = new HBox();
        TextField gradeField = new TextField();
        Label promptGrade = new Label(classInfoCategories[2] + ":");
        getGrade.getChildren().addAll(promptGrade, gradeField);
        GridPane.setConstraints(getGrade, 0, 2);
        
        HBox getProfessorName = new HBox();
        TextField professorNameField = new TextField();
        Label promptProfessorName = new Label(classInfoCategories[3] + ":");
        getProfessorName.getChildren().addAll(promptProfessorName, professorNameField);
        GridPane.setConstraints(getProfessorName, 1, 0);
        
        HBox getSchoolName = new HBox();
        TextField schoolNameField = new TextField();
        Label promptSchoolName = new Label(classInfoCategories[4] + ":");
        getSchoolName.getChildren().addAll(promptSchoolName, schoolNameField);
        GridPane.setConstraints(getSchoolName, 1, 1);
        
        Button saveClass = new Button("Save");
        
        //setup action on save button click
        FileChooser fileChooser = new FileChooser();
        //only updates the filepath for the save window if it has been initialized
        if((new Settings()).getFilePathToDataBaseFile() != null)
        {
            fileChooser.setInitialDirectory(new File((new Settings()).getFilePathToDataBaseFile().get()));
        }
        saveClass.setOnMouseClicked(e -> 
        {
            Stage stage = new Stage();
            fileChooser.showSaveDialog(stage);
        });
        GridPane.setConstraints(saveClass, 1, 2);
        
        classModificationsPane.getChildren().addAll(getClassName, getSemesterName, getGrade, getProfessorName, getSchoolName, saveClass);
    }
}
