package com.company.Entity;

import com.company.GamePanel;
import com.company.TileMap.Tile;
import com.company.TileMap.TileMap;

import java.awt.*;
/*! \class MapObject
    \brief Clasa care descrie comportamentul fiecarei entitati in raport cu harta.
    Orice entitate de pe ecran este derivata din aceasta clasa
 */
public abstract class MapObject {

    // descrierea Tile-urilor
    protected TileMap tileMap; /*< harta alcatuita din Tile-uri */
    protected int tileSize;/*< dimensiunea unui Tile in pixeli */
    protected double xmap;/*< pozitia unui Tile pe harta - axa absciselor */
    protected double ymap;/*< pozitia unui Tile pe harta - axa ordonatelor */

    // Pozitia obiectului si vectorul deplasare
    protected double x; /*< coordonata pe axa absciselor */
    protected double y; /*< coordonata pe axa ordonatelor */
    protected double dx; /*< deplasarea pe axa absciselor */
    protected double dy; /*< deplasarea pe axa ordonatelor */

    // Dimensiunea obiectului
    protected int width; /*< lungimea obiectului pe ecran */
    protected int height; /*< inaltimea obiectului pe ecran */

    // Collision box
    protected int cwidth; /*< lungimea collision box */
    protected int cheight; /*< inaltimea collosion box */

    // Coordonate necesare pentru a calcula coliziunile
    protected int currRow; /*< Randul curent pe harta */
    protected int currCol; /*< Coloana curenta pe harta */
    protected double xdest; /*< Coordonata x rezutata in urma aplicarii vetorului deplasare */
    protected double ydest; /*< Coordonata y rezutata in urma aplicarii vetorului deplasare */
    protected double xtemp; /*< Coordonata x folosita pe post de variabila temporara la calculul coliziunilor */
    protected double ytemp; /*< Coordonata y folosita pe post de variabila temporara la calculul coliziunilor */

    // Cele 4 colturi ale obiectului, folosite la verificarea coliziunilor
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    // Variabile folosite la animatie
    protected Animation animation; /*< animatia este folosita la desenarea obiectelor pe ecran */
    protected int currentAction; /*< variabila de stare folosita de fiecare copil al clasei in functie de propriile conditii */
    protected boolean facingRight; /*< variabila folosita pentru a determina daca imaginea trebuie rotita spre dreapta sau nu */

    // Variabile folosite la validarea miscarii obiectului
    protected boolean left; /*< indica mersul spre stanga */
    protected boolean right; /*< indica mersul spre stanga */

    protected boolean jumping; /*< indica daca obiectul sare */
    protected boolean falling; /*< indica daca obiectul cade */
    protected boolean isHit; /*< indica daca obiectul este lovit */

    // parametrii miscarii
    protected double moveSpeed; /*< viteza curenta de deplasare a obiectului */
    protected double maxSpeed; /*< viteza maxima de deplasare a obiectului */
    protected double stopSpeed; /*< valoarea vitezei pentru care obiectul se opreste */
    protected double fallSpeed; /*< valoarea vitezei pentru care obiectul cade */
    protected double maxFallSpeed; /*< valoarea maxima a vitezei pentru care obiectul cade */
    protected double jumpStart; /*< valoarea vitezei de salt in momentul apasarii tastei SPACE */
    protected double stopJumpSpeed; /*< valoarea vitezei de salt pentru care obiectul nu mai sare*/

    /*! \fn public MapObject(TileMap tm)
        \brief Reprezinta obiectul pe harta si stabileste dimensiunea unui tile
     */
    public MapObject(TileMap tm) {
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    /*! \fn public boolean intersects(MapObject o)
        \brief Verifica daca daca doua obiecte s-au intersectat prin
        intermediul collision box-urilor
     */
    public boolean intersects(MapObject o) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    /*! \fn public Rectangle getRectangle()
        \brief Returneaza collision box
     */
    public Rectangle getRectangle() {
        return new Rectangle(
                (int) x - cwidth / 2,
                (int) y - cheight / 2,
                cwidth,
                cheight
        );
    }

    /*! \fn public void calculateCorners(double x, double y)
        \brief Calculeaza cele 4 colturi ale obiectului.
        Calculul coliziunilor se realizeaza pe baza acestora.
     */
    public void calculateCorners(double x, double y) {

        // am scazut din cwidth pentru a elimina spatiile goale din imaginea obiectului
        int leftTile = (int) (x - (cwidth - 10) / 2) / tileSize;
        int rightTile = (int) (x + (cwidth - 10) / 2 - 1) / tileSize;
        int topTile = (int) (y - (cheight) / 2) / tileSize;
        int bottomTile = (int) (y + (cheight) / 2 - 1) / tileSize;

        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);

        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
    }

