package game;

import static game.ErrorSeverity.SEVERE;

import game.combat.*;
import game.entities.*;
import game.entities.player.Player;
import game.mainmenu.*;
import game.persistence.*;
import game.reward.*;
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
        PersistenceResult pResult = new PersistenceResult(null);
        
        for(int i = 0; i < 2; i++)
        {
            switch(i)
            {
                case 0:
                     mmResult = mainMenuC.start();
                    if(mmResult.getStateUsed() == MainMenuStates.EXIT_GAME) return 0;
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
            player.setEquippedArmour(null);
            persistenceC.fullSave(player, player.getEquippedWeapon(), player.getInventory(), player.getEquippedArmour(), worldC.getWorld());
        }
        else if(mmResult.getStateUsed() == MainMenuStates.LOAD_GAME)
        {
            WorldResult worldResult = worldC.start(MainMenuStates.LOAD_GAME,pResult.getWorld());
            if(worldResult.getError() != WorldErrors.SUCCESS) return 2;
            player.setWeapon(pResult.getWeapon());
            player.setInventory(pResult.getInventory());
            player.setEquippedArmour(pResult.getArmour());
        }
        
        return loop();
    }
    
    public int loop()
    {
        while(isRunning && isAlive)
        {
            WorldResult worldR= worldC.loop();
            if(worldR.getError() != WorldErrors.SUCCESS) return 3;
            RoomResult roomR = roomC.loop(worldR.getRoom(), player);
            if(roomR.getError()  != WorldErrors.SUCCESS) return 4;
            
            CombatResult combatR;
            switch(roomR.getRoomType())
            {
                case COMBAT, MINIBOSS, BOSS -> combatR = combatC.loop(player, (Enemy)roomR.getPersistable());
                case CHEST, EMPTY -> combatR = new CombatResult(CombatErrors.SUCCESS, CombatOutcome.WIN);
                default -> combatR = null;
            }

            RewardResult rewardR = null;
            if(combatR == null || combatR.getError() != CombatErrors.SUCCESS) return 5;
            if(combatR.getCombatOutcome() == CombatOutcome.LOSE) isAlive = false;
            else
            {
                do
                {
                    switch(roomR.getRoomType())
                    {
                        case COMBAT, MINIBOSS, BOSS, CHEST -> rewardR = rewardC.loop(player, roomR.getPersistable());
                        case EMPTY -> rewardR = new RewardResult(RewardErrors.SUCCESS);
                    }
                    
                    if(rewardR == null || rewardR.getError().getSeverity() == SEVERE) return 5;
                    if(rewardR.getError() == RewardErrors.ITEM_DISCARDED || rewardR.getError() == RewardErrors.SUCCESS) break;
                    //TODO Add PlayerController for Inventory Management, Saving, Exiting.
                } while(true);
                
                roomC.loop();
            }
            if(rewardR == null || rewardR.getError() != RewardErrors.SUCCESS) return 6;
            PersistenceResult persistenceR = persistenceC.loop(player, worldR.getRoom());
            if(persistenceR.getError() != PersistenceErrors.SUCCESS) return 6;
        }
        
        return 0;
    }
}
