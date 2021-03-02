package com.company.GameState.Auxiliary;

import com.company.GameState.GameState;
import com.company.GameState.GameStateManager;
import com.company.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

/*! \class SettingsState
    \brief Reprezinta stare care se ocupa de setarile jocului
 */

public class SettingsState extends GameState {
    public Background bg;/*< variabila ce inglobeaza imaginea de background*/

    private String[] options = {
            "Audio",
            "Music",
            "Back"
    };/*< Optiunile utilizatorului */

    private Font font;/*< fontul textului */

    private int currentChoice = 0;/*< optiunea curenta */

    /*! \fn public SettingsState(GameStateManager gsm)
    \brief Constructorul clasei
    \param gsm Variablia ce se ocupa cu gestiunea starilor
 */
    public SettingsState(GameStateManager gsm)
    {
        this.gsm = gsm;

        try{
            bg = new Background("Resources/7HzXe3.gif",1);
            bg.setVector(0,0);
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
    protected void init() {

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
        g.setColor(Color.BLACK);
        g.drawString("Settings",260, 100);//numele si pozitia titlului

        // optiunile meniului
        g.setFont(font);
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
            drawCentredString(options[i],640,360+i*120,g);
        }
    }

    /*! \fn private void select()
        \brief Executa actiunea conform optiunii selectate
    */
    private void select()
    {
        if(currentChoice == 0) // Audio
        {
            try {
                int audio = dbh.getAudio();
                System.out.println(audio);
                if (audio == 0) {
                    dbh.updateAudio(true);
                    //gsm.getState().audioTurn(false);
                    gsm.getLevel().audioTurn(false);
                } else if (audio == 1) {
                    dbh.updateAudio(false);
                    //gsm.getState().audioTurn(true);
                    gsm.getLevel().audioTurn(true);
                }
            } catch (SQLException e) {
                    e.printStackTrace();
            }
        }
        if(currentChoice == 1) //Music
        {

            try {
                int music = dbh.getMusic();
                System.out.println(music);
                if(music == 0)
                {
                    dbh.updateMusic(true);
                    gsm.musicTurn(false);//.adjustMusic(false);
                }
                else if(music == 1)
                {
                    dbh.updateMusic(false);
                    gsm.musicTurn(true);//.adjustMusic(true);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(currentChoice == 2)//quit
        {
            gsm.setState(GameStateManager.currentLevel,gsm.getLevel());
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
