package com.company.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/*! \class HUD
    \brief Descrie Heads Up Display: status bar, animatia pentru viata
 */
public class HUD {

    private Player player; /*< obiectul jucator, necesar pentru a obtine viata curenta */
    private BufferedImage image; /*< imaginea necesara desenarii barei de viata*/
    private BufferedImage[] bf; /*< container-ul de imagini folosite la animarea inimilor de viata */
    private Animation animation; /*< obiect folosit la gestiunea animatiei */
    private ArrayList<BufferedImage[]> hearts;

    private static final int IDLE=0;/*< stare folosita in setarea animatiei */

    /*! \fn public HUD(Player p)
        \brief Constructorul clasei
     */
    public HUD(Player p)
    {
        // HUD-ul urmareste player-ul dat in constructor
        player = p;

        // incarcare sprites
        try{
            hearts = new ArrayList<BufferedImage[]>();

            //imaginea reprezinta status bar
            image = ImageIO.read(new FileInputStream("Resources/Sprites/12-Live and Coins/Live Bar.png"));

            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia
            bf = new BufferedImage[8];
            BufferedImage heartimage = ImageIO.read(new FileInputStream("Resources/Sprites/12-Live and Coins/Big Heart Idle (18x14).png"));

            for(int i=0;i<8;i++)
            {
                bf[i] = heartimage.getSubimage(i*18,0,18,14);
            }

            hearts.add(bf);

            bf = new BufferedImage[2];
            BufferedImage outimage = ImageIO.read(new FileInputStream("Resources/Sprites/12-Live and Coins/Big Heart Hit (18x14).png"));
            for(int i=0;i<2;i++)
            {
                bf[i] = outimage.getSubimage(i*18,0,18,14);
            }

            //initilizare animatie
            hearts.add(bf);
            animation = new Animation();
            animation.setFrames(hearts.get(IDLE));
            animation.setDelay(100);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public void update()
        \brief Actualizeaza animatia
     */
    public void update()
    {
        animation.update();
    }

    /*! \fn public void draw(Graphics2D g)
        \brief Deseneaza pe ecran status bar-ul si numarul de vieti in functie de parametrul health al jucatorului
    */
    public void draw(Graphics2D g)
    {
       g.drawImage(image,0,20,null);
       for(int i=1;i<=player.getHealth();i++) {
           g.drawImage(animation.getImage(), 11*i, 28, null);
       }
    }

}
