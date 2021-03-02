package com.company.GameState.Auxiliary;

import com.company.GameState.GameState;
import com.company.GameState.GameStateManager;
import com.company.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

/*! \class HelpState
    \brief Reprezinta stare care se ocupa de informarea jucatorului cu privire la poveste si butoanele utile
 */
public class HelpState extends GameState {

    private Background bg;/*< variabila ce inglobeaza imaginea de background */

    private Font textFont;/*< fontul textului ce va fi scris pe ecran */

    private Font font;/*< fontul titlului */

    private String[] text = {
      "Guide the Human King to victory!",
      "Use the almighty hammer by pressing Z",
      "The more you hold pressed SPACE, the higher you jump",
      " <- and -> for moving left/right",
            "Press Enter to continue.."
    };/*< textul scris pe ecran*/

    /*! \fn public HelpState(GameStateManager gsm)
        \brief Constructorul clasei
        \param gsm Variablia ce se ocupa cu gestiunea starilor
     */
    public HelpState(GameStateManager gsm)
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

            textFont = new Font("Times New Roman",Font.PLAIN,20);
            font = new Font("Times New Roman",Font.PLAIN,50);
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
        g.drawString("Help",270,100);

        g.setFont(textFont);

        for(int i=0;i<text.length;i++)
        {
            drawCentredString(text[i],640,360+i*60,g);
        }
    }

    /*! \fn public void keyPressed(int k)
        \brief Intercepteaza un eveniment de la tastatura, in acest caz apasarea tastei Enter
     */
    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER)
        {
            gsm.setState(GameStateManager.MENUSTATE,null);
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
