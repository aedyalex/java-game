package com.company.TileMap;

import java.awt.image.BufferedImage;

/*! \class BlockedTile
    \brief Clasa care descrie Tile-ul NORMAL
 */
public class NormalTile extends Tile {

    /*! \fn public NormalTile(BufferedImage image)
    \brief Contructorul clasei
 */
    public NormalTile(BufferedImage image)
    {
        super(image,0);
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
