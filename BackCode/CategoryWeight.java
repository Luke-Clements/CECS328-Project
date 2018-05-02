package BackCode;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Contains the categories of the grading scale and the weight of each category
 * @author lukecjm
 */
public class CategoryWeight 
{   
  
    // create a hash map where the name of the category and the weight are stored.
    HashMap<String, Integer> categories = new HashMap<>();
    
    public CategoryWeight()
    {
        categories.put("Test", 20);
        categories.put("Homework", 20);
        categories.put("Final", 20);
    }
    
    public void addCategory(String cName, Integer aWeight)
    {
        categories.put(cName, aWeight);
    }
    
    public void removeCategory(StringProperty cName)
    {
        categories.remove(cName);
    }
    
    public Integer getWeight(String cName)
    {
        // Return the weight of that category name inputted.
        return categories.get(cName);
    }
    
    public HashMap<String, Integer> getCategoryWeight()
    {
        return categories;
    }
    public String[] getCategoryWeightInfoArray(Map.Entry<String, Integer> entry)
    {
        if(entry != null)
        {
            String[] categoryWeightInfo = new String[2];
            categoryWeightInfo[0] = entry.getKey();
            categoryWeightInfo[1] = entry.getValue() + "";
            return categoryWeightInfo;
        }
        return null;
    }
}
