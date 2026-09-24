package game.persistence;

import static game.ErrorSeverity.*;
import static game.persistence.PersistenceErrors.*;

import game.*;
import game.entities.player.Player;
import game.items.*;
import game.items.armour.Armour;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import game.world.Point;
import game.world.RuntimeRoom;

import java.io.File;
import java.util.*;

public class PersistenceController
{
    private final SaveFile saveF;
    private final LoadFactory loadF;
    
    public PersistenceController()
    {
        saveF = new SaveFile();
        loadF = new LoadFactory();
    }
    
    public PersistenceResult createNewGame()
    {
        Player player;
        do
        {
            //Create Game
            
            player = new Player("charName", null);
            PersistenceErrors error = saveF.createNewSave("fileName");
            if(error.getSeverity() == SEVERE) return new PersistenceResult(error);
            else if(error == SUCCESS) break;
        } while(true);
        
        return new PersistenceResult(SUCCESS, player, player.getEquippedWeapon(), null, null, null);
    }
    
    public File[] getSaveOptions()
    {
        return saveF.getSaveFileNames().getResult();
    }
    
    public PersistenceResult loadSaveGame(SaveFile saveFile)
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
        
        Weapon weapon = new Weapon(WeaponType.RUSTED_KNIFE, 1);
        LoadResult weaponResult = saveF.readObject("weapon data");
        if(weaponResult.getError() != SUCCESS) return new PersistenceResult(weaponResult.getError());
        Optional<?> weaponCreateResult = loadF.create(weaponResult.getClassName(0), weaponResult.getClassData(0));
        if(weaponCreateResult.isEmpty())
        {
//            do
//            {
//                Optional<String> response = consoleH.confirmInput("We could not load your weapon. Would you like to load with the Rusted Knife?").getInput();
//                if(response.isEmpty()) consoleH.printLine("You can not go back here. Please select yes to continue or no to cancel");
//                else if(response.get().toLowerCase().startsWith("y")) { weapon = new Weapon(WeaponType.RUSTED_KNIFE, player.getLevel()); break; }
//                else if(response.get().toLowerCase().startsWith("n")) return new PersistenceResult(LOADING_WEAPON_FAILED);
//                else consoleH.printLine("Please select yes to continue or no to cancel");
//            } while(true);
        }
        else if(weaponCreateResult.get() instanceof Weapon) weapon = (Weapon)weaponCreateResult.get();
        else return new PersistenceResult(SAVE_FILE_CORRUPTED);
        
        LoadResult inventoryResult = saveF.readList("inventory");
        if(inventoryResult.getError() != SUCCESS) return new PersistenceResult(inventoryResult.getError());
        List<Item> inventoryList = new ArrayList<>();
//        for(int i = 0; i < inventoryResult.size(); i++)
//        {
//            if(inventoryCreateResult.isEmpty())
//            {
//                consoleH.printLine(LOADING_ITEM_FAILED.getMessage());
//                do
//                {
//                    Optional<String> response = consoleH.confirmInput("Would you like to skip this item or stop the game?").getInput();
//                    if(response.isEmpty()) consoleH.printLine("You can not go back here. Please select yes to continue or no to cancel");
//                    else if(response.get().toLowerCase().startsWith("y")) break;
//                    else if(response.get().toLowerCase().startsWith("n")) return new PersistenceResult(LOADING_ITEM_FAILED);
//                    else consoleH.printLine("Please select yes to continue or no to cancel");
//                } while(true);
//            }
//            else if(inventoryCreateResult.get() instanceof Item) inventoryList.add((Item)inventoryCreateResult.get());
//            else return new PersistenceResult(SAVE_FILE_CORRUPTED);
//        }
        
        LoadResult armour = saveF.readList("armour");
        if(armour.getError() != SUCCESS) return new PersistenceResult(armour.getError());
        List<Armour> armourList = new ArrayList<>();
//        for(int i = 0; i < armour.size(); i++)
//        {
//            Optional<?> armourCreateResult = loadF.create(armour.getClassName(i), armour.getClassData(i));
//            if(armourCreateResult.isEmpty())
//            {
//                consoleH.printLine(LOADING_ITEM_FAILED.getMessage());
//                do
//                {
//                    Optional<String> response = consoleH.confirmInput("Would you like to skip this item or stop the game?").getInput();
//                    if(response.isEmpty()) consoleH.printLine("You can not go back here. Please select yes to continue or no to cancel");
//                    else if(response.get().toLowerCase().startsWith("y")) break;
//                    else if(response.get().toLowerCase().startsWith("n")) return new PersistenceResult(LOADING_ITEM_FAILED);
//                    else consoleH.printLine("Please select yes to continue or no to cancel");
//                } while(true);
//            }
//            else if(armourCreateResult.get() instanceof Armour) armourList.add((Armour)armourCreateResult.get());
//            else return new PersistenceResult(SAVE_FILE_CORRUPTED);
//        }
        
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
        PersistenceErrors error = saveF.getSaveFileNames().getError();
        if(error != SUCCESS) return error;
        
        do
        {
            if(error == USER_WENT_BACK) return USER_WENT_BACK;
//            error = saveF.selectSave("userInputNumber"));
            
            if(error == SUCCESS) break;
        } while(true);
        
        return error;
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
}