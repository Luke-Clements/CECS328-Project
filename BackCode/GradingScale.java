package BackCode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
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
    public GradingScale()
    {
        A = new SimpleFloatProperty(90);
        B = new SimpleFloatProperty(80);
        C = new SimpleFloatProperty(70);
        D = new SimpleFloatProperty(60);
        F = new SimpleFloatProperty(50);
        gsPassFail = new SimpleBooleanProperty(false);
    }
    
    // Allow the user to customize grade percentages.
    public GradingScale(FloatProperty a, FloatProperty b, FloatProperty c, FloatProperty d, FloatProperty f)
    {
        A = a;
        B = b;
        C = c;
        D = d;
        F = f;
        gsPassFail.set(false);
    }
    
    // If this is a pass/fail, 
    public GradingScale(FloatProperty c)
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
    public String[] getGradingScaleInfoArray()
    {
        String[] gradingScaleInfo = new String[6];
        gradingScaleInfo[0] = A.get() + "";
        gradingScaleInfo[1] = B.get() + "";
        gradingScaleInfo[2] = C.get() + "";
        gradingScaleInfo[3] = D.get() + "";
        gradingScaleInfo[4] = F.get() + "";
        gradingScaleInfo[5] = gsPassFail.get() == true ? "True" : "False";
        return gradingScaleInfo;
    }
}
