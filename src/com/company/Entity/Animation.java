package com.company.Entity;
import java.awt.image.BufferedImage;

/*! \class Animation
    \brief Aceasta clasa este responsabila de gestiunea animatiilor:
    initializare, set/get frame, update

 */
public class Animation {
    private BufferedImage[] frames;/*< vector de imagini folosit la realizarea animatiei */
    private int currentFrame;/*< indicele imaginii curente din animatie */

    private long startTime;/*< timpul la care a inceput desfasurarea animatiei, util in cazul derularii imaginilor */

    private long delay;/*< reprezinta timpul alocat fiecarei imagini de a se desfasura */

    private boolean playedOnce;/* flag-ul ne indica daca animatia a avut o singura desfasurare */

    /*! \fn public Animation()
        \brief Constructorul clasei Animation, seteaza flagul playedOnce false
     */
    public Animation() {
        playedOnce = false;
    }

    /*! \fn public void setFrames(BufferedImage[] frames)
        \brief Seteaza imaginile necesare rularii animatiei
     */
    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
        currentFrame = 0;
        playedOnce = false;
    }

    /*! \fn public void setDelay(long d)
        \brief Seteaza delay-ul
     */
    public void setDelay(long d) { delay = d; }

    /*! \fn public void setFrame(int i)
        \brief Seteaza imaginea curenta
     */
    public void setFrame(int i) { currentFrame = i; }

    /*! \fn public void update()
        \brief Ruleaza animatia in bucla, respectand delay-ul si numarul de cadre
     */
    public void update() {

        if(delay == -1) return;

        // timpul care s-a scurs de la cadrul curent pana la rularea cadrului urmator
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        //daca delay-ul a fost atins, se ruleaza urmatoarea imagine
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        //daca s-a atins ultimul cadru, rularea se face de la capat
        if(currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }

    }

    /*! \fn public int getFrame()
        \brief Returneaza indicele cadrului curent
     */
    public int getFrame() { return currentFrame; }

    /*! \fn public BufferedImage getImage()
        \brief Returneaza cadrul curent
     */
    public BufferedImage getImage() { return frames[currentFrame]; }

    /*! \fn public boolean hasPlayedOnce()
        \brief Returneaza flag-ul playedOnce
     */
    public boolean hasPlayedOnce() { return playedOnce; }

}
