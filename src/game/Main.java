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
import game.player.controller.PlayerController;
import game.player.model.PlayerModel;
import game.player.view.PlayerPanel;
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
        DBManager dbManager = new DBManager("jdbc:derby:database/TechSpaceRPG_DB;create=true", "TSDB", "tsdb");
        DatabaseSchemaManager databaseSchemaManager = new DatabaseSchemaManager(dbManager);
        InventoryDAO inventoryDAO = new InventoryDAO(dbManager);
        PlayerDAO playerDAO = new PlayerDAO(dbManager);
        WorldDAO worldDAO = new WorldDAO(dbManager);
        PersistenceService persistenceService = new PersistenceService(databaseSchemaManager, inventoryDAO, playerDAO, worldDAO);
        GameModel gameModel = new GameModel(persistenceService);
        CombatModel combatModel = new CombatModel(gameModel);
        PlayerModel playerModel = new PlayerModel(gameModel);
        RewardModel rewardModel = new RewardModel(gameModel);

        Map<DisplayTarget, Displayable> displayMap = new HashMap<>();
        
        CombatController combatController = new CombatController(combatModel);
        FrameController frameController = new FrameController();
        OptionsController optionsController = new OptionsController(frameController);
        WorldController worldController = new WorldController(gameModel);
        RewardController rewardController = new RewardController(rewardModel, frameController);
        GameController gameController = new GameController(persistenceService, gameModel, combatController, frameController, rewardController, worldController);
        SavesController savesController = new SavesController(persistenceService, frameController, gameController);
        MainMenuController mainMenuController = new MainMenuController(frameController);
        MainGameMenuController mainGameMenuController = new MainGameMenuController(gameModel, frameController);
        NewGameController newGameController = new NewGameController(frameController, gameController);
        PlayerController playerController = new PlayerController(playerModel, frameController);
        
        OverlayContainer overlayContainer = new OverlayContainer();
        GameFrame gameFrame = new GameFrame("Tech Space RPG", overlayContainer);
        MainMenuScreen mainMenuScreen = new MainMenuScreen("Tech Space RPG", mainMenuController);
        NewGameScreen newGameScreen = new NewGameScreen(newGameController);
        OptionsOverlay optionsOverlay = new OptionsOverlay(optionsController);
        PlayerPanel playerPanel = new PlayerPanel(playerController, gameFrame);
        SavesOverlay savesOverlay = new SavesOverlay(savesController);
        
        WorldMapPanel worldMapPanel = new WorldMapPanel();
        WorldMovementPanel worldMovementPanel = new WorldMovementPanel(worldController);
        WorldPanel worldPanel = new WorldPanel(worldMapPanel, worldMovementPanel);

        RewardStatPanel rewardStatPanel = new RewardStatPanel(rewardController);
        ChestPanel chestPanel = new ChestPanel(rewardController);

        CombatButtonPanel combatButtonPanel = new CombatButtonPanel(combatController);
        CombatTextPane combatTextPane = new CombatTextPane();
        CombatPane combatPane = new CombatPane(chestPanel, combatButtonPanel, combatTextPane, rewardStatPanel);

        MainGameMenuPanel mainGameMenuPanel = new MainGameMenuPanel(mainGameMenuController);

        GameScreen gameScreen = new GameScreen(combatPane, mainGameMenuPanel, playerPanel, worldPanel, gameController);

        displayMap.put(MAIN_GAME, gameScreen);
        displayMap.put(MAIN_MENU, mainMenuScreen);
        displayMap.put(NEW_GAME, newGameScreen);
        displayMap.put(OPTIONS, optionsOverlay);
        displayMap.put(SAVES, savesOverlay);
        
        gameFrame.registerDisplay(displayMap);

        combatController.setCombatViewAction(combatPane);
        combatController.setCombatListener(gameController);
        frameController.setViewActions(gameFrame);
        gameController.addUpdatable(combatController);
        combatController.setGameAction(gameController);
        
        gameController.addUpdatable(playerController);
        playerController.setGameAction(gameController);
        
        gameController.addUpdatable(rewardController);
        rewardController.setGameAction(gameController);
        
        gameController.addUpdatable(worldController);
        worldController.setGameAction(gameController);
        
        newGameController.setNewGameViewAction(newGameScreen);
        optionsController.setOptionsViewAction(optionsOverlay);
        playerController.setPlayerViewAction(playerPanel);
        rewardController.setRewardViewAction(combatPane);
        rewardController.setRewardListener(gameController);
        savesController.setSavesViewAction(savesOverlay);
        worldController.setWorldViewAction(worldPanel);
        worldController.setRoomListener(gameController);

        frameController.requestDisplay(MAIN_MENU);
        gameFrame.setVisible(true);

        Runtime.getRuntime().addShutdownHook(new Thread(dbManager::closeConnection));
    }
}