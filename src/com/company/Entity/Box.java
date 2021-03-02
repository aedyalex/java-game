package com.company.Entity;

import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
/*! \class Box
    \brief Descrie cutia folosita de jucator ca sa sara pe teren inalt
 */
public class Box extends MapObject {
    private BufferedImage[] image;/*< Imaginea cutiei */
    private int health;/*< Viata cutiei */

    /*! \fn Box(TileMap tm)
    \brief Constructorul clasei Box, initializeaza parametrii fizici(cei necesari pentru a reprezenta
    obiectul pe ecran), imaginile folosite la animatie, flaguri si altii
*/
    public Box(TileMap tm) {
        super(tm);

        //setarea dimensiunilor pe ecran + colision box
        width = 32;
        height = 26;
        cwidth = 32;
        cheight = 26;

        //parametrii de stare
        moveSpeed = 3;
        facingRight = true;
        right = true;
        fallSpeed = 0.2;
        stopSpeed = 0.2;
        health = 1;

        // incarcare sprites
        try {
            image = new BufferedImage[1];
            image[0] = ImageIO.read(new FileInputStream("Resources/Sprites/08-Box/Idle.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initilizare animatie
        animation = new Animation();
        animation.setFrames(image);
        animation.setDelay(50);
    }

    /*! \fn public void update()
        \brief Actualizeaza flaguri si animatii pentru Box
    */
    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //falling
        if (falling) {
            dy += fallSpeed;
            if (dy > 0) falling = false;
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < stopSpeed) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > stopSpeed) {
                    dx = 0;
                }
            }
        }
    }

    /*! \fn public void move(double x, double y)
        \brief Imprima viteza altui obiect asupra cutiei

     */
    public void move(double x, double y)
    {
        dx = x;
        dy = y;
    }

    /*! \fn public void draw(Graphics2D g)
        \brief Deseneaza cutia
     */
    @Override
    public void draw(Graphics2D g) {
        if(health!=0)
        {
            super.draw(g);
        }
    }
}
