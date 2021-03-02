package com.company.GameState;

import com.company.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

/*! \class WinState
    \brief Descrie starea in cazul in care jucatorul termina ultimul nivel
 */
public class WinState extends GameState {

    private Background bg;/*< variabila ce inglobeaza imaginea de background */

    private Font textFont;/*< fontul textului ce va fi scris pe ecran */

    private Font font;/*< fontul titlului */

    private String[] text ={
            "Congratulations",
            "You've taken back the Throne",
            "Press ENTER key to close"
    };/*< textul scris pe ecran*/

    /*! \fn public WinState(GameStateManager gsm)
        \brief Constructorul clasei
        \param gsm Variablia ce se ocupa cu gestiunea starilor
     */
    public WinState(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }

    /*! \fn protected void init()
        \brief initializeaza background-ul si fontul titlului/textului
     */
    @Override
    protected void init() {
        this.gsm = gsm;

        try{
            bg = new Background("Resources/7HzXe3.gif",1);
            bg.setVector(-0.05,0);//muta in stanga

            textFont = new Font("Times New Roman",Font.PLAIN,30);
            font = new Font("Times New Roman",Font.PLAIN,60);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public void update()
        \brief Actualizeaza starea Help
     */
    @Override
    public void update() {
        bg.update();
    }

    /*! \fn public void draw(Graphics2D g)
        \brief Deseneaza imaginea de fundal si textul
     */
    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);

        g.setFont(font);
        g.drawString("Win",270,100);

        g.setFont(textFont);

        g.setColor(Color.BLACK);

        for(int i=0;i<text.length;i++)
        {
            drawCentredString(text[i],640,360+i*80,g);
        }
    }

    /*! \fn public void keyPressed(int k)
        \brief Intercepteaza un eveniment de la tastatura, in acest caz apasarea tastei Enter
     */
    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER)
        {
            System.exit(0);
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
