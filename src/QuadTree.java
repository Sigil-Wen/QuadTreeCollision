/**
 * [QuadTree.java]
 * Program creates QuadTrees that subdivides
 * @author Sigil Wen
 * @version 1.0
 * @since 2020/11/6
 */

//need Rectangle class for boundaries
import java.awt.Rectangle;

public class QuadTree {

    private int MAXDEPTH = 5;  //max number of subdivisions can you make
    private int THRESHOLD = 4;  //max number of balls before a subdivision occurs
    private int currentDepth; //amount of subdivision that has been done/ current one
    private QuadTree[] children = new QuadTree[4] ;  //children nodes (null if not subdivided)
    private SimpleLinkedList<BouncingBalls> ballList = new SimpleLinkedList<BouncingBalls>();  //the homemade list of balls
    private Rectangle boundary;  //the region on the screen that this node applies to

    /**[QuadTree]
     * Constructor for the QuadTree
     * @param r
     * @param currentDepth
     */
    QuadTree(Rectangle r,int currentDepth){
        this.boundary = r;
        this.currentDepth = currentDepth;
    }

    /**[isLeaf]
     * Method checks if the current node is a leaf
     */
    public boolean isLeaf(){
        if (children[0] == null){
            return true;
        }else{
            return false;
        }
    }

    /**[add]
     * Program pushes in a ball and pass it along to the children/leaves if necessary, also calls subdivid method to create children if necessary
     * @param ball The ball object being pushed into the tree
     */
    public void add(BouncingBalls ball){
        //check if max depth has been reached
        if(currentDepth <= MAXDEPTH){
            if(isLeaf() == false){ //is not a leaf, adds to children
                //adds item to children node
                children[findQuadrant(ball)].add(ball);
            }else if(ballList.size() < THRESHOLD){//is leaf and doesn't need dividing, adds to list
            //adds item to current list
                ballList.add(ball);
            }else{
                //creates children nodes
                subdivide();
                //adds each item to children nodes
                for(int i = 0; i < ballList.size(); i ++){
                    children[findQuadrant(ballList.get(i))].add(ballList.get(i));
                }
            }
        //adds to current list if max depth is reached
        }else{
            ballList.add(ball);
        }
    }

    /**[findQuadrant]
     * Program passes in a ball and checks for the quadrant that it's supposed to be in according to the children of current node
     * @param ball the ball object that is being checked
     * @return returns the quadrant of which the ball object belongs in, 0 is north west, 1 is north east, 2 is south east, 3 is south west
     */
    private int findQuadrant(BouncingBalls ball){
        //Uses if states to check the location of the ball's x and y values to determine which quadrant they belong in
        if( ball.getX() <= boundary.getX() + boundary.getWidth()/2 && ball.getY() <= boundary.getY() + boundary.getHeight()/2 ){
            return 0;
        }else if( ball.getX() >= boundary.getX() + boundary.getWidth()/2 && ball.getY() <= boundary.getY() + boundary.getHeight()/2 ){
            return 1;
        }else if( ball.getX() >= boundary.getX() + boundary.getWidth()/2 && ball.getY() >= boundary.getY() + boundary.getHeight()/2 ){
            return 2;
        }else{
            return 3;
        }
    }

    /**[subdivide]
     * Method creates a new rectangle object for the creation of subsequent children nodes for the quad tree
     */
    public void subdivide(){
        /**North west rectangle for division */
        Rectangle nw = new Rectangle((int)boundary.getX(), (int)boundary.getY(), (int)boundary.getHeight()/2, (int)boundary.getWidth()/2);
        /** North east rectangle for division*/
        Rectangle ne = new Rectangle((int)boundary.getX() + (int)boundary.getWidth()/2, (int)boundary.getY(), (int)boundary.getHeight()/2, (int)boundary.getWidth()/2);
        /**South east rectangle for division */
        Rectangle se = new Rectangle((int)boundary.getX() + (int)boundary.getWidth()/2, (int)boundary.getY() + (int)boundary.getHeight()/2, (int)boundary.getHeight()/2, (int)boundary.getWidth()/2);
        /**South west rectangle for division */
        Rectangle sw = new Rectangle((int)boundary.getX(), (int)boundary.getY() + (int)boundary.getHeight()/2, (int)boundary.getHeight()/2, (int)boundary.getWidth()/2);
        //creates the children of the node
        this.children[0] = new QuadTree(nw, currentDepth + 1);
        this.children[1] = new QuadTree(ne, currentDepth + 1);
        this.children[2] = new QuadTree(se,  currentDepth + 1);
        this.children[3] = new QuadTree(sw,  currentDepth + 1);
    }

