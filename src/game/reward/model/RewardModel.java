package game.reward.model;

import game.entities.Enemy;
import game.entities.StatType;
import game.entities.player.Player;
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
    private final GameModel gameModel;
    private int unspentPoints;
    private final Map<StatType, Integer> statsLeveledUp;
    private Item offeredItem;

    public RewardModel(GameModel gModel)
    {
        gameModel = gModel;
        statsLeveledUp = new EnumMap<>(StatType.class);
    }

    public int getPlannedStatPoint(StatType stat) { return statsLeveledUp.getOrDefault(stat, 0); }

    public int getRemainingStatPoint() { return unspentPoints; }

    public int getCurrentStatPoint(StatType stat) { return gameModel.getPlayer().getStatValue(stat); }

    public void planIncrease(StatType stat)
    {
        if(getRemainingStatPoint() <= 0) return;
        unspentPoints--;
        statsLeveledUp.put(stat, getPlannedStatPoint(stat) + 1);
    }

    public void planDecrease(StatType stat)
    {
        if(getPlannedStatPoint(stat) <= 0) return;
        statsLeveledUp.put(stat, getPlannedStatPoint(stat) - 1);
        unspentPoints++;
    }

    public boolean hasPointsToAllocate()
    {
        if(unspentPoints > 0) return true;
        for(int planned : statsLeveledUp.values())
            if(planned > 0) return true;

        return false;
    }

    public String confirmLeveledUp()
    {
        String resultMessage = "| ";
        if(statsLeveledUp.isEmpty()) return "No stat point available.";

        for(Map.Entry<StatType, Integer> entry : statsLeveledUp.entrySet())
        {
            for(int i = 0; i < entry.getValue(); i++)
                gameModel.getPlayer().applyStatGain(entry.getKey());
            if(entry.getValue() != 0)
                resultMessage += entry.getKey().getMessage() + " + " +  entry.getValue() + " | ";
        }

        statsLeveledUp.clear();
        if(resultMessage.length() == 2) return "Nothing has been leveled up.";
        return resultMessage;
    }

    public String applyEnemyReward(Enemy enemy)
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

        return rewardMessage;
    }

    public String offerChestItem(Item item)
    {
        offeredItem = item;
        String returnMessage = "The chest contained a " + item.getName();
        if(item instanceof Weapon weapon)
        {
            returnMessage += " which deals " +
                    Math.round(weapon.calculateDamage(gameModel.getPlayer().getStatValue(weapon.getScalingStat()))) + " damage.";
        }
        else if(item instanceof Armour armour)
        {
            returnMessage += " which has " + Math.round(armour.calculateProtection() * 100) + "% protection.";
        }
        return returnMessage;
    }

    public boolean hasOfferedItem()
    {
        return offeredItem != null;
    }

    public String takeOfferedItem()
    {
        if(offeredItem == null) return "There is no item available to take.";
        gameModel.getPlayer().addToInventory(offeredItem);
        String returnMessage = offeredItem.getName() + " has been added to the inventory.";
        offeredItem = null;
        return returnMessage;
    }

    public String discardOfferedItem()
    {
        if(offeredItem == null) return "There is no item available to discard.";
        offeredItem = null;
        return "Item has been discarded.";
    }

    public void resetReward()
    {
        offeredItem = null;
        unspentPoints = 0;
        statsLeveledUp.clear();
    }
}