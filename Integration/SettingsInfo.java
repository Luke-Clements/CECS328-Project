/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Settings;
import Database.Database;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import javafx.beans.property.LongProperty;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author lukecjm
 */
public final class SettingsInfo 
{
    private static final String[] MODE_SETTINGS = {"Teacher", "Student"};
    
    //requires absolute pathing
    public static boolean moveFiles(String source, String destination)
    {
        File folder = new File(source);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) 
        {
            
            try
            {
                if (listOfFiles[i].isFile()) 
                {
                    Files.move(Paths.get(listOfFiles[i].getAbsolutePath()), Paths.get(destination + "/" + listOfFiles[i].getName()), StandardCopyOption.REPLACE_EXISTING);
                } 
                else if (listOfFiles[i].isDirectory()) 
                {
                    Path path = Paths.get(destination + "/" + listOfFiles[i].getName());
                    if(!path.toFile().exists() || !path.toFile().isDirectory())
                    {
                        Files.createDirectory(path);
                    }
                    moveFiles(source + "/" + listOfFiles[i].getName(), destination + "/" + listOfFiles[i].getName());
                }
            }
            catch(FileAlreadyExistsException faee)
            {
                //don't care, skips the selected file from copying process automatically
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
                return false;
            }
        }
        return true;
    }
    public static void Notification(String message)
    {
        Stage secondaryStage = new Stage();
        VBox content = new VBox();
        Label messageLabel = new Label(message);
        Button acknowledgeButton = new Button("OK");
        acknowledgeButton.setAlignment(Pos.CENTER);
        messageLabel.setAlignment(Pos.CENTER);
        
        acknowledgeButton.setOnMouseClicked(e -> {
            secondaryStage.close();
        });
        
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(messageLabel, acknowledgeButton);
        Scene scene = new Scene(content, 400, 50);
        secondaryStage.setScene(scene);
        secondaryStage.showAndWait();
    }
   //finished
    public static Connection SetSelectedDatabaseAndGetConnection(Connection connect, Settings settings, Database db)
    {
        if(settings.getUserMode().get() == settings.STUDENT)
        {
            db = new Database("jdbc:derby:GPTStudentTest;create=true", settings.getFilePathToDataBaseFiles().get());
            connect = db.getConnection();
            

            if(db.IsEmpty(connect))
            {
                db.initializeDatabase(connect, System.getProperty("user.dir") + "/databaseDDL/StudentDatabaseDDL.json");
            }
        }
        else
        {
            db = new Database("jdbc:derby:GPTTeacherTest;create=true", settings.getFilePathToDataBaseFiles().get());
            connect = db.getConnection();
            
            if(db.IsEmpty(connect))
            {
                db.initializeDatabase(connect, System.getProperty("user.dir") + "/databaseDDL/TeacherDatabaseDDL.json");
            }
        }
        return connect;
    }
    //finished
    public static void SetupSettingsMod(LongProperty mode, HBox settingsMod, Settings settings, 
                Database db, Connection conn, String filepathToSettingsFile)
    {
        Button setUsernameID = new Button("Set Username and ID");
        setUsernameID.setOnMouseClicked(e -> {
            GetUsernameAndId(settings);
            SaveSettingsFile(settings, filepathToSettingsFile);
        });
        
        ComboBox modeBox = new ComboBox();
        modeBox.setItems(FXCollections.observableArrayList(MODE_SETTINGS));
        //will never have a parse error here because it is accessing an long value that is either a 1 or a 0
        modeBox.setValue(MODE_SETTINGS[Integer.parseInt(Long.toString(settings.getUserMode().get()))]);
        modeBox.getSelectionModel().selectedItemProperty().addListener(e -> {
            if(modeBox.getSelectionModel().selectedIndexProperty().intValue() != settings.getUserMode().get())
            {
                settings.setUserMode(modeBox.getSelectionModel().selectedIndexProperty().intValue());
                SaveSettingsFile(settings, filepathToSettingsFile);
                mode.setValue(settings.getUserMode().get());
                if(mode.get() != settings.getUserMode().get())
                {
                    SettingsInfo.Notification("Mode switch failed.");
                }
            }
        });

        Label dbFilepath = new Label(settings.getFilePathToDataBaseFiles().get());
        Button setDbFilepath = new Button("Change Database Location");
        setDbFilepath.setOnMouseClicked(e -> {
            String source = settings.getFilePathToDataBaseFiles().get();
            GetNewDatabaseFilepath(settings);
            moveFiles(source, settings.getFilePathToDataBaseFiles().get());

            /****WARNING: Does not account for moving database files to the new location. ALL DATA WILL BE LOST!!!******/
            SaveSettingsFile(settings, filepathToSettingsFile);
            dbFilepath.setText(settings.getFilePathToDataBaseFiles().get());
            SetSelectedDatabaseAndGetConnection(conn, settings, db);
        });
        
        settingsMod.getChildren().addAll(setUsernameID, modeBox, dbFilepath, setDbFilepath);
    }
    //finished
    public static void GetNewDatabaseFilepath(Settings settings)
    {
        Stage primaryStage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        settings.setFilePathToDataBaseFiles(dirChooser.showDialog(primaryStage).getAbsolutePath());
    }
    //finished
    public static void GetUsernameAndId(Settings settings)
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
    public static void LoadSettingsFile(Settings settings, String filePathToSettingsInfo)
    {
        JSONParser parser = new JSONParser();
            
        try
        {
//            System.out.println(filePathToSettingsInfo);
            Object object = parser.parse(new FileReader(filePathToSettingsInfo));
            JSONObject jsonObj = (JSONObject)object;

//            System.out.println("afterparse");
            settings.setFilePathToDataBaseFiles((String)jsonObj.get("DatabaseFilepath"));
//            System.out.println(settings.getFilePathToDataBaseFiles().get());
            settings.setIDNumber((long)jsonObj.get("Id"));
//            System.out.println(settings.getIDNumber());
            settings.setUserName((String)jsonObj.get("Username"));
//            System.out.println(settings.getUserName());
            settings.setUserMode((long)jsonObj.get("UserMode"));
//            System.out.println(settings.getUserMode());
            settings.setClassCounter(Math.toIntExact((long)jsonObj.get("studentClassCounter")), Math.toIntExact(Settings.STUDENT));
            settings.setClassCounter(Math.toIntExact((long)jsonObj.get("teacherClassCounter")), Math.toIntExact(Settings.TEACHER));
            settings.setGradingScaleCounter(Math.toIntExact((long)jsonObj.get("studentGradingScaleCounter")), Math.toIntExact(Settings.STUDENT));
            settings.setGradingScaleCounter(Math.toIntExact((long)jsonObj.get("teacherGradingScaleCounter")), Math.toIntExact(Settings.TEACHER));
        } catch (FileNotFoundException e) {
            SettingsInfo.Notification("The selected file at " + filePathToSettingsInfo + " does not exist.");
        } catch (IOException ex) {
            SettingsInfo.Notification("There was an error reading the file.");
        } catch (ParseException ex) {
            SettingsInfo.Notification("The file data has been corrupted.");
        }
    }
    
    //finished
    public static void SaveSettingsFile(Settings settings, String filePathToSettingsInfo)
    {
        JSONObject obj = new JSONObject();
            
        obj.put("DatabaseFilepath", settings.getFilePathToDataBaseFiles().get());
        obj.put("Id", settings.getIDNumber().get());
        obj.put("Username", settings.getUserName().get());
        obj.put("UserMode", settings.getUserMode().get());
        obj.put("studentClassCounter", settings.getClassCounter(Settings.STUDENT));
        obj.put("teacherClassCounter", settings.getClassCounter(Settings.TEACHER));
        obj.put("studentGradingScaleCounter", settings.getGradingScaleCounter(Settings.STUDENT));
        obj.put("teacherGradingScaleCounter", settings.getGradingScaleCounter(Settings.TEACHER));

        try {
            FileWriter fw = new FileWriter(filePathToSettingsInfo);

            fw.write(obj.toJSONString());
            fw.close();
        } catch (Exception ex) 
        {
            SettingsInfo.Notification("The settings file was not saved.");
        }
    }
}
