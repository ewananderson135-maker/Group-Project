
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
    
    //getter method for strUserName to allow access since private
    public String getUserName()
    {
        return this.strUserName;
    }
    
    //setter method for strUserName to set and allow access since private
    public void setUserName(String us)
    {
        this.strUserName = us;
    }
    
    //getter method for shrScore to allow access since private
    public short getScore()
    {
        return this.shrScore;
    }
    
    //setter method for shrScore to set and allow access since private
    public void setScore(short s)
    {
        this.shrScore = s;
    }
    
    //getter method for shrPersonalHighScore to allow access since private
    public short getPersonalHighScore()
    {
        return this.shrPersonalHighScore;
    }
    
    //setter method for shrPersonalHighScore to set and allow since though private
    public void setPersonalHighScore(short phs)
    {
        this.shrPersonalHighScore = phs;
    }
}
