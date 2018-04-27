/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackCode;

import java.util.HashMap;
import java.util.Set;

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
            if (grades.equals("P")) {
                classesNotConsideredForGPA++;
            } else {
                sumOfFinalGrades += getFinalGrade(grade);
            }
        }
        return sumOfFinalGrades/(float)(grades.length-classesNotConsideredForGPA);
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
            case "NP": return 0;
            default: return 0;
        }
    }
    
    //calculate max score for class
    public static float calculateMaxClassScore(CategoryWeight cw, Assignment[] assignments)
    {
        int numberOfGradingCategories = cw.getCategoryWeight().size();
        HashMap<String, Float> categoryScore = new HashMap<>(); // stores the possible best score by the user
        HashMap<String, Float> categoryMaxScore = new HashMap<>(); // 100% on everything in all categories
        float finalScore = 0;
        
        for (Assignment assignment : assignments) 
        {
            if (assignment.getScore() == null) 
            {
                categoryScore.put(assignment.getCategory().get(), 
                                  categoryScore.get(assignment.getCategory().get()) + assignment.getMaxScore().get());
            }
            else
            {
                categoryScore.put(assignment.getCategory().get(),
                                  categoryScore.get(assignment.getCategory().get()) + assignment.getScore().get());
            }
            categoryMaxScore.put(assignment.getCategory().get(),
                                 categoryScore.get(assignment.getCategory().get()) + assignment.getMaxScore().get());
        }
        
        Set<String> keys = categoryScore.keySet();
        
        for(String key: keys)
        {
            finalScore += 
                    (categoryScore.get(key)/categoryMaxScore.get(key))*cw.getWeight(key).get();
        }
        return finalScore;
    }
}
