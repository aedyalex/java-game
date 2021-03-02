package com.company.GameState.Levels;

import com.company.Entity.*;
import com.company.Entity.Box;
import com.company.Entity.Enemies.Cannon;
import com.company.Entity.Enemies.Enemy;
import com.company.Entity.Enemies.Pig;

import com.company.GamePanel;
import com.company.GameState.GameState;
import com.company.GameState.GameStateManager;
import com.company.GameState.PlayerSaver;
import com.company.TileMap.Background;
import com.company.TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/*! \class Level1State
    \brief Clasa responsabila de gestiunea tuturor mecanismelor din primul nivel
 */
public class Level1State extends GameState {

    private Background bg;/*< variabila ce inglobeaza imaginea de background */

    private ArrayList<Enemy> enemies;/*< Array cu inamicii din nivelul 1*/

    private Door door;/*< Usa de la finalul nivelului*/

    private boolean recap = true;/*< Flag ce indica daca PlayerSaver trebuie instantiat */


    /*! \fn public Level1State(GameStateManager gsm)
        \brief Constructorul clasei
        \param gsm Variablia ce se ocupa cu gestiunea starilor
    */
    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    /*! \fn public void init()
        \brief Initializeaza parametrii nivelului: background, player, tilemap, hud, door.
    */
    public void init() {

        // initializare background
        bg = new Background("Resources/7HzXe3.gif", 0.1);

        // initializare tilemap
        tileMap = new TileMap(32);
        tileMap.loadTiles("Resources/Sprites/14-TileSets/Terrain (32x32).png");
        tileMap.loadMap("Resources/level1.txt");
        tileMap.setPosition(0, 0);
        tileMap.setTween(0.07);

        // initializare player
        Player = new Player(tileMap);
        if(recap) {
            ps = new PlayerSaver(Player);
            recap = false;
        }
        else
        {
            ps.getState(Player);
            Player.setHealth(3);
        }
        Player.setPosition(200, 200);

        //initializare usa
        door = new Door(tileMap);
        door.setPosition(1500, 325);

        //initializare inamici
        populateEnemies();

        //initializare hud
        hud = new HUD(Player);
    }

    /*! \fn public void update()
        \brief apeleaza functiile update din celelalte clase care agrega la clasa Level1State.
        Verifica daca player-ul si inamicii sunt in viata, dar si daca s-a atins usa
    */
    public void update() {
        // seteaza pozitia camerei
        tileMap.setPosition(GamePanel.WIDTH / 2 - Player.getx(), GamePanel.HEIGHT / 2 - Player.gety());

        // seteaza background-ul in functie de camera
        bg.setPosition(tileMap.getx(), tileMap.gety());

        // actualizeaza player-ul si verifica daca e mort
        Player.update();

        // asteapta ca animatia de DEAD sa se termine
        if (Player.isDead()) {
            long elapsed = (System.nanoTime() - Player.getDeadCounter()) / 1000000;

            if (elapsed > 800) {
                if(Player.getLives() == 1)
                {
                    gsm.setState(GameStateManager.GAMEOVERSTATE,null);
                    return;
                }
                else {
                    Player.setLives(Player.getLives()-1);
                    ps.saveState(Player);
                    Player.setDeadCounter();
                    init();
                }
            }
        }

        // actualizeaza inamicii, verifica daca acestia au cauzat daune player-ului sau daca au murit
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
            enemies.get(i).PlayerNearby(Player);
            enemies.get(i).checkAttack(Player);

            if (enemies.get(i).isDead()) {
                // actualizare scor
                Player.setScore(Player.getScore() + 1);
                enemies.remove(i);
                i--;
            }
        }

        // daca cade de pe stalpi, moare
        if (Player.notOnScreen()) {
            init();
        }

        // actualizare trecere la urmatorul nivel
        if (Player.getNextLevel() == true) {
            ps.saveState(Player);
            recap = true;
            gsm.setState(GameStateManager.LEVEL2STATE, null);
        }

        // actualizare interactiune cu usa
        Player.checkDoor(door);

        // actualizare lovituri in inamici
        Player.checkAttack(enemies);

        // actualizare stare usa
        door.PlayerNearby(Player);
        door.update();

        // actualizare HUD
        hud.update();
    }

    /*! \fn private void populateEnemies ()
        \brief Instantiaza inamicii si seteaza pozitia acestora
*/
    private void populateEnemies ()
    {
        enemies = new ArrayList<Enemy>();
        Pig p;
        Cannon cn;

        Point[] points = new Point[]{
                new Point(400, 330),
                new Point(300, 240),
        };

        p = new Pig(tileMap);
        p.setPosition(points[0].x, points[0].y);
        enemies.add(p);

        cn = new Cannon(tileMap);
        cn.setPosition(points[1].x, points[1].y);
        enemies.add(cn);
    }

    /*! \fn public void draw (Graphics2D g)
        \brief Deseneaza componentele primului nivel
    */
    public void draw (Graphics2D g)
    {
        //draw background
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);

        //draw door
        door.draw(g);

        //draw Player
        Player.draw(g);

        //draw score
        Player.drawScore(g);

        //draw lives
        Player.drawLives(g);

        //draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //draw hud
        hud.draw(g);
    }
    /*! \fn public void keyPressed (int k)
        \brief Semnaleaza un eveniment de tipul apasarii unui buton de la tastatura
     */
    public void keyPressed (int k)
    {
        // intrare in starea Pause
        if (k == KeyEvent.VK_ESCAPE) {
            Player.setLeft(false);
            Player.setRight(false);
            Player.setJumping(false);
            gsm.loadState(GameStateManager.LEVEL1STATE, this);
            gsm.setState(GameStateManager.PAUSESTATE, null);
        }

        // controlls
        if (k == KeyEvent.VK_LEFT) Player.setLeft(true);
        if (k == KeyEvent.VK_RIGHT) Player.setRight(true);
        if (k == KeyEvent.VK_UP) Player.setDoorIn(true);
        if (k == KeyEvent.VK_SPACE) Player.setJumping(true);
        if (k == KeyEvent.VK_Z) Player.setScratching(true);
    }

    /*! \fn public void keyReleased (int k)
        \brief Semnaleaza un eveniment de tipul lasarii unui buton deja apasat
    */
    public void keyReleased ( int k)
    {
        if (k == KeyEvent.VK_LEFT) Player.setLeft(false);
        if (k == KeyEvent.VK_RIGHT) Player.setRight(false);
        if (k == KeyEvent.VK_SPACE) Player.setJumping(false);
    }

    /*! \fn public void audioTurn ( boolean status)
        \brief Seteaza efectele audio in functie de status
     */
    public void audioTurn ( boolean status)
    {
        if (status == true) {
            //audioFlag = true;
            Player.setAudio(true);
            door.setAudio(true);
        } else {
            //audioFlag = false;
            Player.setAudio(false);
            door.setAudio(false);
        }
    }

    /*! \fn public void saveStatus ()
        \brief Salveaza in baza de date caracteristici ale player-ului
     */
    public void saveStatus ()
    {
        ps.saveState(Player);
        dbh.saveGame(Player.getCurrentLevel(), Player.getHealth(), Player.getScore());
    }
}
