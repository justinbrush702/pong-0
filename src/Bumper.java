import java.awt.Color;
import java.util.ArrayList;

/**
* Bumper players control with keys to bump the PingPong ball.
* 
* @author Justin Brush and Connor Savage
* @version 2012.11.26
*/
public class Bumper
{
    public final static int SPACE = 10; // Space for the Bumpers to be off the walls of the table
    public final static int LONG_SIDE = 80; // Height of the Bumper
    public final static int SHORT_SIDE = 10; // Width of the Bumper
    public final static int LEFT_X_POSITION = GameFrame.LEFT_BOUND + SPACE; // For Left Bumper
    public final static int RIGHT_X_POSITION = GameFrame.RIGHT_BOUND - SPACE - SHORT_SIDE; // For Right Bumper
    public final static int TOP_Y_POSITION = GameFrame.UPPER_BOUND + SPACE; // For Top Bumper
    public final static int BOTTOM_Y_POSITION = GameFrame.LOWER_BOUND - SPACE - SHORT_SIDE; // For Bottom Bumper
    private int xPosition; // Current xPosition of the bumper
    private int yPosition; // Current yPosition of the bumper - variable because it changes (as opposed to x (in 1972 version))
    private int height; // Height of the bumper
    private int width; // Width of the bumper
    private boolean movesVertical; // If the bumper created moves vertically or not
    private int reflectionCoordinate; // Where the ball will bounce off of if contact is made
    private int score; // Score the player using this bumper has
    private int speed; // Speed of the bumper
    private final int MOVE = 4; // How much the bumper moves in one movement (without upgrades or downgrades)
    private Color myColor; // Color of the Bumper
    private int bumperNumber; // Designates where the bumper is (Left, Right, Top, Bottom)
    private int newLength; // New length of the bumper after growing or shrinking
    private ArrayList<Chance> activeChanceList; // List of Chance objects this bumper is currently utilizing
    private ArrayList<Chance> pendingChanceList; // List of Chance objects waiting to be used
    private boolean stillPlaying; // Whether or not the player is still in the game
    private String name; // Name of the bumper
    private boolean played; // Tried to make it so that users could choose how many players to play with (couldn't figure it out)
    
    /**
     * Creates a new Bumper object.
     * @param n the number to designate where the bumper will be located on the table
     * @param playing Whether or not the bumper is playing
     */
    public Bumper(int n, boolean playing)
    {
        played = true;
        stillPlaying = playing;
        newLength = LONG_SIDE;
        activeChanceList = new ArrayList<Chance>();
        pendingChanceList = new ArrayList<Chance>();
        score = 10;
        speed = MOVE;
        if(n == 0 || n == 1)
        {
            yPosition = GameFrame.SIZE/2 - LONG_SIDE/2; // Middle of table on it's side
            height = LONG_SIDE;
            width = SHORT_SIDE;
            movesVertical = true;
            if(n == 0) // Left Bumper
            {
                bumperNumber = 0;
                xPosition = LEFT_X_POSITION;
                reflectionCoordinate = LEFT_X_POSITION + SHORT_SIDE;
                myColor = Color.RED;
                name = "Mr. Red";
            }
            else if(n == 1) // Right Bumper
            {
                bumperNumber = 1;
                xPosition = RIGHT_X_POSITION;
                reflectionCoordinate = RIGHT_X_POSITION;
                myColor = Color.GREEN;
                name = "Mr. Green";
            }
        }
        else if(n == 2 || n == 3)
        {
            xPosition = GameFrame.MID_COURT - LONG_SIDE/2; // Middle of table on it's side
            height = SHORT_SIDE;
            width = LONG_SIDE;
            movesVertical = false;
            if(n == 2) // Top Bumper
            {
                bumperNumber = 2;
                yPosition = TOP_Y_POSITION;
                reflectionCoordinate = TOP_Y_POSITION + SHORT_SIDE;
                myColor = Color.CYAN;
                name = "Mr. Blue";
            }
            else if(n == 3) // Bottom Bumper
            {
                bumperNumber = 3;
                yPosition = BOTTOM_Y_POSITION;
                reflectionCoordinate = BOTTOM_Y_POSITION;
                myColor = Color.YELLOW;
                name = "Mr. Yellow";
            }
        }
        
        if(stillPlaying == false)
        {
            played = false;
            lose();
        }
    }
    
