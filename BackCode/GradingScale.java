package BackCode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private IntegerProperty A; // represents the lowest score to obtain that grade.
    private IntegerProperty B;
    private IntegerProperty C;
    private IntegerProperty D;
    private IntegerProperty F;
    private BooleanProperty gsPassFail; //indicates whether the class is a pass/fail assessment
//    private CategoryWeight gsGradingScale;
    
    // Default Grading Scale
    public GradingScale()
    {
        A = new SimpleIntegerProperty(90);
        B = new SimpleIntegerProperty(80);
        C = new SimpleIntegerProperty(70);
        D = new SimpleIntegerProperty(60);
        F = new SimpleIntegerProperty(50);
        gsPassFail = new SimpleBooleanProperty(false);
    }
    
    // Allow the user to customize grade percentages.
    public GradingScale(int a, int b, int c, int d, int f, boolean pf)
    {
        A = new SimpleIntegerProperty(a);
        B = new SimpleIntegerProperty(b);
        C = new SimpleIntegerProperty(c);
        D = new SimpleIntegerProperty(d);
        F = new SimpleIntegerProperty(f);
        gsPassFail = new SimpleBooleanProperty(pf);
    }
    
    public int getA()
    {
        return A.get();
    }
    public int getB()
    {
        return B.get();
    }
    public int getC()
    {
        return C.get();
    }
    public int getD()
    {
        return D.get();
    }
    public int getF()
    {
        return F.get();
    }
    public boolean getPassFail()
    {
        return gsPassFail.get();
    }
    // If this is a pass/fail, 
    public GradingScale(IntegerProperty c)
    {
        C = c;
        gsPassFail.set(true);
    }
    
//    public void setCategoryWeights(CategoryWeight cw)
//    {
//        gsGradingScale = cw;
//    }
//    
//    public CategoryWeight getCategoryWeights()
//    {
//        return gsGradingScale;
//    }
    
    public IntegerProperty getLowestScore(SimpleStringProperty grd)
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
