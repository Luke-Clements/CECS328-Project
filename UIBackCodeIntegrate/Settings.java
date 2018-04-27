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
    private StringProperty filePathToDataBaseFile;
    private StringProperty userName;
    private LongProperty idNumber;
    
    public StringProperty getUserName()
    {
        return userName;
    }
    
    public LongProperty getIDNumber()
    {
        return idNumber;
    }
    
    public StringProperty getFilePathToDataBaseFile()
    {
        return filePathToDataBaseFile;
    }
    
    public void setUserName(String un)
    {
        this.userName = new SimpleStringProperty(un);
    }
    
    public void setIDNumber(long id)
    {
        this.idNumber = new SimpleLongProperty(id);
    }
    
    public void setFilePathToDataBaseFile(String filepath)
    {
        this.filePathToDataBaseFile = new SimpleStringProperty(filepath);
    }
}
