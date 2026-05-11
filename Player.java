
/**
 * Nathaniel coded this portion of the code
 * 
 *  To store the user’s name that will be connected to an account with FileIO. 
    Stores the score of the user that round and saves it into the system by FileIO.
    Stores the highest score obtained and saves it into the system by FileIO. If the user’s score that round is higher than the high score, then update the variable to the new score.

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
    
    //Saves and stores the player’s info when opening an existing account. It is already populated with the user’s name, score, and personal high score and can be further updated.
    Player(String un, short s, short phs)
    {
        this.strUserName = un;
        this.shrScore = s;
        this.shrPersonalHighScore = phs;
        
    }
    
    //When a new account is created, populate it with the default information
    Player()
    {
       this.strUserName = "unknown";
       this.shrScore = 0;
       this.shrPersonalHighScore = 0;
        
    }
    
    //getter method for strUserName to get and allow access since private
    public String getUserName()
    {
        return this.strUserName;
    }
    
    //setter method for strUserName to modify and allow access since private
    public void setUserName(String us)
    {
        this.strUserName = us;
    }
    
    //getter method for shrScore to get and allow access since private
    public short getScore()
    {
        return this.shrScore;
    }
    
    //setter method for shrScore to modify and allow access since private
    public void setScore(short s)
    {
        this.shrScore = s;
    }
    
    //getter method for shrPersonalHighScore to get and allow access since private
    public short getPersonalHighScore()
    {
        return this.shrPersonalHighScore;
    }
    
    //setter method for shrPersonalHighScore to modify and allow since though private
    public void setPersonalHighScore(short phs)
    {
        this.shrPersonalHighScore = phs;
    }
}
