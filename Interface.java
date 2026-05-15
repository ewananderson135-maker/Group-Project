
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
    private boolean bolEvent = true;
    private boolean bolEvent2 = true;

    private ArrayList<Point> aSnake = new ArrayList<Point>();
    private ArrayList<Item> aItems = new ArrayList<Item>();
    private short shrHighScore;
    private Player p;
    private int intDelay = 200;
    private long lngStartTime;
    private long lngStartTime2;
    private long lngStartTime3;
    private JPanel pnlTop;
    private JPanel pnlGrid;
    private JLabel lblScore;
    private JLabel lblPersonalHigh;
    private JLabel lblHighScore; 
    Point pTail;
    private byte bytChoice;

    public void createFrame()
    {

        setTitle("Serpent Game");

        //sets frame size
        setSize(1000, 1000);

        //set the layout to GridLayout so the tiles will easily snap in and be the same size
        setLayout(new BorderLayout());
        setVisible(true);

        pnlTop = new JPanel();
        pnlTop.setBackground(Color.BLACK);
        lblScore = new JLabel("Score: 0");
        lblPersonalHigh = new JLabel(" Personal High: " + p.getPersonalHighScore());
        lblHighScore = new JLabel(" Global High: "+ shrHighScore);

        lblScore.setForeground(Color.WHITE);

        lblPersonalHigh.setForeground(Color.WHITE);

        lblHighScore.setForeground(Color.WHITE);

        pnlTop.add(lblScore);

        pnlTop.add(lblPersonalHigh);

        pnlTop.add(lblHighScore);

        add(pnlTop, BorderLayout.NORTH);

        pnlGrid = new JPanel();

        pnlGrid.setLayout(new GridLayout(GRID_LENGTH,GRID_WIDTH));

        add(pnlGrid, BorderLayout.CENTER);
        createGrid();

        aSnake.add(new Point(bytRowHead, bytColHead));
        aSnake.add(new Point(15,15));
        addKeyListener(this);

        aGrid[bytRowHead][bytColHead] = 1;

        for(byte i = 0; i < 5; i++){
            addItem();
        }
        // when it starts running it will actually intialize timer

        lngStartTime = System.currentTimeMillis();
        lngStartTime2 = System.currentTimeMillis();
        lngStartTime3 = System.currentTimeMillis();
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
                pnlGrid.add(aPanels[i][j]);
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
                    p.setScore(aItems.get(i).getPoints());
                    aItems.remove(i);
                    addItem();

                }
            }else if(aItems.get(i) instanceof Food){
                if(((Food)(aItems.get(i))).getRow() == bytTempRow && ((Food)(aItems.get(i))).getCol() == bytTempCol ){
                    bolGrow = true;
                    aGrid[bytTempRow][bytTempCol] = 0;
                    p.setScore(aItems.get(i).getPoints());
                    aItems.remove(i);
                    addItem();

                }
            }else{
                if(((SuperFood)(aItems.get(i))).getRow() == bytTempRow && ((SuperFood)(aItems.get(i))).getCol() == bytTempCol ){
                    bolGrow = true;
                    aGrid[bytTempRow][bytTempCol] = 0;
                    p.setScore(aItems.get(i).getPoints());

                    aItems.remove(i);
                    addItem();

                }
            }

        }
        if(!bolGrow)
        {
            pTail = aSnake.remove(aSnake.size() - 1);

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
        long lngCurrentTime2 = System.currentTimeMillis();
        long lngCurrentTime3 = System.currentTimeMillis();

        if(lngCurrentTime2 - lngStartTime2 >= 30000 && bolEvent == true){
            bolEvent = false;
            for(byte i = 0; i < 20; i++){
                addItem();
            }
        }
        if(lngCurrentTime3 - lngStartTime3 >= 40000 && bolEvent2 == true){
            bolEvent2 = false;
            for(int i = aItems.size() - 1; i > 4 ; i--){
                if(aItems.get(i) instanceof Bomb){
                    aGrid[((Bomb)aItems.get(i)).getRow()][((Bomb)aItems.get(i)).getCol()] = 0;

                }else if(aItems.get(i) instanceof Food){
                    aGrid[((Food)aItems.get(i)).getRow()][((Food)aItems.get(i)).getCol()] = 0;

                }else{
                    aGrid[((SuperFood)aItems.get(i)).getRow()][((SuperFood)aItems.get(i)).getCol()] = 0;

                }

                aItems.remove(i);

            }
        }
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
        p.setTotalScore(shop(p.getTotalScore()));

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
            if(bytChoice == 5){
            byte bytRandom =  (byte)(Math.random() * 6);

            pTemp = aSnake.get(i);
            if(bytRandom == 0){
                aPanels[pTemp.x][pTemp.y].setBackground(Color.GREEN);
            }else if(bytRandom == 1){
                aPanels[pTemp.x][pTemp.y].setBackground(Color.ORANGE);

            }
            else if(bytRandom == 2){
                aPanels[pTemp.x][pTemp.y].setBackground(Color.YELLOW);

            }else if(bytRandom == 3){
                aPanels[pTemp.x][pTemp.y].setBackground(Color.CYAN);

            }

            else if(bytRandom == 5){
                aPanels[pTemp.x][pTemp.y].setBackground(Color.LIGHT_GRAY);
            }
            else if(bytRandom == 4){
                aPanels[pTemp.x][pTemp.y].setBackground(Color.WHITE);

            }
        }
            else if(bytChoice == 1){
                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.GREEN);
            }
            else if(bytChoice == 2){
                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.CYAN);
            }
            else if(bytChoice == 3){
                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.ORANGE);
            }
            else{
                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.YELLOW);
            }

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
        lblScore.setText("Score: " + p.getScore());
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

    public  void startMessage(){
        byte bytAccount = 0;
        JOptionPane.showMessageDialog(null, "Hey and welcome to serpent game!\nTo play use the up,down,left and right keys to change the snakes direction.\nCollect the yellow food for 3 points, the green superfood for 10 points, but avoid the red bombs or you'll lose points!\nMake sure you avoid hitting yourself or the wall or you'll lose!\nHave Fun");
        bytAccount = Byte.parseByte(JOptionPane.showInputDialog("Do you have an account? (input: 1.Yes or 2.No)"));
        boolean bolAccount = true;

        String strName =  " ";

        do{
            try{

                if (bytAccount < 1 || bytAccount > 2)
                {
                    bytAccount = Byte.parseByte(JOptionPane.showInputDialog("input: 1.Yes or 2.No"));
                    bolAccount = true;  
                }
                else if(bytAccount == 1)
                {
                    strName =  JOptionPane.showInputDialog("UserName: ");

                    if(new File(strName +".txt").exists() == true)
                    {

                        try{
                            BufferedReader in;
                            in = new BufferedReader(new FileReader(strName+ ".txt"));
                            String strUserName = in.readLine();

                            short shrPersonalHighScore = Short.parseShort(in.readLine());

                            short shrTotalScore = Short.parseShort(in.readLine());
                            p = new Player(strName,shrPersonalHighScore, shrTotalScore);
                        }
                        catch(FileNotFoundException e)
                        {
                            System.out.println("Error: Cannot open file for reading");
                        }

                        catch(IOException e)
                        {
                            System.out.println("Error: Cannot read from file");
                        }

                    }
                    else
                    {

                        JOptionPane.showMessageDialog(null, "Your account does not exist: creating new account");
                        File file = new File (strName + ".txt");

                        p = new Player(strName,(short)0, (short) 0);

                    }
                    bolAccount = false;    
                }
                else if(bytAccount == 2)

                {
                    strName =  JOptionPane.showInputDialog("UserName: ");

                    File file = new File (strName + ".txt");

                    p = new Player(strName,(short)0, (short)0);

                    bolAccount = false;
                }

            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Wrong Input.");
            }

        }while(bolAccount);
    }

    public void endMessage(){
        p.setTotalScore(p.getScore());
        if(p.getPersonalHighScore() <= p.getScore()){
            p.setPersonalHighScore(p.getScore());
        }
        writeHighScore();
        writePlayer();
        setVisible(false);
        JOptionPane.showMessageDialog(null, "Great Job!");
        JOptionPane.showMessageDialog(null, "Your score was: " + p.getScore() );
        return;

    }

    public void writePlayer(){
        try{
            PrintWriter out = new PrintWriter(new FileWriter(p.getUserName() + ".txt"));
            out.println(p.getUserName());
            out.println(String.valueOf(p.getPersonalHighScore()));
            out.println(String.valueOf(p.getTotalScore()));
            out.close();
        }catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    public void writeHighScore(){
        if (p.getPersonalHighScore() >= shrHighScore)
        {
            try
            {
                shrHighScore = p.getPersonalHighScore();
                PrintWriter out = new PrintWriter(new FileWriter("HighScore.txt"));
                out.write(String.valueOf(shrHighScore));

                out.close();

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

                BufferedReader br = new BufferedReader(new FileReader("HighScore.txt"));
                shrHighScore = Short.parseShort(br.readLine());
                br.close();    
            } catch(IOException e)
            {
                e.printStackTrace();
            }

        }else{
            shrHighScore = 0;

        }

    }

    public short shop(short shrMoney)
    {
        boolean bolShop = true;
        JOptionPane.showMessageDialog(null, "Welcome to the shop!");

        do
        {

            bytChoice = Byte.parseByte(JOptionPane.showInputDialog("\n1.Exit \n2.Green(0$) /n3.Purple(100$) \n4. Orange(300$) \n5.Yellow(450$) \n6.Rainbow(600$)\n\nYou currently have: " + p.getTotalScore() + "$"));
            if(bytChoice == 1)
            {
                bolShop = false;

            bytChoice = Byte.parseByte(JOptionPane.showInputDialog("\n1.Green(0$) /n2.Cyan(100$) \n3. Orange(300$) \n4.Yellow(450$) \n5.Rainbow(600$)\n\nYou currently have: " + p.getTotalScore() + "$"));
            if(bytChoice == 1)
            {

                return 0;
            }
            else if(bytChoice == 2)
            {
                if(shrMoney>=100)
                {
                    return -100;
                    
                }
                else
                {   
                    JOptionPane.showMessageDialog(null, "Not enough money!");

                    bolShop = true;

                }
            }
            else if(bytChoice == 3)
            {
                if(shrMoney>=300)
                {
                    return -300;

                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Not enough money!");

                }            
            }else if(bytChoice == 4)
            {
                if(shrMoney>=450)
                {
                    return -450;

                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Not enough money!");

                }

            }
            else if(bytChoice == 5)
            {
                if(shrMoney>=600)
                {
                    return -600;
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Not enough money!");

                }

            }
            else{
                   JOptionPane.showMessageDialog(null, "Wrong input!");

            }

        }
    }while(bolShop);
        return 0;
    }

    
} 


