package game.persistence;

import static game.persistence.PersistenceErrors.*;

import game.*;
import game.entities.ClassType;
import game.entities.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.mainmenu.MainMenuStates;
import game.world.Point;
import game.world.RuntimeRoom;

import java.util.*;

public class PersistenceController implements Startable<MainMenuStates, Void, PersistenceResult>, Loopable<PersistenceResult>
{
    private final SaveFile saveF;
    private final ConsoleHandler consoleH;
    private final LoadFactory loadF;
    
    public PersistenceController(ConsoleHandler consoleHandler, SaveFile saveFile)
    {
        saveF = saveFile;
        consoleH = consoleHandler;
        loadF = new LoadFactory();
    }
    
    @Override
    public PersistenceResult start(MainMenuStates state, Void input2)
    {
        if(state == MainMenuStates.NEW_GAME) return createNewGame();
        else return loadSaveGame();
    }
    
    public PersistenceResult createNewGame()
    {
        String fileName = "";
        String charName = "";
        ClassType classType = ClassType.UNSELECTED;
        Player player;
        
        do
        {
            for(int i = 0; i < 3; i++)
            {
                switch(i)
                {
                    case 0:
                        fileName = askFileName(consoleH);
                        if(fileName == null) return new PersistenceResult(PersistenceErrors.USER_WENT_BACK, null, null, null, null);
                        break;
                    case 1:
                        Optional<String> userInputName = consoleH.confirmInput("What would you like to name your " + "character?").getInput();
                        if(userInputName.isPresent()) charName = userInputName.get();
                        else i -= 2;
                        break;
                    case 2:
                        Optional<ClassType> userInputClass = consoleH.confirmInput("What class do you want?", "Warrior, Rogue, or Mage", ClassType.class).getInput();
                        if(userInputClass.isPresent()) classType = userInputClass.get();
                        else i -= 2;
                        break;
                }
            }
            
            player = new Player(charName, classType);
            PersistenceErrors error = saveF.createNewSave(fileName);
            if(error != SUCCESS)  consoleH.printLine(error.toString());
            else  break;
        } while(true);
        
        saveF.writePlayer(player);
        return new PersistenceResult(PersistenceErrors.SUCCESS, player, null, null, null);
    }
    
    private String askFileName(ConsoleHandler consoleH)
    {
        String result;
        
        do
        {
            Optional<String> userInput = consoleH.confirmInput("What would you like to call the save file?").getInput();
            if(userInput.isEmpty()) return null;
            if(saveF.canCreateNewSave(userInput.get()))
            {
                result = userInput.get();
                break;
            }
        } while(true);
        
        return result;
    }
    
    public PersistenceResult loadSaveGame()
    {
        PersistenceErrors result = setFileToLoad(consoleH);
        
        if(result != SUCCESS) return new PersistenceResult(PersistenceErrors.USER_WENT_BACK, null, null, null, null);
        
        LoadResult playerResult = saveF.readPlayer();
        Player player = (Player)loadF.create(playerResult.getClassName(0), playerResult.getClassData(0));
        
        LoadResult inventoryResult = saveF.readList("inventory");
        List<Item> inventory = new ArrayList<>();
        for(int i = 0; i < inventoryResult.size(); i++)
        {
            inventory.add((Item)loadF.create(inventoryResult.getClassName(i), inventoryResult.getClassData(i)));
        }
        
        LoadResult equippedResult = saveF.readList("equipped");
        List<EquippableItem> equipped = new ArrayList<>();
        for(int i = 0; i < equippedResult.size(); i++)
        {
            equipped.add((EquippableItem)loadF.create(equippedResult.getClassName(i), equippedResult.getClassData(i)));
        }
        
        LoadResult mapResult = saveF.readList("world");
        Map<Point, RuntimeRoom> world = new HashMap<>();
        for(int i = 0; i < mapResult.size(); i++)
        {
            Point point = (Point)loadF.create(mapResult.getClassName(i), mapResult.getClassData(i));
            RuntimeRoom room = (RuntimeRoom)loadF.create(mapResult.getClassName(i), mapResult.getClassData(i));
            world.put(point, room);
        }
        
        return new PersistenceResult(PersistenceErrors.SUCCESS, player, inventory, equipped, world);
    }
    
    private PersistenceErrors setFileToLoad(ConsoleHandler consoleH)
    {
        Optional<Integer> userInput;
        PersistenceErrors error;
        String question = "Which save would you like to use?";
        String options = saveF.toString();
        
        do
        {
            userInput = consoleH.confirmInput(question, options, "\n").getInput();
            if(userInput.isEmpty()) return null;
            error = saveF.selectSave(userInput.get());
            
            if(error == SUCCESS) break;
            consoleH.printLine(error.toString());
        } while(true);
        
        return error;
    }
    
    @Override
    public PersistenceResult loop()
    {
        return null;
    }
    
    public PersistenceResult fullSave(Player player, List<Item> inventory, List<EquippableItem> equipped, Map<Point, RuntimeRoom> map)
    {
        PersistenceErrors error;
        error = saveF.writePlayer(player);
        if(error != SUCCESS) return new PersistenceResult(error, null, null, null, null);
        
        error = saveF.writeItemList(inventory, "inventory");
        if(error != SUCCESS) return new PersistenceResult(error, null, null, null, null);
        
        error = saveF.writeItemList(equipped, "equipped");
        if(error != SUCCESS) return new PersistenceResult(error, null, null, null, null);
        
        
        error = saveF.writeWorld(new ArrayList<>(map.values()), "world");
        if(error != SUCCESS) return new PersistenceResult(error, null, null, null, null);
        
        return new PersistenceResult(SUCCESS, null, null, null, null);
    }
    
    public PersistenceResult saveItem(String listName, Item item)
    {
        LoadResult result = saveF.readAndInsert(listName, item);
        return result.getError() == SUCCESS ?
                new PersistenceResult(saveF.writeAll(result.getResult()), null, null, null, null) :
                new PersistenceResult(result.getError(), null, null, null, null);
    }
    
    public PersistenceResult removeItem(String listName, Item item)
    {
        LoadResult result = saveF.readAndRemove(listName, item);
        return result.getError() == SUCCESS ?
                new PersistenceResult(saveF.writeAll(result.getResult()), null, null, null, null) :
                new PersistenceResult(result.getError(), null, null, null, null);
    }
    
    public PersistenceResult updatePlayerStatus(Player player)
    {
        return new PersistenceResult(saveF.writeDeltaPlayer(player), null, null, null, null);
    }
    
    public PersistenceResult updateRoomStatus(RuntimeRoom room)
    {
        return new PersistenceResult(saveF.writeDeltaRoom(room), null, null, null, null);
    }
    
    public PersistenceResult start(MainMenuStates state)
    {
        return start(state, null);
    }
}