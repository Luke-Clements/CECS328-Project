package BackCode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Contains the grading values as well as the grading scale from the CategoryWeight class
 * @author lukecjm
 */
public class GradingScale 
{
    private FloatProperty A; // represents the lowest score to obtain that grade.
    private FloatProperty B;
    private FloatProperty C;
    private FloatProperty D;
    private FloatProperty F;
    private BooleanProperty gsPassFail; //indicates whether the class is a pass/fail assessment
    private CategoryWeight gsGradingScale;
    
    // Default Grading Scale
    void GradingScale()
    {
        A.set(90);
        B.set(80);
        C.set(70);
        D.set(60);
        F.set(0);
        gsPassFail.set(false);
    }
    
    // Allow the user to customize grade percentages.
    void GradingScale(FloatProperty a, FloatProperty b, FloatProperty c, FloatProperty d, FloatProperty f)
    {
        A = a;
        B = b;
        C = c;
        D = d;
        F = f;
        gsPassFail.set(false);
    }
    
    // If this is a pass/fail, 
    void GradingScale(FloatProperty c)
    {
        C = c;
        gsPassFail.set(true);
    }
    
    public void setCategoryWeights(CategoryWeight cw)
    {
        gsGradingScale = cw;
    }
    
    public CategoryWeight getCategoryWeights()
    {
        return gsGradingScale;
    }
    
    public FloatProperty getLowestScore(SimpleStringProperty grd)
    {
        
        char grade = grd.get().charAt(0);
        
        
        switch( Character.toUpperCase(grade) )
        {
            case 'A': return A;
            case 'B': return B;
            case 'C': return C;
            case 'D': return D;
            default: return F;
        }
    }
    
    
   
}