    /**
     * Gets the bumper number.
     * @return the bumper's number.
     */
    public int getBumperNumber()
    {
        return bumperNumber;
    }
    
    /**
     * Gets the name of the bumper.
     * @return the name of the bumper.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Gets the played boolean.
     * @return the played boolean.
     */
    public boolean getPlayed()
    {
        return played;
    }
    
    /**
     * Switch from played to not played.
     * Failed attempt (Read explanation in played field)
     */
    public void switchPlayed()
    {
        if(played == true)
        {
            played = false;
            myColor = Color.GRAY;
        }
        else
        {
            played = true;
            if(bumperNumber == 0)
            {
                myColor = Color.RED;
            }
            if(bumperNumber == 1)
            {
                myColor = Color.GREEN;
            }
            if(bumperNumber == 2)
            {
                myColor = Color.CYAN;
            }
            if(bumperNumber == 3)
            {
                myColor = Color.YELLOW;
            }
        }
    }
    
    /**
     * Gets the color of the bumper.
     * @return the color of the bumper.
     */
    public Color getColor()
    {
        return myColor;
    }
    
    /**
     * Sets the color of the bumper.
     * @param c the new color the bumper will be set to.
     */
    public void setColor(Color c)
    {
        myColor = c;
    }
    
    /**
     * Gets the xPosition of the bumper.
     * @return the xPosition of the bumper.
     */
    public int getXPosition()
    {
        return xPosition;
    }
    
    /**
     * Gets the yPosition of the bumper.
     * @return yPosition of the bumper.
     */
    public int getYPosition()
    {
        return yPosition;
    }
    
    /**
     * Gets the width of the bumper.
     * @return the width of the bumper.
     */
    public int getWidth()
    {
        return width;
    }
    
    /**
     * Gets the height of the bumper.
     * @return the height of the bumper.
     */
    public int getHeight()
    {
        return height;
    }
    
    /**
     * Sets the length of the bumper after hitting a Chance object.
     * @param grow whether or not the new length will be twice as long or twice as short.
     */
    public void setNewLength(boolean grow)
    {
        if(grow == true)
        {
            newLength = newLength*2;
        }
        else if(newLength > 5) // Stop shrinking here
        {
            newLength = newLength/2;
        }
    }
    
    /**
     * Method to simulate the animation of the bumper growing until it reaches its new length.
     */
    public void grow()
    {
        if(movesVertical == true)
        {
                height++;
        }
    
        else
        {
                width++;
        }
    }
    
    /**
     * Method to simulate the animation of the bumper shrinking until it reaches its new length.
     */
    public void shrink()
    {
        if(movesVertical == true)
        {
            height--;
        }
        else
        {
            width--;
        }
    }
    
    /**
     * Gets the movesVertical field.
     * @return whether or not the bumper moves vertical.
     */
    public boolean movesVertical()
    {
        return movesVertical;
    }
 
    /**
     * Moves the bumper in a certain direction.
     * @param positive determines whether or not the bumper will move in a positive direction.
     */
    public void move(boolean positive)
    {
        if(positive == true)
        {
            if(movesVertical == true && yPosition + height < GameFrame.LOWER_BOUND + getHeight()/2) // So bumper doesn't go off the top of the screen
            {
                yPosition += speed;
            }
            if(movesVertical == false && xPosition + width < GameFrame.RIGHT_BOUND + getWidth()/2) // So bumper doesn't go off the right of the screen
            {
                xPosition += speed;
            }
        }
        else
        {
            if(movesVertical == true && yPosition > GameFrame.UPPER_BOUND - height/2) // So bumper doesn't go off the bottom of the screen
            {
                yPosition -= speed;
            }
            if(movesVertical == false && xPosition > GameFrame.LEFT_BOUND - width/2) // So bumper doesn't go off the left of the screen
            {
                xPosition -= speed;
            }
        }
        // The bumpers can go half-way off the screen so that they can reach a ball and it won't get stuck
    }
    
    /**
     * Sets the new speed of a bumper after hitting a Chance object.
     * @param faster whether or not the speed is getting twice as fast or twice as slow.
     */
    public void setNewSpeed(boolean faster)
    {
        if(faster == true)
        {
            speed = speed*2;
        }
        else if(speed > 1)
        {
            speed = speed/2;
        }
    }
    
    /**
     * Subtracts a point from a player's score.
     */
    public void score()
    {
        score--;
    }
    
    /**
     * Gets the player's score.
     * @return Score on the bumper.
     */
    public int getScore()
    {
        return score;
    }
    
