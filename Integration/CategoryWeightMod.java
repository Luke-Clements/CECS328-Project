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
public class CategoryWeightMod 
{
    private static final String[] CATEGORYWEIGHT_INFO_CATEGORIES = {"Assignment Category", "Weight"};
    public static final String[] CATEGORYWEIGHT_INFO_EMPTY = {"", ""};
    
    public static void SetupCategoryWeightMod(VBox categoryWeightMod, String[] categoryWeightInfo)
    {
        categoryWeightMod.getChildren().clear();
        HBox getCategoryName = new HBox();
        TextField nameField = new TextField();
        Label promptName = new Label(CATEGORYWEIGHT_INFO_CATEGORIES[0] + ":");
        nameField.setText(categoryWeightInfo[0]);
        getCategoryName.getChildren().addAll(promptName, nameField);
        
        HBox getWeight = new HBox();
        TextField weightField = new TextField();
        Label promptWeight = new Label(CATEGORYWEIGHT_INFO_CATEGORIES[1] + ":");
        weightField.setText(categoryWeightInfo[1]);
        getWeight.getChildren().addAll(promptWeight, weightField);
        
        Button saveCategoryWeight = new Button("Save Category Weight Changes");

        saveCategoryWeight.setOnMouseClicked(e -> 
        {
            //need to execute a update statement in the database
        });
        
        categoryWeightMod.getChildren().addAll(getCategoryName, getWeight, saveCategoryWeight);
    }
}
