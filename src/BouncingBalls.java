/**
 * [BouncingBalls.java]
 * Program is an object for bouncing balls
 * @author Sigil Wen
 * @version 1.0
 * @since 2020.11.6
 */

public class BouncingBalls {
    //x coordinate of the ball
    private double x;
    //y coordinate of the ball
    private double y;
    //x velocity of the ball
    private double xVel; 
    //y velocity of the ball
    private double yVel;
    //diameter of the ball
    private int diameter;
    //color of the ball
    private int[] color;

    /**[BouncingBalls]
     * Constructs a point object with speed and coordinates in both x and y axis
     * @param x The x coordinate of the ball 
     * @param y the y coordinate of the ball
     * @param xVel The x velocity of ball
     * @param yVel y velocity of ball
     * @param diameter radius of ball
     * @param color color of ball in RGB
     */
    BouncingBalls(double x, double y, double xVel, double yVel, int diameter, int[] color){
        this.x = x;
        this.y = y;
        this.xVel = xVel;
        this.yVel = yVel;
        this.diameter = diameter;
        this.color = color;
    }

    /**[getX]
     * @return returns the x position of the ball
     */
    public double getX(){
        return this.x;
    }

    /**[getY]
     * @return returns the y position of the ball
     */
    public double getY(){
        return this.y;
    }

    /**[getXVel]
     * @return returns xVel
     */
    public double getXVel(){
        return this.xVel;
    }

    /**[getYVel]
     * @return returns yVel
     */
    public double getYVel(){
        return this.yVel;
    }

    /**[setXVel]
     * @param xVel sets Object's xVel = xVel passed through
     */
    public void setXVel(double xVel){
        this.xVel = xVel;
    }

    /**[setYVel]
     * @param yVel sets Object's yVel = yVel passed through
     */
    public void setYVel(double yVel){
        this.yVel = yVel;
    }

    /** [move]
     * change the ball's x and y coodinate according to velocity
    */
    public void move(){
        this.x += this.xVel;
        this.y += this.yVel;
    }

    /** [getColor]
     * @return returns the color of the ball in RGB
     */
    public int[] getColor(){
        return this.color;
    }

    /**[getDiameter]
     * @return returns the radius of the ball
     */
    public int getDiameter(){
        return this.diameter;
    }

    /**[getXCentre]
     * @return returns the x coordinate of center of the ball
     */
    public int getXCentre(){
        return (int)(this.x + this.diameter/2);
    }

    /**[getYCentre]
     * @return returns the y coordinate of center of ball
     */
    public int getYCentre(){
        return (int)(this.y + this.diameter/2);
    }
}
