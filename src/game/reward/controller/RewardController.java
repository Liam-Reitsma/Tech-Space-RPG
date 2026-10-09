package game.reward.controller;

import game.Updatable;
import game.reward.view.RewardPanelType;
import game.entities.*;
import game.items.Item;
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
        if(rewardModel.hasOfferedItem()) rewardViewAction.showRewardPanel(RewardPanelType.CHEST);
        else if(rewardModel.hasPointsToAllocate()) rewardViewAction.showRewardPanel(RewardPanelType.STAT_POINTS);
        else rewardViewAction.showRewardPanel(RewardPanelType.NONE);
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
        rewardViewAction.displayMessage(rewardModel.confirmLeveledUp());
        requestUpdate();
    }

    @Override
    public void requestTakeItem()
    {
        rewardViewAction.displayMessage(rewardModel.takeOfferedItem());
        requestUpdate();
    }

    @Override
    public void requestDiscardItem()
    {
        rewardViewAction.displayMessage(rewardModel.discardOfferedItem());
        requestUpdate();
    }

    public void offerChestReward(Item item)
    {
        rewardViewAction.displayMessage(rewardModel.offerChestItem(item));
        requestUpdate();
    }
}