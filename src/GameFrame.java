import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Font;
import java.util.Random;

/**
* Table to play Pong on.
*
* @author Justin Brush
* @version 2012.11.26
*/
public class GameFrame implements ActionListener, KeyListener
{
    public final static int LENGTH = 1200;
    public final static int SIZE = 600; // Width of the table
    public final static int LEFT_BOUND = 300; // Left Bound of the Pong Table
    public final static int RIGHT_BOUND = LEFT_BOUND + SIZE; // Right Bound of the Pong Table
    public final static int MID_COURT = (LEFT_BOUND + RIGHT_BOUND)/2; //Mid Court
    public final static int UPPER_BOUND = 0;
    public final static int LOWER_BOUND = UPPER_BOUND + SIZE; // (Talk to Joel about Mac vs. PC)
    private GamePanel gameTable; // GamePanel object used to draw the game on
    private JFrame frame; // Frame for the game
    private Timer animateClock; // Timer used to refresh the gamePanel
    private boolean lu; //Left Up Key
    private boolean ld; //Left Down Key
    private boolean ru; //Right Up Key
    private boolean rd; //Right Down Key
    private boolean tl; //Top Left Key
    private boolean tr; //Top Right Key
    private boolean bl; //Bottom Left Key
    private boolean br; //Bottom Right Key
    //private Timer newBallClock; // Timer used to add a ball (Testing purposes)
    private Timer chanceClock; // Timer used to initiate a Chance object
    private Random generator;
    private Bumper[] bumperArray;
    private int bumpersLeft;

