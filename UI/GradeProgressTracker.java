/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import BackCode.*;
import Database.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author lukecjm
 */
public class GradeProgressTracker extends Application {
    private final String[] classInfoCategories = {"Class Name", "Semester", "Grade", "Professor", "School"};

    //this filepath contains info for startup of the program. This must be in the 
    //  same folder to the .exe file for this program as it is static
    private final String filePathToSettingsInfo = System.getProperty("user.dir") + "/Settings.json";
    private Settings settings;
    private ObservableList<ClassGrade> classGradeItem;
    private Connection conn;
    
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
        
        InitialStartup(primaryStage);
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
    public void InitialStartup(Stage primaryStage)
    {
        File settingsFile = new File(filePathToSettingsInfo);
        String saveLocationDb;
        Database db;
        settings = new Settings();

        if(settingsFile.exists())
        {
            System.out.println("loaded settings file");
            LoadSettingsFile();
            saveLocationDb = settings.getFilePathToDataBaseFiles().toString();
        }
        else
        {
            //setup action on save button click
            DirectoryChooser dirChooser = new DirectoryChooser();
            saveLocationDb = dirChooser.showDialog(primaryStage).getAbsolutePath();
            
            settings.setFilePathToDataBaseFiles(saveLocationDb);
            GetUsernameAndId();
            settings.setUserMode(settings.STUDENT);
            
            if(!settingsFile.exists())
            {
                SaveSettingsFile();
            }
        }
        System.out.println(settings.getFilePathToDataBaseFiles().get());
        
        if(settings.getUserMode() == settings.STUDENT)
        {
            db = new Database("jdbc:derby:GPTStudentTest;create=true", settings.getFilePathToDataBaseFiles().get());
        }
        else
        {
            db = new Database("jdbc:derby:GPTTeacherTest;create=true", settings.getFilePathToDataBaseFiles().get());
        }
        
        conn = db.getConnection();
//
//        if(db.IsEmpty(conn))
//        {
//            db.initializeDatabase(conn, settings.getFilePathToDataBaseFiles().get());
//        }
    }
    
    //finished
    public void GetUsernameAndId()
    {
        Stage secondaryStage = new Stage();
        VBox usernameID = new VBox();
        HBox username = new HBox();
        HBox id = new HBox();
        TextField idField = new TextField();
        TextField usernameField = new TextField();
        Label idError = new Label("Invalid id number.");
        Label usernameError = new Label("Invalid username.");
        Button saveBtn = new Button("Save");

        username.getChildren().addAll(new Label("Enter a username: "), usernameField, usernameError);
        id.getChildren().addAll(new Label("Enter an id: "), idField, idError);

        saveBtn.setOnMouseClicked((Event event) -> {
            if(!usernameField.getText().equals(""))
            {
                settings.setUserName(usernameField.getText());
                usernameError.setVisible(false);
            }
            else
            {
                usernameError.setTextFill(Color.RED);
                usernameError.setVisible(true);
            }

            try
            {
                if(!idField.getText().equals(""))
                {
                    settings.setIDNumber(Integer.parseInt(idField.getText()));
                    idError.setVisible(false);
                }
                else
                {
                    throw new Exception();
                }
            }
            catch(Exception e)
            {
                idError.setTextFill(Color.RED);
                idError.setVisible(true);
            }

            if(!idError.isVisible() && !usernameError.isVisible())
            {
                secondaryStage.close();
            }
        });

        idError.setVisible(false);
        usernameError.setVisible(false);
        usernameID.getChildren().addAll(username, id, saveBtn);

        Scene scene = new Scene(usernameID, 500, 425);
        secondaryStage.setScene(scene);
        secondaryStage.showAndWait();
    }
    
    //finished
    public void LoadSettingsFile()
    {
        JSONParser parser = new JSONParser();
            
        try
        {
            System.out.println(filePathToSettingsInfo);
            Object object = parser.parse(new FileReader(filePathToSettingsInfo));
            JSONObject jsonObj = (JSONObject)object;

            System.out.println("afterparse");
            settings.setFilePathToDataBaseFiles((String)jsonObj.get("DatabaseFilepath"));
            System.out.println(settings.getFilePathToDataBaseFiles().get());
            settings.setIDNumber((long)jsonObj.get("Id"));
            System.out.println(settings.getIDNumber());
            settings.setUserName((String)jsonObj.get("Username"));
            System.out.println(settings.getUserName());
            settings.setUserMode((long)jsonObj.get("UserMode"));
            System.out.println(settings.getUserMode());
        } catch (FileNotFoundException e) {
            System.out.println("The selected file at " + filePathToSettingsInfo + " does not exist.");
        } catch (IOException ex) {
            System.out.println("There was an error reading the file.");
        } catch (ParseException ex) {
            System.out.println("The file data has been corrupted.");
            ex.printStackTrace();
        }
    }
    
    //finished
    public void SaveSettingsFile()
    {
        JSONObject obj = new JSONObject();
            
        obj.put("DatabaseFilepath", settings.getFilePathToDataBaseFiles().get());
        obj.put("Id", settings.getIDNumber().get());
        obj.put("Username", settings.getUserName().get());
        obj.put("UserMode", settings.getUserMode());

        try {
            FileWriter fw = new FileWriter(filePathToSettingsInfo);

            fw.write(obj.toJSONString());
            fw.close();
        } catch (Exception ex) 
        {

        }
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
    public void SetupSearchPane(GridPane searchPane)
    {
        ArrayList<ClassGrade> cgi = new ArrayList();
        cgi.add(new ClassGrade());
        classGradeItem = FXCollections.observableList(cgi);
        VBox searchPlusTable = new VBox();
        TableView<ClassGrade> classGradeTable = new TableView();
        TextField searchField = new TextField();
        
        classGradeTable.setItems(SetupSearchTableResults(searchField.getText()));
        
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

        classGradeTable.getColumns().setAll(classNameCol, semesterCol, gradeCol, professorCol, schoolCol);
        
        searchField.setOnKeyTyped(e ->{
            classGradeTable.setItems(SetupSearchTableResults(searchField.getText()));
        });
        
        //setup search field/button and table
        searchPlusTable.getChildren().addAll(searchField, classGradeTable);
        searchPlusTable.setFillWidth(true);
        searchPane.getChildren().add(searchPlusTable);
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
        if(settings.getFilePathToDataBaseFiles().get() != null)
        {
            fileChooser.setInitialDirectory(new File(settings.getFilePathToDataBaseFiles().get()));
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
