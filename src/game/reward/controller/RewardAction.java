package game.reward.controller;

import game.entities.StatType;

public interface RewardAction
{
    void requestPlannedIncrease(StatType stat);
    void requestPlannedDecrease(StatType stat);
    void requestConfirmStat();
}
