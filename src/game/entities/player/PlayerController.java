package game.entities.player;

import static game.entities.player.PlayerErrors.*;

import game.ConsoleHandler;
import game.Loopable;
import game.items.Item;
import game.items.weapon.Weapon;

import java.util.Arrays;
import java.util.Optional;


public class PlayerController implements Loopable<Player, Void, PlayerResult<?>>
{
    private final ConsoleHandler consoleH;

    public PlayerController(ConsoleHandler cH)
    {
        consoleH = cH;
    }

    @Override
    public PlayerResult<?> loop(Player player, Void input2)
    {
        Optional<PlayerMenuOption> response = consoleH.confirmInputInteger("What would you like to do?", PlayerMenuOption.optionsToString(), PlayerMenuOption.class).getInput();
        if(response.isEmpty()) return new PlayerResult(USER_WENT_BACK);
        PlayerMenuOption option = response.get();
        switch(option)
        {
            case VIEW_INVENTORY ->
            {
                for(Item item : player.getInventory())
                {
                    consoleH.printLine(item.toString());
                }
            }
            case EQUIP_WEAPON ->
            {
                PlayerResult<Weapon> result = player.hasWeaponsInInventory();
                if(result.getError() != SUCCESS) return new PlayerResult<>(result.getError());
                
                StringBuilder weaponOptions = new StringBuilder();
                String weaponsString = Arrays.toString(result.getResultArray());
                String weaponsSplit = weaponsString.substring(1, weaponsString.length() - 1);
                int i = 1;
                
                for(String weapon : weaponsSplit.split(", "))
                {
                    weaponOptions.append(i++).append(". ").append(weapon).append(", ");
                }
                
                Optional<Integer> choice = consoleH.confirmInput("Which weapon would you like to select?", weaponOptions.toString(), ", ").getInput();
                if(choice.isEmpty()) return new PlayerResult<>(USER_WENT_BACK);
                
                return new PlayerResult<>(player.equipWeapon(result.getResultArray()[choice.get() - 1]));
            }
            case UNEQUIP_WEAPON ->
            {
                if(player.getEquippedWeapon() == null)
                {
                    consoleH.printLine("Sorry, you have no weapon to unequip");
                    return new PlayerResult(SUCCESS);
                }
                else
                {
                    player.unequipWeapon();
                    consoleH.printLine("Weapon successfully unequipped");
                    return new PlayerResult(SUCCESS);
                }
            }
            case EQUIP_ARMOR ->
            {
                consoleH.printLine("Select Armor");
                //prints inventory which user picks from
                //equip the armour piece
            }
            case UNEQUIP_ARMOR ->
            {
                consoleH.printLine("Armor piece unequipped");
                //unequip the chosen piece
            }
        }
        return new PlayerResult(PlayerErrors.SUCCESS);
    }
}