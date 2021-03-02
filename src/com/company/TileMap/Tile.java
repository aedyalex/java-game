package com.company.TileMap;

import java.awt.image.BufferedImage;

/*! \class Tile
    \brief Realizeaza un Tile generic ce va fi generat cu ajutorul fabricii de Tile-uri
 */
public abstract class Tile {

    protected BufferedImage image;/*< Imaginea tile-ului */
    protected int type;/*< Tipul tile-ului */

    //tile types
    public static final int NORMAL =0;/*< Tipul normal, prin care orice entitate poate trece */
    public static final int BLOCKED =1;/*< Tipul blocat, care nu permite trecerea prin el */

    /*! \fn public Tile(BufferedImage image, int type)
        \brief Contructorul clasei
     */
    public Tile(BufferedImage image, int type)
    {
        this.image = image;
        this.type = type;
    }

    /*! \fn public BufferedImage getImage()
        \brief Returneaza imaginea Tile-ului

     */
    public BufferedImage getImage()
    {
        return image;
    }

    /*! \fn public int getType()
        \brief Returneaza tipul Tile-ului
     */
    public int getType()
    {
        return type;
    }

    /*! \fn public void setType(int type)
        \brief Seteaza tipul
     */
    public void setType(int type)
    {
        this.type = type;
    }
}
