import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Contains the grading values as well as the grading scale from the CategoryWeight class
 * @author lukecjm
 */
public class GradingScale {
    private FloatProperty A;
    private FloatProperty B;
    private FloatProperty C;
    private FloatProperty D;
    private FloatProperty F;
    private BooleanProperty gsPassFail; //indicates whether the class is a pass/fail assessment
    private CategoryWeight[] gsGradingScale;
}
