package game;

import game.combat.CombatController;
import game.combat.CombatErrors;
import game.combat.CombatOutcome;
import game.combat.CombatResult;
import game.entities.Enemy;
import game.entities.Player;
import game.mainmenu.MainMenuController;
import game.mainmenu.MainMenuResult;
import game.mainmenu.MainMenuStates;
import game.persistence.PersistenceController;
import game.persistence.PersistenceErrors;
import game.persistence.PersistenceResult;
import game.reward.RewardController;
import game.reward.RewardErrors;
import game.reward.RewardResult;
import game.world.*;

public class GameController
{
    private boolean isAlive;
    private boolean isRunning;
    
    private Player player;
    
    private final CombatController combatC;
    private final MainMenuController mainMenuC;
    private final PersistenceController persistenceC;
    private final RewardController rewardC;
    private final RoomController roomC;
    private final WorldController worldC;
    
    public GameController(CombatController cc, MainMenuController mmc, PersistenceController pc, RewardController rec, RoomController roc, WorldController wc)
    {
        isAlive = true;
        isRunning = true;
        
        combatC = cc;
        mainMenuC = mmc;
        persistenceC = pc;
        rewardC = rec;
        roomC = roc;
        worldC = wc;
    }
    
    public int start()
    {
        MainMenuResult mmResult = new MainMenuResult(null, null);
        PersistenceResult pResult = new PersistenceResult(null, null, null, null, null);
        
        for(int i = 0; i < 2; i++)
        {
            switch(i)
            {
                case 0:
                     mmResult = mainMenuC.start();
                    if(mmResult.getStateUsed() == MainMenuStates.EXIT) return 0;
                    break;
                case 1:
                    pResult = persistenceC.start(mmResult.getStateUsed());
                    if(pResult.getError() == PersistenceErrors.USER_WENT_BACK) i -= 2;
                    break;
            }
        }
        
        player = pResult.getPlayer();
        
        if(mmResult.getStateUsed() == MainMenuStates.NEW_GAME)
        {
            WorldResult worldResult = worldC.start(mmResult.getStateUsed());
            if(worldResult.getError() != WorldErrors.SUCCESS) return 1;
            player.setInventory(null);
            player.setEquipped(null);
            persistenceC.fullSave(player, player.getInventory(), player.getEquipped(), worldC.getWorld());
        }
        else if(mmResult.getStateUsed() == MainMenuStates.LOAD_GAME)
        {
            WorldResult worldResult = worldC.start(MainMenuStates.LOAD_GAME,pResult.getWorld());
            if(worldResult.getError() != WorldErrors.SUCCESS) return 2;
            player.setInventory(pResult.getInventory());
            player.setEquipped(pResult.getEquipped());
        }
        
        return loop();
    }
    
    public int loop()
    {
        while(isRunning && isAlive)
        {
            WorldResult worldR= worldC.loop();
            if(worldR.getError() != WorldErrors.SUCCESS) return 3;
            RoomResult roomR = roomC.loop(worldR.getRoom());
            if(roomR.getError()  != WorldErrors.SUCCESS) return 4;
            
            CombatResult combatR;
            switch(roomR.getRoomType())
            {
                case COMBAT, MINIBOSS, BOSS -> combatR = combatC.loop(player, (Enemy)roomR.getPersistable());
                case EMPTY -> combatR = new CombatResult(CombatErrors.SUCCESS, CombatOutcome.FLEE);
                default -> combatR = null;
            }

            RewardResult rewardR = null;
            if(combatR == null || combatR.getError() != CombatErrors.SUCCESS) return 5;
            if(combatR.getCombatOutcome() == CombatOutcome.LOSE) isAlive = false;
            else
            {
                switch(roomR.getRoomType())
                {
                    case COMBAT, MINIBOSS, BOSS -> rewardR = rewardC.loop(player, (Enemy) roomR.getPersistable());
                }
                roomC.loop();
            }
            if(rewardR == null || rewardR.getError() != RewardErrors.SUCCESS) return 6;
            PersistenceResult persistenceR = persistenceC.loop(player, worldR.getRoom());
            if(persistenceR.getError() != PersistenceErrors.SUCCESS) return 6;
        }
        
        return 0;
    }
}
