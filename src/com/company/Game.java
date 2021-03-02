package com.company;
import javax.swing.JFrame;

/*! \class Game
    \brief Actioneaza pe post de clasa Main, configurand fereastra si setand continutul acesteia
 */
public class Game {
    public static void main(String[] args) {

        JFrame window = new JFrame("Game");
        //Graphics acceleration
        System.setProperty("sun.java2d.opengl", "true");
        //Singleton
        window.setContentPane(GamePanel.getInstance());

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);

    }
}
