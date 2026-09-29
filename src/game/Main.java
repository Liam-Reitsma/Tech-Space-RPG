package game;

import game.entities.player.ClassType;
import game.gui.GameFrame;
import game.mainmenu.MainMenuController;
import game.persistence.DBManager;
import game.persistence.PersistenceService;

public class Main
{
    public static void main(String[] args)
    {
        GameFrame gameFrame = new GameFrame("Tech Space RPG");
        MainMenuController mmController = new MainMenuController(gameFrame);
        gameFrame.setMainMenuAction(mmController);
        DBManager dbManager = new DBManager();
        PersistenceService pController = new PersistenceService(dbManager);
        pController.createNewGame("test", "testGuy", ClassType.GUARDIAN);
        
        gameFrame.setVisible(true);
        
        dbManager.closeSQLConnection();
        System.exit(0);
    }
}