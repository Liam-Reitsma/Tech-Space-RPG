package game.reward.model;

import game.entities.Enemy;
import game.entities.StatType;
import game.entities.player.Player;
import game.items.Potion;
import game.items.PotionType;
import game.maingame.model.GameModel;

import java.util.EnumMap;
import java.util.Map;

public class RewardModel
{
    private final GameModel gameModel;
    private int unspentPoints;
    private final Map<StatType, Integer> statsLeveledUp;

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
        return unspentPoints > 0;
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
        if(resultMessage.length() == 2) return "Nothing has be leveled up.";
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

        rewardMessage += "Enemy has dropped a " + potionTypeReceived + " potion.\n";

        return rewardMessage;
    }
}