import java.awt.Color;
import java.util.Random;

/**
 * Chance objects are randomly instantiated on the table.
 * When touched, these objects will change some values in the bumper that last touched the ball
 * that touched the Chance object (as seen in the color of the PingPong ball).
 * Chance objects may grow or shrink bumpers.
 * Chance objects may speed up or slow down bumpers.
 * 
 * @author Justin Brush and Connor Savage
 * @version 2012.12.6
 */
public class Chance
{
    public final static int DIAMETER = 30; // Diameter of the Chance object
    public final static int RADIUS = DIAMETER / 2;
    private int xPosition;
    private int yPosition;
    private Color myColor;
    private Random generator;
    public final static int LIFE_SPAN = 10000; // Life Span of the Chance object after being activated
    private int age; // Current age of the Chance object
    
    /**
     * Bumper Gets:
     *  Bigger (MAGENTA)
     *  Smaller (ORANGE)
     *  Faster (PINK)
     *  Slower (GRAY)
     *  
     *  @param n the int to determine what color the Chance object will have
     *  @returnToNormal determines whether or not this object is returning the bumper back to its normal state or not
     */
    public Chance(int n, boolean returnToNormal)
    {
        generator = new Random();
        xPosition = generator.nextInt(400) + 400; // Random spot in the middle of the table
        yPosition = generator.nextInt(400) + 100; // Random spot in the middle of the table
        if(n == 0) // Bumper gets Bigger
        {
            myColor = Color.MAGENTA;
        }
        else if(n == 1) // Bumper gets Smaller
        {
            myColor = Color.ORANGE;
        }
        else if(n == 2) // Bumper gets Faster
        {
            myColor = Color.PINK;
        }
        else if(n ==3) // Bumper gets Slower
        {
            myColor = Color.GRAY;
        }
        if(returnToNormal == false)
        {
            age = LIFE_SPAN;
        }
        else
        {
            age = 0;
        }
    }
    
    /**
     * Gets the xPosition of the Chance object.
     * @return the xPosition of the Chance object.
     */
    public int getXPosition()
    {
        return xPosition;
    }
    
    /**
     * Gets the yPosition of the Chance object.
     * @return the yPosition of the Chance object.
     */
    public int getYPosition()
    {
        return yPosition;
    }
    
    /**
     * Gets the xCenter of the Chance object.
     * @return the xCenter of the Chance object.
     */
    public int getXCenter()
    {
        int xCenter = xPosition + RADIUS;
        return xCenter;
    }
    
    /**
     * Gets the yCenter of the Chance object.
     * @return the yCenter of the Chance object.
     */
    public int getYCenter()
    {
        int yCenter = yPosition + RADIUS;
        return yCenter;
    }
    
    /**
     * Gets the Color of the Chance object.
     * @return the Color of the Chance object.
     */
    public Color getColor()
    {
        return myColor;
    }
    
    /**
     * Gets the age of the Chance object.
     * @return the age of the Chance object.
     */
    public int getAge()
    {
        return age;
    }
    
    /**
     * Increments the life of the Chance object by 10 (ms)
     */
    public void ageLife()
    {
        age += 10;
    }
}