package game.persistence;

import static game.ErrorSeverity.*;
import static game.persistence.PersistenceErrors.*;

import game.*;
import game.entities.player.ClassType;
import game.entities.player.Player;
import game.items.*;
import game.items.armour.Armour;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import game.mainmenu.MainMenuStates;
import game.world.Point;
import game.world.RuntimeRoom;

import java.util.*;

public class PersistenceController implements Startable<MainMenuStates, Void, PersistenceResult>, Loopable<Player, RuntimeRoom, PersistenceResult>
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
                        if(fileName == null) return new PersistenceResult(PersistenceErrors.USER_WENT_BACK);
                        break;
                    case 1:
                        Optional<String> userInputName = consoleH.confirmInput("What would you like to name your " + "character?").getInput();
                        if(userInputName.isPresent()) charName = userInputName.get();
                        else i -= 2;
                        break;
                    case 2:
                        Optional<ClassType> userInputClass = consoleH.confirmInputInteger("What class do you want?", ClassType.optionsToString(), ClassType.class).getInput();
                        if(userInputClass.isPresent()) classType = userInputClass.get();
                        else i -= 2;
                        break;
                }
            }
            
            player = new Player(charName, classType);
            PersistenceErrors error = saveF.createNewSave(fileName);
            if(error.getSeverity() == SEVERE) return new PersistenceResult(error);
            else if(error.getSeverity() == RECOVERABLE) consoleH.printLine(error.getMessage());
            else if(error == SUCCESS) break;
        } while(true);
        
        return new PersistenceResult(SUCCESS, player, player.getEquippedWeapon(), null, null, null);
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
            else consoleH.printLine(saveF.createNewSave(userInput.get()).getMessage());
        } while(true);
        
        return result;
    }
    
    public PersistenceResult loadSaveGame()
    {
        PersistenceErrors result = setFileToLoad();
        
        if(result != SUCCESS) return new PersistenceResult(result);
        
        Player player;
        LoadResult playerResult = saveF.readObject("player data");
        if(playerResult.getError() != SUCCESS) return new PersistenceResult(playerResult.getError());
        Optional<?> playerCreateResult = loadF.create(playerResult.getClassName(0), playerResult.getClassData(0));
        if(playerCreateResult.isEmpty()) return new PersistenceResult(LOADING_PLAYER_FAILED);
        else if(playerCreateResult.get() instanceof Player) player = (Player)playerCreateResult.get();
        else return new PersistenceResult(SAVE_FILE_CORRUPTED);
        
        Weapon weapon;
        LoadResult weaponResult = saveF.readObject("weapon data");
        if(weaponResult.getError() != SUCCESS) return new PersistenceResult(weaponResult.getError());
        Optional<?> weaponCreateResult = loadF.create(weaponResult.getClassName(0), weaponResult.getClassData(0));
        if(weaponCreateResult.isEmpty())
        {
            do
            {
                Optional<String> response = consoleH.confirmInput("We could not load your weapon. Would you like to load with the Rusted Knife?").getInput();
                if(response.isEmpty()) consoleH.printLine("You can not go back here. Please select yes to continue or no to cancel");
                else if(response.get().toLowerCase().startsWith("y")) { weapon = new Weapon(WeaponType.RUSTED_KNIFE, player.getLevel()); break; }
                else if(response.get().toLowerCase().startsWith("n")) return new PersistenceResult(LOADING_WEAPON_FAILED);
                else consoleH.printLine("Please select yes to continue or no to cancel");
            } while(true);
            
        }
        else if(weaponCreateResult.get() instanceof Weapon) weapon = (Weapon)weaponCreateResult.get();
        else return new PersistenceResult(SAVE_FILE_CORRUPTED);
        
        LoadResult inventoryResult = saveF.readList("inventory");
        if(inventoryResult.getError() != SUCCESS) return new PersistenceResult(inventoryResult.getError());
        List<Item> inventoryList = new ArrayList<>();
        for(int i = 0; i < inventoryResult.size(); i++)
        {
            Optional<?> inventoryCreateResult = loadF.create(inventoryResult.getClassName(i), inventoryResult.getClassData(i));
            if(inventoryCreateResult.isEmpty())
            {
                consoleH.printLine(LOADING_ITEM_FAILED.getMessage());
                do
                {
                    Optional<String> response = consoleH.confirmInput("Would you like to skip this item or stop the game?").getInput();
                    if(response.isEmpty()) consoleH.printLine("You can not go back here. Please select yes to continue or no to cancel");
                    else if(response.get().toLowerCase().startsWith("y")) break;
                    else if(response.get().toLowerCase().startsWith("n")) return new PersistenceResult(LOADING_ITEM_FAILED);
                    else consoleH.printLine("Please select yes to continue or no to cancel");
                } while(true);
            }
            else if(inventoryCreateResult.get() instanceof Item) inventoryList.add((Item)inventoryCreateResult.get());
            else return new PersistenceResult(SAVE_FILE_CORRUPTED);
        }
        
        LoadResult armour = saveF.readList("armour");
        if(armour.getError() != SUCCESS) return new PersistenceResult(armour.getError());
        List<Armour> armourList = new ArrayList<>();
        for(int i = 0; i < armour.size(); i++)
        {
            Optional<?> armourCreateResult = loadF.create(armour.getClassName(i), armour.getClassData(i));
            if(armourCreateResult.isEmpty())
            {
                consoleH.printLine(LOADING_ITEM_FAILED.getMessage());
                do
                {
                    Optional<String> response = consoleH.confirmInput("Would you like to skip this item or stop the game?").getInput();
                    if(response.isEmpty()) consoleH.printLine("You can not go back here. Please select yes to continue or no to cancel");
                    else if(response.get().toLowerCase().startsWith("y")) break;
                    else if(response.get().toLowerCase().startsWith("n")) return new PersistenceResult(LOADING_ITEM_FAILED);
                    else consoleH.printLine("Please select yes to continue or no to cancel");
                } while(true);
            }
            else if(armourCreateResult.get() instanceof Armour) armourList.add((Armour)armourCreateResult.get());
            else return new PersistenceResult(SAVE_FILE_CORRUPTED);
        }
        
        LoadResult mapResult = saveF.readList("world");
        if(mapResult.getError() != SUCCESS) return new PersistenceResult(mapResult.getError());
        Map<Point, RuntimeRoom> world = new HashMap<>();
        for(int i = 0; i < mapResult.size(); i++)
        {
            Optional<?> roomCreateResult = loadF.create(mapResult.getClassName(i), mapResult.getClassData(i));
            if(roomCreateResult.isEmpty()) return new PersistenceResult(LOADING_MAP_FAILED);
            else if(roomCreateResult.get() instanceof RuntimeRoom) world.put(((RuntimeRoom)roomCreateResult.get()).getPoint(), (RuntimeRoom)roomCreateResult.get());
            else return new PersistenceResult(SAVE_FILE_CORRUPTED);
        }
        
        return new PersistenceResult(SUCCESS, player, weapon, inventoryList, armourList, world);
    }
    
    private PersistenceErrors setFileToLoad()
    {
        Optional<Integer> userInput;
        PersistenceErrors error = saveF.getSaveFileNames().getError();
        if(error != SUCCESS) return error;
        
        do
        {
            userInput = consoleH.confirmInput("Which save would you like to use?", saveF.toString(), "\n").getInput();
            if(userInput.isEmpty()) return USER_WENT_BACK;
            error = saveF.selectSave(userInput.get());
            
            if(error == SUCCESS) break;
            consoleH.printLine(error.getMessage());
        } while(true);
        
        return error;
    }
    
    @Override
    public PersistenceResult loop(Player player, RuntimeRoom room)
    {
        for(int i = 0; i < 2; i++)
        {
            PersistenceResult result = i == 0 ? updatePlayerStatus(player) : updateRoomStatus(room);
            if(result.getError().getSeverity() == SEVERE) return new PersistenceResult(result.getError());
            else if(result.getError().getSeverity() == RECOVERABLE)
            {
                do
                {
                    consoleH.printLine(result.getError().getMessage());
                    Optional<String> response = consoleH.confirmInput("Would you like to keep playing?").getInput();
                    if(response.isEmpty()) consoleH.printLine("You can not go back here. Please select yes to continue or no to cancel");
                    else if(response.get().toLowerCase().startsWith("y")) break;
                    else if(response.get().toLowerCase().startsWith("n")) return new PersistenceResult(USER_EXITED_PROGRAM);
                    else consoleH.printLine("Please select yes to continue or no to cancel");
                } while(true);
            }
        }
        
        return new PersistenceResult(SUCCESS);
    }
    
    public PersistenceResult fullSave(Player player, Weapon weapon, List<Item> inventory, List<Armour> armour, Map<Point, RuntimeRoom> map)
    {
        PersistenceErrors error;
        error = saveF.clearFile();
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeObject(player);
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeObject(weapon);
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeItemList(inventory, "inventory");
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeItemList(armour, "armour");
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeWorld(new ArrayList<>(map.values()), "world");
        if(error != SUCCESS) return new PersistenceResult(error);
        
        return new PersistenceResult(SUCCESS);
    }
    
    public PersistenceResult saveItem(String listName, Item item)
    {
        LoadResult result = saveF.readAndInsert(listName, item);
        return result.getError() == SUCCESS ?
                new PersistenceResult(saveF.writeList(result.getResult())) :
                new PersistenceResult(result.getError());
    }
    
    public PersistenceResult removeItem(String listName, Item item)
    {
        LoadResult result = saveF.readAndRemove(listName, item);
        return result.getError() == SUCCESS ?
                new PersistenceResult(saveF.writeList(result.getResult())) :
                new PersistenceResult(result.getError());
    }
    
    public PersistenceResult updatePlayerStatus(Player player)
    {
        return new PersistenceResult(saveF.writeDeltaObject(player));
    }
    
    public PersistenceResult updateRoomStatus(RuntimeRoom room)
    {
        return new PersistenceResult(saveF.writeDeltaRoom(room));
    }
    
    public PersistenceResult start(MainMenuStates state)
    {
        return start(state, null);
    }
}