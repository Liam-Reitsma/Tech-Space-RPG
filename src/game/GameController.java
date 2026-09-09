package game;

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
    
    private final MainMenuController mainMenuC;
    private final PersistenceController persistenceC;
    private final WorldController worldC;
    
    public GameController(MainMenuController mmc, PersistenceController pc, WorldController wc)
    {
        mainMenuC = mmc;
        persistenceC = pc;
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
                     mmResult = mainMenuC.start(null);
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
            //TODO WorldResult worldResult = worldC.start(mmResult.getStateUsed());
            //if(worldResult.getError() != WorldErrors.SUCCESS) return 1;
            //TODO persistenceC.save(worldC.getMap());
            //TODO Get Inventory & Get Equipped
        }
        else if(mmResult.getStateUsed() == MainMenuStates.LOAD_GAME)
        {
            WorldResult worldResult = worldC.loadMapFromSave(pResult.getWorld());
            if(worldResult.getError() != WorldErrors.SUCCESS) return 2;
            //TODO Set Inventory & Set Equipped
        }
        
        return loop();
    }
    
    public int loop()
    {
        return 0;
    }
}
