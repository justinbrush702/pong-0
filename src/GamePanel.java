import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.font.FontRenderContext;

/**
* GamePanel draws the Pong game being played onto the table.
* 
* @author Justin Brush and Connor Savage
* @version 2012.11.26
*/
//we extend JPanel so that we can add this canvas to JFrames and such
//we are a type of JPanel, so that when normal GUI stuff expects a JPanel
//we can work as well
public class GamePanel extends JPanel
{
    private Graphics2D g2d;
    private ArrayList<PingPong> pongList;
    private Bumper[] bumperArray;
    private boolean stillPlaying; // Is the game over or not
    private boolean firstOpened; // Determines when the game is first opened
    private ArrayList<Chance> chanceList;
    private int[] scores;
    
    /**
     * Creates a new gamePanel object.
     */
    public GamePanel()
    {
        firstOpened = true;
        stillPlaying = false;
        
        bumperArray = new Bumper[4];
        for(int i=0; i < bumperArray.length; i++)
        {
            bumperArray[i] = new Bumper(i,true);
        }
        //bumperArray[2] = new Bumper(2,false);
        
        pongList = new ArrayList<PingPong>(); // Create an ArrayList for PingPong balls
        pongList.add(new PingPong()); // Add a PingPong ball to the list
        //pongList.add(new PingPong(true));
        //pongList.add(new PingPong(true));
        //pongList.add(new PingPong(true));
        //pongList.add(new PingPong(true));
        //pongList.add(new PingPong(true));
        //pongList.add(new PingPong(true)); // Tests
        
        chanceList = new ArrayList<Chance>();
        
        setBackground(Color.BLACK); // Makes the background black (like old school Pong)
    }
    
    /**
     * Creates a new GamePanel object.
     * @param playing0 determines if left bumper is playing this specific game
     * @param playing1 determines if right bumper is playing this specific game
     * @param playing2 determines if top bumper is playing this specific game
     * @param playing3 determines if bottom bumper is playing this specific game
     */
    public GamePanel(boolean playing0, boolean playing1, boolean playing2, boolean playing3)
    {
        firstOpened = true;
        stillPlaying = true;
        
        bumperArray = new Bumper[4];
        bumperArray[0] = new Bumper(0,playing0);
        bumperArray[1] = new Bumper(1,playing1);
        bumperArray[2] = new Bumper(2,playing2);
        bumperArray[3] = new Bumper(3,playing3);
        
        pongList = new ArrayList<PingPong>(); // Create an ArrayList for PingPong balls
        pongList.add(new PingPong()); // Add a PingPong ball to the list
        
        chanceList = new ArrayList<Chance>(); // Create an ArrayList for Chance objects
        
        setBackground(Color.BLACK); // Makes the background black (like old school Pong)
    }

