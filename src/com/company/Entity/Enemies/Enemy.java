package com.company.Entity.Enemies;

import com.company.Entity.MapObject;
import com.company.Entity.Player;
import com.company.TileMap.TileMap;

public class Enemy extends MapObject {

    protected int rangeOfVision = 400; /*< Parametrul descrie campul de viziune al inamicului in raport cu jucatorul. */
    protected int health; /*< Viata curenta a inamicului */
    protected int maxHealth; /*< Viata maxima(initiala) a inamicului */
    protected boolean dead; /*< Variablia flag care indica daca inamicul e mort sau nu */
    protected int damage; /*< Daunele provocate de inamic jucatorului */
    protected int attackrange; /*< Raza de atac a inamicului la contactul cu jucatorul */
    protected boolean flinching; /*< Descrie daca inamicul are o scurta perioada de invincibilitate dupa ce a suferit pagube din partea jucatorului */
    protected long flinchTimer; /*< Descrie cat timp este setat pe true flagul flinching */
    protected boolean finished;
    protected boolean playerNearby; /*< Indica daca jucatorul este in raza de viziune a inamicului */

    /*! \fn Enemy(TileMap tm)
        \brief Constructorul fiecarui inamic seteaza inamicul pe harta
        si cantintatea de pagube generate jucatorului la atac
        \param tm Reprezinta TileMap ul, harta generata la rulare
     */
    public Enemy(TileMap tm)
    {
        super(tm);
        damage = 1;
    }

    /*! \fn public boolean isDead()
        \brief Verifica daca inamicul mai este in viata sau nu
     */
    public boolean isDead()
    {
        return dead;
    }

    /*! \fn public int getDamage()
        \brief Returneaza damage-ul.
     */
    public int getDamage()
    {
        return damage;
    }

    /*! \fn public void hit(int damage)
        \param damage Valoarea pagubei
        \brief Aplica paguba din lovitura eroului inamicului si verifica
        daca acesta a murit, seteaza flagurile flinching si dead corespunzator
     */
    public void hit(int damage)
    {
        if(dead || flinching) return;
        health -= damage;
        if(health<0) health = 0;
        if(health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    /*! \fn public void update()
        \brief Metoda update in general actualizeaza entitatea curenta in functie de parametrii
        din celelalte metode
     */
    public void update()
    {

    }

    /*! \fn checkAttack(Player p)
        \param p Verifica
        \brief Metoda verifica daca inamicul a produs daune jucatorului si actualizeaza parametrii
        pe masura

     */
    public void checkAttack(Player p)
    {

    }

    /*! \fn PlayerNearby(Player p)
        \brief Seteaza flagul playerNearby in functie de pozitia jucatorului relativa la inamic
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

    /*! \fn ProjectileCollison(Player p)
        \brief Verifica daca jucatorul a fost atins de vreun proiectil(in cazul tunurilor)

     */
    public void ProjectileCollison(Player p)
    {

    }
}
