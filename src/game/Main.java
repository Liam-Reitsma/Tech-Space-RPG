package game;

import game.entities.player.ClassType;
import game.gui.GameFrame;
import game.mainmenu.MainMenuController;
import game.options.OptionsController;
import game.persistence.DBManager;
import game.persistence.PersistenceService;
import game.saves.SavesController;

public class Main
{
    public static void main(String[] args)
    {
        DBManager dbManager = new DBManager();
        PersistenceService pService = new PersistenceService(dbManager);
        GameFrame gameFrame = new GameFrame("Tech Space RPG");
        
        SavesController sController = new SavesController(pService, gameFrame);
        MainMenuController mmController = new MainMenuController(sController, gameFrame);
        OptionsController oController = new OptionsController(gameFrame);
        gameFrame.setMainMenuAction(mmController);
        gameFrame.setOptionsAction(oController);
        gameFrame.setSavesAction(sController);
        
        pService.createNewGame("test", "testGuy", ClassType.GUARDIAN);
        
        gameFrame.setVisible(true);
        
        dbManager.closeSQLConnection();
        System.exit(0);
    }
}