
/**
Coded by Michelle
This class will be subclass item type for a superfood item, thats blue worth 10 points,
it will also have a row and column variables for it's location. This was coded earlier and in retrospect
they could've been instance variables in super class. It will be placed in the 2d array as a superitem thats blue
 * If touched will increase snake size
 * @author (Michelle)
 * @version (5/16/2026)
 */
public class SuperFood extends Item
{
    private byte bytRow;
    private byte bytCol;
   SuperFood(byte x, byte y)
    {
        super((short)10, "Blue");
         this.bytRow = x;
        this.bytCol = y;
    }
    public byte getRow(){
        return bytRow;
    }
    public byte getCol(){
        return bytCol;
    }
}
