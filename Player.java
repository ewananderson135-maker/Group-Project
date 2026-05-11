
/**
 * Nathaniel coded this portion of the code
 *
 * @author (Nathaniel Benedik)
 * @version (Monday May 11th, 2026)
 */

public class Player implements java.io.Serializable
{
    //declaring all necessary variables for this class here
    private String strUserName;
    private short shrScore;
    private short shrPersonalHighScore;
    
    //constructor class to initialize
    Player(String un, short s, short phs)
    {
        this.strUserName = un;
        this.shrScore = s;
        this.shrPersonalHighScore = phs;
        
    }
    
    //default constructor to populate with default values
    Player()
    {
       this.strUserName = "unknown";
       this.shrScore = 0;
       this.shrPersonalHighScore = 0;
        
    }
    
    //getter method for strUserName to allow access even though private
    public String getUserName()
    {
        return this.strUserName;
    }
    
    public void setUserName(String us)
    {
        this.strUserName = us;
    }
    
    public short getScore()
    {
        return this.shrScore;
    }
    
    public void setScore(short s)
    {
        this.shrScore = s;
    }
    
    public short getPersonalHighScore()
    {
        return this.shrPersonalHighScore;
    }
    
    public void setPersonalHighScore(short phs)
    {
        this.shrPersonalHighScore = phs;
    }
}
