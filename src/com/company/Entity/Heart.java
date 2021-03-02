package com.company.Entity;

import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

/*! \class Heart
    \brief Reprezinta o descriere a singurului obiect colectabil din joc.
    Daca jucatorul colecteaza acest obiect, viata sa va creste +1
 */
public class Heart extends MapObject {
    // actiuni animate
    private static final int IDLE = 0;/*< stare folosita in setarea animatiei */
    private boolean isOn;/*< Flag ce indica daca inima poate exista pe ecran */

    private BufferedImage[] sprites;/*< container-ul de imagini folosite la animatie */

    /*! \fn public Heart(TileMap m)
        \brief Constructorul clasei
     */
    public Heart(TileMap tm)
    {
        super(tm);

        //setarea dimensiunilor pe ecran + colision box
        width = 32;
        height = 32;
        cwidth = 32;
        cheight = 32;

        //parametrii de stare
        fallSpeed = 0.3;
        maxFallSpeed = 0.9;
        isOn = true;

        // incarcare sprites
        try {
            sprites = new BufferedImage[1];
            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia

            BufferedImage idlesheet = ImageIO.read(new FileInputStream("Resources/heart pixel art/heart pixel art 32x32.png"));
            sprites[0] = idlesheet;

        } catch (Exception e) {
            e.printStackTrace();
        }

        //initilizare animatie si orientare default
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites);
        animation.setDelay(50);

        facingRight = true;

    }

    /*! \fn PlayerNearby(Player p)
        \brief Seteaza flagul isOn false daca jucatorul a luat contact cu item-ul
    */
    public boolean PlayerNearby(Player p)
    {
        if(this.intersects(p))
        {
            // dezactiveaza usa
            isOn = false;

            // incrementeaza viata jucatorului
            if(p.getHealth()<p.getMaxHealth()) {
                p.setHealth(p.getHealth()+1);
            }
            return true;
        }
        return false;
    }

    /*! \fn public void update()
        \brief Actualizeaza flaguri si animatii pentru PigKing
    */
    public void update()
    {
        // verifica coliziunea cu harta
        checkTileMapCollision();

        // in functie de coliziunie, se actualizeaza pozitia pe harta
        setPosition(xtemp,ytemp);

        // daca item-ul este activ, updateaza pozitia
        if(isOn) {
            if (currentAction == IDLE) {
                animation.update();
            }

            if (falling) {
                dy += fallSpeed;
                if (dy > 0) falling = false;
            }
        }
    }

    /*! \fn public void draw(Graphics2D g)
        \brief Deseneaza inima cat timp este activa
     */
    public void draw(Graphics2D g)
    {
        if(isOn)
        {
            super.draw(g);
        }
    }

}
