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
    
    public void setUserName(String un)
    {
        this.userName.setValue(un);
    }
    
    public void setIDNumber(int id)
    {
        this.idNumber.setValue(id);
    }
    
    public void setFilePathToDataBaseFile(String filepath)
    {
        this.filePathToDataBaseFile.setValue(filepath);
    }
}
