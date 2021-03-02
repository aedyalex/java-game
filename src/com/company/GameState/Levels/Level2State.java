package com.company.GameState.Levels;

import com.company.Entity.*;
import com.company.Entity.Enemies.Cannon;
import com.company.Entity.Enemies.Enemy;
import com.company.Entity.Enemies.Pig;
import com.company.Entity.Enemies.PigKing;

import com.company.GamePanel;
import com.company.GameState.GameState;
import com.company.GameState.GameStateManager;
import com.company.TileMap.Background;
import com.company.TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/*! \class Level2State
    \brief Clasa responsabila de gestiunea tuturor mecanismelor din al doilea nivel
 */
public class Level2State extends GameState{

    private Background bg;/*< variabila ce inglobeaza imaginea de background */

    private ArrayList<Enemy> enemies;/*< Array cu inamicii din nivelul 2*/

    private ArrayList<Box> boxes;/*< Array cu cutii */

    private ArrayList<Heart> hearts;/*< Array cu items de tip heart */

    private boolean recap = true;/*< Flag care indica daca viata Player-ului trebuie refacuta */

    private Door door;/*< Usa de la finalul nivelului*/

    private PigKing pk;/*< variablia de tip inamic PigKing */

    /*! \fn public Level2State(GameStateManager gsm)
        \brief Constructorul clasei
        \param gsm Variablia ce se ocupa cu gestiunea starilor
    */
    public Level2State(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }

    /*! \fn public void init()
        \brief Initializeaza parametrii nivelului: background, player, tilemap, hud, door.
    */
    public void init()
    {
        // initializare background
        bg = new Background("Resources/d96txtj-5d565df7-832c-44b6-870c-6a46028beebf.png",0.1);

        // initializare tilemap
        tileMap = new TileMap(32);
        tileMap.loadTiles("Resources/Sprites/14-TileSets/Terrain (32x32).png");
        tileMap.loadMap("C:\\Users\\user\\IdeaProjects\\Joc_PAOO\\Resources\\level2.txt");
        tileMap.setPosition(0,0);
        tileMap.setTween(0.07);

        // initializare player
        Player = new Player(tileMap);

        ps.getState(Player);

        if(recap == false)
        {
            Player.setHealth(3);
        }
        else
        {
            recap = false;
        }

        Player.setPosition(150,200);

        // initializare usa
        door = new Door(tileMap);
        door.setPosition(3000,325);

        // initializare inamici
        populateEnemies();

        // initializare cutii
        populateBoxes();

        // initializare hearts
        populateHearts();

        // initializare hud
        hud = new HUD(Player);
    }

