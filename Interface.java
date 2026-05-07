
/**
 * Write a description of class Interface here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
public class Interface extends JFrame
{
    final byte GRID_LENGTH = 30;
    final byte GRID_WIDTH = 30;
    private byte[][] aGrid = new byte[GRID_LENGTH][GRID_WIDTH];
    private byte bytRowHead = 15;
    private byte bytColHead = 15;
    private short shrScore = 0;
    private short shrHighScore = 0;
    private boolean bolGameOver = false;
    private Timer tmrMove;

    private ArrayList<Point> aSnake = new ArrayList<Point>();
    private ArrayList<Item> aItems = new ArrayList<Item>();
    private JPanel pnlGame;

    public void Interface()
    {
        createGrid();

        setTitle("Serpent Game");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //addKeyListener(this);

        //tmrMove = new Timer(200, );

        // Starting snake
        aSnake.add(new Point(bytRowHead, bytColHead));
    }
       public void createGrid()
    {
        for(int intRow = 0; intRow < GRID_LENGTH; intRow++)
        {
            for(int intCol = 0; intCol < GRID_WIDTH; intCol++)
            {
                aGrid[intRow][intCol] = 0;
            }
        }
    }
 public void paint(Graphics g)
    {
        super.paint(g);

        int intCellSize = 20;

        // Draw grid
        for(int intRow = 0; intRow < GRID_LENGTH; intRow++)
        {
            for(int intCol = 0; intCol < GRID_WIDTH; intCol++)
            {
                //g.setColor(Color.BLACK);

                g.drawRect(
                    intCol * intCellSize + 50,
                    intRow * intCellSize + 50,
                    intCellSize,
                    intCellSize
                );
            }
        }

        // Draw snake
        //g.setColor(Color.GREEN);

        for(Point p : aSnake)
        {
            g.fillRect(
                p.y * intCellSize + 50,
                p.x * intCellSize + 50,
                intCellSize,
                intCellSize
            );
        }
    }
   public void run(){
       Interface();
   }
}
