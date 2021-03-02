package com.company.TileMap;
import com.company.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

/*! \class TileMap
    \brief Clasa care creeaza si gestioneaza harta

 */
public class TileMap{
    //pozitia
    private double x;/*< Coordonata fizica x a hartii */
    private double y;/*< Coordonata fizica y a hartii */

    //margini
    private int xmin;/*< Marginea inferioara x */
    private int ymin;/*< Marginea inferioara y */
    private int xmax;/*< Marginea superioara x */
    private int ymax;/*< Marginea superioara y */

    private double tween;/*< parametrul care influenteaza cat de cursiv ruleaza imaginea */

    //map
    private int[][] map;/*< Matricea hartii extrasa din fisierul text */
    private int tileSize;/*< Dimensiunea unui Tile*/
    private int numRows;/*< Numarul de linii */
    private int numCols;/*< Numarul de coloane*/
    private int width;/*< lungimea cadrului */
    private int height;/*< inaltimea cadrului */

    // tileset
    private BufferedImage tileset;/*< Imaginea ce contine Tile-urile*/
    private int numTilesAcross;/*< Numarul de Tile-uri in lungime */
    private int numTileSub;/*< Numarul de Tile-uri in intaltime*/
    private Tile[][] tiles;/*< Matrice ce stocheaza tile-urile citite din imagine */

    //tileFactory
    private TileFactory tf;/*< Fabrica de Tile-uri */

    //drawing
    private int rowOffset;/*< Linia de la care incepe sa se deseneze */ //de unde sa incepe sa deseneze(la randare)
    private int colOffset;/*< Coloana de la care incepe sa se deseneze */
    private int numRowsToDraw;/*< Numarul de linii de desenat */
    private int numColsToDraw;/*< Numarul de coloane de desenat */

    /*! \fn public TileMap(int tileSize)
        \brief Constructorul clasei, initializeaza tileSize, TileFactory si cate Tileuri sa deseneze
        pe linie/coloana
     */
    public TileMap(int tileSize)
    {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT/tileSize + 2;//pt a nu se pierde tiles
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.07;
        tf = new TileFactory();
    }

    /*! \fn public void loadTiles(String s)
        \brief incarca tile-urile decupate din tileset in matricea tiles

     */
    public void loadTiles(String s)
    {
        try{
            tileset = ImageIO.read(new FileInputStream(s));
            numTilesAcross = tileset.getWidth()/tileSize;
            numTileSub =tileset.getHeight()/tileSize;
            tiles = new Tile[numTileSub][numTilesAcross];
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
                tiles[0][col] = tf.getTile("NORMAL",subimage);
            }

            for(int row = 1; row<numTileSub;row++) {
                for (int col = 0; col < numTilesAcross; col++) {
                    subimage = tileset.getSubimage(col * tileSize, row*tileSize, tileSize, tileSize);
                    tiles[row][col] = tf.getTile("BLOCKED",subimage);//new Tile(subimage, BLOCKED);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public void loadMap(String s)
        \brief Incarca fisierul text cu harta in matricea map

     */
    public void loadMap(String s)
    {
        try{
            FileInputStream in = new FileInputStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;

            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;
            String delim="\\s+";

            for(int row = 0; row<numRows; row++)
            {
                String line = br.readLine();
                String[] tokens = line.split(delim);
                for(int col = 0; col < numCols; col++)
                {
                    map[row][col] = Integer.parseInt(tokens[col]);

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*! \fn public int getTileSize()
        \brief Returneaza tileSize
     */
    public int getTileSize() { return tileSize; }
    /*! \fn  public double getx()
        \brief Returneaza x
     */
    public double getx() { return x;}
    /*! \fn public double gety()
        \brief Returneaza y
     */
    public double gety() { return y;}
    /*! \fn public int getWidth()
        \brief Returneaza width
     */
    public int getWidth() { return width; }
    /*! \fn public int getHeight()
        \brief Returneaza height
     */
    public int getHeight() {return height;}

    /*! \fn public int getType(int row, int col)
        \brief Returneaza tipul Tile-ului de pe pozitia (row,col)
     */
    public int getType(int row, int col)
    {
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    /*! \fn public void setTween(double t)
        \brief Seteaza tween
     */
    public void setTween(double t)
    {
        this.tween = t;
    }

    /*! \fn public void setPosition(double x, double y)
        \brief Seteaza pozitia de la care incepesa se deseneze
     */
    public void setPosition(double x, double y)
    {
        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;

        fixBounds();

        //de unde incepem sa desenam tilemap
        colOffset = (int)-this.x/tileSize;
        rowOffset = (int)-this.y/tileSize;
    }

    /*! \fn private void fixBounds()
        \brief Regleaza limitele
     */
    private void fixBounds()
    {
        if(x<xmin) x=xmin;
        if(y<ymin) y=ymin;
        if(x>xmax) x=xmax;
        if(y>ymax) y=ymax;
    }

    /*! \fn public void draw(Graphics2D g)
        \brief Deseneaza harta
     */
    public void draw(Graphics2D g)
    {
        for(int row = rowOffset; row<rowOffset+numRowsToDraw;row++)
        {
            if(row>=numRows) break;
            for(int col = colOffset;col<colOffset+numColsToDraw;col++)
            {
                if(col >= numCols) break;

                if(map[row][col]==0) continue;//baca e blank

                int rc = map[row][col];
                int r = rc/numTilesAcross;
                int c = rc%numTilesAcross;

                g.drawImage(tiles[r][c].getImage(),(int)x+col*tileSize,(int)y+row*tileSize, null);


            }
        }
    }
}
