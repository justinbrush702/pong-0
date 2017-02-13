import java.util.Random;
import java.awt.*;

/**
* PingPong is the ball that is used to play Pong.
* 
* @author Justin Brush and Connor Savage
* @version 2012.11.26
*/
public class PingPong
{
    public final static int DIAMETER = 10;
    public final static int RADIUS = DIAMETER/2;
    private int xPosition;
    private int yPosition;
    private int xSpeed;
    private int ySpeed;
    private Bumper lastTouched; // The Bumper the ball has last touched
    private Random generator; // Used to generate random speeds
    private int canMove; // Used to hold the ball in place for 3 seconds before moving
    private int startingSpeed; // The starting speed of the ball
    private Color myColor;
    
    /**
     * Creates a new PingPong object.
     */
    public PingPong()
    {
        myColor = Color.WHITE;
        startingSpeed = 3;
        generator = new Random();
        resetBall();
    }
    
    /**
     * Resets the ball after being scored.
     */
    public void resetBall()
    {
        canMove = 0; // Waits 3 seconds to move (Right now it's at 0 ms);
        xPosition = GameFrame.MID_COURT - RADIUS; // Middle of the table
        yPosition = GameFrame.SIZE/2 - RADIUS; // Middle of the table
        
        int direction; // PingPong Ball goes in the direction of the last touched Bumper
        if(lastTouched == null)
        {
            direction = generator.nextInt(4); // Go in random direction at the beginning of the game
        }
        else
        {
            direction = lastTouched.getBumperNumber();
        }
        
        int randomSpeed = generator.nextInt(5) - 2; // Starting ySpeed from -2 to 2
        if(myColor != Color.WHITE)
        {
            if(direction == 0)
            {
                ySpeed = randomSpeed;
                xSpeed = -startingSpeed;
            }
            else if(direction == 1)
            {
                ySpeed = randomSpeed;
                xSpeed = startingSpeed;
            }
            else if(direction == 2)
            {
                xSpeed = randomSpeed;
                ySpeed = -startingSpeed;
            }
            else if(direction == 3)
            {
                xSpeed = randomSpeed;
                ySpeed = startingSpeed;
            }
        }
        else
        {
            // So ball doesn't get stuck bouncing back and forth between two walls (and in corners)
            xSpeed = generator.nextInt(7) - 3;
            ySpeed = generator.nextInt(7) - 3;
            while(xSpeed == 0 || ySpeed == 0 || xSpeed == ySpeed || xSpeed == -ySpeed ||
                  xSpeed == 1 || xSpeed == -1 || ySpeed == 1 || ySpeed == -1)
            {
                xSpeed = generator.nextInt(7) - 3;
                ySpeed = generator.nextInt(7) - 3;
            }
        }
    }
    
    /**
     * Moves the PingPong ball.
     */
    public void move()
    {
        xPosition += xSpeed;
        yPosition += ySpeed;
    }
    
    /**
     * Gets the Color of the ball.
     * @return Color of the ball.
     */
    public Color getColor()
    {
        return myColor;
    }
    
    /**
     * Gets canMove.
     * @return canMove.
     */
    public int getCanMove()
    {
        return canMove;
    }
    
    /**
     * Increment canMove.
     */
    public void incrementCanMove()
    {
        canMove += 10;
    }
    
    /**
     * Gets the xPosition of the ball.
     * @return the xPosition of the ball.
     */
    public int getXPosition()
    {
        return xPosition;
    }
    
    /**
     * Sets the xPosition of the ball.
     * @param newX the ball's new xPosition.
     */
    public void setXPosition(int newX)
    {
        xPosition = newX;
    }
    
    /**
     * Gets the yPosition of the ball.
     * @return the yPosition of the ball.
     */
    public int getYPosition()
    {
        return yPosition;
    }
    
    /**
     * Sets the yPosition of the ball.
     * @param newY the ball's new yPosition.
     */
    public void setYPosition(int newY)
    {
        yPosition = newY;
    }
    
    /**
     * Returns the x-coordinate of the center of the ball.
     * @return the xCenter of the ball.
     */
    public int getXCenter()
    {
        int xCenter = xPosition + RADIUS;
        return xCenter;
    }
    
    /**
     * Returns the y-coordinate of the center of the ball.
     * @return the yCenter of the ball.
     */
    public int getYCenter()
    {
        int yCenter = yPosition + RADIUS;
        return yCenter;
    }
    
    /**
     * Gets the xSpeed of the ball.
     * @return the xSpeed of the ball.
     */
    public int getXSpeed()
    {
        return xSpeed;
    }
    
    /**
     * Gets the ySpeed of the ball.
     * @return the ySpeed of the ball.
     */
    public int getYSpeed()
    {
        return ySpeed;
    }
    