    /**
     * Creates a new Table object.
     */
    public GameFrame()
    {
        lu = false;
        ld = false;
        ru = false;
        rd = false;
        tl = false;
        tr = false;
        bl = false;
        br = false;

        frame = new JFrame();
        frame.setTitle("Pong");
        frame.setSize(LENGTH,SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        generator = new Random();
        bumpersLeft = 4;

        gameTable = new GamePanel();
        gameTable.setPreferredSize(new Dimension(LENGTH, SIZE));
        frame.getContentPane().add(gameTable);
        bumperArray = gameTable.getBumperArray();

        setUpGame(true);
    }

    /**
     * Sets up Pong.
     * @param reset determines whether or not the game is using new or old bumpers
     */
    public void setUpGame(boolean reset)
    {
        if(reset == true)
        {
            gameTable = new GamePanel();
        }
        else
        {
            gameTable = new GamePanel(bumperArray[0].getPlayed(),bumperArray[1].getPlayed(),bumperArray[2].getPlayed(),bumperArray[3].getPlayed());
        }

        gameTable.setPreferredSize(new Dimension(LENGTH, SIZE));
        frame.getContentPane().add(gameTable);

        animateClock = new Timer(10,this); // Repaint the gameTable every 10 milliseconds
        //newBallClock = new Timer(15000,this);
        chanceClock = new Timer(10000,this); // Add a Chance object or an extra ball every 10 seconds

        frame.pack();
        gameTable.addKeyListener(this);
        gameTable.setFocusable(true);
    }

    /**
     * Plays Pong.
     * @param reset determines whether or not the game is using new or old bumpers
     */
    public void playGame(boolean reset)
    {
        setUpGame(reset);
    }

    /**
     * If event originates from the animateClock, the gameTable animates.
     * If event originates from the chanceClock, add either a Chance object or extra ball to the game.
     * @param event The ActionEvent occuring.
     */
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == animateClock)
        {
            //System.out.println("works");
            ArrayList<PingPong> pongList = gameTable.getPongList();
            gameTable.sortPongList();
            for(PingPong p : pongList) // Move each ball in the ballList
            {
                if(p.getCanMove() > 3000)
                {
                    p.move();
                }
                else
                {
                    p.incrementCanMove();
                }
            }

            bumperArray = gameTable.getBumperArray();
            for(int i=0; i < bumperArray.length; i++)
            {
                bumperArray[i].specialEvents(); // Grow or shrink w/ animation
            }

            // Move the bumpers if necessary
            if(bumperArray[0].stillPlaying() == true)
            {
                if(lu == true)
                {
                    bumperArray[0].move(false);
                }
                if(ld == true)
                {
                    bumperArray[0].move(true);
                }
            }
            if(bumperArray[1].stillPlaying() == true)
            {
                if(ru == true)
                {
                    bumperArray[1].move(false);
                }
                if(rd == true)
                {
                    bumperArray[1].move(true);
                }
            }
            if(bumperArray[2].stillPlaying() == true)
            {
                if(tl == true)
                {
                    bumperArray[2].move(false);
                }
                if(tr == true)
                {
                    bumperArray[2].move(true);
                }
            }
            if(bumperArray[3].stillPlaying() == true)
            {
                if(bl == true)
                {
                    bumperArray[3].move(false);
                }
                if(br == true)
                {
                    bumperArray[3].move(true);
                }
            }

            ArrayList<Chance> chanceList = gameTable.getChanceList();

            // Check for touching bumpers and Chance objects
            for(PingPong p : pongList)
            {
                for(int i=0; i < bumperArray.length; i++)
                {
                    if(p.isTouching(bumperArray[i]) == true)
                    {
                        if(bumperArray[i].stillPlaying() == true)
                        {
                            p.reflectionSpeed(bumperArray[i]);
                        }
                        else
                        {
                            p.bounceOff(bumperArray[i]);
                        }
                    }
                }

                ArrayList<Chance> removeChanceList = new ArrayList<Chance>();
                for(Chance c : chanceList)
                {
                    if(p.isTouching(c) == true && p.getColor() != Color.WHITE)
                    {   // Add the reciprocal Chance object to counter the effects after 10 seconds
                        removeChanceList.add(c);
                        if(c.getColor() == Color.MAGENTA || c.getColor() == Color.ORANGE)
                        {
                            p.getLastTouched().addPendingChance(c);
                            if(c.getColor() == Color.MAGENTA)
                            {
                                p.getLastTouched().addPendingChance(new Chance(1,true));
                            }
                            else
                            {
                                p.getLastTouched().addPendingChance(new Chance(0,true));
                            }
                        }
                        else
                        {
                            p.getLastTouched().addPendingChance(c);
                            if(c.getColor() == Color.PINK)
                            {
                                p.getLastTouched().addPendingChance(new Chance(3,true));
                            }
                            else
                            {
                                p.getLastTouched().addPendingChance(new Chance(2,true));
                            }
                        }
                    }
                }
                for(Chance c : removeChanceList)
                {
                    gameTable.removeChance(c);
                }
            }

            ArrayList<PingPong> removePongList = new ArrayList<PingPong>();
             for(PingPong p : pongList)
             {
                // Reset p if p has fallen off the table
                if(p.getXPosition() + PingPong.DIAMETER < LEFT_BOUND) // If p has fallen off the left side of the table
                {
                        p.resetBall(); // Reset p
                        bumperArray[0].score();
                        removePongList.add(p);
                }
                else if(p.getXPosition() > RIGHT_BOUND) // If p has fallen off the right side of the table
                 {
                         p.resetBall(); // Reset p
                         bumperArray[1].score();
                         removePongList.add(p);
                }
                else if(p.getYPosition() + PingPong.DIAMETER < UPPER_BOUND)
                {
                    p.resetBall();
                    bumperArray[2].score();
                    removePongList.add(p);
                }
                else if(p.getYPosition() > LOWER_BOUND)
                {
                    p.resetBall();
                    bumperArray[3].score();
                    removePongList.add(p);
                }
            }

            // PingPong Balls will not cumulate on the table
            if(removePongList.size() > 0 && pongList.size() > 1)
            {
                gameTable.removePong(removePongList.get(0));
            }

            // Check for winners
            int checkBumpersLeft = 4;
            int oneBumperLeft = 0;
            for(int i=0; i < bumperArray.length; i++)
            {
                if(bumperArray[i].getScore() <= 0)
                {
                    bumperArray[i].lose();
                    oneBumperLeft++;
                    checkBumpersLeft--;
                    // If a PingPong Ball had last touched a bumper that lost, set its last touched to null and set its color to white
                    for(PingPong p : pongList)
                    {
                        if(p.getLastTouched() == bumperArray[i])
                        {
                            p.setLastTouched(null);
                        }
                        if(checkBumpersLeft < bumpersLeft)
                        {
                            p.resetBall();
                        }
                    }
                }
            }
            bumpersLeft = checkBumpersLeft;

            if(oneBumperLeft == 3)
            {
                gameTable.endGame();
                gameTable.repaint();
                animateClock.stop();
                //newBallClock.stop();
                chanceClock.stop();
            }

            gameTable.repaint(); // Update the gameTable
        }

        //         if(event.getSource() == newBallClock)
        //         {
        //             //newBall.stop();
        //             gameTable.addPong();
        //         }

