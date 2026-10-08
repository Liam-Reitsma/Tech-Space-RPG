package game.maingame.controller;

import static game.frame.controller.DisplayTarget.MAIN_GAME;

import game.Updatable;
import game.combat.controller.CombatController;
import game.combat.controller.CombatListener;
import game.combat.model.CombatOutcome;
import game.entities.Enemy;
import game.entities.EnemyType;
import game.entities.player.Player;
import game.frame.controller.FrameAction;
import game.maingame.model.GameModel;
import game.maingame.view.GameViewAction;
import game.reward.controller.RewardController;
import game.world.model.Point;
import game.world.controller.WorldController;
import game.world.model.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction, CombatListener
{
    private final List<Updatable> updatables;
    
    private final GameModel gameModel;
    
    private final FrameAction frameAction;
    private final WorldController worldController; //TODO: WorldAction

    private CombatController combatController;
    private RewardController rewardController;

    private Enemy currentEncounter;

    private GameViewAction gameViewAction;
    
    public GameController(GameModel gModel, FrameAction fAction, WorldController wController)
    {
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
        startGame(newPlayer, worldController.requestGenerateWorld());
    }
    
    @Override
    public void requestLoadGame(Player loadedPlayer, Map<Point, RuntimeRoom> loadedWorld)
    {
        startGame(loadedPlayer, loadedWorld);
    }
    
    private void startGame(Player player, Map<Point, RuntimeRoom> world)
    {
        gameModel.setGame(player, world);
        
        requestOpenGameScreen();
        
        gameViewAction.updateMainGameScreen();



        currentEncounter = new Enemy(EnemyType.BRUISER, player.getLevel()); // TODO test only, remove in A5
        combatController.startCombat(currentEncounter);


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
        if(combatOutcome == CombatOutcome.WIN)
        {
            rewardController.giveEnemyReward(currentEncounter);
        }
    }

    public void addUpdatable(Updatable updatable)
    {
        updatables.add(updatable);
    }
}