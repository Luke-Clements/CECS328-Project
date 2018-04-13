package pkg343project;

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
public class Assignment {
    private StringProperty assignmentName;
    private StringProperty assignmentCategory; //category in the grading scale
    private FloatProperty assignmentScore;
    private FloatProperty assignmentTotalScore;
    
    Assignment()
    {
        // default constructor.
        assignmentName = null;
        assignmentTotalScore = null;
        assignmentCategory = null;
    }
    
    Assignment(StringProperty aName, FloatProperty totalScore)
    {
        // if this class isn't weighted, set the category to null.
        this(aName, totalScore, null);
        
    }
    
    Assignment(StringProperty aName, FloatProperty totalScore, StringProperty aCategory)
    {
        assignmentName = aName;
        assignmentTotalScore = totalScore;
        assignmentCategory = aCategory;
    }
    
    public void setScore(FloatProperty aScore)
    {
        assignmentScore = aScore;
    }
    
    /*
    Assignment(StringProperty aName, FloatProperty score, 
            FloatProperty totalScore, StringProperty sName)
    {
        // create teacher assignment
        assignmentName = aName;
        assignmentScore = score;
        assignmentTotalScore = totalScore;
        studentName = sName;
    }
    */
  
    
    
    
    
}
