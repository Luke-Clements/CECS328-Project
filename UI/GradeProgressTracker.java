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
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
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
    private String[] classInfo = {"", "", "", "", ""}; //placeholder for loaded values in the SetupClassModPane function
    private String[] assignmentInfo = {"", "", "", ""};
    private String[] gradingScaleInfo = {"", "", "", "", "", ""};
    private ClassMod classMod;
    private Search search;

    //this filepath contains info for startup of the program. This must be in the 
    //  same folder to the .exe file for this program as it is static
    private final String filePathToSettingsInfo = System.getProperty("user.dir") + "/Settings.json";
    private Settings settings;
    private ObservableList<ClassGrade> classGradeItem;
    private ObservableList<BackCode.Class> classItems;
    private ObservableList<Assignment> assignmentItems;
    private Connection conn;
    private Database db;
    
    @Override
    public void start(Stage primaryStage) 
    {    
        TabPane mainPane = new TabPane();
        GridPane searchPane = new GridPane();
        HBox classAssignmentModificationsPane = new HBox();
        HBox settingsModTab = new HBox();
        GridPane calculatePane = new GridPane();
        Tab classModificationsTab = new Tab("Update Classes");
        Tab searchTab = new Tab("Search");
        Tab calculateGradeTab = new Tab("Calculations");
        Tab settingsTab = new Tab("Settings");
        
        InitialStartup(primaryStage);
        
        search = new Search();
        
        //Setup search tab
        // Name(of class), semester(date), grade, GPA by semester, Professor, SchoolName
        search.SetupSearchPane(searchPane);
        
        classMod = new ClassMod();
        //Setup class, assignment, category weight, grading scale modifications tab
        // name(of class), semester, grade, professor, schoolName
        SetupClassAssignmentModificationsPane(classAssignmentModificationsPane);
        //end classInputPane setup
        
        //Setup calculations tab
        // calculates GPA based on (very similar to search Pane)
        
        
        //Setup settings tab
        SettingsInfo.SetupSettingsMod(settingsModTab, settings, db, conn, filePathToSettingsInfo);
        
        classModificationsTab.setContent(classAssignmentModificationsPane);
        searchTab.setContent(searchPane);
        calculateGradeTab.setContent(calculatePane);
        settingsTab.setContent(settingsModTab);
        
        mainPane.getTabs().addAll(classModificationsTab, searchTab, calculateGradeTab, settingsTab);
        mainPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        Scene scene = new Scene(mainPane, 1000, 425);
        
        primaryStage.sizeToScene();
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
        
        conn = SettingsInfo.SetSelectedDatabaseAndGetConnection(settings, db);
    }
   
    public void SetupClassAssignmentModificationsPane(HBox classAssignmentModificationsPane)
    {
        VBox classTableSearch = new VBox();
        VBox classModBox = new VBox();
        VBox assignmentModBox = new VBox();
        VBox allModBox = new VBox();
        VBox gradingScaleModBox = new VBox();
        VBox categoryWeightModBox = new VBox();
        ScrollPane modPane = new ScrollPane();
        
        classMod.SetupClassMod(classModBox, classInfo);
        classMod.SetupClassTableResults(null);

        classMod.SetupClassTable(classTableSearch, classModBox, assignmentModBox, gradingScaleModBox, categoryWeightModBox);
        TableView<Assignment> assignmentTable;
        if(classMod.getClassTable().getSelectionModel().getSelectedItem() == null)
        {
            assignmentTable = AssignmentMod.SetupAssignmentTable(0);
        }
        else
        {
            assignmentTable = AssignmentMod.SetupAssignmentTable(classMod.getClassTable().getSelectionModel().getSelectedItem().getCID());
        }
        //every time a different class is selected in the table, 
        //  the corresponding values are propogated to the classModGrid and the assignment table
        classMod.getClassTable().getSelectionModel().selectedItemProperty().addListener(e -> 
        {
            ArrayList<Assignment> ai = new ArrayList();
            //implement function to return an arraylist of assignment items based on the class ID
            ai.add(new Assignment());
            assignmentItems = FXCollections.observableList(ai);
            assignmentTable.setItems(assignmentItems);
        });
        
        //sets up modifying the class values
        classMod.SetupClassMod(classModBox, classInfo);
        AssignmentMod.SetupAssignmentMod(assignmentModBox, assignmentInfo);
        GradingScaleMod.SetupGradingScaleMod(gradingScaleModBox, GradingScaleMod.GRADING_SCALE_INFO_EMPTY);
        CategoryWeightMod.SetupCategoryWeightMod(categoryWeightModBox, CategoryWeightMod.CATEGORYWEIGHT_INFO_EMPTY);
        
        allModBox.getChildren().addAll(classModBox, gradingScaleModBox, categoryWeightModBox, assignmentModBox);
        modPane.setContent(allModBox);
        classAssignmentModificationsPane.getChildren().addAll(classTableSearch, assignmentTable, modPane);
    }
}
