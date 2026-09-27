package game;

import game.entities.player.ClassType;
import game.gui.GameFrame;
import game.persistence.PersistenceController;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
//        GameFrame gameFrame = new GameFrame("Tech Space RPG");
        DBManager dbManager = new DBManager();
        PersistenceController pController = new PersistenceController(dbManager);
        pController.createNewGame("test", "testGuy", ClassType.GUARDIAN);
        
//        gameFrame.setVisible(true);
        
        dbManager.closeSQLConnection();
        System.exit(0);
    }
}