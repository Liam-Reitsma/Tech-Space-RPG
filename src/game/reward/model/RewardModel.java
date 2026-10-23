package game.reward.model;

import game.entities.Enemy;
import game.entities.StatType;
import game.player.model.Player;
import game.items.Item;
import game.items.Potion;
import game.items.PotionType;
import game.items.armour.Armour;
import game.items.weapon.Weapon;
import game.maingame.model.GameModel;

import java.util.EnumMap;
import java.util.Map;

public class RewardModel
{
    private int unspentPoints;
    private Item offeredItem;
    
    private final Map<StatType, Integer> statsLeveledUp;
    
    private final GameModel gameModel;
    
    public RewardModel(GameModel gModel)
    {
        gameModel = gModel;
        statsLeveledUp = new EnumMap<>(StatType.class);
    }

    public void planIncrease(StatType stat)
    {
        if(getRemainingStatPoint() <= 0)
            return;
        
        unspentPoints--;
        statsLeveledUp.put(stat, getPlannedStatPoint(stat) + 1);
    }

    public void planDecrease(StatType stat)
    {
        if(getPlannedStatPoint(stat) <= 0)
            return;
        
        statsLeveledUp.put(stat, getPlannedStatPoint(stat) - 1);
        unspentPoints++;
    }

    public boolean hasPointsToAllocate()
    {
        if(unspentPoints > 0)
            return true;
        for(int planned : statsLeveledUp.values())
            if(planned > 0)
                return true;

        return false;
    }

    public RewardResult confirmLeveledUp()
    {
        String resultMessage = "| ";
        
        if(statsLeveledUp.isEmpty())
            return new RewardResult("No stat point available.", false);

        for(Map.Entry<StatType, Integer> entry : statsLeveledUp.entrySet())
        {
            for(int i = 0; i < entry.getValue(); i++)
                gameModel.getPlayer().applyStatGain(entry.getKey());
            if(entry.getValue() != 0)
                resultMessage += entry.getKey().getFullName() + " + " +  entry.getValue() + " | ";
        }

        statsLeveledUp.clear();
        if(resultMessage.length() == 2)
            return new RewardResult("Nothing has been leveled up.", true);
        
        return new RewardResult(resultMessage, false);
    }

    public RewardResult applyEnemyReward(Enemy enemy)
    {
        Player player = gameModel.getPlayer();

        int levelsGained = player.applyXPGain(enemy.getXP());
        unspentPoints += levelsGained;
        player.applyGoldGain(enemy.getGold());
        PotionType potionTypeReceived = PotionType.getRandomPotionType();
        player.addToInventory(new Potion(potionTypeReceived));

        String rewardMessage = "You have gained " + enemy.getXP() + " XP and " + enemy.getGold() + " Gold.\n";

        if(levelsGained > 0)
            rewardMessage += "You have leveled up!\n" + unspentPoints + " stat points to spend.\n";

        rewardMessage += enemy.getName() + " has dropped a " + potionTypeReceived + " potion.\n";

        return new RewardResult(rewardMessage, false);
    }

    public RewardResult offerChestItem(Item item)
    {
        offeredItem = item;
        String returnMessage = "The chest contained a " + item.getName();
        if(item instanceof Weapon weapon)
            returnMessage += " which deals " +
                    Math.round(weapon.calculateDamage(gameModel.getPlayer().getStatValue(weapon.getScalingStat()))) + " damage.";
        else if(item instanceof Armour armour)
            returnMessage += " which has " + Math.round(armour.calculateProtection() * 100) + "% protection.";
        
        return new RewardResult(returnMessage, false);
    }

    public boolean hasOfferedItem()
    {
        return offeredItem != null;
    }

    public RewardResult takeOfferedItem()
    {
        if(offeredItem == null)
            return new RewardResult("There is no item available to take.", false);
        
        if(!gameModel.getPlayer().addToInventory(offeredItem))
            return new RewardResult("Inventory is full. Cannot take " + offeredItem.getName() + ".", true);
        
        String returnMessage = offeredItem.getName() + " has been added to the inventory.";
        
        offeredItem = null;
        return new RewardResult(returnMessage, false);
    }

    public RewardResult discardOfferedItem()
    {
        if(offeredItem == null)
            return new RewardResult("There is no item available to discard.", false);
        
        offeredItem = null;
        return new RewardResult("Item has been discarded.", false);
    }

    public void resetReward()
    {
        offeredItem = null;
        unspentPoints = 0;
        statsLeveledUp.clear();
    }
    
    public int getPlannedStatPoint(StatType stat) { return statsLeveledUp.getOrDefault(stat, 0); }
    
    public int getRemainingStatPoint() { return unspentPoints; }
    
    public int getCurrentStatPoint(StatType stat) { return gameModel.getPlayer().getStatValue(stat); }
}