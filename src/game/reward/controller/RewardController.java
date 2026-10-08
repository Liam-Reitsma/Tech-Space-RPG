package game.reward.controller;

import game.Updatable;
import game.entities.*;
import game.entities.StatType;
import game.reward.model.RewardModel;
import game.reward.view.RewardViewAction;

public class RewardController implements RewardAction, Updatable
{
    private final RewardModel rewardModel;
    private RewardViewAction rewardViewAction;

    public RewardController(RewardModel rModel) {
        rewardModel = rModel;
    }

    public void setViewAction(RewardViewAction rvAction)
    {
        rewardViewAction = rvAction;
    }

    public void giveEnemyReward(Enemy enemy)
    {
        rewardModel.applyEnemyReward(enemy);
        requestUpdate();
    }

    @Override
    public void requestUpdate()
    {
        for(StatType statType : StatType.values())
        {
            rewardViewAction.updateStat(statType, rewardModel.getCurrentStatPoint(statType), rewardModel.getPlannedStatPoint(statType));
        }
        rewardViewAction.updateRemaining(rewardModel.getRemainingStatPoint());
        rewardViewAction.showStatPanel(rewardModel.hasPointsToAllocate());
    }

    @Override
    public void requestPlannedIncrease(StatType stat) {

    }

    @Override
    public void requestPlannedDecrease(StatType stat) {

    }

    @Override
    public void requestConfirmStat() {

    }
}
