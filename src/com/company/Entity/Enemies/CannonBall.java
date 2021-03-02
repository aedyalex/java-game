package com.company.Entity.Enemies;

import com.company.Entity.Animation;
import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

/*! \class CannonBall
    \brief Clasa reprezinta proiectilul aruncat de Cannon

 */
public class CannonBall extends Enemy {

    private boolean hit;/*< flag ce determina daca proiectilul a lovit jucatorul */
    private boolean remove;/*< flag ce determina daca proiectilul trebuie sa dispara de pe harta */
    private BufferedImage[] sprite;/*< container-ul de imagini folosite la animatie */

    public CannonBall(TileMap tm, boolean right)
    {
        super(tm);

        //setarea dimensiunilor pe ecran + colision box
        width = 44;
        height = 28;
        cwidth = 20;
        cheight = 14;

        //parametrii de stare
        facingRight = right;
        moveSpeed = 4.0;
        damage = 1;

        //in functie de dispunerea proiectilului, se stabileste sensul miscarii sale
        if(right) dx = moveSpeed;
        else dx = -moveSpeed;

        // incarcare sprites
        try{

            sprite = new BufferedImage[1];

            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia

            sprite[0] = ImageIO.read(new FileInputStream("Resources/Sprites/10-Cannon/Cannon Ball.png"));

            //initilizare animatie si orientare default
            animation = new Animation();
            animation.setFrames(sprite);
            animation.setDelay(50);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public void setHit()
        \brief Verifica daca proiectilul a atins jucatorul.
     */
    public void setHit()
    {
        if(hit) return;
        hit = true;
        dx=0;
    }

    /*! \fn public boolean shouldRemove()
        \brief Returneaza flag-ul remove.
     */
    public boolean shouldRemove()
    {
        return remove;
    }

    /*! \fn public void update()
        \brief Actualizeaza flaguri si animatii pentru proiectil.
     */
    public void update()
    {
        checkTileMapCollision();
        setPosition(xtemp,ytemp);

        if(dx==0 && !hit)
        {
            setHit();
        }

        animation.update();
        if(hit && animation.hasPlayedOnce())
        {
            remove = true;
        }
    }

    /*! \fn public void draw(java.awt.Graphics2D g)
    \brief Deseneaza bila cat timp este necesara prezenta ei pe ecran.
 */
    @Override
    public void draw(Graphics2D g) {
        if(!remove) {
            super.draw(g);
        }
    }

    /*! \fn public Rectangle getRectangle()
        \brief Returneaza dreptughiul de coliziune.
    */
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth,
                (int)y - cheight/2,
                cwidth,
                cheight
        );
    }
}
