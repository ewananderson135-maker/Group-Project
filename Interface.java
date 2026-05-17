
/**
Nathaniel  coded write and reader for highscore
Michelle coded start message method, including upload for player class, also the shop
Ewan coded rest


Everything including GUIs will be running from this class. Creates a 2d array to hold
temp locations of snake and items. Snake will gradually increase speed, controlled by keys\
You lose if you hit the wall or yourself. It keeps track of your personal highscore,
the highscore of all time and your current score. After 30 seconds an event will occur when tons of items spawn in
At the begining of each round a shop will open where you will be able to buy a skin, based
on total amount of score(/money) on account. Has an arraylist of all items and also snake will
be controlled by an array list of points. By eating a superfood or food it will incerase in size
Superfood = blue = 10 points
food  = magenta = 5 points
bomb = red = -5 points
 * @author (Ewan, Michelle, Nathaniel)
 * @version (5/16/2026)
 */
//importing everything necessary for guis, point system, to be able to use keys, arraylist and file IO
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
//needs to implement key Listener and action listnener to be able to move each second and use keys
public class Interface extends JFrame implements KeyListener, ActionListener
{
    //all the instance variables are declared below, they aren't stuck in methods because passing the parameters would be annoying to constance reuse
    //constants for the grid size
    private final byte GRID_LENGTH = 30;
    private final byte GRID_WIDTH = 30;
    //2d array where 0 is empty space, snake is 1
    private byte[][] aGrid = new byte[GRID_LENGTH][GRID_WIDTH];
    // starting position for the head right in middle
    private byte bytRowHead = 15;
    private byte bytColHead = 15;
    //to check if game is over
    private boolean bolGameOver = false;
    //timer to move snake based on time
    private Timer tmrTimer;
    //direction snake travels and will start right
    private String strDirection = "RIGHT";
    //GUIs, the jpanel will devide the screen into a grid with 30 x 30 length
    private JPanel[][] aPanels = new JPanel[GRID_LENGTH][GRID_WIDTH];
    //these are both to make the event only happen once and not repeat.
    private boolean bolEvent = true;
    private boolean bolEvent2 = true;

    //point arraylist for all of snakes body
    private ArrayList<Point> aSnake = new ArrayList<Point>();
    //arraylist for all items of all types
    private ArrayList<Item> aItems = new ArrayList<Item>();
    //to save highscore
    private short shrHighScore;
    //this will be the name of temp player used each round
    private Player p;
    //the start speed of timer
    private int intDelay = 200;
    //next 3 are to keep track of start time for the events to happen, ex increasing speed
    private long lngStartTime;
    private long lngStartTime2;
    private long lngStartTime3;
    //These will be used as GUI tital;s and to devide it by top and grid
    private JPanel pnlTop;
    private JPanel pnlGrid;
    private JLabel lblScore;
    private JLabel lblPersonalHigh;
    private JLabel lblHighScore; 
    
