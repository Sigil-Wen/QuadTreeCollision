/**
* [QuadTree.java]
* A program that displays a QuadTree w/ collision detection
* @author Sigil Wen
* @version 1.0
* @since 2020/11/6
*/

//Graphics &GUI imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//random number generation
import java.util.Random;

//Rectangle class for boundaries
import java.awt.Rectangle;

class QuadTreeDisplay extends JFrame { 

   static QuadTree tree; 
   static SimpleLinkedList<BouncingBalls> ballList = new SimpleLinkedList<BouncingBalls>();
   static GameAreaPanel gamePanel;  
   static Random r = new Random();
   static int screenSize = 512;
   static Rectangle rect = new Rectangle(0,0,screenSize,screenSize);

  /**[main]
   * Main method!
   * @param args
   */
   public static void main(String[] args) {
     new QuadTreeDisplay ();
   }

  /**[QuadTree Display]
   * called within the main method. Starts the game
   */
  QuadTreeDisplay () {
    super("QuadTree Fun!");      
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    gamePanel = new GameAreaPanel();
    this.add(new GameAreaPanel());    
    MyKeyListener keyListener = new MyKeyListener();
    this.addKeyListener(keyListener);
    this.requestFocusInWindow();    
    this.setVisible(true);
    
    //***** Initialize the Tree *****
    tree = new QuadTree(rect,1);

    Thread t = new Thread(new Runnable() { public void run() { animate(); }}); //start the gameLoop 
    t.start();
  } //End of Constructor

  //the main game loop
  public void animate() {
    while(true){
    try{ Thread.sleep(50);} catch (Exception exc){exc.printStackTrace();}  //delay
      this.repaint();
    }    
  }
  
  /** --------- INNER CLASSES ------------- **/
  
  // Inner class for the the game area - This is where all the drawing of the screen occurs
  private class GameAreaPanel extends JPanel {
    public void paintComponent(Graphics g) {   
       super.paintComponent(g); //required
       setDoubleBuffered(true); 
       g.setColor(Color.BLUE); //There are many graphics commands that Java can use
      
      //create new tree
      tree = new QuadTree(rect,1);

      //adds balls from ballList to QuadTree
      for(int i = 0; i < ballList.size(); i ++){
        tree.add(ballList.get(i));
      }

      //calls command to draw QuadTree and subdivided rectangles
       recursiveDrawRect(g,tree);

       ///***** draws balls on the screen *****
     
      for(int i = 0; i < ballList.size(); i ++){
        //sets color
        Color myColor = new Color(ballList.get(i).getColor()[0],ballList.get(i).getColor()[1],ballList.get(i).getColor()[2]);
        g.setColor(myColor);
        //draw individual circles
        g.fillOval((int)Math.round(ballList.get(i).getX()), (int)Math.round(ballList.get(i).getY()),ballList.get(i).getDiameter(),ballList.get(i).getDiameter());
      }

      //detects wall collisions
      for(int i = 0; i < ballList.size(); i ++){

        //if x values go out of bounds, reverse the velocity
        if(ballList.get(i).getX() + ballList.get(i).getDiameter() > screenSize || ballList.get(i).getX() +  ballList.get(i).getDiameter()< 0){
           ballList.get(i).setXVel(-1.0* ballList.get(i).getXVel());
        }

        //if y values go out of bounds, reverse the velocity
        if(ballList.get(i).getY() + ballList.get(i).getDiameter() > screenSize || ballList.get(i).getY() +  ballList.get(i).getDiameter()< 0){
          ballList.get(i).setYVel(-1.0* ballList.get(i).getYVel());
       }
      }

      //checks in each leaf of the quad trees for collisions and updates velocities
      tree.quadTreeCollision();

      //moves every ball
      for(int i = 0; i < ballList.size(); i ++){
        ballList.get(i).move();
      }
    }

    /** [recursiveDrawRect]
     * Program draws the quad tree division by recursive through each QuadTree node and their children
     * @param g Graphics to draw
     * @param tree Passing through the "head" of the QuadTree
     */
    public void recursiveDrawRect(Graphics g, QuadTree tree){
      //draw the rectangle of current node
      g.drawRect((int)tree.getBoundary().getX(), (int)tree.getBoundary().getY(), (int)tree.getBoundary().getWidth(), (int)tree.getBoundary().getHeight());
      //when not a leaf, recurse to children and draw their rectangles
      if(tree.isLeaf() == false){
        for(int i = 0; i < 4; i ++){
          recursiveDrawRect(g,tree.getChildren()[i]);
        }
      }
    }
  }
 
   // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code
    private class MyKeyListener implements KeyListener {
  
      public void keyTyped(KeyEvent e) {}
      public void keyReleased(KeyEvent e) {}
      public void keyPressed(KeyEvent e) {
       
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
          //add a random ball
          BouncingBalls tempBall;
          
          int num1 = r.nextInt(screenSize - 40)+20; //Ensure that the edge of the ball spawns inside the box
          int num2 = r.nextInt(screenSize - 40)+20 ;
          double xVel = 2*r.nextDouble()-1; //variable for random velocities
          double yVel = 2*r.nextDouble()-1;
          int diameter; //variable for random radius

          /* generate random colors
          int[] randomColor = new int[3]; // variable for random color
          for(int i = 0; i < 3; i ++){
            randomColor[i] = (int)Math.round(255.0*r.nextDouble());
          }
          //generate random radius
          //diameter = r.nextInt(25) + 10;
          */
          //sets diameter to 10
          diameter = 10;
          //creating a new blue ball
          tempBall = new BouncingBalls(num1,num2,xVel,yVel, diameter, new int[]{0,0,255});
          //adding a new ball
          ballList.add(tempBall);
          System.out.println("Adding Ball!");
          //escape key exists program
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { 
          System.out.println("YIKES ESCAPE KEY!"); 
          System.exit(0);
        } 
      }              
    }  
}


  


