package game.reward;

import static game.reward.RewardErrors.*;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.*;
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
        if(persistable instanceof Enemy enemy) return enemyReward(player, enemy);
        else if(persistable instanceof EquippableItem item) return chestReward(player, item);
        return new RewardResult(RewardErrors.SUCCESS);
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