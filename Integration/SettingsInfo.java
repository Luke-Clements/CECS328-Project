/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.Settings;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author lukecjm
 */
public class SettingsInfo 
{
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
    public static void SaveSettingsFile(Settings settings, String filePathToSettingsInfo)
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
    
}
