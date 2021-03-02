package com.company.TileMap;

import java.awt.image.BufferedImage;
/*! \class TileFactory
    \brief Descrie fabrica de Tile-uri
 */
public class TileFactory {
    /*! \fn public Tile getTile(String type, BufferedImage bf)
        \brief Metoda care returneaza un Tile in functie de tip
        \param type tipul Tile-ului
        \param bf Imaginea Tile-ului

     */
    public Tile getTile(String type, BufferedImage bf)
    {
        if(type.equalsIgnoreCase("NORMAL"))
        {
            return new NormalTile(bf);
        }
        else if(type.equalsIgnoreCase("BLOCKED"))
        {
            return new BlockedTile(bf);
        }
        else
            return null;
    }

}
