import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Contains information regarding the class that the student is taking
 * @author lukecjm
 */
public class Class {
    private StringProperty cName;
    private StringProperty cTeacherName;
    private StringProperty cSemester;
    private IntegerProperty cID;
    private GradingScale cGradingScale; //not sure if this should be a string or not
    private Assignment[] cAssignments;
}
