package BackCode;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
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
    private FloatProperty assignmentMaxScore;
    private FloatProperty assignmentGrade;
    
    public Assignment()
    {
        // default constructor.
        assignmentName = null;
        assignmentMaxScore = null;
        assignmentCategory = null;
        assignmentGrade = null;
    }
    
    public Assignment(StringProperty aName, FloatProperty totalScore)
    {
        // if this class isn't weighted, set the category to null.
        this(aName, totalScore, null);
        
    }
    
    public Assignment(StringProperty aName, FloatProperty maxScore, 
            StringProperty aCategory)
    {
        assignmentName = aName;
        assignmentMaxScore = maxScore;
        assignmentCategory = aCategory;
        assignmentGrade = null;
    }
    
    // set methods
    
    public void setScore(FloatProperty aScore)
    {
        assignmentScore = aScore;
        assignmentGrade = new SimpleFloatProperty(assignmentScore.get()/assignmentMaxScore.get());
    }
    
    public void setMaxScore(FloatProperty aMaxScore)
    {
        assignmentMaxScore = aMaxScore;
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
    
    public FloatProperty getAssignmentScore()
    {
        return assignmentScore;
    }
    
    public FloatProperty getAssignmentMaxScore()
    {
        return assignmentMaxScore;
    }
    
    public String getAssignmentName()
    {
        return assignmentName.get();
    }
    
    public String getAssignmentCategory()
    {
        return assignmentCategory.get();
    }
    public FloatProperty getAssignmentGrade()
    {
        return assignmentGrade;
    }
}