    /**
     * Overrides normal paintComponent method with our own custom painting
     * @param g a "graphics context" that the JPanel's contents are being drawn on.
     * See http://docs.oracle.com/javase/6/docs/api/java/awt/Graphics.html
     */
    //This is the method that gets called whenever the JPanel is drawn on the screen--so when it is made
    //visible, when we call "repaint()", etc
    //Basically we customize what we want to be shown/drawn on this JPanel
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); //make sure to call parent's (JPanel's) version of paintComponent, 
                                 //in case there are any other pieces of the panel that are needed
        g2d = (Graphics2D)g; //cast our graphics context into a Graphics2D object.
                                        //this gives us more fun methods. 
                                        //See: http://docs.oracle.com/javase/6/docs/api/java/awt/Graphics2D.html
        
        //New stuff added for Pong
        g2d.setPaint(Color.WHITE);
        
        g2d.setFont(new Font("Impact", Font.PLAIN, 48));
        if(firstOpened == true && stillPlaying == false)
        {
            g2d.drawString("PRESS SPACEBAR TO PLAY", GameFrame.MID_COURT - (int)(getTextWidth("PRESS SPACEBAR TO PLAY"))/2, (GameFrame.SIZE*7)/8);
        }
        
        //Dashed Lines
        int tableSection = GameFrame.SIZE / 20;
        for(int i=4; i < 16; i+=2) // Draw a dashed line at midCourt
        {
            g2d.drawLine(GameFrame.MID_COURT, GameFrame.UPPER_BOUND + i*tableSection + tableSection/2, GameFrame.MID_COURT, GameFrame.UPPER_BOUND + (i+1)*tableSection + tableSection/2);
            g2d.drawLine(GameFrame.LEFT_BOUND + i*tableSection + tableSection/2, GameFrame.SIZE / 2, GameFrame.LEFT_BOUND + (i+1)*tableSection + tableSection/2, GameFrame.SIZE / 2);
        }
        
        // Two White Lines
        g2d.fillRect(GameFrame.LEFT_BOUND - Bumper.SHORT_SIDE, GameFrame.UPPER_BOUND, Bumper.SHORT_SIDE, GameFrame.LOWER_BOUND);
        g2d.fillRect(GameFrame.RIGHT_BOUND, GameFrame.UPPER_BOUND, Bumper.SHORT_SIDE, GameFrame.LOWER_BOUND);
        
        // The Bumpers
        for(int i=0; i < bumperArray.length; i++)
        {
            Bumper paddle = bumperArray[i];
            g2d.setPaint(paddle.getColor());
            g2d.fillRect(paddle.getXPosition(), paddle.getYPosition(), paddle.getWidth(), paddle.getHeight());
        }
        
        // The Chance Objects
        for(Chance c : chanceList)
        {
            g2d.setColor(c.getColor());
            g2d.fillOval(c.getXPosition(), c.getYPosition(), Chance.DIAMETER, Chance.DIAMETER);
        }
        
        // The PingPong Balls
        for(PingPong p : pongList) // Each PingPong ball in pongList
        {
            g2d.setPaint(p.getColor());
            g2d.fillOval(p.getXPosition(), p.getYPosition(), PingPong.DIAMETER, PingPong.DIAMETER);
        }
        
        g2d.setPaint(Color.WHITE);
        
        //Scores
        g2d.setFont(new Font("Impact", Font.PLAIN, 24));
        for(int i=0; i < bumperArray.length; i++)
        {
            g2d.setPaint(bumperArray[i].getColor());
            g2d.drawString(bumperArray[i].getName() + ": " + bumperArray[i].getScore(), 10, GameFrame.SIZE/4 + i*96);
        }
        
        // Controls for Bumpers
        g2d.setFont(new Font("Impact", Font.PLAIN, 18));
        g2d.setPaint(bumperArray[0].getColor());
        g2d.drawString("Controls: 'a' and 's'", 10, GameFrame.SIZE/4 + 0*96 + 40);
        g2d.setPaint(bumperArray[1].getColor());
        g2d.drawString("Controls: 'f' and 'g'", 10, GameFrame.SIZE/4 + 1*96 + 40);
        g2d.setPaint(bumperArray[2].getColor());
        g2d.drawString("Controls: 'j' and 'k'", 10, GameFrame.SIZE/4 + 2*96 + 40);
        g2d.setPaint(bumperArray[3].getColor());
        g2d.drawString("Controls: ';' and ''' (apostrophe)", 10, GameFrame.SIZE/4 + 3*96 + 40);
        
        // Title
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Impact", Font.PLAIN, 60));
        g2d.drawString("PONG", (GameFrame.LEFT_BOUND-Bumper.SHORT_SIDE)/2 - (int)(getTextWidth("PONG"))/2, 75);
        
        // Explanations on Right Side
        g2d.setFont(new Font("Impact", Font.PLAIN, 96));
        g2d.setPaint(Color.MAGENTA);
        g2d.fillOval(GameFrame.RIGHT_BOUND + 50, 100, Chance.DIAMETER, Chance.DIAMETER);
        g2d.setPaint(Color.ORANGE);
        g2d.fillOval(GameFrame.RIGHT_BOUND + 50, 200, Chance.DIAMETER, Chance.DIAMETER);
        g2d.setPaint(Color.PINK);
        g2d.fillOval(GameFrame.RIGHT_BOUND + 50, 300, Chance.DIAMETER, Chance.DIAMETER);
        g2d.setPaint(Color.LIGHT_GRAY);
        g2d.fillOval(GameFrame.RIGHT_BOUND + 50, 400, Chance.DIAMETER, Chance.DIAMETER);
        
        g2d.setPaint(Color.WHITE);
        g2d.setFont(new Font("Impact", Font.PLAIN, 16));
        g2d.drawString("Bumper Effects from Chance Objects", (GameFrame.RIGHT_BOUND + Bumper.SHORT_SIDE + GameFrame.LENGTH)/2 - (int)(getTextWidth("Bumper Effects from Chance Objects"))/2, 50);
        g2d.drawString("Grows Bumper", 1100 - (int)(getTextWidth("Grows Bumper"))/2, 123);
        g2d.drawString("Shrinks Bumper", 1100 - (int)(getTextWidth("Shrinks Bumper"))/2, 223);
        g2d.drawString("Speeds up Bumper", 1100 - (int)(getTextWidth("Speeds up Bumper"))/2, 323);
        g2d.drawString("Slows down Bumper", 1100 - (int)(getTextWidth("Slows down Bumper"))/2, 423);
        g2d.drawString("Chance Events last for 10 seconds each.", (GameFrame.RIGHT_BOUND + Bumper.SHORT_SIDE + GameFrame.LENGTH)/2 - (int)(getTextWidth("Chance Events last for 10 seconds each."))/2, 500);
        g2d.drawString("Every 10 seconds a Chance Object or", (GameFrame.RIGHT_BOUND + Bumper.SHORT_SIDE + GameFrame.LENGTH)/2 - (int)(getTextWidth("Every 10 seconds a Chance Object or"))/2, 550);
        g2d.drawString("extra Ball is added to the Table.", (GameFrame.RIGHT_BOUND + Bumper.SHORT_SIDE + GameFrame.LENGTH)/2 - (int)(getTextWidth("extra Ball is added to the Table."))/2, 580);

        if(firstOpened == false && stillPlaying == false)
        {
            g2d.setFont(new Font("Impact", Font.PLAIN, 40));
            g2d.drawString("PRESS 'r' TO RESET", GameFrame.MID_COURT - (int)(getTextWidth("PRESS 'r' TO RESET"))/2, GameFrame.SIZE/8);
            g2d.drawString("PRESS SPACEBAR TO PLAY AGAIN", GameFrame.MID_COURT - (int)(getTextWidth("PRESS SPACEBAR TO PLAY AGAIN"))/2, (GameFrame.SIZE*7)/8);
        }        
        
        // Check for Winner
        int losers = 0;
        for(int i=0; i < bumperArray.length; i++)
        {
            if(bumperArray[i].getScore() <= 0)
            {
                losers++;
            }
        }
        
        if(losers == 3)
        {
            for(int i=0; i < bumperArray.length; i++)
            {
                if(bumperArray[i].getScore() > 0)
                {
                    g2d.setPaint(bumperArray[i].getColor());
                    g2d.setFont(new Font("Impact", Font.PLAIN, 60));
                    g2d.drawString("WINNER", (GameFrame.LEFT_BOUND-Bumper.SHORT_SIDE)/2 - (int)(getTextWidth("WINNER"))/2, 550);
                }
            }
        }
        
        //g2d.setPaint(Color.RED);
        //g2d.drawLine(Table.LEFT_BOUND, Table.SIZE/2, Table.RIGHT_BOUND, Table.SIZE/2); // Test for middle yPosition of the table
    }

    /**
     * Gets the list of PingPong balls.
     * @return the list of PingPong balls.
     */
    public ArrayList<PingPong> getPongList()
    {
        return pongList;
    }

    /**
     * Adds a PingPong ball to pongList.
     */
    public void addPong()
    {
        pongList.add(new PingPong());
    }
    
    /**
     * Removes any extra PingPong Balls from the pongList.
     * @param p the PingPong Ball being removed.
     */
    public void removePong(PingPong p)
    {
        pongList.remove(p);
    }
    
    /**
     * Sorts the list of PingPong balls so that the ball on top of the middle of the screen is the next one to move.
     * (Used for when I had a lot of PingPong Balls in the pongList)
     */
    public void sortPongList()
    {
        // Put pongList into a local array
        PingPong[] pongArray = new PingPong[pongList.size()];
        for(int i=0; i < pongArray.length; i++)
        {
            pongArray[i] = pongList.get(i);
        }
        
        for(int i=0; i < pongArray.length; i++)
        {
            for(int j=0; j < pongArray.length; j++)
            {
                if(pongArray[i].getCanMove() < pongArray[j].getCanMove())
                {
                    //swap
                    PingPong a = pongArray[i];
                    PingPong b = pongArray[j];
                    pongArray[i] = b;
                    pongArray[j] = a;
                }
            }
        }
        
        pongList = new ArrayList<PingPong>();
        for(int i=0; i < pongArray.length; i++)
        {
            pongList.add(pongArray[i]);
        }
    }
    
    /**
     * A method to get the width of a text drawn on the canvas.
     * @param text the text for which width to be found
     * @return the width.
     */
    public double getTextWidth(String text)
    {
        // Get the font renderer context
        FontRenderContext frc = g2d.getFontRenderContext();
        // Get the bounding rectangle
        Rectangle2D textBound = g2d.getFont().getStringBounds(text, frc);
        return textBound.getWidth();
    }
    
    /**
     * Gets whether or not the game is over or not.
     * @return whether or not the game is over or not.
     */
    public boolean stillPlaying()
    {
        return stillPlaying;
    }
    
    /**
     * Starts the game.
     */
    public void startGame()
    {
        stillPlaying = true;
    }
    
    /**
     * Ends the game.
     */
    public void endGame()
    {
        stillPlaying = false;
    }
    
    /**
     * Gets whether or not the game is first opened.
     * @return whether or not the game is first opened.
     */
    public boolean isFirstOpened()
    {
        return firstOpened;
    }
    
    /**
     * Plays the game for the first time.
     */
    public void playFirstTime()
    {
        firstOpened = false;
    }
    
    /**
     * Gets the bumperArray.
     * @return the bumperArray.
     */
    public Bumper[] getBumperArray()
    {
        return bumperArray;
    }
    
    /**
     * Gets the chanceList.
     * @return the chanceList.
     */
    public ArrayList<Chance> getChanceList()
    {
        return chanceList;
    }
    
    /**
     * Adds a Chance object to the chanceList.
     * @param n the int to determine what color the Chance object will have
     * @b determines whether or not this object is returning the bumper back to its normal state or not
     */
    public void addChance(int n, boolean b)
    {
        chanceList.add(new Chance(n,b));
    }
    
    /**
     * Removes a Chance object from the chanceList.
     * @param c the Chance object being removed from the chanceList.
     */
    public void removeChance(Chance c)
    {
        chanceList.remove(c);
    }
    
    /**
     * Resets the GamePanel object to when it first opened up.
     */
    public void resetFirstOpened()
    {
        firstOpened = true;
        stillPlaying = false;
    }
}