    /**
     * Gets the bumper's reflection coordinate.
     * @return the bumper's reflection coordinate.
     */
    public int getReflectionCoordinate()
    {
        return reflectionCoordinate;
    }
    
    /**
     * Adds a Chance object to it's pending list.
     * @param c the object being added.
     */
    public void addPendingChance(Chance c)
    {
        pendingChanceList.add(c);
    }
    
    /**
     * For the growing and shrinking of bumpers 
     * if two or more growing and shrinking chance objects have been touched.
     * This method also takes care of speeding up and slowing down on time.
     */
    public void specialEvents()
    {
        ArrayList<Chance> removeChanceList = new ArrayList<Chance>();
        for(Chance c : pendingChanceList)
        {
            //System.out.println(c.getAge());
            if(c.getAge() < Chance.LIFE_SPAN)
            {
                c.ageLife();
            }
            else
            {
                if(c.getColor() == Color.MAGENTA)
                {
                    setNewLength(true);
                }
                else if(c.getColor() == Color.ORANGE)
                {
                    setNewLength(false);
                }
                activeChanceList.add(c);
                removeChanceList.add(c);
            }
        }
        for(Chance c : removeChanceList)
        {
            pendingChanceList.remove(c);
        }
        
        if(activeChanceList.size() > 0)
        {
            if(activeChanceList.get(0).getColor() == Color.MAGENTA)
            {
                if(movesVertical == true)
                {
                    if(height < newLength)
                    {
                        grow();
                    }
                    else
                    {
                        activeChanceList.remove(0);
                    }
                }
                else
                {
                    if(width < newLength)
                    {
                        grow();
                    }
                    else
                    {
                        activeChanceList.remove(0);
                    }
                }
            }
            else if(activeChanceList.get(0).getColor() == Color.ORANGE)
            {
                if(movesVertical == true)
                {
                    if(height > newLength)
                    {
                        shrink();
                    }
                    else
                    {
                        activeChanceList.remove(0);
                    }
                }
                else
                {
                    if(width > newLength)
                    {
                        shrink();
                    }
                    else
                    {
                        activeChanceList.remove(0);
                    }
                }
            }
            else 
            {
                if(activeChanceList.get(0).getColor() == Color.PINK)
                {
                    setNewSpeed(true);
                }
                else if(activeChanceList.get(0).getColor() == Color.GRAY)
                {
                    setNewSpeed(false);
                }
                activeChanceList.remove(0);
            }
        }
    }
    
    /**
     * Gets the stillPlaying boolean.
     * @return whether or not the bumper is still playing.
     */
    public boolean stillPlaying()
    {
        return stillPlaying;
    }
    
    /**
     * Sets the stillPlaying boolean.
     * @param playing the boolean to set if the bumper is still playing or not.
     */
    public void setStillPlaying(boolean playing)
    {
        stillPlaying = playing;
    }
    
    /**
     * The player controlling this bumper loses.
     * The bumper basically becomes the wall on its side.
     */
    public void lose()
    {
        score = 0; // Just in case it went to -1 or something
        stillPlaying = false;
        setColor(Color.WHITE);
        if(played == false)
        {
            setColor(Color.GRAY);
        }
        
        if(bumperNumber == 0) // Left Bumper
        {
            reflectionCoordinate = GameFrame.LEFT_BOUND;
            xPosition = GameFrame.LEFT_BOUND - width;
            yPosition = GameFrame.UPPER_BOUND;
            height = GameFrame.SIZE;
        }
        else if(bumperNumber == 1) // Right Bumper
        {
            reflectionCoordinate = GameFrame.RIGHT_BOUND;
            xPosition = GameFrame.RIGHT_BOUND;
            yPosition = GameFrame.UPPER_BOUND;
            height = GameFrame.SIZE;
        }
        else if(bumperNumber == 2) // Top Bumper
        {
            reflectionCoordinate = GameFrame.UPPER_BOUND;
            xPosition = GameFrame.LEFT_BOUND;
            yPosition = GameFrame.UPPER_BOUND - height;
            width = GameFrame.SIZE;
        }
        else if(bumperNumber == 3) // Bottom Bumper
        {
            reflectionCoordinate = GameFrame.LOWER_BOUND;
            xPosition = GameFrame.LEFT_BOUND;
            yPosition = GameFrame.LOWER_BOUND;
            width = GameFrame.SIZE;
        }
    }
}