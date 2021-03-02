package com.company.TileMap;
import com.company.GamePanel;


import java.awt.image.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.Serializable;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.ImageIO.*;
import javax.swing.*;

/*! \class Background
    \brief Clasa care se ocupa cu configurarea si desenarea imaginii de background
 */
public class Background {
    private transient BufferedImage image;/*< Imaginea de desenat */

    private double x;/*< coordonata x in planul ecranului */
    private double y;/*< coordonata y in planul ecranului */
    private double dx;/*< deplasarea pe axa absciselor a imaginii */
    private double dy;/*< deplasarea pe axa ordonatelor a imaginii */

    private double moveScale;/*< parametrul determina cat de repede se misca imaginea*/

    /*! \fn public Background(String s, double ms)
        \brief Constructorul clasei, incarca imeaginea
        \param s Numele imaginii
        \param ms Parametrul moveScale
     */
    public Background(String s, double ms)
    {
        try{
            image = ImageIO.read(new FileInputStream(s));
            moveScale=ms;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public void setPosition(double x, double y)
        \brief Seteaza pozitia pe ecran a imaginii

     */
    public void setPosition(double x, double y)
    {
        this.x=(x * moveScale) % GamePanel.WIDTH;//pentru a nu trece de ecran
        this.y=(y * moveScale) % GamePanel.HEIGHT;//pentru a nu trece de ecran
    }

    /*! \fn public double getX()
        \brief Returneaza x.
     */
    public double getX() {
        return x;
    }

    /*! \fn public double getY()
        \brief Returneaza y.
     */
    public double getY() {
        return y;
    }

    /*! \fn public void setVector(double dx, double dy)
        \brief Seteaza vectorul deplasare.
     */
    public void setVector(double dx, double dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    /*! \fn public void update()
        \brief Actualizeaza imaginea.
     */
    public void update()
    {
        x+=dx;
        y+=dy;
    }

    /*! \fn public void draw(Graphics2D g)
        \brief deseneaza imaginea si realizeaza efectul de repetare
     */
    public void draw(Graphics2D g)
    {
        g.drawImage(image, (int)x, (int)y,null);
        if(x<0)
        {
            g.drawImage(image, (int)x+ GamePanel.WIDTH,(int)y,null);
        }
        if(x>0)
        {
             g.drawImage(image, (int)x-GamePanel.WIDTH,(int)y,null);
        }
    }

}