        if(event.getSource() == chanceClock)
        {
            int chanceEvent = generator.nextInt(5);
            //chanceEvent = 4;
            if(chanceEvent < 4)
            {
                gameTable.addChance(chanceEvent,false);
            }
            else
            {
                gameTable.addPong();
            }
        }
    }

    /**
     * If a certain key is pressed, its respective boolean is set to true.
     * @param e The key being pressed.
     */
    public void keyPressed(KeyEvent e)
    {
        //System.out.println(e);
        if(e.getKeyChar() == 'a')
        {
            lu = true;
            //System.out.println("Move up"); // Used before we decided to make boolean variables
        }
        if(e.getKeyChar() == 's')
        {
            ld = true;
        }
        if(e.getKeyChar() == 'f')
        {
            ru = true;
        }
        if(e.getKeyChar() == 'g')
        {
            rd = true;
        }
        if(e.getKeyChar() == 'j')
        {
            tl = true;
        }
        if(e.getKeyChar() == 'k')
        {
            tr = true;
        }
        if(e.getKeyChar() == ';')
        {
            bl = true;
        }
        if(e.getKeyChar() == '\'')
        {
            br = true;
        }
    }

    /**
     * If a certain key is released, its respective boolean is set to false.
     * @param e The key being released.
     */
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyChar() == 'a')
        {
            lu = false;
        }
        if(e.getKeyChar() == 's')
        {
            ld = false;
        }
        if(e.getKeyChar() == 'f')
        {
            ru = false;
        }
        if(e.getKeyChar() == 'g')
        {
            rd = false;
        }
        if(e.getKeyChar() == 'j')
        {
            tl = false;
        }
        if(e.getKeyChar() == 'k')
        {
            tr = false;
        }
        if(e.getKeyChar() == ';')
        {
            bl = false;
        }
        if(e.getKeyChar() == '\'')
        {
            br = false;
        }
    }

    /**
     * Typed keys influence what happens between games and how a new game is reset.
     * They're only effective between games.
     * @param e The key being typed.
     */
    public void keyTyped(KeyEvent e)
    {
        // Start/Restart the game
        if(e.getKeyChar() == ' ')
        {
            if(gameTable.stillPlaying() == false)
            {
                gameTable.startGame();
                playGame(false);
                animateClock.start(); // Start the clock
                //newBallClock.start();
                chanceClock.start();
                if(gameTable.isFirstOpened() == true)
                {
                    gameTable.playFirstTime();
                }
            }
        }
        // Reset the Game
        if(e.getKeyChar() == 'r')
        {
            if(gameTable.stillPlaying() == false)
            {
                gameTable.resetFirstOpened();
                playGame(true);
            }
        }

        //This was all the code I tried implementing to turn bumpers on and off so players didn't always have to start with 4 players.

        //         if(e.getKeyChar() == 'a')
        //         {
        //             if(gameTable.stillPlaying() == true && gameTable.isFirstOpened() == true)
        //             {
        //                 bumperArray[0].switchPlayed();
        //                 boolean alive = bumperArray[0].getPlayed();
        //                 bumperArray[0] = new Bumper(0,alive);
        //                 gameTable.repaint();
        //             }
        //         }
        //         if(e.getKeyChar() == 'f')
        //         {
        //             if(gameTable.stillPlaying() == true && gameTable.isFirstOpened() == true)
        //             {
        //                 bumperArray[1].switchPlayed();
        //                 boolean alive = bumperArray[1].getPlayed();
        //                 bumperArray[1] = new Bumper(1,alive);
        //                 gameTable.repaint();
        //             }
        //         }
        //         if(e.getKeyChar() == 'j')
        //         {
        //             if(gameTable.stillPlaying() == true && gameTable.isFirstOpened() == true)
        //             {
        //                 bumperArray[2].switchPlayed();
        //                 boolean alive = bumperArray[2].getPlayed();
        //                 bumperArray[2] = new Bumper(2,alive);
        //                 gameTable.repaint();
        //             }
        //         }
        //         if(e.getKeyChar() == ';')
        //         {
        //             if(gameTable.stillPlaying() == true && gameTable.isFirstOpened() == true)
        //             {
        //                 bumperArray[3].switchPlayed();
        //                 boolean alive = bumperArray[3].getPlayed();
        //                 bumperArray[3] = new Bumper(3,alive);
        //                 gameTable.repaint();
        //             }
        //         }
    }
}
