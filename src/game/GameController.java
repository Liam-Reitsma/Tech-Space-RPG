package game;

import game.entities.Persistable;
import game.mainmenu.MainMenuController;
import game.mainmenu.MainMenuResult;
import game.mainmenu.MainMenuStates;

import java.util.List;

public class GameController
{
    private List<Persistable> entities;
    private List<Persistable> equippedItems;
    private List<Persistable> inventoryItems;
    
    private final MainMenuController mmController;
    
    public GameController(MainMenuController mm)
    {
        mmController = mm;
    }
    
    public int start()
    {
        MainMenuResult mmResult = mmController.start();
        if(mmResult.getStateUsed() == MainMenuStates.EXIT) return 0;
        entities = mmResult.getList().getFirst();
        equippedItems = mmResult.getList().get(1);
        inventoryItems = mmResult.getList().get(2);
        //TODO Add World Saving & Getting World Save
        
        return loop();
    }
    
    public int loop()
    {
        return 0;
    }
}
