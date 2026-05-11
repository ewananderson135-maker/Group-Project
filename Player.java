
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class Player implements java.io.Serializable
{
    private String strUserName;
    private short shrScore;
    private short shrPersonalHighScore;
    
    Player(String un, short s, short phs)
    {
        this.strUserName = un;
        this.shrScore = s;
        this.shrPersonalHighScore = phs;
        
    }
    
    Player()
    {
       this.strUserName = "unknown";
       this.shrScore = 0;
       this.shrPersonalHighScore = 0;
        
    }
}
