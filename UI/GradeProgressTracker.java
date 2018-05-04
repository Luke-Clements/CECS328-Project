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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
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
    private String[] classInfo = {"", "", "", "", "", ""}; //placeholder for loaded values in the SetupClassModPane function
    private String[] assignmentInfo = {"", "", "", ""};
    private String[] gradingScaleInfo = {"", "", "", "", "", ""};
    private ClassMod classMod;
    private AssignmentMod assignmentMod;
    private CategoryWeightMod categoryWeightMod;
    private Search search;

    //this filepath contains info for startup of the program. This must be in the 
    //  same folder to the .exe file for this program as it is static
    private final String filePathToSettingsInfo = System.getProperty("user.dir") + "/Settings.json";
    private Settings settings;
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
        
        //setting up the change between modes
        settings.getUserMode().addListener(e -> {
            System.out.println("howintheworld");
        });
        
        //Setup settings tab
        SettingsInfo.SetupSettingsMod(settingsModTab, settings, db, conn, filePathToSettingsInfo);
        settingsTab.setContent(settingsModTab);
        
        ContinuedStartup(mainPane, settingsTab, classModificationsTab, searchTab, searchPane, searchTable, searchGradeInfo, classAssignmentModificationsPane);
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

    public void ContinuedStartup(TabPane mainPane, Tab settingsTab, Tab classModificationsTab, Tab searchTab, HBox searchPane, VBox searchTable, VBox searchGradeInfo, HBox classAssignmentModificationsPane)
    {
        search = new Search();
        
        //Setup search tab
        // Name(of class), semester(date), grade, GPA by semester, Professor, SchoolName
        search.SetupSearchTable(conn, searchTable, searchGradeInfo);
        search.SetupGradeInfo(conn, search, searchGradeInfo);
        
        classMod = new ClassMod();
        assignmentMod = new AssignmentMod();
        categoryWeightMod = new CategoryWeightMod();
        //Setup class, assignment, category weight, grading scale modifications tab
        // name(of class), semester, grade, professor, schoolName
        SetupClassAssignmentModificationsPane(classAssignmentModificationsPane);
        //end classInputPane setup
        
        classModificationsTab.setContent(classAssignmentModificationsPane);
        searchTab.setContent(searchPane);
        
        searchPane.getChildren().addAll(searchTable, searchGradeInfo);
        mainPane.getTabs().addAll(classModificationsTab, searchTab, settingsTab);
        mainPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        mainPane.selectionModelProperty().getValue().selectedItemProperty().addListener(e -> {
            search.SetupSearchTable(conn, searchTable, searchGradeInfo);
        });
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
        
        classMod.SetupClassMod(settings, null, classModBox, classInfo, conn);
        classMod.SetupClassTableResults(null);

        TableView<Map.Entry<String, Integer>> categoryWeightTable;
        TableView<Assignment> assignmentTable;

            assignmentTable = assignmentMod.SetupAssignmentTable(conn, assignmentModBox, classMod);
            categoryWeightTable = categoryWeightMod.SetupCategoryWeightTable(conn, classMod, categoryWeightModBox, 0);

        classMod.SetupClassTable(categoryWeightMod, assignmentMod, settings, conn, classTableSearch, classModBox, assignmentModBox, gradingScaleModBox, categoryWeightModBox, categoryWeightTable);
        //every time a different class is selected in the table, 
        //  the corresponding values are propogated to the classModGrid and the assignment table
//        classMod.getClassTable().getSelectionModel().selectedItemProperty().addListener(e -> 
//        {
////            ArrayList<Assignment> ai = new ArrayList();
////            //implement function to return an arraylist of assignment items based on the class ID
////            ai.add(new Assignment());
////            assignmentItems = FXCollections.observableList(ai);
////            assignmentTable.setItems(assignmentItems);
//            
//            //implement function to return an arraylist of assignment items based upon the class ID
//            categoryWeightItems = FXCollections.observableArrayList(cw.getCategoryWeight().entrySet());
//            categoryWeightTable.setItems(categoryWeightItems);
//        });
        
        //sets up modifying the class values
        classMod.SetupClassMod(settings, null, classModBox, classInfo, conn);
        assignmentMod.SetupAssignmentMod(classMod, conn, assignmentModBox, assignmentInfo);
        GradingScaleMod.SetupGradingScaleMod(gradingScaleModBox, GradingScaleMod.GRADING_SCALE_INFO_EMPTY);
        categoryWeightMod.SetupCategoryWeightMod(classMod, conn, categoryWeightModBox, CategoryWeightMod.CATEGORYWEIGHT_INFO_EMPTY);
        
        allModBox.getChildren().addAll(classModBox, gradingScaleModBox, categoryWeightModBox, assignmentModBox);
        modPane.setContent(allModBox);
        classAssignmentModificationsPane.getChildren().addAll(classTableSearch, assignmentTable, modPane);
    }
}
