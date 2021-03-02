package com.company.GameState;

import com.company.DBHandler;
import com.company.Entity.HUD;
import com.company.Entity.Player;
import com.company.TileMap.TileMap;

import java.awt.*;

/*! \class GameState
    \brief Clasa generica care descrie o stare a jocului
 */
abstract public class GameState  {

    protected GameStateManager gsm;/*< Variablia ce se ocupa cu gestiunea starilor */
    protected volatile DBHandler dbh = DBHandler.getInstance();/*< Variabila ce se ocupa cu gestiunea bazei de date */
    protected static Player Player;/*< Instanta Player */
    protected HUD hud;/*< Variabila de tip hud */
    protected TileMap tileMap;/*< Variabila de tip TileMap*/
    protected static PlayerSaver ps = new PlayerSaver(Player);/*< Instanta clasei PlayerSaver, ultilizata pentru a pastra starea curenta a Player-ului */

    /*! \fn protected void init()
        \brief Metoda va fi implementata de clasele fiu
     */
    protected abstract void init();

    /*! \fn public abstract void update()
        \brief Metoda va fi implementata de clasele fiu
     */
    public abstract void update();

    /*! \fn public abstract void draw(Graphics2D g)
        \brief Metoda va fi implementata de clasele fiu
    */
    public abstract void draw(Graphics2D g);

    /*! \fn public abstract void keyPressed(int k)
        \brief Metoda va fi implementata de clasele fiu
    */
    public abstract void keyPressed(int k);

    /*! \fn public abstract void keyReleased(int k)
        \brief Metoda va fi implementata de clasele fiu
    */
    public abstract void keyReleased(int k);

    /*! \fn public void musicTurn(boolean status)
        \brief Metoda va fi implementata de clasele fiu
    */
    public void musicTurn(boolean status)
    {

    }

    /*! \fn public void audioTurn(boolean status)
        \brief Metoda va fi implementata de clasele fiu
     */
    public void audioTurn(boolean status)
    {

    }

    /*! \fn public void saveStatus()
        \brief Metoda va fi implementata de clasele fiu
    */
    public void saveStatus()
    {

    }

    /*! \fn public void sound()
        \brief Metoda va fi implementata de clasele fiu
     */
    public void sound()
    {

    }
}
