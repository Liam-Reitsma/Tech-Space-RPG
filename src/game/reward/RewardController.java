package game.reward;

import static game.ErrorSeverity.RECOVERABLE;
import static game.ErrorSeverity.SEVERE;
import static game.reward.RewardErrors.*;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.*;
import game.entities.player.Player;
import game.items.EquippableItem;

import java.util.Optional;

public class RewardController implements Loopable<Player, Persistable, RewardResult>
{
    private final ConsoleHandler consoleH;

    public  RewardController(ConsoleHandler cH)
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
                Optional<Integer> response = consoleH.confirmInput("Would you like to Clean your Inventory, or Discard this Item?", "Fix Inventory|Discard", "|").getInput();
                if(response.isEmpty() || response.get() == 1) return new RewardResult(CLEARING_INVENTORY);
                else if(response.get() == 2) return new RewardResult(ITEM_DISCARDED);
            }
            else if(result.getError() == SUCCESS) return new RewardResult(result.getError());
        }
        
        return new RewardResult(SUCCESS);
    }
    
    private RewardResult enemyReward(Player player, Enemy enemy)
    {
        int levelChange = player.applyXP(enemy.getXP());
        
        for (int i = 0; i < levelChange; i++) {
            Optional<StatType> userInput;
            do
            {
                userInput = consoleH.confirmInput("Which stat would you like to level up?", StatType.optionsToString(), StatType.class).getInput();
                if (userInput.isEmpty()) consoleH.printLine("You need to enter a stat type.");
                else
                {
                    player.addStat(userInput.get());
                    break;
                }
            } while (true);
        }
        player.addGold(enemy.getGold());
        return new RewardResult(SUCCESS);
    }
    
    private RewardResult chestReward(Player player, EquippableItem item)
    {
        return player.addToInventory(item) ?  new RewardResult(SUCCESS) :  new RewardResult(INVENTORY_FULL);
    }
}