package BackCode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
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
public class User {
    private StringProperty uName;
    private IntegerProperty uIDNumber;
    private BooleanProperty uIsTeacher;
    
    public User(){
        uName = null;
        uIDNumber = null;
        uIsTeacher = null;
    }
    
    //Setters
    public void setName(StringProperty userName){//Name
       uName = userName; 
    }
    
    public void setID(IntegerProperty userIDNumber){//ID Number
        uIDNumber = userIDNumber;
    }
    
    public void setTeacher(BooleanProperty userIsTeacher){//Teacher
        uIsTeacher = userIsTeacher;
    }
    
    //Getters
    public StringProperty getName(){//Name
       return uName;
    }
    
    public IntegerProperty getID(){//ID Number
       return uIDNumber;
    }
    
    public BooleanProperty getTeacher(){//Teacher
       return uIsTeacher;
    }
}

