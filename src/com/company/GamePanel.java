package com.company;
import com.company.GameState.GameStateManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/*! \class GamePanel
    \brief Reprezinta continutul intregului joc si parametrii ferestrei

 */
public class GamePanel extends JPanel implements Runnable, KeyListener{

    // dimensiuni
    public static final int WIDTH = 640;/*< Lungimea ecranului */
    public static final int HEIGHT = 480;/*< Inaltimea ecranului */

    private static GamePanel gp = null;/*< */

    // game thread

    private Thread thread;/*< Thread-ul pe care ruleaza jocul */
    private boolean running;/*< Flag care determina daca thread ul ruleaza */
    private int FPS = 100;/*< Frames per Second */
    private long targetTime = 1000/FPS;/*< Target time */

    //image
    private BufferedImage image;/*< canvas */
    private Graphics2D g;/*< Obiectul Graphics ce deseneaza pe canvas */

    //game state manager

    private GameStateManager gsm;/*< Game State Manager*/

    /*! \fn private GamePanel()
        \brief Constructorul clasei, instantiaza panelul
     */
    private GamePanel()
    {
            super();
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setFocusable(true);
            requestFocus();
    }

    /*! \fn public static GamePanel getInstance()
        \brief Singleton pentru a asigura o singura instanta a clasei GamePanel
     */
    public static GamePanel getInstance()
    {
        if(gp==null)
        {
            gp = new GamePanel();
        }
        return gp;
    }

    /*! \fn public void addNotify()
        \brief Metoda face componenta sa fie afisata(displayalbe)
     */
    public void addNotify()
    {
        super.addNotify();
        if(thread==null)
        {
           thread = new Thread(this);
           addKeyListener(this);
           thread.start();
        }
    }

    /*!

     */
    private void init()
    {
        image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        running = true;

        gsm = new GameStateManager();
    }

    /*! \fn public void run()
        \brief Descrie codul executat de Thread-ul jocului
     */
    public void run()
    {
         init();

        long start;
        long elapsed;
        long wait;

        //game loop

        while(running)
        {
            start = System.nanoTime();
            update();
            draw();
            drawToScreen();

            elapsed = (System.nanoTime() - start);
            wait = targetTime - elapsed/1000000;//nanosecunde -> secunde

            if(wait < 0) wait = 5;
            try {
                Thread.sleep(Math.abs(wait));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /*! \fn private void update()
        \brief Actualizeaza starea curenta
     */
    private void update()
    {
        gsm.update();
    }

    /*! \fn private void draw()
        \brief Deseneaza starea curenta
     */
    private void draw()
    {
        gsm.draw(g);
    }

    /*! \fn private  void drawToScreen()
        \brief Deseneaza canvas-ul
     */
    private  void drawToScreen()
    {
        Graphics g2 = getGraphics();

        g2.drawImage(image,0,0, WIDTH,HEIGHT,null);
        g2.dispose();
    }

    /*! \fn public void keyTyped(KeyEvent key)
        \brief Metoda din interfata keyTyped
     */
    public void keyTyped(KeyEvent key)
    {}

    /*! \fn public void keyPressed(KeyEvent key)
        \briefIntercepteaza un eveniment de la tastatura
     */
    public void keyPressed(KeyEvent key)
    {
        gsm.keyPressed((key.getKeyCode()));
    }

    /*! \fn public void keyReleased(KeyEvent key)
        \brief Intercepteaza un evenimentul de lasare a butonului de la tastatura
     */
    public void keyReleased(KeyEvent key)
    {
        gsm.keyReleased(key.getKeyCode());
    }
}
