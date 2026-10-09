package game;

import static game.frame.controller.DisplayTarget.*;

import game.combat.controller.CombatController;
import game.combat.model.CombatModel;
import game.combat.view.CombatButtonPanel;
import game.combat.view.CombatPane;
import game.combat.view.CombatTextPane;
import game.frame.controller.DisplayTarget;
import game.frame.controller.FrameController;
import game.frame.view.GameFrame;
import game.frame.view.OverlayContainer;
import game.gui.config.Displayable;
import game.maingame.controller.GameController;
import game.maingame.model.GameModel;
import game.maingame.view.GameScreen;
import game.maingamemenu.controller.MainGameMenuController;
import game.maingamemenu.view.MainGameMenuPanel;
import game.mainmenu.controller.MainMenuController;
import game.mainmenu.view.MainMenuScreen;
import game.newgame.controller.NewGameController;
import game.newgame.view.NewGameScreen;
import game.options.controller.OptionsController;
import game.options.view.OptionsOverlay;
import game.persistence.DBManager;
import game.persistence.DatabaseSchemaManager;
import game.persistence.PersistenceService;
import game.reward.controller.RewardController;
import game.reward.model.RewardModel;
import game.reward.view.ChestPanel;
import game.reward.view.RewardStatPanel;
import game.persistence.dao.InventoryDAO;
import game.persistence.dao.PlayerDAO;
import game.persistence.dao.WorldDAO;
import game.saves.controller.SavesController;
import game.saves.view.SavesOverlay;
import game.world.controller.WorldController;
import game.world.view.WorldMapPanel;
import game.world.view.WorldMovementPanel;
import game.world.view.WorldPanel;

import java.util.HashMap;
import java.util.Map;

public class Main
{
    public static void main(String[] args)
    {
        DBManager dbManager = new DBManager();
        DatabaseSchemaManager databaseSchemaManager = new DatabaseSchemaManager(dbManager);
        InventoryDAO inventoryDAO = new InventoryDAO(dbManager);
        PlayerDAO playerDAO = new PlayerDAO(dbManager);
        WorldDAO worldDAO = new WorldDAO(dbManager);
        PersistenceService persistenceService = new PersistenceService(databaseSchemaManager, inventoryDAO, playerDAO, worldDAO);
        GameModel gameModel = new GameModel(persistenceService);
        CombatModel combatModel = new CombatModel(gameModel);
        RewardModel rewardModel = new RewardModel(gameModel);

        Map<DisplayTarget, Displayable> displayMap = new HashMap<>();
        
        FrameController frameController = new FrameController();
        OptionsController optionsController = new OptionsController(frameController);
        WorldController worldController = new WorldController(gameModel);
        GameController gameController = new GameController(persistenceService, gameModel, frameController, worldController);
        NewGameController newGameController = new NewGameController(frameController, gameController);
        RewardController rewardController = new RewardController(rewardModel);
        SavesController savesController = new SavesController(persistenceService, frameController, gameController);
        CombatController combatController = new CombatController(combatModel);
        MainMenuController mainMenuController = new MainMenuController(frameController, newGameController, optionsController, savesController);
        MainGameMenuController mainGameMenuController = new MainGameMenuController(gameModel, frameController, mainMenuController, optionsController, savesController);

        OverlayContainer overlayContainer = new OverlayContainer();
        GameFrame gameFrame = new GameFrame("Tech Space RPG", overlayContainer);
        MainMenuScreen mainMenuScreen = new MainMenuScreen("Tech Space RPG", mainMenuController);
        NewGameScreen newGameScreen = new NewGameScreen(newGameController);
        OptionsOverlay optionsOverlay = new OptionsOverlay(optionsController);
        SavesOverlay savesOverlay = new SavesOverlay(savesController);
        
        WorldMapPanel worldMapPanel = new WorldMapPanel();
        WorldMovementPanel worldMovementPanel = new WorldMovementPanel(worldController);
        WorldPanel worldPanel = new WorldPanel(worldMapPanel, worldMovementPanel);

        RewardStatPanel rewardStatPanel = new RewardStatPanel(rewardController);
        ChestPanel chestPanel = new ChestPanel(rewardController);

        CombatButtonPanel combatButtonPanel = new CombatButtonPanel(combatController);
        CombatTextPane combatTextPane = new CombatTextPane();
        CombatPane combatPane = new CombatPane(combatButtonPanel, combatTextPane, rewardStatPanel, chestPanel);

        MainGameMenuPanel mainGameMenuPanel = new MainGameMenuPanel(mainGameMenuController);

        GameScreen gameScreen = new GameScreen(combatPane, mainGameMenuPanel, worldPanel, gameController);

        displayMap.put(MAIN_GAME, gameScreen);
        displayMap.put(MAIN_MENU, mainMenuScreen);
        displayMap.put(NEW_GAME, newGameScreen);
        displayMap.put(OPTIONS, optionsOverlay);
        displayMap.put(SAVES, savesOverlay);
        
        gameFrame.registerDisplay(displayMap);

        combatController.setCombatViewAction(combatPane);
        combatController.setCombatListener(gameController);
        frameController.setFrameViewAction(gameFrame);
        worldController.setWorldViewAction(worldPanel);

        gameController.setGameViewAction(gameScreen);
        gameController.setCombatController(combatController);
        gameController.setRewardController(rewardController);

        gameController.addUpdatable(combatController);
        gameController.addUpdatable(rewardController);

        newGameController.setNewGameViewAction(newGameScreen);
        rewardController.setRewardViewAction(combatPane);
        savesController.setSavesViewAction(savesOverlay);

        frameController.display(MAIN_MENU);
        gameFrame.setVisible(true);

        Runtime.getRuntime().addShutdownHook(new Thread(dbManager::closeSQLConnection));
    }
}