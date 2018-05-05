/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import BackCode.*;
import Database.*;
import Integration.AssignmentMod;
import Integration.CategoryWeightMod;
import Integration.ClassMod;
import Integration.GradingScaleMod;
import Integration.Search;
import Integration.SettingsInfo;
import Integration.StudentMod;
import java.io.File;
import java.sql.Connection;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


/**
 *
 * @author lukecjm
 */
public class GradeProgressTracker extends Application 
{
    private String[] classInfo = {"", "", "", "", "", ""}; //placeholder for loaded values in the SetupClassModPane function
    private String[] assignmentInfo = {"", "", "", ""};
    private String[] gradingScaleInfo = {"", "", "", "", "", ""};
    private ClassMod classMod;
    private AssignmentMod assignmentMod;
    private CategoryWeightMod categoryWeightMod;
    private StudentMod studentMod;
    private Search search;

    //this filepath contains info for startup of the program. This must be in the 
    //  same folder to the .exe file for this program as it is static
    private final String filePathToSettingsInfo = System.getProperty("user.dir") + "/Settings.json";
    private Settings settings;
    private LongProperty userMode;
    private ObservableList<ClassGrade> classGradeItem;
    private ObservableList<BackCode.Class> classItems;
    private ObservableList<Assignment> assignmentItems;
    private ObservableList<Map.Entry<String, Integer>> categoryWeightItems;
    private Connection conn;
    private Database db;
    
