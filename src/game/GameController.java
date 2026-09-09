package game;

import game.entities.Persistable;
import game.mainmenu.MainMenuController;
import game.mainmenu.MainMenuResult;
import game.mainmenu.MainMenuStates;
import game.world.*;

import java.util.List;
import java.util.Map;

public class GameController
{
    private List<Persistable> entities;
    private List<Persistable> equippedItems;
    private List<Persistable> inventoryItems;
    private Map<Point, RuntimeRoom> worldMap;
    
    private final MainMenuController mmController;
    private final WorldController worldController;
    
    public GameController(MainMenuController mm, WorldController w)
    {
        mmController = mm;
        worldController = w;
    }
    
    public int start()
    {
        MainMenuResult mmResult = mmController.start();
        if(mmResult.getStateUsed() == MainMenuStates.EXIT) return 0;
        entities = mmResult.getList().getFirst();
        equippedItems = mmResult.getList().get(1);
        inventoryItems = mmResult.getList().get(2);
        
        if(mmResult.getStateUsed() == MainMenuStates.NEW_GAME)
        {
            WorldResult worldResult = worldController.start();
            if(worldResult.getError() != WorldErrors.SUCCESS) return 1;
        }
        else if(mmResult.getStateUsed() == MainMenuStates.LOAD_GAME)
        {
            WorldResult worldResult = worldController.loadMapFromSave(worldMap);
            if(worldResult.getError() != WorldErrors.SUCCESS) return 2;
        }
        
        return loop();
    }
    
    public int loop()
    {
        return 0;
    }
}
