package com.company.Entity;

import com.company.Audio.AudioPlayer;
import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/*! \class Door
    \brief Clasa descrie reprezetarea grafica a tranzitiei intre niveluri, gata sa se deschida atunci cand
    jucatorul se aproprie de ea

 */
public class Door extends MapObject {
    // actiuni animate
    static private final int IDLE = 0;/*< stare folosita in setarea animatiei */
    static private final int OPENING = 1;/*< stare folosita in setarea animatiei */
    static private final int OPEN = 2;/*< stare folosita in setarea animatiei */

    private boolean open;/*< flag ce semnifica dispozitia usii, true atunci cand usa este deschisa */
    private boolean playerNearby;/*< Indica daca jucatorul este in raza de viziune a usii */
    private int rangeOfVision; /*< Indica raza de viziune a usii relativa la jucator */
    private static boolean audioEnable = true; /*< flag-ul care permite rularea sound effects */

    private ExecutorService t = newFixedThreadPool(1); /*< Thread Pool folosit pentru a rula sound effect-ul de deschidere a usii*/
    private HashMap<String,AudioPlayer> sfx; /*< structura ce mapeaza soundtrack-urile in functie de o descriere data prin cheia de tip String */
    private ArrayList<BufferedImage[]> sprites;/*< container-ul de imagini folosite la animatie */

    /*! \fn public Door(TileMap tm)
        \brief Constructorul clasei
     */
    public Door(TileMap tm)
    {
        super(tm);

        //setarea dimensiunilor pe ecran + colision box
        width = 46;
        height = 56;
        cwidth = 30;
        cheight = 50;

        //parametrii de stare
        playerNearby = false;
        rangeOfVision = 100;
        open = false;

        // incarcare sprites
        try{
            sprites = new ArrayList<>();

            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia

            BufferedImage[] bi = new BufferedImage[1];
            BufferedImage idlesprite = ImageIO.read(new FileInputStream("Resources/Sprites/11-Door/Idle.png"));
            bi[0] = idlesprite;

            sprites.add(bi);

            bi = new BufferedImage[5];
            BufferedImage opensprite = ImageIO.read(new FileInputStream("Resources/Sprites/11-Door/Opening (46x56).png"));
            for(int j=0;j<5;j++)
            {
                bi[j] = opensprite.getSubimage(j*width,0,width,height);
            }

            sprites.add(bi);

            bi = new BufferedImage[1];
            bi[0] = opensprite.getSubimage(4*width,0,width,height);

            sprites.add(bi);

            //initilizare animatie si orientare default
            animation = new Animation();
            animation.setFrames(sprites.get(IDLE));
            animation.setDelay(-1);
            currentAction = IDLE;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // initializare hashmap pentru sound effects
        sfx = new HashMap<>();
        sfx.put("doorin",new AudioPlayer("Resources/jail_cell_door.mp3",false));

    }

    /*! \fn PlayerNearby(Player p)
        \brief Seteaza flagul playerNearby in functie de pozitia jucatorului relativa la usa
    */
    public void PlayerNearby(Player p)
    {
        if(Math.sqrt((x-p.getx())*(x-p.getx())+(y-p.gety())*(y-p.gety()))<rangeOfVision)
        {
            playerNearby = true;
        }
        else
        {
            playerNearby = false;
        }
    }

    /*! \fn public void setAudio(boolean status)
        \brief Seteaza flagu-ul audioEnable
     */
    public void setAudio(boolean status)
    {
        audioEnable = status;
    }

    /*! \fn public void update()
    \brief Actualizeaza flaguri si animatii pentru Door
    */
    public void update()
    {
        //verifica coliziunea cu harta
        checkTileMapCollision();

        //in functie de coliziunie, se actualizeaza pozitia pe harta
        setPosition(xtemp,ytemp);

        // Verifica daca jucatorul este in apropiere si deschide usa
        if(playerNearby)
        {
            if(currentAction == OPENING)
            {
                if(animation.hasPlayedOnce())
                {
                    // Usa ramane deschisa dupa rularea animatiei
                    if(currentAction != OPEN) {
                        animation.setFrames(sprites.get(OPEN));
                        animation.setDelay(-1);
                        open = true;
                        currentAction = OPEN;
                    }
                }
            }
            // Usa se deschide
            else {
                if (currentAction != OPENING && !open) {
                    animation.setFrames(sprites.get(OPENING));
                    animation.setDelay(70);
                    currentAction = OPENING;
                    if(audioEnable)
                    {
                        t.execute(sfx.get("doorin"));
                    }
                }
            }

        }
        else
        {
            // Usa ramane inchisa
            if(currentAction != IDLE)
            {
                open = false;
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(50);
            }
        }

        // Actualizeaza animatia
        animation.update();
    }

    /*! \fn public void draw(java.awt.Graphics2D g)
        \brief Deseneaza usa.
    */
    public void draw(Graphics2D g)
    {
        super.draw(g);
    }
}