    @Override
    public void start(Stage primaryStage) 
    {    
        TabPane mainPane = new TabPane();
        HBox searchPane = new HBox();
        HBox classAssignmentModificationsPane = new HBox();
        HBox settingsModTab = new HBox();
        
        VBox searchGradeInfo = new VBox();
        VBox searchTable = new VBox();
        
        Tab classModificationsTab = new Tab("Update Classes");
        Tab searchTab = new Tab("Search");
        Tab settingsTab = new Tab("Settings");
        
        InitialStartup(primaryStage);
        
        userMode = settings.getUserMode();
        
        //Setup settings tab
        SettingsInfo.SetupSettingsMod(userMode, settingsModTab, settings, db, conn, filePathToSettingsInfo);
        settingsTab.setContent(settingsModTab);
        
        userMode.addListener(e -> {
            SetupTabs(settings, mainPane, settingsTab, classModificationsTab, searchTab, searchPane, searchTable, searchGradeInfo, classAssignmentModificationsPane);
        });
        //setting up the change between modes
        
        SetupTabs(settings, mainPane, settingsTab, classModificationsTab, searchTab, searchPane, searchTable, searchGradeInfo, classAssignmentModificationsPane);
        Scene scene = new Scene(mainPane, 1250, 425);
        
        primaryStage.sizeToScene();
        primaryStage.setTitle("Grade Progress Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> {
            SettingsInfo.SaveSettingsFile(settings, filePathToSettingsInfo);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void SetupTabs(Settings settings, TabPane mainPane, Tab settingsTab, Tab classModificationsTab, Tab searchTab, 
            HBox searchPane, VBox searchTable, VBox searchGradeInfo, HBox classAssignmentModificationsPane)
    {
        mainPane.getTabs().clear();
        classModificationsTab.setContent(null);
        searchTab.setContent(null);
        searchPane.getChildren().clear();
        searchTable.getChildren().clear();
        searchGradeInfo.getChildren().clear();
        classAssignmentModificationsPane.getChildren().clear();
        
        //resets the connection so it is to the correct database
        conn = SettingsInfo.SetSelectedDatabaseAndGetConnection(conn, settings, db);
        
        search = new Search();
        classMod = new ClassMod();
        assignmentMod = new AssignmentMod();
        categoryWeightMod = new CategoryWeightMod();
        studentMod = new StudentMod();
        
        //Setup class, assignment, category weight, grading scale modifications tab
        // name(of class), semester, grade, professor, schoolName
        SetupClassAssignmentModificationsPane(classAssignmentModificationsPane);
        //end classInputPane setup
        
        //Setup search tab
        // Name(of class), semester(date), grade, GPA by semester, Professor, SchoolName
        search.SetupSearchTable(settings, studentMod, conn, searchTable, searchGradeInfo);
        search.SetupGradeInfo(settings, studentMod, conn, search, searchGradeInfo);
        
        classModificationsTab.setContent(classAssignmentModificationsPane);
        searchTab.setContent(searchPane);
        
        searchPane.getChildren().addAll(searchTable, searchGradeInfo);
        mainPane.getTabs().addAll(classModificationsTab, searchTab, settingsTab);
        mainPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        mainPane.selectionModelProperty().getValue().selectedItemProperty().addListener(e -> {
            search.SetupSearchTable(settings, studentMod, conn, searchTable, searchGradeInfo);
        });
    }
    
    //complete
    public void InitialStartup(Stage primaryStage)
    {
        File settingsFile = new File(filePathToSettingsInfo);
        String saveLocationDb;
        settings = new Settings();

        if(settingsFile.exists())
        {
            SettingsInfo.LoadSettingsFile(settings, filePathToSettingsInfo);
        }
        else
        {
            //setup action on save button click
            DirectoryChooser dirChooser = new DirectoryChooser();
            saveLocationDb = dirChooser.showDialog(primaryStage).getAbsolutePath();
            
            settings.setFilePathToDataBaseFiles(saveLocationDb);
            SettingsInfo.GetUsernameAndId(settings);
            settings.setUserMode(settings.STUDENT);
            
            if(!settingsFile.exists())
            {
                SettingsInfo.SaveSettingsFile(settings, filePathToSettingsInfo);
            }
        }
    }
   
    public void SetupClassAssignmentModificationsPane(HBox classAssignmentModificationsPane)
    {
        VBox classTableSearch = new VBox();
        VBox classModBox = new VBox();
        VBox assignmentModBox = new VBox();
        VBox allModBox = new VBox();
        VBox gradingScaleModBox = new VBox();
        VBox categoryWeightModBox = new VBox();
        VBox studentModBox = new VBox();
        ScrollPane modPane = new ScrollPane();
        
        classMod.SetupClassMod(settings, null, classModBox, classInfo, conn);
        classMod.SetupClassTableResults(null);

        TableView<Map.Entry<String, Integer>> categoryWeightTable;
        TableView<Assignment> assignmentTable;
        TableView<Student> studentTable = null;

        if(userMode.get() == Settings.TEACHER)
        {
            studentTable = studentMod.SetupStudentTable(settings, conn, studentModBox, assignmentModBox, classMod, assignmentMod);
        }
        assignmentTable = assignmentMod.SetupAssignmentTable(studentMod, settings, conn, assignmentModBox, classMod);
        categoryWeightTable = categoryWeightMod.SetupCategoryWeightTable(conn, classMod, categoryWeightModBox, 0);

        classMod.SetupClassTable(studentMod, categoryWeightMod, assignmentMod, settings, conn, classTableSearch, classModBox, assignmentModBox, gradingScaleModBox, studentModBox, categoryWeightModBox, categoryWeightTable);
        
        //sets up modifying the class values
        classMod.SetupClassMod(settings, null, classModBox, classInfo, conn);
        assignmentMod.SetupAssignmentMod(studentMod, settings, classMod, conn, assignmentModBox, assignmentInfo);
        GradingScaleMod.SetupGradingScaleMod(gradingScaleModBox, GradingScaleMod.GRADING_SCALE_INFO_EMPTY);
        categoryWeightMod.SetupCategoryWeightMod(classMod, conn, categoryWeightModBox, CategoryWeightMod.CATEGORYWEIGHT_INFO_EMPTY);
        studentMod.SetupStudentMod(classMod, conn, studentModBox, StudentMod.STUDENT_INFO_EMPTY);
        
        if(userMode.get() == Settings.TEACHER)
        {
            allModBox.getChildren().addAll(classModBox, gradingScaleModBox, categoryWeightModBox, studentModBox, assignmentModBox);
        }
        else
        {
            allModBox.getChildren().addAll(classModBox, gradingScaleModBox, categoryWeightModBox, assignmentModBox);

        }
        modPane.setContent(allModBox);
        if(userMode.get() == Settings.TEACHER)
        {
            classAssignmentModificationsPane.getChildren().addAll(classTableSearch, studentTable, assignmentTable, modPane);
        }
        else
        {
            classAssignmentModificationsPane.getChildren().addAll(classTableSearch, assignmentTable, modPane);
        }
    }
}
