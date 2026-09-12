package game.entities.player;

import game.ConsoleHandler;
import game.Loopable;
import game.items.Item;

import java.util.Optional;


public class PlayerController implements Loopable<Player, Void, PlayerResult>
{
    private final ConsoleHandler consoleH;

    public PlayerController(ConsoleHandler cH)
    {
        consoleH = cH;
    }

    @Override
    public PlayerResult loop(Player player, Void unused)
    {
        Optional<PlayerMenuOption> response = consoleH.confirmInputInteger("What would you like to do?", PlayerMenuOption.optionsToString(), PlayerMenuOption.class).getInput();
        if(response.isEmpty()) return new PlayerResult(PlayerError.GO_BACK);
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
                consoleH.printLine("Select Weapon");
                //prints inventory which user picks from
                //equip the weapon
            }
            case UNEQUIP_WEAPON ->
            {
                consoleH.printLine("Weapon unequipped");
                //unequip the weapon
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
        return new PlayerResult(PlayerError.SUCCESS);
    }
}
