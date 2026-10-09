package game.maingame.controller;

import static game.frame.controller.DisplayTarget.MAIN_GAME;

import game.Updatable;
import game.audio.AudioManager;
import game.audio.MusicTrack;
import game.audio.SoundEffect;
import game.combat.controller.CombatController;
import game.combat.controller.CombatListener;
import game.combat.model.CombatOutcome;
import game.entities.Enemy;
import game.entities.player.Player;
import game.frame.controller.FrameAction;
import game.gui.errors.DisplayError;
import game.maingame.model.GameModel;
import game.maingame.view.GameViewAction;
import game.reward.controller.RewardController;
import game.persistence.PersistenceService;
import game.persistence.SavedData;
import game.world.controller.WorldAction;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction, CombatListener
{
    private final AudioManager audioManager;
    private final List<Updatable> updatables;
    
    private final PersistenceService persistenceService;
    private final GameModel gameModel;
    
    private final FrameAction frameAction;
    private final WorldAction worldAction; //TODO: WorldAction
    
    private CombatController combatController;
    private RewardController rewardController;

    private Enemy currentEncounter;

    private GameViewAction gameViewAction;
    
    public GameController(PersistenceService pService, GameModel gModel, FrameAction fAction, WorldAction wAction)
    {
        audioManager = AudioManager.getInstance();
        updatables = new ArrayList<>();
        
        persistenceService = pService;
        gameModel = gModel;
        
        frameAction = fAction;
        worldAction = wAction;
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
        frameAction.requestDisplay(MAIN_GAME);
        audioManager.playSound(SoundEffect.MAIN_GAME_BACKSTORY_SOUNDBITE);
        audioManager.playMusic(MusicTrack.MAIN_GAME_AMBIANCE);
    }
    
    @Override
    public void requestNewGame(Player newPlayer)
    {
        Map<Point, RuntimeRoom> world = worldAction.requestGenerateWorld();
        if(persistenceService.doesSaveExist(newPlayer.getName()))
            frameAction.requestDisplayError(
                    DisplayError.DUPLICATE_SAVE_NAME,
                    e -> frameAction.requestCloseError(),
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
        frameAction.requestCloseError();
        startGame(player, world);
    }
    
    private void startGame(Player player, Map<Point, RuntimeRoom> world)
    {
        gameModel.setGame(player, world);
        
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
    
    public void addUpdatable(Updatable updatable)
    {
        updatables.add(updatable);
    }

    @Override
    public void onCombatEnded(CombatOutcome combatOutcome)
    {
        if(combatOutcome == CombatOutcome.WIN)
        {
            rewardController.giveEnemyReward(currentEncounter);
        }
    }
}