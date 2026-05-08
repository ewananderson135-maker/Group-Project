
/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    private short shrPoints;
    private String strColour;

    Item(short p, String c)
    {
        this.shrPoints = p;
        this.strColour = c;
    }

    public short getPoints()
    {
        return this.shrPoints;

    }

    public void setPoints(short p)
    {
        this.shrPoints = p;
    }

    public String getColour()
    {
        return this.strColour;
    }

    public void setColour(short c)
    {
        this.shrPoints = c;
    }
}
