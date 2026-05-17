
/**
Coded by Michelle
Subclass of item and if touched will subtract points by 5. Also, the bomb will not
increase the size of the snake. It will be red. This class will be a part of the
arraylist of items, and will have coordinates with getters.
 * @author (Michelle)
 * @version (5/16/2026)
 */
//extends superclass of item
public class Bomb extends Item
{
    //instance variables for coordinates
    private byte bytRow;
    private byte bytCol;
    
    //constructor for bomb
    Bomb(byte x, byte y)
    {
        //calls item constructor
        super((short)-5, "Red");
        this.bytRow = x;
        this.bytCol = y;
    }
    //getters for the coordinates
    
    public byte getRow(){
        return bytRow;
    }
    public byte getCol(){
        return bytCol;
    }
}
