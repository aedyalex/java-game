package com.company;

import java.sql.*;

/*! \class DBHandler
    \brief Clasa care se ocupa cu gestiunea bazei de date
 */
public class DBHandler {
    private static Connection c = null;

    Statement stmt;
    PreparedStatement pstm;
    ResultSet rs;
    private static DBHandler dbh = null;

    /* \fn private Connection getConn()
        \brief Reprezinta un singleton pentru conexiune
     */
    private Connection getConn()
    {
        if(c == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:Resources/database.db");
            }
            catch (SQLException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return c;
    }

    /* \fn private DBHandler()
       \brief Constructorul clasei apelat in metoda getInstance()
    */
    private DBHandler()
    {
        try
        {
            c = getConn();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            String instr = "UPDATE SETTINGS set MUSIC =? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,0);
            pstm.executeUpdate();

            System.out.println("AM UPDATAT");
            c.commit();

            instr = "UPDATE SETTINGS set AUDIO =? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,0);
            pstm.executeUpdate();

        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    /* \fn public static DBHandler getInstance()
       \brief Reprezinta un Singleton pentru DBHandler
     */
    public static DBHandler getInstance()
    {
        if(dbh==null)
        {
            return new DBHandler();
        }
        else
        {
            return dbh;
        }
    }

    /* \fn public void updateMusic(boolean status) throws SQLException
       \brief Actualizeaza campul MUSIC din baza de date
     */
    public void updateMusic(boolean status) throws SQLException
    {
        if(status == true)
        {
            String instr = "UPDATE SETTINGS set MUSIC =? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,1);
            pstm.executeUpdate();

            c.commit();
            pstm.close();
        }
        else if(status == false)
        {
            String instr = "UPDATE SETTINGS set MUSIC =? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,0);

            pstm.executeUpdate();

            c.commit();
            pstm.close();
        }

    }

    /* \fn public int getMusic() throws SQLException
       \brief Returneaza valoarea campului MUSIC din baza de date
     */
    public int getMusic() throws SQLException
    {
        rs = stmt.executeQuery("Select * FROM SETTINGS");
        int r = rs.getInt("Music");
        c.commit();
        stmt.close();
        return r;
    }

    /* \fn public void updateAudio(boolean status) throws SQLException
       \brief Actualizeaza campul AUDIO din baza de date
     */
    public void updateAudio(boolean status) throws SQLException
    {
        if(status == true)
        {
            String instr = "UPDATE SETTINGS set AUDIO =? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,1);
            pstm.executeUpdate();

            c.commit();
            pstm.close();
        }
        else
        {
            String instr = "UPDATE SETTINGS set AUDIO =? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,0);
            pstm.executeUpdate();

            c.commit();
            pstm.close();
        }
    }

    /* \fn public int getAudio() throws SQLException
       \brief Returneaza campul AUDIO din baza de date
     */
    public int getAudio() throws SQLException
    {
        rs = stmt.executeQuery("Select * FROM SETTINGS");
        return rs.getInt("AUDIO");
    }

    /* \fn public void saveGame(int level, int health, int score)
       \brief Salveaza parametrii jucatorului in baza de date
     */
    public void saveGame(int level, int health, int score) {
        try {
            String instr = "UPDATE LOADSAVE set LEVEL=? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,level);
            pstm.executeUpdate();
            c.commit();

            instr = "UPDATE LOADSAVE set HEALTH=? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,health);
            pstm.executeUpdate();
            c.commit();

            instr = "UPDATE LOADSAVE set SCORE=? WHERE ROWID=1";
            pstm = c.prepareStatement(instr);
            pstm.setInt(1,score);
            pstm.executeUpdate();

            c.commit();
            pstm.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /* \fn public int getLevel() throws SQLException
       \brief Returneaza nivelul din baza de date
     */
    public int getLevel() throws SQLException
    {
        rs = stmt.executeQuery("Select * FROM LOADSAVE");
        return rs.getInt("Level");
    }
}