    /**[getBoundary]
     * Method returns the rectangle object bounding the node
     * @return the rectangle object bounding the node
    */
    public Rectangle getBoundary(){
        return this.boundary;
    }

    /**[getChildren]
     * @return method returns the children of the node
     */
    public QuadTree[] getChildren(){
        return this.children;
    }

    /**[getBouncingBalls]
     * @return method returns the list of balls 
     */
    public SimpleLinkedList<BouncingBalls> getBouncingBalls(){
        return ballList;
    }
    

    /**[quadTreeCollision]
     * Recursive method that checks first if quad tree itself is a leaf node, if it is not, it continues recursively in children nodes
     */
    public void quadTreeCollision(){
        if(isLeaf() == true){
            for(int i = 0; i < ballList.size()-1; i ++){
                for(int j = i +1; j < ballList.size(); j ++){
                    collision(ballList.get(i), ballList.get(j));
                }
            }
        }else{
            children[0].quadTreeCollision();
            children[1].quadTreeCollision();
            children[2].quadTreeCollision();
            children[3].quadTreeCollision();
        }
    }

    /**[collision]
     * takes item1 and item2 checks if the 2 balls objects collided, if they did, velocities of each change accordingly
     * @param item1 item 1 of comparison
     * @param item2 item 2 of comparison
     */
    private void collision(BouncingBalls item1, BouncingBalls item2){
        //x and y coordinates of the center of the balls
        int x1 = item1.getXCentre();
        int y1 = item1.getYCentre();
        int x2 = item2.getXCentre();
        int y2 = item2.getYCentre();
        int deltaX = x1 - x2;
        int deltaY = y1 - y2;

        //get mass of each item, for this program mass will equal to area
        double mass1 = Math.PI * Math.pow(item1.getDiameter()/2.0,2);
        double mass2 = Math.PI * Math.pow(item2.getDiameter()/2.0,2);

        //distance between the two centers
        double distance = Math.sqrt( Math.pow( deltaX , 2 ) + Math.pow( deltaY , 2 ) );

        //if balls are touching
        if(distance <= item1.getDiameter()/2.0 + item2.getDiameter()/2.0){
            
            System.out.println("Collision!");
            //initial velocities
            double xVel1 = item1.getXVel();
            double yVel1 = item1.getYVel();
            double xVel2 = item2.getXVel();
            double yVel2 = item2.getYVel();

            double FXVel1 = xVel1*(mass1 - mass2)/(mass1+mass2) + xVel2*(2*mass2)/(mass1+mass2);
            double FYVel1 = yVel1*(mass1 - mass2)/(mass1+mass2) + yVel2*(2*mass2)/(mass1+mass2);
            double FXVel2 = xVel2*(mass1 - mass2)/(mass1+mass2) + xVel1*(2*mass1)/(mass1+mass2);
            double FYVel2 = yVel2*(mass1 - mass2)/(mass1+mass2) + yVel1*(2*mass1)/(mass1+mass2);

            //set velocities after change
            item1.setXVel(FXVel1);
            item1.setYVel(FYVel1);
            item2.setXVel(FXVel2);
            item2.setYVel(FYVel2);

            /* An attempt at fixing clips :( Math hardy
            double newX1 = item1.getXCentre()+item1.getXVel();
            double newY1 = item1.getYCentre()+item1.getYVel();
            double newX2 = item2.getXCentre()+item2.getXVel();
            double newY2 = item2.getYCentre()+item2.getYVel();

            double dX = newX1 - newX2;
            double dY = newY1 - newY2;

            double distance2 = Math.sqrt( Math.pow( dX , 2 ) + Math.pow( dY , 2 ) );

            //checks if balls clip
            if(distance2 <= item1.getDiameter()/2.0 + item2.getDiameter()/2.0 ){
                double slope = (newY2 - newY1)/(newX2-newX1);
                double difference = item1.getDiameter()/2.0 + item2.getDiameter()/2.0 - distance2;
            }
             */
        }
    }
}
