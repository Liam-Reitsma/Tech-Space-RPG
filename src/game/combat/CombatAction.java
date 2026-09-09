package game.combat;

import game.items.*;
import game.entities.*;
import game.ConsoleHandler;
import java.util.*;

public enum CombatAction
{
    FIGHT("Fight")
        {
            @Override
            public ActionDecider execute(ConsoleHandler consoleH, Player player, Enemy opponent)
            {
                Optional<Weapon> equippedWeaponDamage = player.getEquippedWeapon();
                int damageDealt = player.getStats()[0];
                if (equippedWeaponDamage.isPresent())
                {
                    damageDealt += equippedWeaponDamage.get().use();
                }
                opponent.takeDamage(damageDealt);

                String message =  "Dealt: " +  damageDealt + "\n Opponent Health: " + opponent.getHealth();
                return new ActionDecider(message, Optional.empty());
            }
        }
    , RUN("Run")
        {
            @Override
            public ActionDecider execute(ConsoleHandler consoleH, Player player, Enemy opponent)
            {
                return new ActionDecider("You ran Away.", Optional.of(CombatErrors.FLEE));
            }
        }
    , USE_ITEM("Use Item")
        {
            @Override
            public ActionDecider execute(ConsoleHandler consoleH, Player player, Enemy opponent)
            {
                int response;
                List<Item> usableItems = new ArrayList<>();
                for (Item item : player.getInventory())
                {
                    if (item instanceof Potion)
                    {
                        usableItems.add(item);
                    }
                }
                do
                {
                    consoleH.printLine("What potion would you like to use? Press '~' to go back.");
                    for (int i = 1; i <= usableItems.size(); i++)
                    {
                        consoleH.printLine(i + ". " + usableItems.get(i-1).toString());
                    }

                    Optional<Integer> userInput = consoleH.nextInt().getInput();
                    if (userInput.isEmpty()) break;
                    response = userInput.get();
                    if (response > 0 && response <= usableItems.size())
                    {
                        Item chosenItem = usableItems.get(response - 1);
                        player.healing(((Potion) chosenItem).use());
                        player.removeFromInventory(chosenItem);
                        String message = chosenItem.toString() + " has been used.";
                        return new ActionDecider(message, Optional.empty());
                    }
                } while(true);
                String message =  "Going back.";
                return new  ActionDecider(message, Optional.empty());
            }
        };

    private final String option;
    public abstract ActionDecider execute(ConsoleHandler consoleH, Player player, Enemy opponent);


    CombatAction(String m)
    {
        option = m;
    }
    @Override
    public String toString()
    {
        return option;
    }
}
