package BackCode;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Contains the name, category, and score of each assignment in the class
 * @author lukecjm
 */
public class Assignment 
{
    private StringProperty assignmentName;
    private StringProperty assignmentCategory; //category in the grading scale
    private FloatProperty assignmentScore;
    private FloatProperty assignmentTotalScore;
    
    public Assignment()
    {
        // default constructor.
        assignmentName = null;
        assignmentTotalScore = null;
        assignmentCategory = null;
    }
    
    public Assignment(StringProperty aName, FloatProperty totalScore)
    {
        // if this class isn't weighted, set the category to null.
        this(aName, totalScore, null);
        
    }
    
    public Assignment(StringProperty aName, FloatProperty totalScore, 
            StringProperty aCategory)
    {
        assignmentName = aName;
        assignmentTotalScore = totalScore;
        assignmentCategory = aCategory;
    }
    
    // set methods
    
    public void setScore(FloatProperty aScore)
    {
        assignmentScore = aScore;
    }
    
    public void setTotalScore(FloatProperty aTotalScore)
    {
        assignmentTotalScore = aTotalScore;
    }
    
    public void setName(StringProperty aName)
    {
        assignmentName = aName;
    }
    
    public void setCategory(StringProperty aCategory)
    {
        assignmentCategory = aCategory;
    }
    
    
    // get methods
    
    public FloatProperty getScore()
    {
        return assignmentScore;
    }
    
    public FloatProperty getTotalScore()
    {
        return assignmentTotalScore;
    }
    
    public StringProperty getName()
    {
        return assignmentName;
    }
    
    public StringProperty getCategory()
    {
        return assignmentCategory;
    }
      
}
