package com.company.GameState.Auxiliary;

import com.company.Audio.AudioPlayer;
import com.company.DBHandler;
import com.company.GameState.GameState;
import com.company.GameState.GameStateManager;
import com.company.TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;


/*! \class MenuState
    \brief Reprezinta stare care se ocupa de prezentarea optiunilor de start
 */
public class MenuState extends GameState {

    public Background bg;/*< variabila ce inglobeaza imaginea de background*/
    private BufferedImage title;/*< imaginea de titlu */

    private String[] options = {
            "Start",
            "Load Game",
            "Help",
            "Quit"
    };/*< Optiunile utilizatorului */

    private Font font; /*< fontul textului */

    private int currentChoice = 0; /*< optiunea curenta */

    /*! \fn public MenuState(GameStateManager gsm)
        \brief Constructorul clasei
        \param gsm Variablia ce se ocupa cu gestiunea starilor

     */
    public MenuState(GameStateManager gsm)
    {
        this.gsm = gsm;
        try{

            bg = new Background("Resources/7HzXe3.gif",1);
            bg.setVector(-0.1,0);//muta in stanga


            title = ImageIO.read(new FileInputStream("Resources/Sprites/Kings and Pigs.png"));
            font = new Font("Times New Roman",Font.PLAIN,40);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public void init()
        \brief Metoda goala
    */
    @Override
    public void init() {

    }

    /*! \fn public void update()
        \brief Actualizeaza starea Menu
     */
    @Override
    public void update() {
        bg.update();
    }

    /*! \fn public void draw(Graphics2D g)
        \brief Deseneaza imaginea de fundal si textul.
        De asemenea desenam cu alta culoare optiunea selectata
     */
    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);

        g.setFont(font);

        g.drawImage(title,150,100,300,60,null);

        // optiunile meniului
        for(int i=0; i<options.length;i++)
        {
            if(i == currentChoice)
            {
                g.setColor(Color.RED);
            }
            else
            {
                g.setColor(Color.BLACK);
            }
            drawCentredString(options[i],610,400+i*90,g);//distanta de 55 pixeli una fata de alta
        }
    }

    /*! \fn private void select()
        \brief Executa actiunea conform optiunii selectate
    */
    private void select()
    {
        if(currentChoice == 0) // am ales start
        {
            gsm.setState(GameStateManager.LEVEL1STATE,null);
        }
        if(currentChoice == 1) //load
        {

        }
        if(currentChoice == 2) //help
        {
            gsm.setState(GameStateManager.HELP,null);
        }
        if(currentChoice == 3)//quit
        {
            System.exit(0);
        }
    }

    /*! \fn public void keyPressed(int k)
        \brief Intercepteaza un eveniment de la tastatura, in acest caz apasarea tastei Enter
        precum si navigarea in meniu
     */
    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER)
        {
            select();
        }

        if(k == KeyEvent.VK_UP)
        {
            currentChoice--;
            if(currentChoice == -1)
            {
                currentChoice=options.length-1;
            }
        }

        if(k==KeyEvent.VK_DOWN)
        {
            currentChoice++;
            if(currentChoice == options.length)
            {
                currentChoice=0;
            }
        }
    }

    /*! \fn public void keyReleased(int k)
        \brief Intercepteaza un evenimentul de lasare a butonului de la tastatura
     */
    @Override
    public void keyReleased(int k) {

    }

    /*! \fn public void drawCentredString(String s,int w,int h,Graphics2D g)
        \brief Deseneaza textul centrat pe ecran
     */
    public void drawCentredString(String s,int w,int h,Graphics2D g)
    {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s))/2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent()))/2);
        g.drawString(s,x,y);
    }
}
