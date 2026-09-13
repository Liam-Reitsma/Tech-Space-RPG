package game.combat;

import static game.combat.CombatAction.*;
import static game.combat.CombatErrors.USER_WENT_BACK;
import static game.combat.CombatErrors.SUCCESS;
import static game.combat.CombatErrors.NO_POTION_TO_USE;
import static game.combat.CombatOutcome.*;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.*;
import game.entities.player.Player;
import game.items.Potion;

import java.util.Arrays;
import java.util.Optional;

/** CombatController implements {@link Loopable} by taking the {@link Player}, {@link Enemy} and the {@link CombatResult}.
 * It is in charge of holding the main combat loop by checking whether it is the {@link Player}'s turn or the {@link Enemy}'s turn.
 * If it is the {@link Player}'s turn, then it checks and performs the {@link Player}'s choice of action, while checking for errors.
 * If it is the {@link Enemy}'s turn, then it calculates and applies the damage to the {@link Player}.
 * */
public class CombatController implements Loopable<Player, Enemy, CombatResult>
{
    private final ConsoleHandler consoleH;

    public CombatController(ConsoleHandler cH)
    {
        consoleH = cH;
    }

    /** The main loop which has a while(true) loop.
     * It checks for whose turn it is, and if it is the {@link Player}'s turn, it prints a message of the current combat info by using printCombatInfo() method, and gets the outcome of the {@link Player}'s turn. If the outcome is anything other than NOTHING, then it exits the while loop with SUCCESS and the outcome.
     * If it is the {@link Enemy}'s turn, it gets the outcome of the turn and if it is anything other than NOTHING, it returns SUCCESS and the outcome.
     * */
    @Override
    public CombatResult loop(Player player, Enemy enemy)
    {
        CombatOutcome outcome;
        boolean userTurn = true;
        
        while(true)
        {
            if(userTurn)
            {
                printCombatInfo(player, enemy);
                outcome = playerTurn(player, enemy);
                if(outcome != NOTHING) return new CombatResult(SUCCESS, outcome);
                userTurn = false;
            }
            else
            {
                outcome = enemyTurn(player, enemy);
                if(outcome != NOTHING)
                {
                    consoleH.printLine(outcome.getMessage());
                    return new CombatResult(SUCCESS, outcome);
                }
                userTurn = true;
            }
        }
    }

    /** Prompts the {@link Player} to choose an action to perform.
     * If the action is HEAL, then it uses getPotionToUse() to check for {@link Potion}s in inventory, and if there are no {@link Potion}s in the inventory or the {@link Player} chooses "~" when selecting {@link Potion}, it prompts the {@link Player} to choose an action again.
     * Otherwise, it performs the chosen action and returns the outcome of it.*/
    private CombatOutcome playerTurn(Player player, Enemy enemy)
    {
        Optional<CombatAction> action;
        do
        {
            do
            {
                action = consoleH.confirmInputInteger("What would you like to do?", CombatAction.optionsToString(), CombatAction.class).getInput();
                if(action.isEmpty())
                {
                    consoleH.printLine("You cannot return here. Please choose an option");
                    continue;
                }
                break;
            } while(true);
            
            if(action.get() == HEAL)
            {
                CombatErrors error = getPotionToUse(player);
                if(error == USER_WENT_BACK) continue;
                else if(error == NO_POTION_TO_USE)
                {
                    consoleH.printLine(error.getMessage());
                    continue;
                }
            }
            break;
        } while(true);
        
        ActionResult result = action.get().doAction(player, enemy);
        consoleH.printLine(result.getMessage());
        return result.getOutcome();
    }

    /** Calculates and applies the {@link Enemy}'s damage to the {@link Player} and if the {@link Player}'s health becomes 0, it sets the outcome to LOSE.
     * It then prints "(Enemy) attacked you" along with "(Enemy) dealt "x" to you!", and returns the outcome.*/
    private CombatOutcome enemyTurn(Player player, Enemy enemy)
    {
        StringBuilder message = new StringBuilder();
        CombatOutcome outcome = NOTHING;
        float damage = enemy.attack();
        player.takeDamage(damage, 0);
        message.append(enemy.getName()).append(" attacked you!\n");
        message.append(enemy.getName()).append(" dealt ").append(damage).append(" to you!\n");
        if(player.getHealth() <= 0) outcome = LOSE;
        consoleH.printLine(message.toString());
        
        return outcome;
    }

    /** Checks the {@link Player}'s inventory for {@link Potion}s and puts them in an array. If there are no {@link Potion}s then it returns NO_POTION_TO_USE. Otherwise, it prints a list of {@link Potion} for the {@link Player} to choose from.
     * If the {@link Player} chooses to go back by using "~" it returns USER_WENT_BACK. If not, it sets the {@link Player} selected {@link Potion} to the {@link Player}'s choice and returns SUCCESS.*/
    private CombatErrors getPotionToUse(Player player)
    {
        Potion[] potions = player.getPotionsInInventory().getResultArray();
        if(potions.length == 0) return NO_POTION_TO_USE;
        StringBuilder potionOptions = new StringBuilder();
        String potionString = Arrays.toString(potions);
        String potionsSplit = potionString.substring(1, potionString.length() - 1);
        int i = 1;
        
        for(String potion : potionsSplit.split(", "))
        {
            potionOptions.append(i++).append(". ").append(potion).append(", ");
        }

        potionOptions.delete(potionOptions.length() - ", ".length(), potionOptions.length());

        Optional<Integer> choice = consoleH.confirmInput("Which potion would you like to use?", potionOptions.toString(), ", ").getInput();
        if(choice.isEmpty()) return USER_WENT_BACK;
        
        player.setSelectedPotion(potions[choice.get() - 1]);
        return SUCCESS;
    }

    /** Prints a message that tells the {@link Player} how much {@link Player} health is remaining and how much {@link Enemy} health is remaining.*/
    private void printCombatInfo(Player player, Enemy enemy)
    {
        StringBuilder info = new StringBuilder();
        
        info.append(player.getName()).append(" has ").append((int)player.getHealth()).append("/").append((int)player.getMaximumHealth()).append("!\n");
        info.append(enemy.getName()).append(" has ").append((int)enemy.getHealth()).append(" hp remaining!\n");
        
        consoleH.printLine(info.toString());
    }
}