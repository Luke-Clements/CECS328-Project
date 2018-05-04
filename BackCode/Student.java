package BackCode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lukecjm
 */
public class Student {
    private StringProperty sName;
    private StringProperty sEmail;
    private IntegerProperty sID;
    
    public Student(){
        sName = new SimpleStringProperty("Select to add");
        sEmail = new SimpleStringProperty("");
        sID = new SimpleIntegerProperty(-1);
    }
    
    //Setters
    public void setSName(StringProperty userName){//Name
       sName = userName; 
    }
    
    public void setSID(IntegerProperty userIDNumber){//ID Number
        sID = userIDNumber;
    }
    
    public void setSEmail(StringProperty email){//Teacher
        sEmail = email;
    }
    
    //Getters
    public String getSName(){//Name
       return sName.get();
    }
    
    public int getSID(){//ID Number
       return sID.get();
    }
    
    public String getSEmail(){//Teacher
       return sEmail.get();
    }
    
    public String[] getStudentInfoArray()
    {
        String[] studentInfo = new String[3];
        studentInfo[0] = sName.get();
        studentInfo[1] = sEmail.get();
        studentInfo[2] = sID.get()+"";
        return studentInfo;
    }
}

