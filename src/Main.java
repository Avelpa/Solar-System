import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;


// make sure you rename this class if you are doing a copy/paste
public class Main extends JComponent implements KeyListener{

    // Height and Width of our game
    static final int WIDTH = 1200;
    static final int HEIGHT = 850;
    
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000)/desiredFPS;
    
   
    
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    int X = Main.WIDTH/2, Y = Main.HEIGHT/2;
    int rS = 50;
    
    boolean u, d, l, r;
    boolean snapToSun;
    
    double offsetX = 0;
    double offsetY = 0;
    double snapX = -1000;
    double snapY = -1000;
    int offsetScale = 10;
    
    double kConstant = 60;
    
    // radius of earth's orbit = 1.5e8 km
    // 1 pixel = 2440/3 km
    
    Planet[] planets = {
        new Planet(5, "Mercury.png", 100, -1, kConstant), //mercury
        new Planet(13, "Venus.png", 200, -1, kConstant), //venus 
        new Planet(13, "Earth.png", 250, -1, kConstant), //earth
        new Planet(7, "Mars.png", 300, -1, kConstant), //mars
        new Planet(29, "Jupiter.png", 800, -1, kConstant), //jupiter
        new Planet(40, "saturn.png", 1000, -1, kConstant), // saturn <-- FIX
        new Planet(20, "neptune.png", 1400, -1, kConstant),
    
    };
    
    BufferedImage sun = ImageHelper.loadImage("images\\sun.png");
    
    
    @Override
    public void paintComponent(Graphics g)
    {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        
        // GAME DRAWING GOES HERE 
        if (sun != null)
        {
            g.drawImage(sun, X-rS+(int)offsetX, Y-rS+(int)offsetY, null);
        }
        else
        {
            g.setColor(Color.YELLOW);
            g.fillOval(X-rS+(int)offsetX, Y-rS+(int)offsetY, rS*2, rS*2);
        }
        
        for (Planet p: planets)
        {
            p.draw(g, X, Y, (int)offsetX, (int)offsetY);
        }
        // GAME DRAWING ENDS HERE
    }
    
    
    // The main game loop
    // In here is where all the logic for my game will go
    public void run()
    {
        sun = ImageHelper.resize(sun, rS*2, rS*2);
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;
        
        // the main game loop section
        // game will end if you set done = false;
        boolean done = false; 
        while(!done)
        {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();
            
            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            if (!snapToSun)
            {
                for (Planet p: planets)
                {
                    p.move();
                }
                if (u)
                {
                    offsetY += offsetScale;
                }
                if (d)
                {
                    offsetY -= offsetScale;
                }
                if (l)
                {
                    offsetX += offsetScale;
                }
                if (r)
                {
                    offsetX -= offsetScale;
                }
            }
            else
            {
               
                if (snapX == -1000)
                {
                    snapX = -offsetX/60;
                }
                if (snapY == -1000)
                {
                    snapY = -offsetY/60;
                }
                if (Math.abs(offsetX) > 2 || Math.abs(offsetY) > 2)
                {
                    if (Math.abs(offsetX) > 2)
                        offsetX += snapX;
                    if (Math.abs(offsetY) > 2)
                        offsetY += snapY;
                }
                else
                {
                    offsetX = 0;
                    offsetY = 0;
                    snapX = -1000;
                    snapY = -1000;
                    snapToSun = false;
                }
            }
            
            // GAME LOGIC ENDS HERE 
            
            // update the drawing (calls paintComponent)
            repaint();
            
            
            
            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if(deltaTime > desiredTime)
            {
                //took too much time, don't wait
            }else
            {
                try
                {
                    Thread.sleep(desiredTime - deltaTime);
                }catch(Exception e){};
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");
       
        // creates an instance of my game
        Main game = new Main();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        // adds the game to the window
        frame.add(game);
         
        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            u = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            d = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            l = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            r = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_W)
        {
//            for (Planet p: planets)
//            {
//                p.zoom(2, offsetX);
//            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            u = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            d = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            l = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            r = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            snapToSun = true;
        }
            
        
    }
}