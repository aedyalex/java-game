package com.company.Entity.Enemies;

import com.company.Entity.Animation;
import com.company.Entity.Player;
import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;

/*! \class Cannon
    \brief Reprezinta unul din cei trei inamici cu care jucatorul se va confrunta
    \Nu se misca efectiv pe harta, dar arunca proiectile periculoase

 */
public class Cannon extends Enemy {
    // actiuni animate
    private static final int IDLE = 0;/*< stare folosita in setarea animatiei */
    private static final int SHOOT = 1;/*< stare folosita in setarea animatiei */
    private ArrayList<BufferedImage[]> sprites;/*< container-ul de imagini folosite la animatie */
    private CannonBall ball = null;/*< entitatea proiectil lansata de tun */
    private boolean on;/*< flag folosit pentru a valida existenta ghiulelei */

    /*! \fn Cannon(TileMap tm)
        \brief Constructorul clasei Cannon, initializeaza parametrii fizici(cei necesari pentru a reprezenta
        obiectul pe ecran), imaginile folosite la animatie, flaguri si alti parametrii
     */
    public Cannon(TileMap tm)
    {
        //setarea dimensiunilor pe ecran + colision box
        super(tm);
        width = 44;
        height = 28;
        cwidth = 20;
        cheight = 20;

        //parametrii de stare
        fallSpeed = 0.2;
        damage = 1;
        playerNearby = false;
        on = false;
        falling = true;

        // incarcare sprites
        try{

            sprites = new ArrayList<>();
            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia
            BufferedImage[] bi = new BufferedImage[1];
            bi[0] = ImageIO.read(new FileInputStream("Resources/Sprites/10-Cannon/Idle.png"));
            sprites.add(bi);

            bi = new BufferedImage[4];
            BufferedImage shotimage = ImageIO.read(new FileInputStream("Resources/Sprites/10-Cannon/Shoot (44x28).png"));
            for(int i=0;i<4;i++)
            {
                bi[i] = shotimage.getSubimage(i*width,0,width,height);
            }
            sprites.add(bi);

            //initilizare animatie si orientare default
            animation = new Animation();
            animation.setFrames(sprites.get(IDLE));
            animation.setDelay(50);
            currentAction = IDLE;
            left = true;
            facingRight = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public void update()
        \brief Actualizeaza flaguri si animatii, atat pentru Cannon cat si pentru Ball

     */
    @Override
    public void update()
    {
        //verifica coliziunea cu harta
        checkTileMapCollision();
        //in functie de coliziunie, se actualizeaza pozitia pe harta a
        //tunului
        setPosition(xtemp,ytemp);

        //verifica daca jucatorul este in preajma si porneste focul
        //actualizeaza animatia corespunzator
        if(playerNearby)
        {
            if(currentAction != SHOOT && !on)
            {
                currentAction = SHOOT;
                animation.setFrames(sprites.get(SHOOT));
                animation.setDelay(200);
            }
            else if(currentAction == SHOOT && !on)
            {
                //asteapta ca animatia SHOOT a tunul sa ruleze o singura data
                //si lanseaza proiectilul
                if(animation.hasPlayedOnce())
                {
                    on = true;
                    ball = new CannonBall(tileMap,false);
                    ball.setPosition(x,y-10);
                    currentAction = IDLE;
                    animation.setFrames(sprites.get(IDLE));
                    animation.setDelay(10);
                }
            }
        }
        //daca jucatorul nu se afla in preajma ramane in starea IDLE
        else if(!playerNearby)
        {
            if(currentAction != IDLE)
            {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(100);
            }
        }

        //actualizeaza starea proiectilului
        if(on)
        {
            ball.update();
        }

        //verifica daca mai este necesara prezenta bilei pe ecran
        if(on) {
            if(ball.shouldRemove())
            {
                ball = null;
                on = false;
            }
        }

        //actualizeaza animatia corespunzator
        animation.update();

        //verifica daca tunul cade(util la spawn)
        if (falling) {
            dy += fallSpeed;
            if (dy > 0) falling = false;
        }
    }

    /*! \fn public void ProjectileCollison(Player p)
        \brief Verifica daca proiectilul a atins jucatorul
        si actualizeaza parametrii de stare
    */
    public void ProjectileCollison(Player p)
    {
        if(ball != null) {
            if (p.intersects(ball)) {
                p.hit(ball.getDamage());
            }
        }
    }

    /*! \fn public void draw(Graphics@D g)
        \brief Deseneaza pe ecran Cannon si Ball atata timp
        cat proiectilul a fost lansat
     */
    public void draw(Graphics2D g)
    {
        //desenam pe ecran proiectilul atata timp cat a fost lansat sau cat
        //timp se afla in cadru
        if(on && ball.notOnScreen()==false)
        {
            ball.draw(g);
        }
        //desenam tunul
        super.draw(g);
    }

    /*! \fn public Rectangle getRectangle()
        \brief Returneaza dreptughiul de coliziune
     */
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth/2 +10,
                (int)y - cheight/2,
                cwidth,
                cheight
        );
    }
}
