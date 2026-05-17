
/**
 *Coded by Michelle
 *
 *This is the superclass for items, where it will store it's color and the amount of points it's worth
 *The color was never actually really used, it was assumed to be used, but never was.
 *This will controll all types of items, held in an array of Items.
 * @author (Michelle)
 * @version (5/16/2026)
 */
public class Item
{
    //instance variables for the clas
    private short shrPoints;
    private String strColour;

    //consructor for the color and amount of points
    Item(short p, String c)
    {
        this.shrPoints = p;
        this.strColour = c;
    }

    //getter for amount of points
    public short getPoints()
    {
        return this.shrPoints;

    }
    //setter for points
    public void setPoints(short p)
    {
        this.shrPoints = p;
    }
    //getter for the colour
    public String getColour()
    {
        return this.strColour;
    }
    //setter for the colour
    public void setColour(short c)
    {
        this.shrPoints = c;
    }
}
