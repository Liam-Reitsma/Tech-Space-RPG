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

    public void setRewardViewAction(RewardViewAction rvAction)
    {
        rewardViewAction = rvAction;
    }

    public void giveEnemyReward(Enemy enemy)
    {
        rewardViewAction.displayMessage(rewardModel.applyEnemyReward(enemy));
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
    public void requestPlannedIncrease(StatType stat)
    {
        rewardModel.planIncrease(stat);
        requestUpdate();
    }

    @Override
    public void requestPlannedDecrease(StatType stat)
    {
        rewardModel.planDecrease(stat);
        requestUpdate();
    }

    @Override
    public void requestConfirmStat()
    {
        rewardModel.confirmLeveledUp();
        requestUpdate();
    }
}
