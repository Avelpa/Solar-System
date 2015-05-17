
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dmitry
 */
public class Planet {
    
    private double T; // orbit period
    private double r; // orbit radius
    private Color c; 
    private BufferedImage sprite;
    private int x, y;
    private int rP; // planet radius
    
    private double theta;
    private double increment;
    
    private double dotAngle;
    private int dotSpace = 10;
    
    public Planet(int rP, Color c, double r, double T, double kConstant )
    {
        this.r = r;
        this.T = T;
        
        if (r < 0)
        {
            this.r = Math.cbrt(Math.pow(T, 2)*kConstant);
        }
        else if (T < 0)
        {
            this.T = Math.sqrt(Math.pow(r, 3)/kConstant);
        }
        
        this.increment = -360.0/this.T; // angle which controls orbit (triangles.. a^2 + b^2 = c^2)
        
        this.c = c;
        this.rP = rP;
        
        this.dotAngle = 360/((2*Math.PI*this.r)/this.dotSpace); // orbital paths
    }
    public Planet(int rP, String imagePath, double r, double T, double kConstant )
    {
        this.r = r;
        this.T = T;
        
        if (r < 0)
        {
            this.r = Math.cbrt(Math.pow(T, 2)*kConstant);
        }
        else if (T < 0)
        {
            this.T = Math.sqrt(Math.pow(r, 3)/kConstant);
        }
        
        this.increment = -360.0/this.T;
        
        this.rP = rP;
        
        this.sprite = ImageHelper.loadImage("images\\" + imagePath);
        this.sprite = ImageHelper.resize(this.sprite, rP*2, rP*2);
        
        this.dotAngle = 360/((2*Math.PI*this.r)/this.dotSpace);
    }
    
    public void move()
    {

        x = (int)(r*Math.cos(Math.toRadians(theta)));
        y = (int)(r*Math.sin(Math.toRadians(theta)));

        theta += increment;
        if (theta >= 360)
        {
            theta = 0;
        }
    }
    
    public void zoom(double factor, int offsetX)
    {
        rP *= factor;
        sprite = ImageHelper.resize(sprite, rP, rP);
        x = Main.WIDTH/2 + (int)((x+offsetX-Main.WIDTH/2)*2);
    }
    
    public void draw(Graphics g, int X, int Y, int offsetX, int offsetY)
    {
        g.setColor(Color.GRAY);
        for (double i = this.dotAngle; i <= 360; i += this.dotAngle)
        {
            g.drawOval(X+(int)(r*Math.cos(Math.toRadians(i)))+offsetX, Y+(int)(r*Math.sin(Math.toRadians(i)))+offsetY, 1, 1);
        }
        
        if (c != null)
        {
            g.setColor(c);
            g.fillOval(X+x-rP+offsetX, Y+y-rP+offsetY, rP*2, rP*2);
        }
        else if (sprite != null)
        {
            g.drawImage(sprite, X+x-rP+offsetX, Y+y-rP+offsetY, null);
        }
        
//        g.setColor(Color.GREEN);
//        g.drawLine(X+offsetX, Y+offsetY, x+X+offsetX, y+Y+offsetY);
//        g.drawLine(X+offsetX, Y+offsetY, x+X+offsetX, Y+offsetY);
//        g.drawLine(x+X+offsetX, Y+offsetY, x+X+offsetX, y+Y+offsetY);
        
    }
    
}
