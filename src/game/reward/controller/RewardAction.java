package game.reward.controller;

import game.entities.Enemy;
import game.entities.StatType;
import game.items.Item;

public interface RewardAction
{
    void requestOfferChestReward(Item item);
    void requestResetReward();
    void requestEnemyReward(Enemy enemy);
    void requestTemporaryStatIncrease(StatType stat);
    void requestTemporaryStatDecrease(StatType stat);
    void requestConfirmStat();
    void requestKeepItem();
    void requestDiscardItem();
}