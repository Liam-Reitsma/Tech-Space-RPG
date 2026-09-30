package game;

import game.gui.GameFrame;
import game.mainmenu.MainMenuController;
import game.newgame.NewGameController;
import game.options.OptionsController;
import game.persistence.DBManager;
import game.persistence.PersistenceService;
import game.saves.SavesController;
import game.world.WorldController;

public class Main
{
    public static void main(String[] args)
    {
        DBManager dbManager = new DBManager();
        PersistenceService persistenceService = new PersistenceService(dbManager);
        GameFrame gameFrame = new GameFrame("Tech Space RPG");
        GameModel gameModel = new GameModel();
        
        WorldController worldController = new WorldController(gameModel, gameFrame);
        GameController gameController = new GameController(worldController, gameModel, gameFrame);
        
        NewGameController newGameController = new NewGameController(gameFrame, gameController);
        SavesController savesController = new SavesController(gameController, persistenceService, gameFrame);
        MainMenuController mainMenuController = new MainMenuController(savesController, gameFrame);
        OptionsController optionsController = new OptionsController(gameFrame);
        gameFrame.setMainMenuAction(mainMenuController);
        gameFrame.setOptionsAction(optionsController);
        gameFrame.setSavesAction(savesController);
        gameFrame.setNewGameAction(newGameController);
        
        //persistenceService.createNewGame("test", "testGuy", ClassType.GUARDIAN);
        gameFrame.openMainMenuScreen();
        gameFrame.setVisible(true);
        
        //dbManager.closeSQLConnection();
        //System.exit(0);
    }
}