    /**
     * Changes the speed of the ball depending on where it hits the bumper.
     * @param b the Bumper that the ball just hit.
     */
    public void reflectionSpeed(Bumper b)
    {
        setLastTouched(b); // Set the ball's lastTouched to b
        int divide = 5; // Preferrably should be an odd number so there is ySpeed = 0 if hit in the middle
        
        if(b.movesVertical() == true) // Up and Down Bumpers
        {
            int bumperSection = b.getHeight() / divide; // Divides the bumper into (divide) equal sections
            xPosition = b.getReflectionCoordinate();
            if(b.getBumperNumber() == 1) // Right Bumper
            {
                xPosition = b.getReflectionCoordinate() - DIAMETER;
            }
            if(xSpeed > 0)
            {
                xSpeed = -3;
            }
            else
            {
                xSpeed = 3;
            }
            //xSpeed = -xSpeed, but xSpeed may not be 3 or -3
            for(int i=0; i < divide; i++)
            {
                if(yPosition <= b.getYPosition() + bumperSection*(divide - i)) // Formula to come up with reflection speed
                {   // Starts at the bottom of the bumper, works it way up as i increases
                    ySpeed = ((divide - 1) / 2 - i)*2;
                }
            }
            if(ySpeed == 0) // Turbo Function - allows the ball to zoom straight across the table
            {
               if(xSpeed > 0 && xSpeed < 6) // When ball is going to the right
               {   // 7 is too fast of a speed. 7 "goes through" the bumper
                   xSpeed = 6;
               }
               else if(xSpeed < 0 && xSpeed > -6) // When ball is going to the left
               {
                   xSpeed = -6;
               } 
            }
        }
        else // Left and Right Bumpers
        {
            int bumperSection = b.getWidth() / divide; // Divides the bumper into (divide) equal sections
            yPosition = b.getReflectionCoordinate();
            if(b.getBumperNumber() == 3) // Bottom Bumper
            {
                yPosition = b.getReflectionCoordinate() - DIAMETER;
            }
            if(ySpeed > 0)
            {
                ySpeed = -3;
            }
            else
            {
                ySpeed = 3;
            }
            //ySpeed = -ySpeed, but ySpeed may not be 3 or -3
            for(int i=0; i < divide; i++)
            {
                if(xPosition <= b.getXPosition() + bumperSection*(divide - i)) // Formula to come up with reflection speed
                {   // Starts at the bottom of the bumper, works it way up as i increases
                    xSpeed = ((divide - 1) / 2 - i)*2;
                }
            }
            if(xSpeed == 0) // Turbo Function - allows the ball to zoom straight across the table
            {
                if(ySpeed > 0 && ySpeed < 6) // When ball is going to the right
                {   // 7 is too fast of a speed. 7 "goes through" the bumper
                    ySpeed = 6;
                }
                else if(ySpeed < 0 && ySpeed > -6) // When ball is going to the left
                {
                    ySpeed = -6;
                }
            }
        }
    }
    
    /**
     * Ball simply reflects off of a bumper which has become the wall.
     * @param b the bumper the ball is being bounced off of
     */
    public void bounceOff(Bumper b)
    {
        if(b.getBumperNumber() < 2) // Left or Right Bumpers
        {
            xSpeed = -xSpeed;
        }
        else
        {
            ySpeed = -ySpeed; // Top and Bottom Bumpers
        }
    }
    
    /**
     * Checks to see if the ball is touching the bumper.
     * @param b the bumper being checked.
     * @return whether or not the ball is touching the bumper.
     */
    public boolean isTouching(Bumper b)
    {
        boolean isTouching = false;
        if(b.movesVertical() == true)
        {
            if(Math.abs(getXCenter() - b.getReflectionCoordinate()) < RADIUS) // If xPositions match up
            {
                if(getYCenter() > b.getYPosition() && getYCenter() < b.getYPosition() + b.getHeight()) // If yCenter is on the bumper
                {
                    isTouching = true;
                }
            }
        }
        else
        {
            if(Math.abs(getYCenter() - b.getReflectionCoordinate()) < RADIUS) // If yPositions match up
            {
                if(getXCenter() > b.getXPosition() && getXCenter() < b.getXPosition() + b.getWidth()) // If xCenter is on the bumper
                {
                    isTouching = true;
                }
            }
        }
        return isTouching;
    }
    
    /**
     * Checks to see if the ball is touching a Chance object.
     * @param c the Chance object being checked.
     * @return whether or not the ball is touching the Chance object.
     */
    public boolean isTouching(Chance c)
    {
        boolean isTouching = false; // Local boolean
        int distance = PingPong.RADIUS + Chance.RADIUS; //distance between centers of PingPong and Chance Object
        if(Math.sqrt( 
        (this.getXCenter()-c.getXCenter())*(this.getXCenter()-c.getXCenter()) +
        (this.getYCenter()-c.getYCenter())*(this.getYCenter()-c.getYCenter())
        ) <= distance) // Pythagorean Theorem
        {
        isTouching = true;
        }
        return isTouching;
    }
    
    /**
     * Gets the lastTouched bumper of the ball.
     * @return The Bumper the ball has last touched.
     */
    public Bumper getLastTouched()
    {
        return lastTouched;
    }
    
    /**
     * Sets the lastTouched variable to the bumper the ball was last touched by.
     * @param b the Bumper the ball has last touched.
     */
    public void setLastTouched(Bumper b)
    {
        lastTouched = b;
        if(b != null)
        {
            myColor = b.getColor(); // Set color to the color of the bumper
        }
        else
        {
            myColor = Color.WHITE;
        }
    }
}