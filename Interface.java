
/**
 * Write a description of class Interface here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
public class Interface extends JFrame implements KeyListener, ActionListener
{
    final byte GRID_LENGTH = 30;
    final byte GRID_WIDTH = 30;
    private byte[][] aGrid = new byte[GRID_LENGTH][GRID_WIDTH];
    private byte bytRowHead = 15;
    private byte bytColHead = 15;
    private short shrScore = 0;
    private boolean bolGameOver = false;
    private Timer tmrTimer;
    private String strDirection = "RIGHT";
    private JPanel[][] aPanels =
        new JPanel[GRID_LENGTH][GRID_WIDTH];

    private Player p;



    private ArrayList<Point> aSnake = new ArrayList<Point>();
    private ArrayList<Item> aItems = new ArrayList<Item>();
    private short shrHighScore;

    private int intDelay = 200;
    private long lngStartTime;

    public void createFrame()
    {

        setTitle("Serpent Game");

        //sets frame size
        setSize(1000, 1000);

        //set the layout to GridLayout so the tiles will easily snap in and be the same size
        setLayout(new GridLayout(GRID_LENGTH, GRID_WIDTH));
        setVisible(true);

        createGrid();

        aSnake.add(new Point(bytRowHead, bytColHead));
        addKeyListener(this);
        aSnake.add(new Point(15, 15));

        aGrid[bytRowHead][bytColHead] = 1;
    
        for(byte i = 0; i < 5; i++){
            addItem();
        }
        // when it starts running it will actually intialize timer

        lngStartTime = System.currentTimeMillis();
        tmrTimer = new Timer(intDelay, this);
        tmrTimer.start();
    }


      


    public void createGrid()
    {
        for(int i = 0; i < GRID_LENGTH; i++)
        {
            for(int j = 0; j < GRID_WIDTH; j++)
            {
                aGrid[i][j] = 0;
                aPanels[i][j] = new JPanel();

                aPanels[i][j].setBackground(Color.BLACK);
                add(aPanels[i][j]);
            }
        }
    }

    public void movePlayer(){
        Point pHead = aSnake.get(0);
        byte bytTempRow = (byte)pHead.x;
        byte bytTempCol = (byte)pHead.y;
        boolean bolGrow = false;

        if(strDirection.equals("UP"))
        {
            bytTempRow--;
        }

        else if(strDirection.equals("DOWN"))
        {
            bytTempRow++;
        }

        else if(strDirection.equals("LEFT"))
        {
            bytTempCol--;
        }

        else if(strDirection.equals("RIGHT"))
        {
            bytTempCol++;
        }

        if(checkBoundaries(bytTempRow, bytTempCol) == false){
            tmrTimer.stop();
            endMessage();
            return;
        }

        aSnake.add(0,new Point(bytTempRow, bytTempCol));
        aGrid[bytTempRow][bytTempCol] = 1;
        for(byte i = 0; i < aItems.size();i++){
            if(aItems.get(i) instanceof Bomb){
                if(((Bomb)(aItems.get(i))).getRow() == bytTempRow && ((Bomb)(aItems.get(i))).getCol() == bytTempCol ){
                    aGrid[bytTempRow][bytTempCol] = 0;
                    aItems.remove(i);
                    addItem();

                }
            }else if(aItems.get(i) instanceof Food){
                if(((Food)(aItems.get(i))).getRow() == bytTempRow && ((Food)(aItems.get(i))).getCol() == bytTempCol ){
                    bolGrow = true;
                    aGrid[bytTempRow][bytTempCol] = 0;
                    aItems.remove(i);
                    addItem();

                }
            }else{
                if(((SuperFood)(aItems.get(i))).getRow() == bytTempRow && ((SuperFood)(aItems.get(i))).getCol() == bytTempCol ){
                    bolGrow = true;
                    aGrid[bytTempRow][bytTempCol] = 0;
                    aItems.remove(i);
                    addItem();

                }

            }

        }

        if(!bolGrow)
        {
            Point pTail = aSnake.remove(aSnake.size() - 1);

            aGrid[pTail.x][pTail.y] = 0;
        }

        updateBoard();
    }

    public boolean checkBoundaries(byte bytTempRow, byte bytTempColumn){

        for(byte i = 0; i< aSnake.size(); i++)
        {
            byte bytRow, bytCol;
            bytRow = (byte)aSnake.get(i).x;
            bytCol = (byte)aSnake.get(i).y;
            if(bytTempRow == bytRow && bytTempColumn == bytCol)
            {
                return false;
            }
        }

        if(bytTempRow>GRID_LENGTH - 1 || bytTempRow < 0)
        {
            return false;
        }
        else if(bytTempColumn>GRID_WIDTH - 1|| bytTempColumn <0)
        {
            return false;
        }

        else
        {
            return true;
        }

    }

    public void keyPressed(KeyEvent e)
    {
        int intKey = e.getKeyCode();

        if(intKey == KeyEvent.VK_UP &&
        !strDirection.equals("DOWN"))
        {
            strDirection = "UP";
        }

        else if(intKey == KeyEvent.VK_DOWN &&
        !strDirection.equals("UP"))
        {
            strDirection = "DOWN";
        }

        else if(intKey == KeyEvent.VK_LEFT &&
        !strDirection.equals("RIGHT"))
        {
            strDirection = "LEFT";
        }

        else if(intKey == KeyEvent.VK_RIGHT &&
        !strDirection.equals("LEFT"))
        {
            strDirection = "RIGHT";
        }

        // this is pause the game if space pressed
        else if(intKey == KeyEvent.VK_SPACE)
        {
            if(tmrTimer.isRunning())
            {
                tmrTimer.stop();
            }

            else
            {
                tmrTimer.start();
            }
        }
    }
    //needs this for keylistner to acctually work, but it does nothing
    public void keyReleased(KeyEvent e)
    {
    }
    //needs this for keylistner to work, but does nothing when happens.
    public void keyTyped(KeyEvent e)
    {
    }
    //runs this every second and checks if games over or not, where actions happen from

    public void actionPerformed(ActionEvent e)
    {
        long lngCurrentTime = System.currentTimeMillis();

        if(lngCurrentTime - lngStartTime >= 10000)
        {  
            // this stops timer from becomiung way to fast
            if(intDelay > 50)
            {
                intDelay -= 20;
            }

            tmrTimer.setDelay(intDelay);

            lngStartTime = lngCurrentTime;
        }
        movePlayer();
    }

    public void run(){
        uploadHighScore();
        startMessage();

        



        createFrame();
        updateBoard();
    }

    public void updateBoard(){
        Point pTemp;
        for(byte i = 0; i < GRID_LENGTH; i++){
            for(byte j = 0; j < GRID_WIDTH; j++){
                aPanels[i][j].setBackground(Color.BLACK);
            }
        }

        for(byte i = 0; i < aSnake.size(); i++ ){
            pTemp = aSnake.get(i);
            aPanels[pTemp.x][pTemp.y].setBackground(Color.GREEN);
        }
        for(byte i = 0; i < aItems.size(); i++){
            if(aItems.get(i) instanceof Bomb){
                aPanels[((Bomb)(aItems.get(i))).getRow()][((Bomb)(aItems.get(i))).getCol()].setBackground(Color.RED); 
            }else if(aItems.get(i) instanceof Food){
                aPanels[((Food)(aItems.get(i))).getRow()][((Food)(aItems.get(i))).getCol()].setBackground(Color.MAGENTA); 

            }else{
                aPanels[((SuperFood)(aItems.get(i))).getRow()][((SuperFood)(aItems.get(i))).getCol()].setBackground(Color.BLUE); 

            }
        }
        repaint();
        revalidate();

    }

    public void addItem(){
        byte bytRowRandom = (byte)(Math.random() * GRID_LENGTH);
        byte bytColRandom = (byte) (Math.random() * GRID_WIDTH);
        byte bytItemType;
        if(aGrid[bytRowRandom][bytColRandom] != 0){
            addItem();
        }

        bytItemType = (byte)(Math.random() * 3);

        if(bytItemType == 0){
            aItems.add(new Bomb(bytRowRandom, bytColRandom));
            aGrid[bytRowRandom][bytColRandom] = 2;
        }
        else if(bytItemType == 1){
            aItems.add(new Food(bytRowRandom, bytColRandom));
            aGrid[bytRowRandom][bytColRandom] = 2;
        }else{
            aItems.add(new SuperFood(bytRowRandom, bytColRandom));
            aGrid[bytRowRandom][bytColRandom] = 2;
        }
    }

    public  String startMessage(){
        byte bytAccount = 0;
        JOptionPane.showMessageDialog(null, "Hey and welcome to serpent game!\nTo play use the up,down,left and right keys to change the snakes direction.\nCollect the yellow food for 3 points, the green superfood for 10 points, but avoid the red bombs or you'll lose points!\nMake sure you avoid hitting yourself or the wall or you'll lose!\nHave Fun");
        bytAccount = Byte.parseByte(JOptionPane.showInputDialog("Do you have an account? (input: 1.Yes or 2.No)"));
        boolean bolAccount = true;
        String strName =  " ";
        do{
            try{

                while (bytAccount!=1 && bytAccount != 2)
                {
                    bytAccount = Byte.parseByte(JOptionPane.showInputDialog("Wrong Input: enter 1 or 2"));

                }
                
                strName =  JOptionPane.showInputDialog("UserName: ");
                if(bytAccount == 1)
                {
                    if(new File(strName +".txt").exists() == true)
                    {
                        BufferedReader in;
                        in = new BufferedReader(new FileReader(strName+ ".txt"));
                        String strUserName = in.readLine();

                        
                        byte bytPersonalHighScore = Byte.parseByte(in.readLine());
                         p = new Player(strName,bytPersonalHighScore);

                        short shrPersonalHighScore = Byte.parseByte(in.readLine());
                        Player p = new Player(strName,shrPersonalHighScore);

                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Your account does not exist: creating new account");
                        File file = new File (strName + ".txt");

                         p = new Player(strName,(short)0);

                        Player p = new Player(strName,(short)0);

                        //return strName;
                    }
                    bolAccount = false;    
                }
                else if(bytAccount == 2)


                    {
                        File file = new File (strName + ".txt");
                         p = new Player(strName,(short)0);
                        //return strName;
                    }

                {
                    File file = new File (strName + ".txt");
                    Player p = new Player(strName,(short)0);
                    //return strName;

                    bolAccount = false;
                }
                
                    
                
                
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Wrong Input.");
            }

        }while(bolAccount);
        return strName;
    }

    public void endMessage(){
        setVisible(false);
        JOptionPane.showMessageDialog(null, "Great Job!");
        JOptionPane.showMessageDialog(null, "Your high score was:"  );
        return;

    }

    
    
    
    public void writeHighScore(){
        //shrHighScore
        if (p.getPersonalHighScore() < shrHighScore)
        {
            try
            {
                BufferedWriter br = new BufferedWriter(new FileWriter("Highscore.txt"));
                br.write(p.getPersonalHighScore());
                
                br.close();
                
            } 
            catch (IOException e)
            {
                e.printStackTrace();
                
            }
            
        }
        
        
    }
    
    
    
    
    
    
    
    


    


    public void uploadHighScore()
    {
        if(new File("HighScore.txt").exists() == true){
            try
            {
            BufferedReader br = new BufferedReader(new FileReader("Highscore.txt"));
            
            String line;
            
            System.out.println(line);
          
            shrHighScore = Short.parseShort(br.readLine());
            
            br.close();    
        } 
        catch(IOException e)
            {
                e.printStackTrace();
            }
        }else{
            shrHighScore = 0;

        }

        
    
    }

} 
