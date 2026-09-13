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

public class CombatController implements Loopable<Player, Enemy, CombatResult>
{
    private final ConsoleHandler consoleH;

    public CombatController(ConsoleHandler cH)
    {
        consoleH = cH;
    }

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
                if(outcome != NOTHING)  return new CombatResult(SUCCESS, outcome);
                userTurn = false;
            }
            else
            {
                outcome = enemyTurn(player, enemy);
                if(outcome != NOTHING) return new CombatResult(SUCCESS, outcome);
                userTurn = true;
            }
        }
    }

    private CombatOutcome playerTurn(Player player, Enemy enemy)
    {
        Optional<CombatAction> action;
        do
        {
            do
            {
                action = consoleH.confirmInput("What would you like to do?", CombatAction.optionsToString(), CombatAction.class).getInput();
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

    private CombatOutcome enemyTurn(Player player, Enemy enemy)
    {
        StringBuilder message = new StringBuilder();
        CombatOutcome outcome = NOTHING;
        float damage = enemy.attack();
        player.takeDamage(damage);
        message.append(enemy.getName()).append(" attacked you!\n");
        message.append(enemy.getName()).append(" dealt ").append(damage).append(" to you!\n");
        if(player.getHealth() <= 0) outcome = LOSE;
        consoleH.printLine(message.toString());
        
        return outcome;
    }
    
    private CombatErrors getPotionToUse(Player player)
    {
        Potion[] potions = player.getPotionsInInventory().getResultArray();
        if(potions.length == 0) return NO_POTION_TO_USE;
        StringBuilder potionOptions = new StringBuilder();
        String potionString = Arrays.toString(potions);
        String potionsSplit = potionString.substring(1, potionString.length() - 1);
        int i = 1;
        
        for(String weapon : potionsSplit.split(", "))
        {
            potionOptions.append(i++).append(". ").append(weapon).append(", ");
        }
        
        Optional<Integer> choice = consoleH.confirmInput("Which potion would you like to use?", potionOptions.toString(), ", ").getInput();
        if(choice.isEmpty()) return USER_WENT_BACK;
        
        player.setSelectedPotion(potions[choice.get() - 1].getPotionType());
        return SUCCESS;
    }
    
    private void printCombatInfo(Player player, Enemy enemy)
    {
        StringBuilder info = new StringBuilder();
        
        info.append(player.getName()).append(" has ").append(player.getHealth()).append(" hp remaining!\n");
        info.append(enemy.getName()).append(" has ").append(enemy.getHealth()).append(" hp remaining!\n");
        
        consoleH.printLine(info.toString());
    }
}