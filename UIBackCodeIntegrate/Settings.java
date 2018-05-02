package BackCode;


import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lukecjm
 */
public class Settings 
{
    private StringProperty filePathToDataBaseFiles;
    private StringProperty userName;
    private LongProperty idNumber;
    public static final long STUDENT = 1; //for comparing which mode is being used 
    public static final long TEACHER = 0;
    private LongProperty userMode;
    
    public StringProperty getUserName()
    {
        return userName;
    }
    
    public LongProperty getIDNumber()
    {
        return idNumber;
    }
    
    public StringProperty getFilePathToDataBaseFiles()
    {
        return filePathToDataBaseFiles;
    }
    
    public long getUserMode()
    {
        if(this.userMode.get() == STUDENT)
        {
            return STUDENT;
        }
        else
        {
            return TEACHER;
        }
    }
    
    public void setUserName(String un)
    {
        this.userName = new SimpleStringProperty(un);
    }
    
    public void setIDNumber(long id)
    {
        this.idNumber = new SimpleLongProperty(id);
    }
    
    public void setFilePathToDataBaseFiles(String filepath)
    {
        this.filePathToDataBaseFiles = new SimpleStringProperty(filepath);
    }
    
    public void setUserMode(long um)
    {
        this.userMode = new SimpleLongProperty(um);
    }
    
    public static Settings deepEquals(Settings set)
    {
        Settings settings = new Settings();
        
        settings.setFilePathToDataBaseFiles(set.getFilePathToDataBaseFiles().get());
        settings.setIDNumber(set.getIDNumber().get());
        settings.setUserMode(set.getUserMode());
        settings.setUserName(set.getUserName().get());
        return settings;
    }
}
