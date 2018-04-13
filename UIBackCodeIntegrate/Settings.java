package BackCode;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lukecjm
 */
public class Settings 
{
    private StringProperty filePathToDataBaseFile;
    private StringProperty userName;
    private IntegerProperty idNumber;
    
    public StringProperty getUserName()
    {
        return userName;
    }
    
    public IntegerProperty getIDNumber()
    {
        return idNumber;
    }
    
    public StringProperty getFilePathToDataBaseFile()
    {
        return filePathToDataBaseFile;
    }
    
    public void setUserName(StringProperty un)
    {
        this.userName = un;
    }
    
    public void setIDNumber(IntegerProperty id)
    {
        this.idNumber = id;
    }
    
    public void setFilePathToDataBaseFile(StringProperty filepath)
    {
        this.filePathToDataBaseFile = filepath;
    }
}