    /*! \fn public void checkTileMapCollision()
        \brief Aceasta metoda este cea care realizeaza calculul efectiv al coliziunilor.
     */
    public void checkTileMapCollision() {
        // aflarea cooronatelor pe harta
        currCol = (int) x / tileSize;
        currRow = (int) y / tileSize;

        // aflarea coordonatelor finale in functie de sensul deplasarii
        xdest = x + dx;
        ydest = y + dy;

        // variabile temporare folosite pentru a compara pozitia initiala cu pozitia finala
        xtemp = x;
        ytemp = y;

        // calcul coliziune cu colturile de sus, respectiv jos
        // si actualizare vector deplasare, respectiv flaguri corespunzator
        calculateCorners(x, ydest);
        if (dy < 0) {
            if (topLeft || topRight) {
                dy = 0;
                ytemp = currRow * tileSize + (cheight) / 2;
            } else {
                ytemp += dy;
            }
        }
        if (dy > 0) {
            if (bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - (cheight) / 2;
            } else {
                ytemp += dy;
            }
        }

        // calcul coliziune cu colturile din stanga, respectiv dreapta
        // si actualizare vector deplasare, respectiv flaguri corespunzator
        calculateCorners(xdest, y);
        if (dx < 0) {
            if (topLeft || bottomLeft) {
                dx = 0;
                xtemp = currCol * tileSize + (cwidth - 10) / 2;
            } else {
                xtemp += dx;
            }
        }
        if (dx > 0) {
            if (topRight || bottomRight) {
                dx = 0;
                xtemp = (currCol + 1) * tileSize - (cwidth - 10) / 2;
            } else {
                xtemp += dx;
            }
        }

        // calcul coliziune in partea de jos si actualizare flaguri
        // calculul are loc in momentul in care obiectul trebuie sa se opreasca din cadere
        if (!falling) {
            calculateCorners(x, ydest + 1);
            if (!bottomLeft && !bottomRight) {
                falling = true;
            }
        }
    }

    /*! \fn public int getx()
        \brief Returneaza x.
     */
    public int getx() { return (int)x; }
    /*! \fn public int getx()
        \brief Returneaza y.
     */
    public int gety() { return (int)y; }
    /*! \fn public int getdx()
        \brief Returneaza dx.
     */
    public double getdx() { return dx; }
    /*! \fn public int getWidth()
        \brief Returneaza width.
     */
    public int getWidth() { return width; }
    /*! \fn public int getHeight()
        \brief Returneaza height.
     */
    public int getHeight() { return height; }
    /*! \fn int getHeight()
        \brief Returneaza pozitia(x si y).
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /*! \fn public void setMapPosition()
        \brief Seteaza pozitia pe harta a obiectului(pozitia reala).
     */

    public void setMapPosition() {
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    /*! \fn public void setLeft(boolean b)
        \brief Seteaza flag-ul left.
     */
    public void setLeft(boolean b) { left = b; }

    /*! \fn public void setLeft(boolean b)
        \brief Seteaza flag-ul right.
     */
    public void setRight(boolean b) { right = b; }

    /*! \fn public void setJumping(boolean b)
        \brief Seteaza flag-ul jumping.

     */
    public void setJumping(boolean b) { jumping = b; }

    /*! \fn public boolean notOnScreen()
        \brief Verifica daca obiectul se afla pe ecran.
     */
    public boolean notOnScreen() {
        if(y + ymap + 64 >= GamePanel.HEIGHT)
                return true;
        else
            return false;
    }

    /*! \fn public void draw(java.awt.Graphics2D g)
        \brief Deseneaza obiectul pe ecran corespunzator cu flagul facingRight
        si respectand coliziunile
     */
    public void draw(java.awt.Graphics2D g) {
        // Verifica daca obiectul se alfa pe ecran, atunci il deseneaza
        if (notOnScreen() == false) {
            // pozitia relativa la harta
            setMapPosition();
            // in functie de flagul facingRight, imaginea este rasturnata spre stanga sau spre dreapta
            if (facingRight) {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2.5),
                        (int) (y + ymap - height / 2), width, height,
                        null
                );
            } else {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap + width / 2.5),
                        (int) (y + ymap - height / 2),
                        -width,
                        height,
                        null
                );

            }

//            Rectangle r = getRectangle();
//            r.x += xmap;
//            r.y += ymap;
//            g.draw(r);
        }

    }
}
