    //choice for the shop
    private byte bytChoice;
    //creates frame for the game to be played on
    public void createFrame()
    {
        //title
        setTitle("Serpent Game");

        //sets frame size
        setSize(1000, 1000);

        //set the layout to borderlayout so we can snap in the points and also the grid into same thing
        
        setLayout(new BorderLayout());
        setVisible(true);
    
        //sets up the top panel with all the score info
        pnlTop = new JPanel();
        pnlTop.setBackground(Color.BLACK);
        lblScore = new JLabel("Score: 0");
        lblPersonalHigh = new JLabel(" Personal High: " + p.getPersonalHighScore());
        lblHighScore = new JLabel(" Global High: "+ shrHighScore);

        //font colors for words
        lblScore.setForeground(Color.WHITE);
        lblPersonalHigh.setForeground(Color.WHITE);
        lblHighScore.setForeground(Color.WHITE);
    
        //adds all to the top part which will be added to layour
        pnlTop.add(lblScore);
        pnlTop.add(lblPersonalHigh);
        pnlTop.add(lblHighScore);
        //adds all the text to the top of the layout for scores
        add(pnlTop, BorderLayout.NORTH);
        
        //jpanel for the grid
        pnlGrid = new JPanel();
        //sets layout of grid to an actual grid
        pnlGrid.setLayout(new GridLayout(GRID_LENGTH,GRID_WIDTH));
        //adds to the layour
        add(pnlGrid, BorderLayout.CENTER);
        //creates the physical tiles
        createGrid();
        //creates base of snake, with 2 points to start
        aSnake.add(new Point(bytRowHead, bytColHead));
        aSnake.add(new Point(15,15));
        //needs to add the key listner into the code
        addKeyListener(this);
        //on the 2d array has a 1 to show current postion of head
        aGrid[bytRowHead][bytColHead] = 1;

        //starts by adding 5 items
        for(byte i = 0; i < 5; i++){
            addItem();
        }
    
        //starts the timer and sets start time for all counters for events
        lngStartTime = System.currentTimeMillis();
        lngStartTime2 = System.currentTimeMillis();
        lngStartTime3 = System.currentTimeMillis();
        tmrTimer = new Timer(intDelay, this);
        tmrTimer.start();
    }
    //cerates actuall grid
    public void createGrid()
    {
        for(int i = 0; i < GRID_LENGTH; i++)
        {
            for(int j = 0; j < GRID_WIDTH; j++)
            {   
                //for the 2darray starts at everything blank by 0
                aGrid[i][j] = 0;
                aPanels[i][j] = new JPanel();
                //cretes the actual display panels and starts them as black
                aPanels[i][j].setBackground(Color.BLACK);
                //adds to display.
                pnlGrid.add(aPanels[i][j]);
            }
        }
    }
    //this method will check for food and move player based on direction.
    public void movePlayer(){
        //checks head location and creates the row and col for it based on it.
        Point pHead = aSnake.get(0);
        byte bytTempRow = (byte)pHead.x;
        byte bytTempCol = (byte)pHead.y;
        boolean bolGrow = false;
        //checks current direction and changes either row or col accordingly
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
        //runs the check boundaries method to see if you either hit yourself or are out of bounds. and will end game and stop timer
        if(checkBoundaries(bytTempRow, bytTempCol) == false){
            tmrTimer.stop();
            endMessage();
            return;
        }
        
        //if doesnt end, it will change head position
        aSnake.add(0,new Point(bytTempRow, bytTempCol));
        //and make it on 2d array 1 to indicated snake
        aGrid[bytTempRow][bytTempCol] = 1;
        //checks if snake is touching item
        for(byte i = 0; i < aItems.size();i++){
            //sepperates for each subclass
            if(aItems.get(i) instanceof Bomb){
                //checks if snakes touching it
                if(((Bomb)(aItems.get(i))).getRow() == bytTempRow && ((Bomb)(aItems.get(i))).getCol() == bytTempCol ){
                    //sets the position to 0, adds points then removes item and replaces with new
                    aGrid[bytTempRow][bytTempCol] = 0; 
                    p.setScore(aItems.get(i).getPoints());
                    aItems.remove(i);
                    addItem();

                }
                
            }else if(aItems.get(i) instanceof Food){
                                //checks if snakes touching it

                if(((Food)(aItems.get(i))).getRow() == bytTempRow && ((Food)(aItems.get(i))).getCol() == bytTempCol ){
                                        //sets the position to 0, adds points then removes item and replaces with new
                    //sets bolgrow to true also so length of snake increases
                    bolGrow = true;
                    aGrid[bytTempRow][bytTempCol] = 0;
                    p.setScore(aItems.get(i).getPoints());
                    aItems.remove(i);
                    addItem();

                }
            }else{
                                //checks if snakes touching it

                if(((SuperFood)(aItems.get(i))).getRow() == bytTempRow && ((SuperFood)(aItems.get(i))).getCol() == bytTempCol ){
                      //sets the position to 0, adds points then removes item and replaces with new
                    //sets bolgrow to true also so length of snake increases
                    bolGrow = true;
                    aGrid[bytTempRow][bytTempCol] = 0;
                    p.setScore(aItems.get(i).getPoints());

                    aItems.remove(i);
                    addItem();

                }
            }

        }
        //if the bolgrow is false, meaning not touching food or superfood, it will the end part of snake.
        if(!bolGrow)
        {
            //removes last part of snake
            aSnake.remove(aSnake.size() - 1);

        }
        //updates visiual method for GUI
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

            bytChoice = Byte.parseByte(JOptionPane.showInputDialog("\n1.Green(0$)\n2.Cyan(100$) \n3. Orange(300$) \n4.Yellow(450$) \n5.Rainbow(600$)\n\nYou currently have: " + p.getTotalScore() + "$"));
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

        }while(bolShop);
        return 0;


    }

    
} 


