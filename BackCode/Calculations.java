/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackCode;

import java.util.HashMap;
import java.util.Set;
import javafx.collections.ObservableList;

/**
 *
 * @author lukecjm
 */
public class Calculations 
{
    //calculate GPA
    public static float calculateGPA(String[] grades)
    {
        int classesNotConsideredForGPA = 0;
        int sumOfFinalGrades = 0;
        for (String grade : grades) {
            if (grade.equals("P") || grade.equals("NP")) {
                classesNotConsideredForGPA++;
            } else {
                sumOfFinalGrades += getFinalGrade(grade);
            }
        }
        return sumOfFinalGrades/(float)(grades.length-classesNotConsideredForGPA);
    }
    
    public static String getFinalLetterGrade(float grade, GradingScale gs)
    {
        if(grade > gs.getA()) return "A";
        else if(grade > gs.getB()) return "B";
        else if(grade > gs.getC()) return "C";
        else if(grade > gs.getD()) return "D";
        else return "F";
    }
    private static int getFinalGrade(String letter)
    {
        switch(letter)
        {
            case "A": return 4;
            case "B": return 3;
            case "C": return 2;
            case "D": return 1;
            case "F": return 0;
            default: return 0;
        }
    }
    
    public static float calculateCurrentClassScore(HashMap<String, Integer> cw, ObservableList<Assignment> assignments)
    {
        HashMap<String, Float> categoryScore = new HashMap<>(); // stores the possible best score by the user
        HashMap<String, Float> categoryMaxScore = new HashMap<>(); // 100% on everything in all categories
        float finalScore = 0;
        
        for (Assignment assignment : assignments) 
        {
            //only gets assignments that have had a score input
            if (assignment.getAssignmentScore(true) != null) 
            {
                if(categoryScore.get(assignment.getAssignmentCategory()) != null)
                {
                    categoryScore.put(assignment.getAssignmentCategory(),
                                      categoryScore.get(
                                              assignment.getAssignmentCategory()) + 
                                                assignment.getAssignmentScore());
                    categoryMaxScore.put(assignment.getAssignmentCategory(), 
                                      categoryMaxScore.get(assignment.getAssignmentCategory()) + 
                                              assignment.getAssignmentMaxScore());
                }
                else
                {
                    categoryScore.put(assignment.getAssignmentCategory(),
                                                assignment.getAssignmentScore());
                    categoryMaxScore.put(assignment.getAssignmentCategory(), 
                                              assignment.getAssignmentMaxScore());
                }
            }
        }
        
        Set<String> keys = categoryScore.keySet();
        
        for(String key: keys)
        {
            finalScore += 
                    (categoryScore.get(key)/categoryMaxScore.get(key))*cw.get(key);
        }
        return finalScore;
    }
    
    //calculate max score for class
    public static float calculateMaxClassScore(HashMap<String, Integer> cw, Assignment[] assignments)
    {
        int numberOfGradingCategories = cw.size();
        HashMap<String, Float> categoryScore = new HashMap<>(); // stores the possible best score by the user
        HashMap<String, Float> categoryMaxScore = new HashMap<>(); // 100% on everything in all categories
        float finalScore = 0;
        
        for (Assignment assignment : assignments) 
        {
            if (assignment.getAssignmentScore(true) == null) 
            {
                categoryScore.put(assignment.getAssignmentCategory(), 
                                  categoryScore.get(assignment.getAssignmentCategory()) + assignment.getAssignmentMaxScore());
            }
            else
            {
                categoryScore.put(assignment.getAssignmentCategory(),
                                  categoryScore.get(assignment.getAssignmentCategory()) + assignment.getAssignmentScore());
            }
            categoryMaxScore.put(assignment.getAssignmentCategory(),
                                 categoryScore.get(assignment.getAssignmentCategory()) + assignment.getAssignmentMaxScore());
        }
        
        Set<String> keys = categoryScore.keySet();
        
        for(String key: keys)
        {
            finalScore += 
                    (categoryScore.get(key)/categoryMaxScore.get(key))*cw.get(key);
        }
        return finalScore;
    }
}
