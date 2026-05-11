
/**
 * Write a description of class Bomb here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Bomb extends Item
{
    private byte bytRow;
    private byte bytCol;
    Bomb(byte x, byte y)
    {
        super((short)-5, "Red");
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
