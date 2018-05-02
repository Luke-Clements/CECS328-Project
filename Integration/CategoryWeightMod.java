/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import BackCode.CategoryWeight;
import static Integration.AssignmentMod.SetupAssignmentMod;
import java.util.ArrayList;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author lukecjm
 */
public class CategoryWeightMod 
{
    private static final String[] CATEGORYWEIGHT_INFO_CATEGORIES = {"Category", "Weight"};
    public static final String[] CATEGORYWEIGHT_INFO_EMPTY = {"", ""};
    private static ObservableList<Map.Entry<String, Integer>> categoryWeightItems;
    
    //sets up Assignment table
    public static TableView<Map.Entry<String, Integer>> SetupCategoryWeightTable(VBox categoryWeightModBox, int classID)
    {
        TableView<Map.Entry<String, Integer>> categoryWeightTable = new TableView<>();
        CategoryWeight cw = new CategoryWeight();
        
        categoryWeightItems = FXCollections.observableArrayList(cw.getCategoryWeight().entrySet());
        
        categoryWeightTable.setItems(categoryWeightItems);
        
        TableColumn<Map.Entry<String, Integer>, String> categoryCol = new TableColumn(CATEGORYWEIGHT_INFO_CATEGORIES[0]);
        categoryCol.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> p) -> {
            return new SimpleStringProperty(p.getValue().getKey());
        });
        
        TableColumn<Map.Entry<String, Integer>, Number> weightCol = new TableColumn(CATEGORYWEIGHT_INFO_CATEGORIES[1]);
        weightCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Number>, ObservableValue<Number>>() {

                @Override
                public ObservableValue<Number> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Number> p) {
                    // for second column we use value
                    return new SimpleIntegerProperty(p.getValue().getValue());
                }
            });

        categoryWeightTable.getSelectionModel().selectedIndexProperty().addListener(e -> {
            Map.Entry<String, Integer> a = categoryWeightTable.getSelectionModel().getSelectedItem();
            String[] categoryWeightInfo = cw.getCategoryWeightInfoArray(a);
            SetupCategoryWeightMod(categoryWeightTable, categoryWeightModBox, categoryWeightInfo);
        });

        categoryWeightTable.getColumns().setAll(categoryCol, weightCol);
        return categoryWeightTable;
    }
    
    public static void SetupCategoryWeightMod(TableView<Map.Entry<String, Integer>> categoryWeightTable, VBox categoryWeightMod, String[] categoryWeightInfo)
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
        categoryWeightMod.getChildren().addAll(categoryWeightTable, getCategoryName, getWeight, saveCategoryWeight);
    }
}
