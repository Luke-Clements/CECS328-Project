package BackCode;

import java.util.HashMap;
import javafx.beans.property.IntegerProperty;
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
    HashMap<StringProperty, IntegerProperty> categories = new HashMap<>();
    
    public CategoryWeight()
    {
        // default constructor.
    }
    
    public void addCategory(StringProperty cName, IntegerProperty aWeight)
    {
        categories.put(cName, aWeight);
    }
    
    public void removeCategory(StringProperty cName)
    {
        categories.remove(cName);
    }
    
    public IntegerProperty getWeight(StringProperty cName)
    {
        // Return the weight of that category name inputted.
        return categories.get(cName);
    }
    
    
}
