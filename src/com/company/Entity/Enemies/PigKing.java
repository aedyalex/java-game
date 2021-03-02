package com.company.Entity.Enemies;

import com.company.Entity.Animation;
import com.company.Entity.Player;
import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;

/*! \class PigKing
    \brief Cel mai puternic inamic, eroul trebuie sa se confrunte cu acesta in nivelul 2.
    Pe langa patrula Pig-ului, acest inamic poate sa urmareasca pozitia jucatorului, astfel urmarindu-l
    Are cele mai multe puncte de viata
 */

public class PigKing extends Enemy {
    // actiuni animate
    private static final int IDLE = 0;/*< stare folosita in setarea animatiei */
    private static final int WALKING = 1;/*< stare folosita in setarea animatiei */
    private static final int HIT = 2;/*< stare folosita in setarea animatiei */

    protected int rangeOfVision = 300;

    private ArrayList<BufferedImage[]> sprites;/*< container-ul de imagini folosite la animatie */

    /*! \fn PigKing(TileMap tm)
        \brief Constructorul clasei PigKing, initializeaza parametrii fizici(cei necesari pentru a reprezenta
        obiectul pe ecran), imaginile folosite la animatie, flaguri si altii
*/
    public PigKing(TileMap tm)
    {
        super(tm);

        //setarea dimensiunilor pe ecran + colision box
        width = 38;
        height = 28;
        cwidth = 16;
        cheight = 22;

        //parametrii de stare
        moveSpeed = 0.3;
        maxSpeed = 0.4;
        fallSpeed = 0.2;
        maxFallSpeed = 4.0;
        jumpStart = -6.0;
        stopJumpSpeed = 0.6;

        health = maxHealth = 5;
        damage = 1;
        attackrange = 40;

        // incarcare sprites
        try {
            sprites = new ArrayList<BufferedImage[]>();

            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia

            BufferedImage idlesheet = ImageIO.read(new FileInputStream("Resources/Sprites/02-King Pig/Idle (38x28).png"));
            BufferedImage[] bi = new BufferedImage[12];
            for (int j = 0; j < 12; j++) {
                bi[j] = idlesheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage walksheet = ImageIO.read(new FileInputStream("Resources/Sprites/02-King Pig/Run (38x28).png"));
            bi = new BufferedImage[6];
            for (int j = 0; j < 6; j++) {
                bi[j] = walksheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage hitsheet = ImageIO.read(new FileInputStream("Resources/Sprites/02-King Pig/Hit (38x28).png"));
            bi = new BufferedImage[2];
            for (int j = 0; j < 2; j++) {
                bi[j] = hitsheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //initilizare animatie si orientare default
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(50);

        left = true;
        facingRight = true;

    }

    /*! \fn public void InteractionWithPlayer(Player p)
        \brief Aceasta metoda este specifica PigKing-ulul.
         Verifica daca eroul este in preajma inamicului, si determina orientarea acestuia din urma
         spre jucator.
     */
    public void InteractionWithPlayer(Player p)
    {
        PlayerNearby(p);

        // Verifica daca jucatorul este in preajma iar daca nu esta mort porneste spre el
        if(playerNearby && (!p.isDead() || !p.getHiting()))
        {

            if(p.getx() < x && p.gety() +20 >= y)
            {
                right = false;
                left = true;
                facingRight = true;
            }
            if( p.getx() > x && p.gety()+20 >= y)
            {
                right = true;
                left = false;
                facingRight = false;
            }

            //
//            if(dx==0)
//            {
//                if(!jumping) {
//                    jumping = true;
//                    if (!falling) {
//                        dy = jumpStart;
//                        falling = true;
//                        jumping = false;
//                    }
//                }

                if(falling) {

                    if(dy > 0) dy += fallSpeed * 0.1;
                    else dy += fallSpeed;

                    if(dy > 0) jumping = false;
                    if(dy < 0 && !jumping) dy += stopJumpSpeed;

                    if(dy > maxFallSpeed) dy = maxFallSpeed;

                }
            }
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

        // daca a fost lovit, imprima o miscare scurta inapoi
        if (isHit) {
            if (right) {
                dx -= 0.3;
                right = false;
                left = true;
            } else if (!facingRight || left) {
                dx += 0.3;
                left = false;
                right = true;
            }
        }

        // verifica caderea si opreste inamicul
        if (falling) {
            dy += fallSpeed * 0.1;
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
    /*! \fn public void update()
        \brief Actualizeaza flaguri si animatii pentru PigKing

 */
    public void update()
    {
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

    /*! \fn public void draw(java.awt.Graphics2D g)
        \brief Deseneaza inamicul cat timp este in viata
     */
    public void draw(Graphics2D g) {
        if (health !=0) {
            super.draw(g);
        }
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
                (int)x - cwidth/2,
                (int)y - cheight/2,
                cwidth,
                cheight
        );
    }

}
