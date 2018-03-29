/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg343project;

import java.util.ArrayList;
import java.util.Vector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        TabPane mainPane = new TabPane();
        GridPane searchPane = new GridPane();
        GridPane classPane = new GridPane();
        GridPane calculateTab = new GridPane();
        GridPane saveLoadTab = new GridPane();
        Tab classInputTab = new Tab("Class Input");
        Tab searchTab = new Tab("Search");
        Tab calculateGradeTab = new Tab("Calculator");
        Tab sLTab = new Tab("Save/Load");
        
        
        System.out.println((new ClassGrade()).getClassName().toString());
        //SetupSearchPane
        // Name(of class), semester(date), grade, GPA by semester, Professor, SchoolName
        ArrayList<ClassGrade> cg = new ArrayList();
        cg.add(new ClassGrade());
        TableView<ClassGrade> classGradeTable = new TableView();
        ObservableList<ClassGrade> classGradeItem = FXCollections.observableList(cg);
        classGradeTable.setItems(classGradeItem);
        
        TableColumn<ClassGrade,String> firstNameCol = new TableColumn("Class Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("className"));
        TableColumn<ClassGrade,String> lastNameCol = new TableColumn("Professor Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory("professorName"));

        classGradeTable.getColumns().setAll(firstNameCol, lastNameCol);
        
        searchPane.getChildren().add(classGradeTable);
        
        //SetupClassInputPane
        // name(of class), semester, grade, professor, schoolName
        HBox getClassName = new HBox();
        TextField nameField = new TextField();
        Label promptName = new Label("Class Name:");
        getClassName.getChildren().addAll(promptName, nameField);
        GridPane.setConstraints(getClassName, 0, 0);
        
        HBox getSemesterName = new HBox();
        TextField semesterField = new TextField();
        Label promptSemester = new Label("Semester:");
        getSemesterName.getChildren().addAll(promptSemester, semesterField);
        GridPane.setConstraints(getSemesterName, 0, 1);
        
        HBox getGrade = new HBox();
        TextField gradeField = new TextField();
        Label promptGrade = new Label("Grade:");
        getGrade.getChildren().addAll(promptGrade, gradeField);
        GridPane.setConstraints(getGrade, 0, 2);
        
        HBox getProfessorName = new HBox();
        TextField professorNameField = new TextField();
        Label promptProfessorName = new Label("Professor:");
        getProfessorName.getChildren().addAll(promptProfessorName, professorNameField);
        GridPane.setConstraints(getProfessorName, 1, 0);
        
        HBox getSchoolName = new HBox();
        TextField schoolNameField = new TextField();
        Label promptSchoolName = new Label("School:");
        getSchoolName.getChildren().addAll(promptSchoolName, schoolNameField);
        GridPane.setConstraints(getSchoolName, 1, 1);
        
        Button saveClass = new Button("Save");
        
        //setup action on save button click
        FileChooser fileChooser = new FileChooser();
        saveClass.setOnMouseClicked(e -> 
        {
            Stage stage = new Stage();
            fileChooser.showSaveDialog(stage);
        });
        GridPane.setConstraints(saveClass, 1, 2);
        
        classPane.getChildren().addAll(getClassName, getSemesterName, getGrade, getProfessorName, getSchoolName, saveClass);
        //end classInputPane setup
        
        //SetupCalculatePane
        // calculates GPA based on (very similar to search Pane)
        
        classInputTab.setContent(classPane);
        searchTab.setContent(searchPane);
        calculateGradeTab.setContent(calculateTab);
        sLTab.setContent(saveLoadTab);
        
        mainPane.getTabs().addAll(classInputTab, searchTab, calculateGradeTab);
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
    
}
