package com.company.Audio;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;

/*! \class AudioPlayer
    \brief Clasa aceasta ne permite sa rulam continut audio in timpul rularii jocului
    Metodele acesteia constau in a citi un fisier media, a-l rula sau a-l opri la nevoie
    Aceste actiuni sunt sustinute pe baza unui thread dedicat pornirii/opririi mp3-urilor
 */
public class AudioPlayer extends PlaybackListener implements Runnable{

    private boolean loop=false;
    private AdvancedPlayer clip;
    private String filename;
    private boolean paused = false;

    /*! \fn public AudioPlayer(String s, boolean loop)
        \brief Constructorul de initializare al clasei AudioPlayer ce seteaza
        numele fisierului audio si modul de rulare

        \param s Este un String ce indica numele fisierului audio
        \param loop Indica modul in care ruleaza fisierul:
        daca ruleaza in bucla infinita atunci va avea valoarea true,
        pentru rulare o singura data false
     */
    public AudioPlayer(String s, boolean loop) {

        try {
            filename = s;
            this.loop = loop;
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    /*! \fn public run()
        \brief Metoda run descrie codul executat in paralel de thread-ul responsabil cu continutul audio
        Parametrul loop din contructor este folosit aici pentru a stabili de cate ori se va rula mp3-ul
     */
    public void run() {

        if (loop)
            do {
                try {
                    clip = new AdvancedPlayer(new FileInputStream(filename));
                    clip.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (loop);
        else {
            try {
                clip = new AdvancedPlayer(new FileInputStream(filename));
                clip.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*! \fn public void stop() throws InterruptedException
        \brief Metoda stop opreste clip-ul audio pana cand va fi reluat
        in metoda resume()
     */
    public void stop() throws InterruptedException{
        paused = true;
        clip.close();
        loop = false;

    }

    /*! \fn public resume()
        \brief Metoda resume reia executia mp3-ului, folosita mai ales
        la setarea Music din Meniul jocului impreuna cu metoda stop
     */
    public void resume() 
    {
        paused = false;
        loop = true;
    }
}














