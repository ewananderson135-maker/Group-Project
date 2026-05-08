
/**
 * Write a description of class Interface here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.Point;
import java.awt.event.*;
import java.awt.*;
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
    
    public void createFrame()
    {
         JFrame Frame = new JFrame("Serpent Game");

        //sets frame size
        Frame.setSize(1000, 1000);
    
        //set the layout to GridLayout so the tiles will easily snap in and be the same size
        Frame.setLayout(new GridLayout(GRID_LENGTH, GRID_WIDTH));
        setVisible(true);

        createGrid();
        //addKeyListener(this);

        //tmrMove = new Timer(200, );

        // Starting snake
        aSnake.add(new Point(bytRowHead, bytColHead));
    }
       public void createGrid()
    {
        for(int i = 0; i < GRID_LENGTH; i++)
        {
            for(int j = 0; j < GRID_WIDTH; j++)
            {
                aGrid[i][j] = 0;
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
                g.setColor(Color.BLACK);

                g.drawRect(
                    intCol * intCellSize + 50,
                    intRow * intCellSize + 50,
                    intCellSize,
                    intCellSize
                );
            }
        }

        // Draw snake
        g.setColor(Color.GREEN);

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
       startMessage();
       createFrame();
   }
   public void startMessage(){
       JOptionPane.showMessageDialog(null, "Hey and welcome to serpent game!\nTo play use the up,down,left and right keys to change the snakes direction.\nCollect the yellow food for 3 points, the green superfood for 10 points, but avoid the red bombs or you'll lose points!\nMake sure you avoid hitting yourself or the wall or you'll lose!\nHave Fun");
   }
   public void endMessage(){
       setVisible(false);
       JOptionPane.showMessageDialog(null, "Great Job!");
       JOptionPane.showMessageDialog(null, "Your high score was:"  );
       
   }
   
}
