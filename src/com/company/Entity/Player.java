package com.company.Entity;

import com.company.Audio.AudioPlayer;
import com.company.Entity.Enemies.Cannon;
import com.company.Entity.Enemies.Enemy;
import com.company.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;
/*! \class Player
    \brief Descrie propietatile jucatorului si ce poate face acesta.
    De asemenea se stabileste interactiunea jucatorului cu celelalte entitati( monstrii, cutii, items).
 */
public class Player extends MapObject {

    // Flag-uri si parametrii jucatorului
    private int score; /*< scorul jucatorului */
    private int health; /*< viata curenta a jucatorului */
    private int maxHealth; /*< viata maxima/initiala a jucatorului */
    private boolean dead; /*< flag care indica daca jucatorul este in viata sau nu*/
    private boolean flinching;/*< flinching */
    private long flinchTimer;/*< timpul in care flag-ul flinching este activ */
    private boolean doorIn; /*< flag-ul care indica daca jucatorul a intrat pe usa*/
    private boolean nextToDoor; /*< flag-ul care indica daca jucatorul este in apropierea usii */
    private boolean nextLevel; /*< flag-ul care indica nivelul urmator */
    private int currentLevel; /*< flag-ul care indica nivelul curent */
    private static boolean audioEnable = true; /*< flag-ul care permite rularea sound effects */
    private int nrOfLives;/*< numarul de vieti ale jucatorului */
    private ExecutorService pool = newFixedThreadPool(3); /*< acest ThreadPool este folosit pentru rularea sunetelor la diverse interactiuni */

    // attack
    private boolean attacking; /*< flag-ul care indica daca jucatorul ataca */
    private int attackDamage; /*< cantitatea de pagube pe care jucatorul le provoaca inamicilor */
    private int attackRange; /*< raza atacului */
    private boolean finished; /*< flag folosit la cronometrarea animatiei de DEAD */

    // animations
    private ArrayList<BufferedImage[]> sprites; /*< container-ul de imagini folosite la animatie*/

    private HashMap<String, AudioPlayer> sfx; /*< structura ce mapeaza soundtrack-urile in functie de o descriere data prin cheia de tip String */

    // actiuni animate
    private static final int IDLE = 0;/*< stare folosita in setarea animatiei */
    private static final int WALKING = 1;/*< stare folosita in setarea animatiei */
    private static final int JUMPING = 2;/*< stare folosita in setarea animatiei */
    private static final int FALLING = 3;/*< stare folosita in setarea animatiei */
    private static final int ATTACKING = 4;/*< stare folosita in setarea animatiei */
    private static final int HIT = 5;/*< stare folosita in setarea animatiei */
    private static final int DEAD = 6;/*< stare folosita in setarea animatiei */
    private static final int DOORIN = 7;/*< stare folosita in setarea animatiei */

    private static long deadCounter = 0;  /*< timpul scurs la rularea animatiei corespunzatoare starii DEAD */

