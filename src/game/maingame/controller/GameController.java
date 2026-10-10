package game.maingame.controller;

import static game.frame.controller.DisplayTarget.MAIN_GAME;

import game.Updatable;
import game.combat.controller.CombatAction;
import game.combat.controller.CombatListener;
import game.combat.model.CombatOutcome;
import game.entities.Enemy;
import game.entities.player.Player;
import game.frame.controller.FrameAction;
import game.items.Item;
import game.maingame.model.GameModel;
import game.maingame.view.GameViewAction;
import game.persistence.InventoryChange;
import game.reward.controller.RewardAction;
import game.persistence.PersistenceService;
import game.persistence.SavedData;
import game.reward.controller.RewardListener;
import game.world.controller.RoomListener;
import game.world.model.Point;
import game.world.controller.WorldController;
import game.world.model.room.RoomContent;
import game.world.model.room.RoomModel;
import game.world.model.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction, CombatListener, RoomListener, RewardListener
{
    private final List<Updatable> updatables;
    
    private final PersistenceService persistenceService;
    private final GameModel gameModel;
    private final RoomModel roomModel;

    private final CombatAction combatAction;
    private final FrameAction frameAction;
    private final RewardAction rewardAction;
    private final WorldController worldController; //TODO: WorldAction

    private Enemy currentEncounter;

    private GameViewAction gameViewAction;
    
    public GameController(PersistenceService pService, GameModel gModel, CombatAction cAction,  FrameAction fAction, RewardAction rAction, WorldController wController)
    {
        persistenceService = pService;
        gameModel = gModel;
        roomModel = new RoomModel();

        combatAction = cAction;
        frameAction = fAction;
        rewardAction = rAction;
        
        updatables = new ArrayList<>();
        worldController = wController;
        updatables.add(wController);
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
        worldController.setMovementLocked(false);
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
            case WIN -> { room.setCompleted(); rewardAction.requestEnemyReward(currentEncounter); }
            case FLEE -> room.setFled();
            case LOSE -> { }
        }
        currentEncounter = null;
        gameModel.quickSave(room, InventoryChange.NONE, null);
        worldController.setMovementLocked(false);
        requestGameUpdate();
    }

    public void addUpdatable(Updatable updatable)
    {
        updatables.add(updatable);
    }

    @Override
    public void onRoomEntered(RuntimeRoom room)
    {
        if(room.isCompleted()) return;

        RoomContent content = roomModel.createRoomContent(room.getRoomState().getRoomType(), gameModel.getPlayer());

        if(content instanceof Enemy enemy)
        {
            currentEncounter = enemy;
            worldController.setMovementLocked(true);
            combatAction.startCombat(currentEncounter);
        }
        else if(content instanceof Item item)
        {
            worldController.setMovementLocked(true);
            rewardAction.requestOfferChestReward(item);
            room.setCompleted();
            gameModel.quickSave(room, InventoryChange.NONE, null);
        }
        else
        {
            room.setCompleted();
            gameModel.quickSave(room, InventoryChange.NONE, null);
        }
        requestGameUpdate();
    }

    @Override
    public void onChestDecision()
    {
        worldController.setMovementLocked(false);
        requestGameUpdate();
    }
}