
/**
 * Write a description of class Food here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Food extends Item
{
    private byte bytRow;
    private byte bytCol;
     Food(byte x, byte y)
    {
        super((short)3, "Yellow");
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
