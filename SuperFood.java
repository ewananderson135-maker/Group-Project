
/**
 * Write a description of class SuperFood here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SuperFood extends Item
{
    private byte bytRow;
    private byte bytCol;
   SuperFood(byte x, byte y)
    {
        super((short)10, "Green");
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
