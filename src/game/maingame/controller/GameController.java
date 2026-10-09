package game.maingame.controller;

import static game.frame.controller.DisplayTarget.MAIN_GAME;

import game.Updatable;
import game.combat.controller.CombatController;
import game.combat.controller.CombatListener;
import game.combat.model.CombatOutcome;
import game.entities.Enemy;
import game.entities.player.Player;
import game.frame.controller.FrameAction;
import game.maingame.model.GameModel;
import game.maingame.view.GameViewAction;
import game.persistence.InventoryChange;
import game.reward.controller.RewardController;
import game.persistence.PersistenceService;
import game.persistence.SavedData;
import game.world.model.Point;
import game.world.controller.WorldController;
import game.world.model.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction, CombatListener, RoomListener
{
    private final List<Updatable> updatables;
    
    private final PersistenceService persistenceService;
    private final GameModel gameModel;
    
    private final FrameAction frameAction;
    private final WorldController worldController; //TODO: WorldAction

    private CombatController combatController;
    private RewardController rewardController;

    private Enemy currentEncounter;

    private GameViewAction gameViewAction;
    
    public GameController(PersistenceService pService, GameModel gModel, FrameAction fAction, WorldController wController)
    {
        persistenceService = pService;
        gameModel = gModel;
        
        frameAction = fAction;
        
        updatables = new ArrayList<>();
        worldController = wController;
        updatables.add(wController);
    }

    public void setCombatController(CombatController cController) {
        combatController = cController;
    }

    public void setRewardController(RewardController rController)
    {
        rewardController = rController;
    }

    public void setGameViewAction(GameViewAction gvAction) { gameViewAction = gvAction; }
    
    @Override
    public void requestOpenGameScreen()
    {
        frameAction.display(MAIN_GAME);
    }
    
    @Override
    public void requestNewGame(Player newPlayer)
    {
        Map<Point, RuntimeRoom> world = worldController.requestGenerateWorld();
        persistenceService.createNewSaveGame(newPlayer.getName(), newPlayer.getName(), newPlayer.getClassType(), world);
        startGame(newPlayer, world);
    }
    
    @Override
    public void requestLoadGame(SavedData savedData)
    {
        savedData.player.setInventory(savedData.inventory);
        savedData.player.setEquipped(savedData.equipped);
        startGame(savedData.player, savedData.world);
    }
    
    private void startGame(Player player, Map<Point, RuntimeRoom> world)
    {
        gameModel.setGame(player, world);
        world.get(player.getPoint()).setCurrent();

        requestOpenGameScreen();
        gameViewAction.updateMainGameScreen();

        requestGameUpdate();
    }
    
    @Override
    public void requestGameUpdate()
    {
        for(Updatable controller : updatables)
        {
            controller.requestUpdate();
        }
    }

    @Override
    public void onCombatEnded(CombatOutcome combatOutcome)
    {
        RuntimeRoom room = gameModel.getWorld().get(gameModel.getPlayer().getPoint());
        switch(combatOutcome)
        {
            case WIN -> { room.setCompleted(); rewardController.giveEnemyReward(currentEncounter); }
            case FLEE -> room.setFled();
            case LOSE -> { room.setCompleted(); }
        }
        currentEncounter = null;
        gameModel.quickSave(room, InventoryChange.NONE, null);
        requestGameUpdate();
    }

    public void addUpdatable(Updatable updatable)
    {
        updatables.add(updatable);
    }

    @Override
    public void onRoomEntered(RuntimeRoom room)
    {
        System.out.println("Entered " + room.getRoomState()); // TODO Temporary
    }
}