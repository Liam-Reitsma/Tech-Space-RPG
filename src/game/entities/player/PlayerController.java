package game.entities.player;

import static game.entities.player.PlayerErrors.*;

import game.ConsoleHandler;
import game.Loopable;
import game.Startable;
import game.items.Item;
import game.items.armour.Armour;
import game.items.weapon.Weapon;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class PlayerController implements Startable<Player, Void, PlayerResult<?>>, Loopable<Void, Void, PlayerResult<?>>
{
    private final ConsoleHandler consoleH;
    private Player player;
    
    public PlayerController(ConsoleHandler cH)
    {
        consoleH = cH;
    }
    
    @Override
    public PlayerResult<?> start(Player startPlayer, Void input1)
    {
        player = startPlayer;
        return new PlayerResult<>(SUCCESS);
    }

    @Override
    public PlayerResult<?> loop(Void input1, Void input2)
    {
        Optional<PlayerMenuOption> response = consoleH.confirmInputInteger("What would you like to do?", PlayerMenuOption.optionsToString(), PlayerMenuOption.class).getInput();
        if(response.isEmpty()) return new PlayerResult<>(USER_WENT_BACK);
        PlayerMenuOption option = response.get();
        return switch(option)
        {
            case SKIP_MENU -> new PlayerResult<Void>(SKIP);
            case VIEW_INVENTORY ->
            {
                for(Item item : player.getInventory()) consoleH.printLine(item.toString());
                yield new PlayerResult<Void>(RETRY);
            }
            case EQUIP_WEAPON ->
            {
                PlayerResult<Weapon> result = player.getWeaponsInInventory();
                if(result.getError() != SUCCESS) yield new PlayerResult<>(result.getError());
                
                StringBuilder weaponOptions = new StringBuilder();
                String weaponsString = Arrays.toString(result.getResultArray());
                String weaponsSplit = weaponsString.substring(1, weaponsString.length() - 1);
                int i = 1;
                
                for(String weapon : weaponsSplit.split(", "))
                {
                    weaponOptions.append(i++).append(". ").append(weapon).append(", ");
                }
                
                Optional<Integer> choice = consoleH.confirmInput("Which weapon would you like to equip?", weaponOptions.toString(), ", ").getInput();
                if(choice.isEmpty()) yield new PlayerResult<>(USER_WENT_BACK);
                
                yield new PlayerResult<>(player.equipWeapon(result.getResultArray()[choice.get() - 1]));
            }
            case UNEQUIP_WEAPON ->
            {
                Weapon weapon = player.getEquippedWeapon();
                PlayerErrors error = player.unequipWeapon();
                if(error != SUCCESS)
                {
                    consoleH.printLine(error.getMessage());
                    yield new PlayerResult<>(error);
                }
                else
                {
                    consoleH.printLine(weapon.toString() + " successfully unequipped");
                    yield new PlayerResult<>(SUCCESS);
                }
            }
            case EQUIP_ARMOR ->
            {
                PlayerResult<Armour> result = player.getArmourInInventory();
                if(result.getError() != SUCCESS) yield new PlayerResult<>(result.getError());
                
                StringBuilder armourOptions = new StringBuilder();
                String armourString = Arrays.toString(result.getResultArray());
                String armourSplit = armourString.substring(1, armourString.length() - 1);
                int i = 1;
                
                for(String weapon : armourSplit.split(", "))
                {
                    armourOptions.append(i++).append(". ").append(weapon).append(", ");
                }
                
                Optional<Integer> choice = consoleH.confirmInput("Which armour would you like to equip?", armourOptions.toString(), ", ").getInput();
                if(choice.isEmpty()) yield new PlayerResult<>(USER_WENT_BACK);
                
                yield new PlayerResult<>(player.equipArmour(result.getResultArray()[choice.get() - 1]));
            }
            case UNEQUIP_ARMOR ->
            {
                List<Armour> armourList = player.getEquippedArmour();
                if(armourList.isEmpty()) yield new PlayerResult<>(NO_ARMOUR_EQUIPPED);
                
                StringBuilder armourOptions = new StringBuilder();
                String armourString = armourList.toString();
                String armourSplit = armourString.substring(1, armourString.length() - 1);
                int i = 1;
                
                for(String armour : armourSplit.split(", "))
                {
                    armourOptions.append(i++).append(". ").append(armour).append(", ");
                }
                
                Optional<Integer> choice = consoleH.confirmInput("Which armour would you like to unequip?", armourOptions.toString(), ", ").getInput();
                if(choice.isEmpty()) yield new PlayerResult<>(USER_WENT_BACK);
                
                Armour armour = armourList.get(choice.get() - 1);
                
                PlayerErrors error = player.unequipArmour(armour);
                if(error != SUCCESS)
                {
                    consoleH.printLine(error.getMessage());
                    yield new PlayerResult<>(error);
                }
                else
                {
                    consoleH.printLine(armour.toString() + " successfully unequipped");
                    yield new PlayerResult<>(SUCCESS);
                }
            }
            case SAVE_GAME -> new PlayerResult<>(SAVE_GAME);
            case EXIT_GAME -> new PlayerResult<>(EXIT_GAME);
        };
    }
}