package com.company.GameState;

import com.company.Audio.AudioPlayer;
import com.company.GameState.Auxiliary.HelpState;
import com.company.GameState.Auxiliary.MenuState;
import com.company.GameState.Auxiliary.PauseState;
import com.company.GameState.Auxiliary.SettingsState;
import com.company.GameState.Levels.Level1State;
import com.company.GameState.Levels.Level2State;

/*! \class GameStateManager
    \brief Clasa care gestioneaza starile pe parcursul gameplay-ului
 */
public class GameStateManager {

    private GameState[] gameStates;/*< Vector cu starile jocului */
    public static int currentState;/*< parametrul care indica starea curenta */
    public static int currentLevel;/*< parametrul care indica nivelul curent */

    private AudioPlayer bgMusic;/*< Muzica de fundal */
    private Thread t;/*< Thread ce se ocupa cu rularea muzicii de fundal */

    // stari
    public static final int NUMGAMESTATES = 8;/*< numarul de stari */
    public static final int HELP = 3;/*< HelpState */
    public static final int PAUSESTATE = 4;/*< PauseState */
    public static final int SETTINGSTATE = 5;/*< SettingsState */
    public static final int GAMEOVERSTATE = 7;/*< Game Over State */
    public static final int WINSTATE = 6; /*< Win State */
    public static final int MENUSTATE = 0;/*< MenuState */
    public static final int LEVEL1STATE = 1;/*< Level1State */
    public static final int LEVEL2STATE = 2;/*< Level2State */

    /*! \fn public GameStateManager()
        \brief Constructorul clasei, initializeaza muzica de fundal si vectorul de stari
     */
    public GameStateManager() {

        gameStates = new GameState[NUMGAMESTATES];

        currentState = MENUSTATE;
        loadState(currentState,null);

        bgMusic = new AudioPlayer("C:\\Users\\user\\IdeaProjects\\Joc_PAOO\\.idea\\WorldmapTheme.mp3.mp3",true);

        t = new Thread(bgMusic);
        t.start();
    }

    /*! \fn public void loadState(int state,GameState gs)
        \brief Incarca o stare noua sau o stare curenta in cauza pauzei
        \params state Indicele starii care se incarca
        \params gs Starea care se incarca
     */
    public void loadState(int state,GameState gs) {
        currentState = state;
        if (gs == null){
            if (state == MENUSTATE)
                gameStates[state] = new MenuState(this);
            if (state == HELP)
                gameStates[state] = new HelpState(this);
            if (state == PAUSESTATE)
                gameStates[state] = new PauseState(this);
            if (state == LEVEL1STATE) {
                gameStates[state] = new Level1State(this);
                currentLevel = LEVEL1STATE;
            }
            if (state == LEVEL2STATE) {
                gameStates[state] = new Level2State(this);
                currentLevel = LEVEL2STATE;
            }
            if (state == SETTINGSTATE)
                gameStates[state] = new SettingsState(this);
            if (state == WINSTATE)
                gameStates[state] = new WinState(this);
            if (state == GAMEOVERSTATE)
                gameStates[state] = new GameOverState(this);
        }
        else {
            if (state == MENUSTATE)
                gameStates[state] = gs;
            if (state == HELP)
                gameStates[state] = gs;
            if (state == PAUSESTATE)
                gameStates[state] = gs;
            if(state == SETTINGSTATE)
                gameStates[state] = gs;
            if (state == LEVEL1STATE) {
                gameStates[state] = gs;
                currentLevel = LEVEL1STATE;
            }
            if (state == LEVEL2STATE) {
                gameStates[state] = gs;
                currentLevel = LEVEL2STATE;
            }
            if (state == WINSTATE)
                gameStates[state] = gs;
            if (state == GAMEOVERSTATE)
                gameStates[state] = gs;
        }
        currentState = state;
    }

    /*! \fn public void setState(int state,GameState gs)
        \brief Seteaza starea curenta.
     */
    public void setState(int state,GameState gs) {
        currentState = state;
        loadState(currentState,gs);
    }

    /*! \fn public GameState getState()
        \brief Returneaza starea curenta.
     */
    public GameState getState()
    {
        return gameStates[currentState];
    }

    /*! \fn public GameState getLevel()
        \brief Retunrneaza nivelul curent
     */
    public GameState getLevel()
    {
        return gameStates[currentLevel];
    }

    /*! \fn public void update()
        \brief Actualizeaza starea.
     */
    public void update() {
        try {
            if(gameStates[currentState]!=null)
                gameStates[currentState].update();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*! \fn public void draw(java.awt.Graphics2D g)
        \brief Actualizeaza desenul pe ecran al starii curente.
     */
    public void draw(java.awt.Graphics2D g) {
        try {
            if(gameStates[currentState]!=null)
                gameStates[currentState].draw(g);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*! \fn public void musicTurn(boolean status)
        \brief Opreste/Porneste muzica
     */
    public void musicTurn(boolean status) {
        if(status == false)
        {
            try {
                bgMusic.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            bgMusic.resume();
            t = new Thread(bgMusic);
            t.start();
        }
    }

    /*! \fn public void keyPressed(int k)
        \brief Intercepteaza un eveniment de la tastatura, in acest caz apasarea tastei Enter
     */
    public void keyPressed(int k) {
        gameStates[currentState].keyPressed(k);
    }

    /*! \fn public void keyReleased(int k)
        \brief Intercepteaza un evenimentul de lasare a butonului de la tastatura
     */
    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }

}









