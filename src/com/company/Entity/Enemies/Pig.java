package com.company.Entity.Enemies;

import com.company.Entity.Animation;
import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;

/*! \class Pig
    \brief Reprezinta unul din cei trei inamici cu care jucatorul se va confrunta
    \Se misca stanga-dreapta si provoaca daune jucatorului la contact
    \Odata ce a atins eroul, se va indrepta spre directia opusa

 */
public class Pig extends Enemy {

    // actiuni animate
    private static final int IDLE = 0;/*< stare folosita in setarea animatiei */
    private static final int WALKING = 1;/*< stare folosita in setarea animatiei */
    private static final int HIT = 2;/*< stare folosita in setarea animatiei */
//    private static final int DEAD = 3;/*< stare folosita in setarea animatiei */
    private ArrayList<BufferedImage[]> sprites;/*< container-ul de imagini folosite la animatie */

    /*! \fn Pig(TileMap tm)
    \brief Constructorul clasei Pig, initializeaza parametrii fizici(cei necesari pentru a reprezenta
    obiectul pe ecran), imaginile folosite la animatie, flaguri si altii
 */
    public Pig(TileMap tm) {
        super(tm);

        //setarea dimensiunilor pe ecran + colision box
        width = 34;
        height = 28;
        cwidth = 15;
        cheight = 20;

        //parametrii de stare
        moveSpeed = 0.3;
        maxSpeed = 0.9;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        finished = false;

        health = maxHealth = 3;
        damage = 1;
        attackrange = 40;

        // incarcare sprites
        try {
            sprites = new ArrayList<BufferedImage[]>();
            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia

            BufferedImage idlesheet = ImageIO.read(new FileInputStream("Resources/Sprites/03-Pig/Idle (34x28).png"));
            BufferedImage[] bi = new BufferedImage[11];
            for (int j = 0; j < 11; j++) {
                bi[j] = idlesheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage walksheet = ImageIO.read(new FileInputStream("Resources/Sprites/03-Pig/Run (34x28).png"));
            bi = new BufferedImage[6];
            for (int j = 0; j < 6; j++) {
                bi[j] = walksheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage hitsheet = ImageIO.read(new FileInputStream("Resources/Sprites/03-Pig/Hit (34x28).png"));
            bi = new BufferedImage[2];
            for (int j = 0; j < 2; j++) {
                bi[j] = hitsheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

//            BufferedImage deadsheet = ImageIO.read(new FileInputStream("Resources/Sprites/03-Pig/Dead (34x28).png"));
//            bi = new BufferedImage[4];
//            for (int j = 0; j < 4; j++) {
//                bi[j] = deadsheet.getSubimage(j * width, 0, width, height);
//            }
//
//            sprites.add(bi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initilizare animatie si orientare default
        animation = new Animation();
        currentAction = WALKING;
        animation.setFrames(sprites.get(WALKING));
        animation.setDelay(50);

        right = true;
        facingRight = false;

    }

    /*! \fn private void getNextPosition()
        \brief Seteaza vectorul deplasare in functie de orientarea inamicului
        Verifica daca inamicul se afla in cadere si realizeaza actualizarea deplasarii
     */
    private void getNextPosition() {

        // miscare
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }

        // verifica caderea si opreste inamicul
        if (falling) {
            dy += fallSpeed;
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }


    }


    public boolean getFinished()
    {
        return finished;
    }

    /*! \fn public void update()
    \brief Actualizeaza flaguri si animatii pentru Pig

 */
    @Override
    public void update() {

        getNextPosition();
        //verifica coliziunea cu harta
        checkTileMapCollision();
        //in functie de coliziunie, se actualizeaza pozitia pe harta
        setPosition(xtemp, ytemp);


        if (currentAction == HIT) {
                if (animation.hasPlayedOnce()) {
                    isHit = false;
                }
        }

        //check flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 400) {
                flinching = false;
            }
        }

        // daca loveste un zid, deplasarea se reia in directia opusa
        if (right && dx == 0) {
            right = false;
            left = true;
            facingRight = true;
        } else if (left && dx == 0) {
            right = true;
            left = false;
            facingRight = false;
        }

        // verifica daca inamicul a fost lovit
        if(isHit)
        {
            if(currentAction != HIT)
            {
                currentAction = HIT;
                animation.setFrames(sprites.get(HIT));
                animation.setDelay(70);
            }
        }
        // verifica animatia de deplasare
         else   if (left || right) {
                if (currentAction != WALKING) {
                    currentAction = WALKING;
                    animation.setFrames(sprites.get(WALKING));
                    animation.setDelay(50);
                    width = 34;
                }
            }
        // verifica animatia de idle
         else {
             if (currentAction != IDLE) {
                 currentAction = IDLE;
                 animation.setFrames(sprites.get(IDLE));
                 animation.setDelay(50);
                 width = 34;
             }
         }

         // verifica caderea si actualizeaza flag-ul destinat acesteia
        if (falling) {
            dy += fallSpeed;
            if (dy > 0) falling = false;
        }

        // actualizeaza animatia
        animation.update();

        // verifica orientarea imaginii
        if (right) facingRight = false;
        if (left) facingRight = true;
    }


    /*! \fn public void hit(int damage)
        \brief verifica daca inamicul a suferit pagube
     */
    @Override
    public void hit(int damage) {
        super.hit(damage);
        isHit = true;
    }

    /*! \fn public Rectangle getRectangle()
    \brief Returneaza dreptughiul de coliziune
 */
    @Override
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth/2-5,
                (int)y - cheight/2+5,
                cwidth,
                cheight-5
        );
    }


    /*! \fn public void draw(java.awt.Graphics2D g)
        \brief deseneaza inamicul pe ecran, tinand cond si de flagul facing.
        Aceasta metoda a fost suprascrisa deoarece a fost nevoie sa modificam
        unele coordonate ale imaginii in mod special pentru Pig
     */
    public void draw(java.awt.Graphics2D g) {
        if (notOnScreen() == false) {
            setMapPosition();
            if (facingRight) {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 1.5),
                        (int) (y + ymap - height / 2), width, height,
                        null
                );
            } else {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap + width / 2.5),
                        (int) (y + ymap - height / 2),
                        -width,
                        height,
                        null
                );

            }
            //deseneaza dreptunghiul de coliziune
//            Rectangle r = getRectangle();
//            r.x += xmap;
//            r.y += ymap;
//            g.draw(r);
        }

    }
}
