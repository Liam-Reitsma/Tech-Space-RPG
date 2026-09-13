package game;

import static game.ErrorSeverity.SEVERE;
import static game.entities.player.PlayerErrors.*;

import game.combat.*;
import game.entities.*;
import game.entities.player.Player;
import game.entities.player.PlayerController;
import game.entities.player.PlayerResult;
import game.mainmenu.*;
import game.persistence.*;
import game.reward.*;
import game.world.*;

public class GameController
{
    private boolean isAlive;
    private boolean isRunning;
    private boolean fled;
    
    private Player player;
    
    private final CombatController combatC;
    private final MainMenuController mainMenuC;
    private final PersistenceController persistenceC;
    private final PlayerController playerC;
    private final RewardController rewardC;
    private final RoomController roomC;
    private final WorldController worldC;
    
    public GameController(CombatController cc, MainMenuController mmc, PersistenceController pec, PlayerController plc, RewardController rec, RoomController roc, WorldController wc)
    {
        isAlive = true;
        isRunning = true;
        fled = false;
        
        combatC = cc;
        mainMenuC = mmc;
        persistenceC = pec;
        playerC = plc;
        rewardC = rec;
        roomC = roc;
        worldC = wc;
    }
    
    public int start()
    {
        MainMenuResult mmResult = new MainMenuResult(null, null);
        PersistenceResult pResult = new PersistenceResult(null);
        do
        {
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
            playerC.start(player);
            
            if(mmResult.getStateUsed() == MainMenuStates.NEW_GAME)
            {
                WorldResult worldResult = worldC.start(mmResult.getStateUsed());
                if(worldResult.getError() != WorldErrors.SUCCESS) return 1;
                player.setInventory(null);
                player.setEquippedArmourPieces(null);
                persistenceC.fullSave(player, player.getEquippedWeapon(), player.getInventory(), player.getEquippedArmour(), worldC.getWorld());
            }
            else if(mmResult.getStateUsed() == MainMenuStates.LOAD_GAME)
            {
                WorldResult worldResult = worldC.start(MainMenuStates.LOAD_GAME,pResult.getWorld());
                if(worldResult.getError() != WorldErrors.SUCCESS) continue;
                player.setWeapon(pResult.getWeapon());
                player.setInventory(pResult.getInventory());
                player.setEquippedArmourPieces(pResult.getArmour());
            }
            break;
        } while(true);
        
        return loop();
    }
    
    public int loop()
    {
        while(isRunning && isAlive)
        {
            do
            {
                PlayerResult<?> playerR = playerC.loop();
                if(playerR.getError() == SAVE_GAME) persistenceC.fullSave(player, player.getEquippedWeapon(), player.getInventory(), player.getEquippedArmour(), worldC.getWorld());
                else if(playerR.getError() == EXIT_GAME)
                {
                    persistenceC.fullSave(player, player.getEquippedWeapon(), player.getInventory(), player.getEquippedArmour(), worldC.getWorld());
                    isRunning = false;
                }
                else if(playerR.getError() != SKIP) continue;
                break;
            } while(true);
            
            if(!isRunning) continue;
            
            WorldResult worldR= worldC.loop(roomC.getRoom(fled));
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
            
            if(combatR != null) fled = combatR.getCombatOutcome() == CombatOutcome.FLEE;
            
            RewardResult rewardR = null;
            if(combatR == null || combatR.getError() != CombatErrors.SUCCESS) return 5;
            if(combatR.getCombatOutcome() == CombatOutcome.LOSE) isAlive = false;
            else if(combatR.getCombatOutcome() != CombatOutcome.FLEE)
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
                    playerC.loop();
                } while(true);
                
                roomC.loop();
            }
            else rewardR = new RewardResult(RewardErrors.SUCCESS);
            if(rewardR == null || rewardR.getError() != RewardErrors.SUCCESS) return 6;
            PersistenceResult persistenceR = persistenceC.loop(player, worldR.getRoom());
            if(persistenceR.getError() != PersistenceErrors.SUCCESS) return 6;
        }
        
        return 0;
    }
}