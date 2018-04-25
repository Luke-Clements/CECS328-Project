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
    
    //Setters
    public void setUserName(StringProperty un){//User Name
        this.userName = un;
    }
    
    public void setIDNumber(IntegerProperty id)//ID Number
    {
        this.idNumber = id;
    }
    
    public void setFilePathToDataBaseFile(StringProperty filepath)//File Path
    {
        this.filePathToDataBaseFile = filepath;
    }
    
    //Getters
    public StringProperty getUserName()//User Name
    {
        return userName;
    }
    
    public IntegerProperty getIDNumber()//ID Number
    {
        return idNumber;
    }
    
    public StringProperty getFilePathToDataBaseFile()//File Path
    {
        return filePathToDataBaseFile;
    }
}
