package com.company.GameState;

import com.company.Entity.Player;

/*! \class PlayerSaver
    \brief Clasa incapsuleaza Player-ul cu scopul e a retine informatii despre el
    si de a le folosi la trecerea intre nivele
 */
public class PlayerSaver {
    private static Player pl = null;/*< Instanta de tip Player */

    /*! \fn public PlayerSaver(Player p)
        \brief Constructorul clasei
     */
    public PlayerSaver(Player p) {
        pl = p;
    }

    /*! \fn public void saveState(Player p)
        \brief Salveaza starea Playerului
     */
    public void saveState(Player p) {
        pl.setHealth(p.getHealth());
        pl.setScore(p.getScore());
        pl.setLives(p.getLives());
    }

    /*! \fn public void getState(Player p)
        \brief Seteaza starea Playerului
    */
    public void getState(Player p)
    {
        p.setScore(pl.getScore());
        p.setHealth(pl.getHealth());
        p.setLives(pl.getLives());
    }
}
