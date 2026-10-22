package game.maingame.controller;

import static game.frame.controller.DisplayTarget.MAIN_GAME;

import game.Updatable;
import game.audio.AudioManager;
import game.audio.MusicTrack;
import game.audio.SoundEffect;
import game.combat.controller.CombatAction;
import game.combat.controller.CombatListener;
import game.combat.model.CombatOutcome;
import game.entities.Enemy;
import game.gui.popup.PopupError;
import game.player.model.Player;
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
import game.world.controller.WorldAction;
import game.world.model.Point;
import game.world.model.room.RoomContent;
import game.world.model.room.RoomModel;
import game.world.model.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction, CombatListener, RoomListener, RewardListener
{
    private final AudioManager audioManager;
    private final List<Updatable> updatables;
    
    private final PersistenceService persistenceService;
    private final GameModel gameModel;
    private final RoomModel roomModel;

    private final CombatAction combatAction;
    private final FrameAction frameAction;
    private final RewardAction rewardAction;

    private final WorldAction worldAction; //TODO: WorldAction

    private Enemy currentEncounter;

    private GameViewAction gameViewAction;
    
    public GameController(PersistenceService pService, GameModel gModel, CombatAction cAction, FrameAction fAction, RewardAction rAction, WorldAction wAction)
    {
        audioManager = AudioManager.getInstance();
        updatables = new ArrayList<>();

        persistenceService = pService;
        gameModel = gModel;
        roomModel = new RoomModel();

        combatAction = cAction;
        frameAction = fAction;
        rewardAction = rAction;
        worldAction = wAction;
    }

    public void setGameViewAction(GameViewAction gvAction) { gameViewAction = gvAction; }
    
    @Override
    public void requestOpenGameScreen()
    {
        audioManager.playSound(SoundEffect.MAIN_GAME_BACKSTORY_SOUNDBITE);
        audioManager.playMusic(MusicTrack.MAIN_GAME_AMBIANCE);
    }
    
    @Override
    public void requestNewGame(Player newPlayer)
    {
        Map<Point, RuntimeRoom> world = worldAction.requestGenerateWorld();
        if(persistenceService.doesSaveExist(newPlayer.getName()))
            frameAction.requestDisplayError(
                    PopupError.DUPLICATE_SAVE_NAME,
                    e -> frameAction.requestClosePopup(),
                    e -> forceStartNewGame(newPlayer, world));
        else
        {
            persistenceService.createNewSaveGame(newPlayer.getName(), newPlayer.getName(), newPlayer.getClassType(), world);
            startGame(newPlayer, world);
        }
    }
    
    @Override
    public void requestLoadGame(SavedData savedData)
    {
        savedData.player.setInventory(savedData.inventory);
        savedData.player.setEquipped(savedData.equipped);
        startGame(savedData.player, savedData.world);
    }
    
    private void forceStartNewGame(Player player, Map<Point, RuntimeRoom> world)
    {
        persistenceService.forceCreateNewSaveGame(player.getName(), player.getName(), player.getClassType(), world);
        frameAction.requestClosePopup();
        startGame(player, world);
    }

    private void startGame(Player player, Map<Point, RuntimeRoom> world)
    {
        gameModel.setGame(player, world);
        boolean isDead = player.getHealth() <= 0;
        world.get(player.getPoint()).setCurrent();
        worldAction.requestSetMovementLocked(isDead);
        combatAction.requestResetCombat();
        currentEncounter = null;
        rewardAction.requestResetReward();
        if(isDead) combatAction.requestDeathMessage();
        requestOpenGameScreen();

        frameAction.requestDisplay(MAIN_GAME);

        gameViewAction.updateMainGameScreen();
        requestGameUpdate();
        if(!isDead) onRoomEntered(world.get(player.getPoint()));
    }
    
    @Override
    public void requestGameUpdate()
    {
        for(Updatable controller : updatables)
        {
            controller.requestUpdate();
        }
    }

    public void addUpdatable(Updatable updatable)
    {
        updatables.add(updatable);
    }

    @Override
    public void onCombatEnded(CombatOutcome combatOutcome)
    {
        RuntimeRoom room = gameModel.getWorld().get(gameModel.getPlayer().getPoint());

        if(combatOutcome == CombatOutcome.LOSE)
        {
            currentEncounter = null;
            gameModel.quickSave(room, InventoryChange.NONE, null);
            requestGameUpdate();
            return;
        }
        switch(combatOutcome)
        {
            case WIN -> { room.setCompleted(); rewardAction.requestEnemyReward(currentEncounter); }
            case FLEE -> room.setFled();
            default -> { }
        }
        currentEncounter = null;
        gameModel.quickSave(room, InventoryChange.NONE, null);
        worldAction.requestSetMovementLocked(false);
        requestGameUpdate();
    }

    @Override
    public void onRoomEntered(RuntimeRoom room)
    {
        if(room.isCompleted()) return;

        RoomContent content = roomModel.createRoomContent(room.getRoomState().getRoomType(), gameModel.getPlayer());

        if(content instanceof Enemy enemy)
        {
            currentEncounter = enemy;
            worldAction.requestSetMovementLocked(true);
            combatAction.requestStartCombat(currentEncounter);
        }
        else if(content instanceof Item item)
        {
            worldAction.requestSetMovementLocked(true);
            rewardAction.requestOfferChestReward(item);
            room.setCompleted();
            gameModel.quickSave(room, InventoryChange.NONE, null); //TODO Add item
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
        worldAction.requestSetMovementLocked(false);
        requestGameUpdate();
    }
}