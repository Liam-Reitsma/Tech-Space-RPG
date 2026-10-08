package game.reward;

import static game.ErrorSeverity.RECOVERABLE;
import static game.ErrorSeverity.SEVERE;
import static game.reward.RewardErrors.*;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.*;
import game.entities.player.Player;
import game.items.EquippableItem;
import game.items.Potion;
import game.items.PotionType;
import game.items.armour.Armour;
import game.items.weapon.Weapon;

import java.util.Optional;

public class RewardControllerOLD implements Loopable<Player, Persistable, RewardResult>
{
    private final ConsoleHandler consoleH;

    public RewardControllerOLD(ConsoleHandler cH)
    {
        consoleH = cH;
    }

    @Override
    public RewardResult loop(Player player, Persistable persistable)
    {
        if(persistable == null) return new RewardResult(SUCCESS);
        if(persistable instanceof Enemy enemy) return enemyReward(player, enemy);
        if(persistable instanceof EquippableItem item)
        {
            RewardResult result = chestReward(player, item);
            if(result.getError().getSeverity() == SEVERE) return new RewardResult(result.getError());
            if(result.getError().getSeverity() == RECOVERABLE)
            {
                consoleH.printLine(result.getError().getMessage());
                Optional<Integer> response = consoleH.confirmInput("Would you like to Clean your Inventory, or Discard this Item?", "1. Fix Inventory\n2. Discard", "\n").getInput();
                if(response.isEmpty() || response.get() == 1) return new RewardResult(CLEARING_INVENTORY);
                else if(response.get() == 2) return new RewardResult(ITEM_DISCARDED);
            }
            else if(result.getError() == SUCCESS) return new RewardResult(result.getError());
        }
        
        return new RewardResult(SUCCESS);
    }
    
    private RewardResult enemyReward(Player player, Enemy enemy)
    {
        consoleH.printLine("You got " + enemy.getXP() + " XP from " + enemy.getName() + "!");
        consoleH.printLine(enemy.getName() + " also dropped " + enemy.getGold() + " gold!");
        int levelChange = player.applyXPGain(enemy.getXP());
        
        if(levelChange == 1) consoleH.printLine("You levelled up one time!");
        else if(levelChange > 1) consoleH.printLine("You levelled up " + levelChange + " times!");
        
        for (int i = 0; i < levelChange; i++) {
            Optional<StatType> userInput;
            do
            {
                userInput = consoleH.confirmInputInteger("Which stat would you like to level up?", StatType.optionsToString(), StatType.class).getInput();
                if (userInput.isEmpty()) consoleH.printLine("You need to enter a stat type.");
                else
                {
                    player.applyStatGain(userInput.get());
                    break;
                }
            } while (true);
        }

        player.addToInventory(new Potion(PotionType.getRandomPotionType()));

        player.applyGoldGain(enemy.getGold());
        return new RewardResult(SUCCESS);
    }
    
    private RewardResult chestReward(Player player, EquippableItem item)
    {
        consoleH.printLine("Would you like to pick up the " + item.getName() + "?");
        if(item instanceof Armour armour)
        {
            consoleH.printLine("The protection values is: " + armour.calculateProtection());
        }
        else if(item instanceof Weapon weapon)
        {
//            if(player.addToInventory(weapon) == PlayerErrors.MAX_INVENTORY_REACHED) return new RewardResult(INVENTORY_FULL);
//            player.equipWeapon(weapon);
//            consoleH.printLine("The damage value is: " + player.attack());
//            player.unequipWeapon();
//            player.removeFromInventory(weapon);
        }
        
        Optional<Integer> confirm = consoleH.confirmInput("Would you like to pick up the item?", "1.Yes\n2.No", "\n").getInput();
        if(confirm.isEmpty()) return new RewardResult(CANNOT_ADD_ITEM);
        else if(confirm.get() == 2) return new RewardResult(ITEM_DISCARDED);
        
        player.addToInventory(item);
        return new RewardResult(SUCCESS);
    }
}