    /*! \fn public Player(TileMap tm)
        \brief Constructorul clasei Player

     */
    public Player(TileMap tm) {

        super(tm);
        //setarea dimensiunilor pe ecran + colision box
        width = 78;
        height = 58;
        cwidth = 30;
        cheight = 30;

        //parametrii de stare si setare flag-uri initiale
        doorIn = false;
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;
        nrOfLives = 3;

        nextLevel = false;
        finished = false;
        facingRight = true;
        isHit = false;
        health = maxHealth = 3;

        dead = false;
        score = 0;
        //audioEnable = true;
        currentLevel = 1;

        attackDamage = 1;
        attackRange = 50;

        // incarcare sprites
        try {

            sprites = new ArrayList<BufferedImage[]>();

            //fiecare elemente din array-ul sprites va fi un vector de imaginii, care prin rularea lor in bucla
            //vor crea animatia

            BufferedImage idlesheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Idle (78x58).png"));
            BufferedImage temp;
            BufferedImage[] bi = new BufferedImage[11];
            for (int j = 0; j < 11; j++) {
                temp = idlesheet.getSubimage(j * width, 0, width, height);
                bi[j] = temp.getSubimage(0, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage walksheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Run (78x58).png"));
            bi = new BufferedImage[8];
            for (int j = 0; j < 8; j++) {
                bi[j] = walksheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage jumpsheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Jump (78x58).png"));
            bi = new BufferedImage[1];

            bi[0] = jumpsheet.getSubimage(0, 0, width, height);
            sprites.add(bi);

            BufferedImage fallsheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Fall (78x58).png"));
            bi = new BufferedImage[1];

            bi[0] = fallsheet.getSubimage(0, 0, width, height);
            sprites.add(bi);

            BufferedImage scratchsheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Attack (78x58).png"));
            bi = new BufferedImage[3];
            for (int j = 0; j < 3; j++) {
                bi[j] = scratchsheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage hitsheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Hit (78x58).png"));
            bi = new BufferedImage[2];
            for (int j = 0; j < 2; j++) {
                bi[j] = hitsheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage deadsheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Dead (78x58).png"));
            bi = new BufferedImage[4];
            for (int j = 0; j < 4; j++) {
                bi[j] = deadsheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);

            BufferedImage doorinsheet = ImageIO.read(new FileInputStream("Resources/Sprites/01-King Human/Door In (78x58).png"));
            bi = new BufferedImage[8];
            for (int j = 0; j < 8; j++) {
                bi[j] = doorinsheet.getSubimage(j * width, 0, width, height);
            }

            sprites.add(bi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // initializare animatii
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(50);

        // initializare hashmap pentru sound effects
        sfx = new HashMap<>();
        sfx.put("hit", new AudioPlayer("Resources/swing (online-audio-converter.com).mp3", false));
        sfx.put("jump", new AudioPlayer("Resources/Jump (online-audio-converter.com).mp3", false));
        sfx.put("damage", new AudioPlayer("Resources/classic_hurt.mp3", false));

    }

    /*! \fn public boolean getNextLevel()
        \brief Returneaza nextLevel.
     */
    public boolean getNextLevel() {
        return nextLevel;
    }

    /*! \fn public int getHealth()
        \brief Returneaza health.
     */
    public int getHealth() {
        return health;
    }

    /*! \fn public int getMaxHealth()
        \brief Returneaza maxhealth.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /*! \fn public Animation getAnimation()
        \brief Returneaza animation.
     */
    public Animation getAnimation() {
        return animation;
    }

    /*! \fn public int getAction()
        \brief Returneaza currentAction.
     */
    public int getAction() {
        return currentAction;
    }

    /*! \fn public void (boolean b)
        \brief Seteaza attacking.
     */
    public void setScratching(boolean b) {
        attacking = b;
    }

    /*! \fn public void setDoorIn(boolean b)
        \brief Seteaza doorIn.
     */
    public void setDoorIn(boolean b) {
        doorIn = b;
    }

    public boolean getFinished() {
        return finished;
    }

    /*! \fn public void setHealth(int h)
        \brief Seteaza health.

     */
    public void setHealth(int h) {
        health = h;
    }

    /*! \fn public long getDeadCounter()
        \brief Returneaza deadCounter.
     */
    public long getDeadCounter() {
        return deadCounter;
    }

    /*! \fn public void setDeadCounter()
        \brief Seteaza deadCounter la 0.
     */
    public void setDeadCounter() {
        deadCounter = 0;
    }

    /*! \fn public int getScore()
        \brief Returneaza score.

     */
    public int getScore() {
        return score;
    }

    /*! \fn public void setScore(int s)
        \brief Seteaza score.

     */
    public void setScore(int s) {
        score = s;
    }

    /*! \fn public void setLives(int l)
        \brief Seteaza numarul de vieti.
     */
    public void setLives(int l)
    {
        nrOfLives = l;
    }

    /*! \fn public int getLives()
        \brief Returneaza numarul de vieti.
     */
    public int getLives()
    {
        return nrOfLives;
    }

    /*! \fn public int getCurrentLevel()
        \brief Returneaza currentLevel.
    */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /*! \fn public void setAudio(boolean status)
        \brief Seteaza audioEnable.
     */
    public void setAudio(boolean status) {
        audioEnable = status;
    }

    /*! \fn public boolean isDead()
        \brief Returneaza daca jucatorul e mort, in cazul in care este porneste deadCounter.
     */
    public boolean isDead() {
        finished = false;

        if (health == 0) {
            if (deadCounter == 0)
                deadCounter = System.nanoTime();
            flinching = false;

            if (!dead) dead = true;

            return true;

        }
        return false;
    }

    /*! \fn public boolean getHiting()
        \brief Returneaza isHit.
     */
    public boolean getHiting() {
        return isHit;
    }

    /*! \fn public void checkDoor(Door door)
        \brief Verifica daca jucatorul se afla langa o usa
        \param door Referinta la un obiect de tip Door
     */
    public void checkDoor(Door door) {
        if (intersects(door) == false) {
            if (nextToDoor)
                doorIn = true;
            else
                doorIn = false;
        }
    }

    /*! \fn public void checkInteraction(ArrayList<Box> boxes)
        \brief Verifica daca jucatorul a interactionat cu cutii si actualizeaza parametrii acestuia
        \param boxes Reprezinta un Array de cutii instantiat la inceputul fiecarui nivel
     */
    public void checkInteraction(ArrayList<Box> boxes) {
        for (int i = 0; i < boxes.size(); i++) {
            Box b = boxes.get(i);

            if (intersects(b)) {
                if (y <= b.gety() && falling) {
                    //y = b.gety() - cwidth;
                    falling = false;
                    //left = false;
                    dy = 0;
                    //dx = 0;
                    //y = b.gety()-cwidth;


                } else {
                    b.move(dx, 1);
                    b.checkTileMapCollision();
                    if (b.getdx() == 0) {
                        if (x >= b.getx() && left) {
                            left = false;
                            dx = 0;
                        } else if (x - b.getWidth() < b.getx() && right) {
                            right = false;
                            dx = 0;
                        }
                    }
                }
            }
        }

    }

    /*! \fn public void checkAttack(ArrayList<Enemy> enemies)
        \brief Verifica daca jucatorul a atacat sau a fost atacat
        \param enemies Reprezinta un Array de inamici instantiat la inceputul fiecarui nivel

     */
    public void checkAttack(ArrayList<Enemy> enemies) {

        for (int i = 0; i < enemies.size(); i++) {
            //inamicul curent
            Enemy e = enemies.get(i);

            // verificam daca a atacat
            if (attacking) {
                // in functie de facing verificam daca atacul jucatorului a atins inamicul
                if (facingRight) {
                    if (
                            e.getx() > x &&
                                    e.getx() < x + attackRange &&
                                    e.gety() > y - height / 2 &&
                                    e.gety() < y + height / 2
                    ) {
                        e.hit(attackDamage);
                    }
                } else {
                    if (
                            e.getx() < x &&
                                    e.getx() > x - attackRange &&
                                    e.gety() > y - height / 2 &&
                                    e.gety() < y + height / 2
                    ) {
                        e.hit(attackDamage);
                    }
                }
            }

            // apelam functia care verifica daca jucatorul a fost atins de proiectile
            e.ProjectileCollison(this);
            // verificam daca inamicii au atacat jucatorul si daca acesta mai este in viata
            if (intersects(e) && !flinching) {
                hit(e.getDamage());
                if (dead) {
                    stop();
                } else if (x < e.getx() && !(e instanceof Cannon)) {
                    e.right = true;
                    e.left = false;
                    e.facingRight = false;
                } else if (x > e.getx() && !(e instanceof Cannon)) {
                    e.right = false;
                    e.left = true;
                    e.facingRight = true;
                }
            }
        }

    }

    /*! \fn public void hit(int damage)
        \brief Determina comportamentul jucatorului atunci cand este lovit
     */
    public void hit(int damage) {
        if (flinching) return;


        isHit = true;
        health -= damage;
        if (health < 0) health = 0;
        if (health == 0) dead = true;
        //stop();
        if (!dead) {
            if (audioEnable)
                pool.execute(sfx.get("damage"));
            flinching = true;
            flinchTimer = System.nanoTime();
        }
    }

    /*! \fn public void stop()
        \brief Opreste actiunile jucatorului pana la un nou eveniment
     */
    public void stop() {
        right = left = flinching = jumping = attacking = false;
    }

    /*! \fn private void getNextPosition()
        \brief Seteaza vectorul deplasare in functie de orientarea jucatorului
        Verifica daca jucatorul se afla in cadere si realizeaza actualizarea deplasarii.
        Verifica daca jucatorul este lovit si daca a murit
     */
    private void getNextPosition() {

        // Daca jucatorul e in mort il oprim
        if (dead) {
            dx = 0;
            dy = 0;
            stop();
            return;
            //reset();
        }
        // verificare miscare
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        // daca a fost lovit, aplicam un efect de knockback
        else if (isHit && health != 0) {
            attacking = false;
            if (facingRight) {
                dx = -1;
            } else {
                dx = 1;
            }
            dy = -3;
            dy += fallSpeed * 2;
            return;
        } else
            // daca s-a intersectat cu TileMap
            {

            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // daca sare actualizam dy si falling
        if (jumping && !falling) {
            dy = jumpStart;
            falling = true;
            // rulam efectul audio
            String s = "jump";
            if (audioEnable)
                pool.execute(sfx.get(s));
        }


        // verificam daca jucatorul se afla in cadere
        if (falling) {

            //if (dy > 0 &) dy += fallSpeed * 0.1;
            /*else*/ dy += fallSpeed;

            if (dy > 0) jumping = false;
            if (dy < 0 && !jumping) dy += stopJumpSpeed;

            if (dy > maxFallSpeed) dy = maxFallSpeed;

        }

    }

    /*! \fn public void update()
        \brief Actualizeaza flaguri si animatii pentru Player
     */
    public void update() {
        getNextPosition();

        // verifica coliziunea cu harta
        checkTileMapCollision();

        // in functie de coliziunie, se actualizeaza pozitia pe harta
        setPosition(xtemp, ytemp);

        // verifica daca jucatorul e mort si a rulat o singura data animatia de DEAD
        if (currentAction == DEAD) {
            if (animation.hasPlayedOnce()) {
                //finished = true;
                dead = false;
                //isHit = false;
                return;
            }
        }
        // verifica daca animatia pentru HIT a rula o data
        else if (currentAction == HIT) {
            if (animation.hasPlayedOnce()) {
                isHit = false;
            }
        }

        // verifica daca animatia de intrat pe usa a rulat odata
        if (currentAction == DOORIN) {
            if (animation.hasPlayedOnce()) {
                doorIn = false;
                nextToDoor = false;
                nextLevel = true;

            }
        }

        // verifica daca animatia de atac a rulat odata
        if (currentAction == ATTACKING) {
            if (animation.hasPlayedOnce())
                attacking = false;
        }

        //verifica daca flinching e true

        if (flinching && !dead) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 1500) {
                flinching = false;
            }
        }

        // verifica daca jucatorul ataca
        if (attacking) {
            if (currentAction != ATTACKING) {
                currentAction = ATTACKING;
                if (audioEnable) {
                    String s = "hit";
                    pool.execute(sfx.get(s));
                }
                animation.setFrames(sprites.get(ATTACKING));
                animation.setDelay(60);
                width = 78;
            }
        }
        // verifica daca jucatorul a fost lovit
        else if (isHit) {
            if (currentAction != HIT) {
                currentAction = HIT;
                animation.setFrames(sprites.get(HIT));
                animation.setDelay(40);
            }
        }
        // verifica daca a intrat in usa
        else if (doorIn) {
            if (currentAction != DOORIN) {
                currentAction = DOORIN;
                animation.setFrames(sprites.get(DOORIN));
                animation.setDelay(60);
            }
        }
        // verifica daca jucatorul cade
        else if (dy > 0) {
            if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(10);
                width = 78;
            }
        }
        // verifica daca jucatorul sare
        else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(10);
                width = 78;
            }
        }
        // verifica daca se misca stanga-dreapta
        else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(50);
                width = 78;
            }
        } else if (dead) {
            isHit = false;
            dx = 0;
            dy = 0;
            stop();
            if (currentAction != DEAD) {
                animation.setFrames(sprites.get(DEAD));
                animation.setDelay(200);
            }
            animation.update();
            return;
        }
        // verifica daca sta pe loc
        else {
            if (currentAction != IDLE && !doorIn) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(50);
                width = 78;
            }
        }

        // actualizeaza animatia
        animation.update();

        // seteaza orientarea imaginii
        if (currentAction != ATTACKING) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }

    }

    /*! \fn public void draw(Graphics2D g)
        \brief Deseneaza jucatorul in functie de orientarea imaginii

     */
    public void draw(Graphics2D g) {

        // draw player
        if (flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return;
            }
        }

        super.draw(g);

    }

    /*! \fn public void drawScore(Graphics2D g)
        \brief Desneaza scorul obtinut de jucator in timp real
     */
    public void drawScore(Graphics2D g) {
        super.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        g.drawString("Score: " + score, 500, 30);

    }

    /*! \fn public void drawLives(Graphics2D g)
        \brief Deseneaza numarul de vieti ale eroului in timp real
     */
    public void drawLives(Graphics2D g)
    {
        super.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        g.drawString("Lives: " + nrOfLives, 500, 50);
    }
}
