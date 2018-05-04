/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableView<Map.Entry<String, Integer>> categoryWeightTable = new TableView<>();
    
    public CategoryWeightMod()
    {
        categoryWeightTable.setMaxHeight(175);
        categoryWeightTable.setEditable(true);
    }
    
    //sets up Assignment table
    public TableView<Map.Entry<String, Integer>> SetupCategoryWeightTable(Connection conn, ClassMod cm, VBox categoryWeightModBox, int classID)
    {
        if(!(cm.getClassTable() == null))
        {
            classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
        }
        else
        {
            classID = -1;
        }
        
        categoryWeightItems = GetCategoryWeightTableValues(classID, conn);
        
        categoryWeightTable.setItems(categoryWeightItems);
        
        TableColumn<Map.Entry<String, Integer>, String> categoryCol = new TableColumn(CATEGORYWEIGHT_INFO_CATEGORIES[0]);
        categoryCol.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> p) -> {
            return new SimpleStringProperty(p.getValue().getKey());
        });
        categoryCol.setEditable(true);
        
        TableColumn<Map.Entry<String, Integer>, Number> weightCol = new TableColumn(CATEGORYWEIGHT_INFO_CATEGORIES[1]);
        weightCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Number>, ObservableValue<Number>>() {

                @Override
                public ObservableValue<Number> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Number> p) {
                    
                    return new SimpleIntegerProperty(p.getValue().getValue());
                }
            });
        weightCol.setEditable(true);

//        categoryWeightTable.getSelectionModel().selectedIndexProperty().addListener(e -> {
//            Map.Entry<String, Integer> a = categoryWeightTable.getSelectionModel().getSelectedItem();
//            String[] categoryWeightInfo = {a.getKey(), a.getValue()+""};
//            SetupCategoryWeightMod(gsID, conn, categoryWeightModBox, categoryWeightInfo);
//        });

        categoryWeightTable.getColumns().setAll(categoryCol, weightCol);
        return categoryWeightTable;
    }
    
    public void SetupCategoryWeightMod(ClassMod cm, Connection conn, VBox categoryWeightMod, String[] categoryWeightInfo)
    {           
        categoryWeightMod.getChildren().clear();
        HBox getCategory = new HBox();
        TextField categoryField = new TextField();
        Label promptCategory = new Label(CATEGORYWEIGHT_INFO_CATEGORIES[0] + ":");
        categoryField.setText(categoryWeightInfo[0]);
        getCategory.getChildren().addAll(promptCategory, categoryField);
        
        HBox getWeight = new HBox();
        TextField weightField = new TextField();
        Label promptWeight = new Label(CATEGORYWEIGHT_INFO_CATEGORIES[1] + ":");
        weightField.setText(categoryWeightInfo[1]);
        getWeight.getChildren().addAll(promptWeight, weightField);

        Button addCategoryWeight = new Button("Add Category Weight");
        addCategoryWeight.setOnMouseClicked(e -> {
            if(!weightField.getText().equals("") && !categoryField.getText().equals(""))
            {
                categoryWeightItems.add(new SimpleEntry(categoryField.getText(), Integer.parseInt(weightField.getText())));
                categoryWeightTable.setItems(categoryWeightItems);
            }
            else
            {
                System.out.println("invalid values");
                //error window
            }
        });
        Button removeCategoryWeight = new Button("Remove Category Weight");
        removeCategoryWeight.setOnMouseClicked(e -> {
            categoryWeightItems.remove(categoryWeightTable.getSelectionModel().getSelectedItem());
            categoryWeightTable.setItems(categoryWeightItems);
        });
        Button saveCategoryWeight = new Button("Save Category Weight Changes");
        saveCategoryWeight.setOnMouseClicked(e -> 
        {
            if(weightSumValid())
            {
                int classID = cm.getClassTable().getSelectionModel().getSelectedItem().getCID();
                updateCategoryWeight(conn, classID);
            }
            else
            {
                System.out.println("invalid weight sum");
                //Notification window
            }
        });
        categoryWeightMod.getChildren().addAll(categoryWeightTable, getCategory, getWeight, addCategoryWeight, removeCategoryWeight, saveCategoryWeight);
    }
    //finished
    public static boolean isValidCategory(String cat, Connection conn, int classID)
    {
        String stmt = "SELECT cwCategory FROM CategoryWeight WHERE classID=" + classID;
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
            {
                do
                {
                    if(rs.getString("cwCategory").equals(cat))
                    {
                        return true;
                    }
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return false;
                
    }
    //finished
    private void updateCategoryWeight(Connection conn, int classID)
    {
        String delStmt = "DELETE FROM CategoryWeight WHERE classID=" + classID;
        String insStmt = "INSERT INTO CategoryWeight(cwCategory, weight, classID) "+
                "VALUES(?, ?, ?)";
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(delStmt);
            //deletes old categoryWeight data
            ps.executeUpdate();
            
            //adds all new categoryWeight data
            for(Map.Entry<String, Integer> entry:categoryWeightTable.getItems())
            {
                ps = conn.prepareStatement(insStmt);
                ps.setString(1, entry.getKey());
                ps.setInt(2, entry.getValue());
                ps.setInt(3, classID);
                ps.executeUpdate();
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
    }
    //finished
    private boolean weightSumValid()
    {
        int sum = 0;
        for(Map.Entry<String, Integer> entry:categoryWeightTable.getItems())
        {
            sum += entry.getValue();
        }
        if(sum == 100)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //finished
    public static int getGradingScaleID(Connection conn, int classID)
    {
        String stmt = "SELECT gsID FROM Class WHERE cID=" + classID;
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) return rs.getInt("gsID");
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return -1;
    }
    
    //finished
    public ObservableList<Map.Entry<String, Integer>> GetCategoryWeightTableValues(int classID, Connection conn)
    {
        String stmt = "SELECT * FROM CategoryWeight WHERE classID=" + classID;
        HashMap<String, Integer> cw = new HashMap();
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
            {
                do
                {
                    cw.put(rs.getString("cwCategory"), rs.getInt("weight"));
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            
        }
        return FXCollections.observableArrayList(cw.entrySet());
    }
    
    //finished
    public static HashMap<String, Integer> GetCategoryWeightValues(int classID, Connection conn)
    {
        String stmt = "SELECT * FROM CategoryWeight WHERE classID=" + classID;
        HashMap<String, Integer> cw = new HashMap();
        
        try
        {
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
            {
                do
                {
                    cw.put(rs.getString("cwCategory"), rs.getInt("weight"));
                }while(rs.next());
            }
        }
        catch(SQLException se)
        {
            
        }
        return cw;
    }
}
