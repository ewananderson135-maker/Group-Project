
/**
 * Coded by Michelle
 * 
 *This will be a subclass of item, stored in the 2d array in interface class. 
 *It will be used to track amount of points to update on, and track it's location
 *and if touched using the coordinates instance variables.
 *If touched will increase snake size
 * @author (Michelle)
 * @version (5/16/2026)
 */
//extends superclass
public class Food extends Item
{
    //instance variables of coordinates
    private byte bytRow;
    private byte bytCol;
    //constructor to intialize instance variables
     Food(byte x, byte y)
    {
        //calls superclass constructor
        super((short)3, "Magenta");
         this.bytRow = x;
        this.bytCol = y;
    }
    //getters for row and columns to have the location at all times
    public byte getRow(){
        return bytRow;
    }
    public byte getCol(){
        return bytCol;
    }
}