    public void update()
    {
        // seteaza pozitia camerei
        tileMap.setPosition( GamePanel.WIDTH/2- Player.getx(), GamePanel.HEIGHT/2 - Player.gety());

        // seteaza background-ul in functie de camera
        bg.setPosition(tileMap.getx(),tileMap.gety());

        // actualizeaza player-ul si verifica daca e mort
        Player.update();

        // asteapta ca animatia de DEAD sa se termine
        if(Player.isDead()) {
            long elapsed = (System.nanoTime() - Player.getDeadCounter())/1000000;

            if (elapsed>800) {
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

        // actualizare trecere la urmatorul nivel
        if(Player.getNextLevel() == true){
            gsm.setState(GameStateManager.WINSTATE,null);

        }

        // daca cade de pe stalpi, moare
        if(Player.notOnScreen())
        {
            init();
        }

        // actualizare interactiune cu cutii
        Player.checkInteraction(boxes);

        // actualizare interactiune cu usa
        Player.checkDoor(door);

        // actualizare lovituri in inamici
        Player.checkAttack(enemies);

        // actualizare PigKing
        pk.update();
        pk.InteractionWithPlayer(Player);

        // actualizeaza inamicii, verifica daca acestia au cauzat daune player-ului sau daca au murit
        for(int i=0;i<enemies.size();i++)
        {
            enemies.get(i).update();
            enemies.get(i).PlayerNearby(Player);
            enemies.get(i).checkAttack(Player);

            if (enemies.get(i).isDead()) {
                Player.setScore(Player.getScore()+1);
                    enemies.remove(i);
                    i--;
            }
        }


        // actualizare cutii
        for(int i=0;i<boxes.size();i++)
        {
            Box b = boxes.get(i);
            b.update();

        }

        // actualizarea items
        for(int i=0;i<hearts.size();i++)
        {
            if(hearts.get(i).PlayerNearby(Player)==true)
            {
                hearts.remove(i);
            }
            else {
                hearts.get(i).update();
            }
        }

        // actualizare usa
        door.PlayerNearby(Player);
        door.update();

        // actualizare hud
        hud.update();
    }

    /*! \fn private void populateHearts()
        \brief Instantiaza hearts si seteaza pozitia acestora
     */
    private void populateHearts()
    {
        hearts = new ArrayList<>();
        Heart h1;

        Point[] points = new Point[]{
                new Point(2447,100)
        };

        h1 = new Heart(tileMap);
        h1.setPosition(points[0].x,points[0].y);
        hearts.add(h1);
    }

    /*! \fn private void populateBoxes()
        \brief Instantiaza cutii si seteaza pozitia acestora
    */
    private void populateBoxes()
    {
        boxes = new ArrayList<Box>();
        Box b;

        Point[] points = new Point[]{
                new Point(900,250)
        };

        b = new Box(tileMap);
        b.setPosition(points[0].x,points[0].y);
        boxes.add(b);

    }

    /*! \fn private void populateEnemies()
        \brief Instantiaza inamicii si seteaza pozitia acestora
    */
    private void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();
        Pig p;

        Cannon cn1,cn2,cn3;

        Point[] points = new Point[]{
                new Point(550,330),
                new Point(430,200),
                new Point(300,100),
                new Point(1900,200),
                new Point(2300,100)
        };


        pk = new PigKing(tileMap);
        pk.setPosition(points[4].x,points[4].y);
        enemies.add(pk);
        
        p = new Pig(tileMap);
        p.setPosition(points[0].x,points[0].y);
        enemies.add(p);

        cn1 = new Cannon(tileMap);
        cn1.setPosition(points[1].x,points[1].y);
        enemies.add(cn1);

        cn2 = new Cannon(tileMap);
        cn2.setPosition(points[2].x,points[2].y);
        enemies.add(cn2);

        cn3 = new Cannon(tileMap);
        cn3.setPosition(points[3].x,points[3].y);
        enemies.add(cn3);
    }

    /*! \fn public void draw (Graphics2D g)
        \brief Deseneaza componentele celui de al doilea nivel
    */
    public void draw(Graphics2D g)
    {
        //draw background
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);

        //draw door
        door.draw(g);

        //draw player
        Player.draw(g);

        //draw score
        Player.drawScore(g);

        //draw lives
        Player.drawLives(g);

        //draw enemies
        for(int i=0;i<enemies.size();i++)
        {
            enemies.get(i).draw(g);
        }

        //draw PigKing
        pk.draw(g);

        //draw boxes
        for(int i=0;i<boxes.size();i++)
        {
            boxes.get(i).draw(g);
        }

        //draw hearts
        for(int i=0;i<hearts.size();i++)
        {
            hearts.get(i).draw(g);
        }

        //draw hud
        hud.draw(g);
    }

    /*! \fn public void keyPressed (int k)
        \brief Semnaleaza un eveniment de tipul apasarii unui buton de la tastatura
    */
    public void keyPressed(int k)
    {
        // intrare in starea Pause
        if(k == KeyEvent.VK_ESCAPE)
        {
            Player.setLeft(false);
            Player.setRight(false);
            Player.setJumping(false);
            gsm.loadState(GameStateManager.LEVEL2STATE,this);
            gsm.setState(GameStateManager.PAUSESTATE,null);

        }

        //controlls
        if(k == KeyEvent.VK_LEFT) Player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) Player.setRight(true);
        if(k == KeyEvent.VK_UP) Player.setDoorIn(true);
        if(k == KeyEvent.VK_SPACE) Player.setJumping(true);
        if(k == KeyEvent.VK_Z) Player.setScratching(true);
    }

    /*! \fn public void keyReleased (int k)
        \brief Semnaleaza un eveniment de tipul lasarii unui buton deja apasat
    */
    public void keyReleased(int k)
    {
        if(k == KeyEvent.VK_LEFT) Player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) Player.setRight(false);
        if(k == KeyEvent.VK_UP) Player.setDoorIn(false);
        //if(k == KeyEvent.VK_DOWN) Player.setDown(false);
        if(k == KeyEvent.VK_SPACE) Player.setJumping(false);
    }

    /*! \fn public void audioTurn ( boolean status)
        \brief Seteaza efectele audio in functie de status
    */
    public void audioTurn(boolean status)
    {
        if(status==true)
        {
            Player.setAudio(true);
            door.setAudio(true);
        }
        else
        {
            Player.setAudio(false);
            door.setAudio(false);
        }
    }

    /*! \fn public void saveStatus ()
        \brief Salveaza in baza de date caracteristici ale player-ului
    */
    public void saveStatus()
    {
        ps.saveState(Player);
        dbh.saveGame(Player.getCurrentLevel(),Player.getHealth(),Player.getScore());
    }
}
