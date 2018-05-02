package BackCode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
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
    private StringProperty className;
    private StringProperty classTeacherName;
    private StringProperty classSemester;
    private StringProperty classSchool;
    private IntegerProperty cID;
    private LongProperty classTime;

    public Class(){
        className = new SimpleStringProperty("Select");
        classTeacherName = new SimpleStringProperty("This");
        classSemester = new SimpleStringProperty("To Add");
        classSchool = new SimpleStringProperty("New Class");
        cID = new SimpleIntegerProperty(-1);
        classTime = new SimpleLongProperty(0);
    }
    
    public Class(StringProperty cName, StringProperty cTeacherName, StringProperty cSemester, StringProperty cSchool){
        className = cName;
        classTeacherName = cTeacherName;
        classSemester = cSemester;
        classSchool = cSchool;
    }
    
    
    //Setters
    public void setName(StringProperty cName){
        className = cName;
    }
    
    public void setTeacher(StringProperty cTeacherName){
        classTeacherName = cTeacherName;
    }
    
    public void setSemester(StringProperty cSemester){
        classSemester = cSemester;
    }
    public void setSchool(StringProperty cSchool){
        classSchool = cSchool;
    }
    public void setID(IntegerProperty id){
        cID = id;
    }
    public void setTime(LongProperty time){
        classTime = time;
    }
    
    
    //Getters
    public String getClassName(){
        return className.get();
    }
    
    public String getClassTeacherName(){
        return classTeacherName.get();
    }
    
    public String getClassSemester(){
        return classSemester.get();
    }
    public String getClassSchool(){
        return classSchool.get();
    }
    public int getCID(){
        return cID.get();
    }
    public Long getClassTime(){
        return classTime.get();
    }
    public String[] getClassInfoArray(){
        String[] classInfo = new String[6];
        classInfo[0] = cID.get() + "";
        classInfo[1] = className.get();
        classInfo[2] = classSemester.get();
        classInfo[3] = classTeacherName.get();
        classInfo[4] = classSchool.get();
        classInfo[5] = classTime.get()+"";
        return classInfo;
    }
}
