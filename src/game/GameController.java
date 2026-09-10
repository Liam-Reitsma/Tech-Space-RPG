package game;

import game.combat.CombatController;
import game.entities.Player;
import game.mainmenu.MainMenuController;
import game.mainmenu.MainMenuResult;
import game.mainmenu.MainMenuStates;
import game.persistence.PersistenceController;
import game.persistence.PersistenceErrors;
import game.persistence.PersistenceResult;
import game.world.*;

public class GameController
{
    private Player player;
    
    private final CombatController combatC;
    private final MainMenuController mainMenuC;
    private final PersistenceController persistenceC;
    private final RoomController roomC;
    private final WorldController worldC;
    
    public GameController(CombatController cc, MainMenuController mmc, PersistenceController pc, RoomController rc, WorldController wc)
    {
        combatC = cc;
        mainMenuC = mmc;
        persistenceC = pc;
        roomC = rc;
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
        return 0;
    }
}
