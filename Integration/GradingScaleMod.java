/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author lukecjm
 */
public class GradingScaleMod 
{
    private static final String[] GRADING_SCALE_INFO_CATEGORIES = {"A", "B", "C", "D", "F", "Pass or Fail"};
    public static final String[] GRADING_SCALE_INFO_EMPTY = {"", "", "", "", "", ""};
    
    public static void SetupGradingScaleMod(VBox gradingScaleMod, String[] gsi)
    {
        gradingScaleMod.getChildren().clear();
        HBox a = new HBox();
        TextField aText = new TextField();
        Label promptA = new Label(GRADING_SCALE_INFO_CATEGORIES[0] + ":");
        aText.setText(gsi[0]);
        a.getChildren().addAll(promptA, aText);
        
        HBox b = new HBox();
        TextField bText = new TextField();
        Label promptB = new Label(GRADING_SCALE_INFO_CATEGORIES[1] + ":");
        bText.setText(gsi[1]);
        b.getChildren().addAll(promptB, bText);
        
        HBox c = new HBox();
        TextField cText = new TextField();
        Label promptC = new Label(GRADING_SCALE_INFO_CATEGORIES[2] + ":");
        cText.setText(gsi[2]);
        c.getChildren().addAll(promptC, cText);
        
        HBox d = new HBox();
        TextField dText = new TextField();
        Label promptD = new Label(GRADING_SCALE_INFO_CATEGORIES[3] + ":");
        dText.setText(gsi[3]);
        d.getChildren().addAll(promptD, dText);
        
        HBox f = new HBox();
        TextField fText = new TextField();
        Label promptF = new Label(GRADING_SCALE_INFO_CATEGORIES[4] + ":");
        fText.setText(gsi[4]);
        f.getChildren().addAll(promptF, fText);
        
        HBox passFail = new HBox();
        TextField passFailText = new TextField();
        Label promptpassFail = new Label(GRADING_SCALE_INFO_CATEGORIES[5] + ":");
        passFailText.setText(gsi[3]);
        passFail.getChildren().addAll(promptpassFail, passFailText);
        
        Button saveGradingScale = new Button("Save Grading Scale Changes");

        saveGradingScale.setOnMouseClicked(e -> 
        {
            //need to execute a update statement in the database
        });
        
        gradingScaleMod.getChildren().addAll(a, b, c, d, f, passFail, saveGradingScale);
    }
}