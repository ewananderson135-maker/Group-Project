
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

    
    //checks boundaries each time player moves and returns true or false based on if the snake dies
    public boolean checkBoundaries(byte bytTempRow, byte bytTempColumn){
        //takes in current head position
        
        //checks if he's currently touching any part of himself
        for(byte i = 0; i< aSnake.size(); i++)
        {
            byte bytRow, bytCol;
            bytRow = (byte)aSnake.get(i).x;
            bytCol = (byte)aSnake.get(i).y;
            //if he is then return false to end game
            if(bytTempRow == bytRow && bytTempColumn == bytCol)
            {
                return false;
            }
        }
        //these check if the snake is out of the bounds, either greater than row or col
        if(bytTempRow>GRID_LENGTH - 1 || bytTempRow < 0)
        {
            return false;
        }
        else if(bytTempColumn>GRID_WIDTH - 1|| bytTempColumn <0)
        {
            return false;
        }

        //if it's in all boundaries returns true to continue game
        else
        {
            return true;
        }

    }
    
    //checks if any input is pressed form keys
    public void keyPressed(KeyEvent e)
    {   
        //holds temp number for key
        int intKey = e.getKeyCode();
        
        //makes sure if you press up direction is not down, then sets direction to up
        if(intKey == KeyEvent.VK_UP &&
        !strDirection.equals("DOWN"))
        {
            strDirection = "UP";
        }
            //makes sure if you press down direction is not up, then sets direction to down

        else if(intKey == KeyEvent.VK_DOWN &&
        !strDirection.equals("UP"))
        {
            strDirection = "DOWN";
        }
        //checks if direction is right, and if left was pressed then set to left
        else if(intKey == KeyEvent.VK_LEFT &&
        !strDirection.equals("RIGHT"))
        {
            strDirection = "LEFT";
        }
        //then checks if direction is left and if right pressed set to right
        else if(intKey == KeyEvent.VK_RIGHT &&
        !strDirection.equals("LEFT"))
        {
            strDirection = "RIGHT";
        }

        // this is pause the game if space pressed
        //checks if space is ever oressed
        else if(intKey == KeyEvent.VK_SPACE)
        {   
            //if the timer is running it will stop it
            if(tmrTimer.isRunning())
            {
                tmrTimer.stop();
            }
            //otherwirse it will unpause and continue program
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
    //runs dependant on timer
    public void actionPerformed(ActionEvent e)
    {   
        //these keep track of current time for each event
        long lngCurrentTime = System.currentTimeMillis();
        long lngCurrentTime2 = System.currentTimeMillis();
        long lngCurrentTime3 = System.currentTimeMillis();
        
        //after 30 second, it will run this to add 20 random items event
        if(lngCurrentTime2 - lngStartTime2 >= 30000 && bolEvent == true){
            bolEvent = false;
            for(byte i = 0; i < 20; i++){
                addItem();
            }
        }
        //15 seconds after the event, it will remove all the items till 5 are left
        if(lngCurrentTime3 - lngStartTime3 >= 45000 && bolEvent2 == true){
            bolEvent2 = false;
            for(int i = aItems.size() - 1; i > 4 ; i--){
                //checks what type it is and sets 2d array to 0 so items can spawn their
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
        //every 10 seconds, incrase the speed
        if(lngCurrentTime - lngStartTime >= 10000)
        {  
            // this stops timer from becomiung way to fast
            if(intDelay > 50)
            {
                intDelay -= 20;
            }
            // continue to increase speed
            tmrTimer.setDelay(intDelay);
            //restarts the start time
            lngStartTime = lngCurrentTime;
        }
        //every second it will run move player method
        movePlayer();
    }
    //where all the code starts and runs from
    public void run(){
        //uploads highscore
        uploadHighScore();
        //starts with message and creates player
        startMessage();
        //total score for player
        p.setTotalScore(shop(p.getTotalScore()));

        //creates the base frame, then does the first board update
        createFrame();
        updateBoard();
    }

    public void updateBoard(){
        Point pTemp;
        //starts by just making everything black, and will work around that
        for(byte i = 0; i < GRID_LENGTH; i++){
            for(byte j = 0; j < GRID_WIDTH; j++){
                aPanels[i][j].setBackground(Color.BLACK);
            }
        }
        //checks which skin the player is using, and will loop through snake body and color accordingly
        for(byte i = 0; i < aSnake.size(); i++ ){
            //for rainbow it will use random to color each randomly
            if(bytChoice == 5){
            byte bytRandom =  (byte)(Math.random() * 6);

            pTemp = aSnake.get(i);
            if(bytRandom == 0){
                                //gets coordinates using the temp point

                aPanels[pTemp.x][pTemp.y].setBackground(Color.GREEN);
            }else if(bytRandom == 1){
                                //gets coordinates using the temp point

                aPanels[pTemp.x][pTemp.y].setBackground(Color.ORANGE);

            }
            else if(bytRandom == 2){
                                //gets coordinates using the temp point

                aPanels[pTemp.x][pTemp.y].setBackground(Color.YELLOW);

            }else if(bytRandom == 3){
                                //gets coordinates using the temp point

                aPanels[pTemp.x][pTemp.y].setBackground(Color.CYAN);

            }

            else if(bytRandom == 5){
                                //gets coordinates using the temp point

                aPanels[pTemp.x][pTemp.y].setBackground(Color.LIGHT_GRAY);
            }
            else if(bytRandom == 4){
                                //gets coordinates using the temp point

                aPanels[pTemp.x][pTemp.y].setBackground(Color.WHITE);

            }


        }
        //for green
            else if(bytChoice == 1){
                //gets coordinates using the temp point
                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.GREEN);
            }
            //for cyan
            else if(bytChoice == 2){
                                //gets coordinates using the temp point

                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.CYAN);
            }
            else if(bytChoice == 3){
                                //gets coordinates using the temp point

                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.ORANGE);
            }
            else{
                                //gets coordinates using the temp point

                pTemp = aSnake.get(i);
                aPanels[pTemp.x][pTemp.y].setBackground(Color.YELLOW);
            }



        }
        //loops through all items to make their color accordingly
        for(byte i = 0; i < aItems.size(); i++){
            if(aItems.get(i) instanceof Bomb){
                //make red
                aPanels[((Bomb)(aItems.get(i))).getRow()][((Bomb)(aItems.get(i))).getCol()].setBackground(Color.RED); 
            }else if(aItems.get(i) instanceof Food){
                //make magenta
                aPanels[((Food)(aItems.get(i))).getRow()][((Food)(aItems.get(i))).getCol()].setBackground(Color.MAGENTA); 

            }else{
                //make blue
                aPanels[((SuperFood)(aItems.get(i))).getRow()][((SuperFood)(aItems.get(i))).getCol()].setBackground(Color.BLUE); 

            }
        }
        
        //updates score at the top
        lblScore.setText("Score: " + p.getScore());
        
        //refresh board to everything is new and changed
        repaint();
        revalidate();

    }
    
    
    //adds new items to board
    public void addItem(){
        //randomly generate coordinates
        byte bytRowRandom = (byte)(Math.random() * GRID_LENGTH);
        byte bytColRandom = (byte) (Math.random() * GRID_WIDTH);
        byte bytItemType;
        //checks if something is on the point, if yes then recall and generate new points
        if(aGrid[bytRowRandom][bytColRandom] != 0){
            addItem();
        }
        //randomly decide item type
        bytItemType = (byte)(Math.random() * 3);
    
        if(bytItemType == 0){
            //creates random bomb
            aItems.add(new Bomb(bytRowRandom, bytColRandom));
            aGrid[bytRowRandom][bytColRandom] = 2;
        }
        else if(bytItemType == 1){
            //creates random food
            aItems.add(new Food(bytRowRandom, bytColRandom));
            aGrid[bytRowRandom][bytColRandom] = 2;
        }else{
            //creates random superfood
            aItems.add(new SuperFood(bytRowRandom, bytColRandom));
            aGrid[bytRowRandom][bytColRandom] = 2;
        }

    }

    //welcomes users, gives instructions and creates player to keep track of mmoney and points
    public  void startMessage(){
        //declare variables
        byte bytAccount = 0;
        //gives instructions
        JOptionPane.showMessageDialog(null, "Hey and welcome to serpent game!\nTo play use the up,down,left and right keys to change the snakes direction.\nCollect the magenta food for 5 points, the blue superfood for 10 points, but avoid the red bombs or you'll lose points!\nMake sure you avoid hitting yourself or the wall or you'll lose!\nBy clicking space you can also pause the game.\nAfter 30 seconds an event will occur with extra items!\nHave Fun");
        //checks if user has an account
        bytAccount = Byte.parseByte(JOptionPane.showInputDialog("Do you have an account? (input: 1.Yes or 2.No)"));
        //to repeat creating playter if error occurs
        boolean bolAccount = true;
        //for their username to check files
        String strName =  " ";

        do{
            try{
                //will reask question if the user did not enter 1 or 2 for if they have an account
                if (bytAccount < 1 || bytAccount > 2)
                {
                    bytAccount = Byte.parseByte(JOptionPane.showInputDialog("input: 1.Yes or 2.No"));
                    bolAccount = true;  
                }
                //if they have one
                else if(bytAccount == 1)
                {   //ask for their name
                    strName =  JOptionPane.showInputDialog("UserName: ");
                    //checks if they're account alr exists
                    if(new File(strName +".txt").exists() == true)
                    {
                        //if it exists read frm file
                        try{
                            //buffered reader to read frm the name file
                            BufferedReader in;
                            in = new BufferedReader(new FileReader(strName+ ".txt"));
                            //makes a string for their username, short for their personal highscore and short for their points
                            String strUserName = in.readLine();

                            short shrPersonalHighScore = Short.parseShort(in.readLine());

                            short shrTotalScore = Short.parseShort(in.readLine());
                            //creates instance of player
                            p = new Player(strName,shrPersonalHighScore, shrTotalScore);
                        }
                        //catches errors for reading frm files
                        catch(FileNotFoundException e)
                        {
                            System.out.println("Error: Cannot open file for reading");
                        }

                        catch(IOException e)
                        {
                            System.out.println("Error: Cannot read from file");
                        }

                    }//if player doesn't exist
                    else
                    {
                        //creates new default player with that name
                        JOptionPane.showMessageDialog(null, "Your account does not exist: creating new account");

                        p = new Player(strName,(short)0, (short) 0);

                    }
                    //dont repeat loop
                    bolAccount = false;    
                }
                
                else if(bytAccount == 2)

                {
                    //if they don't have an account it will prompt for username and create new player
                    strName =  JOptionPane.showInputDialog("UserName: ");

//creates default player
                    p = new Player(strName,(short)0, (short)0);

                    bolAccount = false;
                }

            }
            //reloop if an error occurs
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Wrong Input.");
                bolAccount = true;
            }

        }while(bolAccount);
    }
    
    //ends game
    public void endMessage(){
        //sets totalscore/ money by adding score of round
        p.setTotalScore(p.getScore());
        // if theyre score is higher then the personal highscore it will set it
        if(p.getPersonalHighScore() <= p.getScore()){
            p.setPersonalHighScore(p.getScore());
        }
        //will save player and highscore
        writeHighScore();
        writePlayer();
        //closes gui
        setVisible(false);
        //prints end message
        JOptionPane.showMessageDialog(null, "Great Job!");
        JOptionPane.showMessageDialog(null, "Your score was: " + p.getScore() );
        return;

    }

    //writes player to file
    public void writePlayer(){
        //print writer and writes username, then personalhighscore then mmoney on file
        try{
            PrintWriter out = new PrintWriter(new FileWriter(p.getUserName() + ".txt"));
            out.println(p.getUserName());
            out.println(String.valueOf(p.getPersonalHighScore()));
            out.println(String.valueOf(p.getTotalScore()));
            out.close();
            
        }
        //below is to error trap
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    //writes highscore
    public void writeHighScore(){
        //runs if their new personal highscore is greater than all time highscore
        if (p.getPersonalHighScore() >= shrHighScore)
        
        {
            try
            {   
                //sets highscore to the personal highscore
                shrHighScore = p.getPersonalHighScore();
                //uses printwriter to writer to sepereate file named highscore.txt
                PrintWriter out = new PrintWriter(new FileWriter("HighScore.txt"));
                out.write(String.valueOf(shrHighScore));

                out.close();

            } 
            //used to catch errors
            catch (IOException e)
            {
                e.printStackTrace();

            }

        }

    }
    //uploads highscore for display at begin of each round
    public void uploadHighScore()
    {   
        //checks whether a highscore has been saved or not
        if(new File("HighScore.txt").exists() == true){
            try
            {
                //if it exists it will read it form the file and set that equal to highscore variable
                BufferedReader br = new BufferedReader(new FileReader("HighScore.txt"));
                shrHighScore = Short.parseShort(br.readLine());
                br.close();    
                
            }
            //checks for any errors while reading
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }else{
            //if not make it 0
            shrHighScore = 0;

        }

    }

    //will run at begining of each playthrough to purhcase skin
    public short shop(short shrMoney)
    {   
        //loops the shop for wrong input
        boolean bolShop = true;
        //welcome messaage
        JOptionPane.showMessageDialog(null, "Welcome to the shop!");

        do
        {
            //choice of skin color
            try{
            bytChoice = Byte.parseByte(JOptionPane.showInputDialog("\n1.Green(0$)\n2.Cyan(100$) \n3. Orange(300$) \n4.Yellow(450$) \n5.Rainbow(600$)\n\nYou currently have: " + p.getTotalScore() + "$"));
        }catch(Exception e){
            //runs to catch error
            
            JOptionPane.showMessageDialog(null, "Please enter one of the options");
            bolShop = true;
        }
            if(bytChoice == 1)
            {
                //returns for greeen becuse its free
                return 0;
            }
            else if(bytChoice == 2)
            {
                //checks is user has enough money
                if(shrMoney>=100)
                {
                    //returns cyan and subtracts money
                    return -100;
                    
                }
                else
                {   
                    //reloops if they dont have enough money
                    JOptionPane.showMessageDialog(null, "Not enough money!");

                    bolShop = true;

                }
            }
            //for orange
            else if(bytChoice == 3)
            {   //checks if user has 300 points to spend
                if(shrMoney>=300)
                {
                    //subtracts 300 from points
                    return -300;

                }
                else
                {//reloops if they dont have enough
                    JOptionPane.showMessageDialog(null, "Not enough money!");
                                        bolShop = true;

                }   
                //yellow
            }else if(bytChoice == 4)
            {//checks if they have 450 points to spend
                if(shrMoney>=450)
                {
                    //return -450 to subtract points if they have ennough
                    return -450;

                }
                else
                {
                    //rellops if not enough to pick another option
                    JOptionPane.showMessageDialog(null, "Not enough money!");
                    bolShop = true;

                }

            }
            //for rainbow
            else if(bytChoice == 5)
            { //checks if they have 600 points to spend
                if(shrMoney>=600)
                {
                    //returns to subtract 600 from total speding points
                    return -600;
                }
                else
                {   //error message and reloops
                    JOptionPane.showMessageDialog(null, "Not enough money!");
                                        bolShop = true;

                }

            }
            else{   
                // if none of those options are picked it will reloop
                   JOptionPane.showMessageDialog(null, "Wrong input!");
                   bolShop = true;

            }

        }while(bolShop);
        //needs this incase, but wont run
        return 0;


    }

    
} 


