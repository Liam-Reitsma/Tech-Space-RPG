package game.reward.controller;

import game.Updatable;
import game.maingame.controller.GameAction;
import game.reward.view.RewardPanelType;
import game.entities.*;
import game.items.Item;
import game.reward.model.RewardModel;
import game.reward.view.RewardViewAction;

public class RewardController implements RewardAction, Updatable
{
    private final RewardModel rewardModel;

    private GameAction gameAction;
    
    private RewardViewAction rewardViewAction;
    private RewardListener rewardListener;

    public RewardController(RewardModel rModel)
    {
        rewardModel = rModel;
    }
    
    public void setGameAction(GameAction gAction) { gameAction = gAction;}

    public void setRewardViewAction(RewardViewAction rvAction)
    {
        rewardViewAction = rvAction;
    }

    public void setRewardListener(RewardListener rListener)
    {
        rewardListener = rListener;
    }

    public void requestEnemyReward(Enemy enemy)
    {
        rewardViewAction.displayMessage(rewardModel.applyEnemyReward(enemy));
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestTemporaryStatIncrease(StatType stat)
    {
        rewardModel.planIncrease(stat);
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestTemporaryStatDecrease(StatType stat)
    {
        rewardModel.planDecrease(stat);
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestConfirmStat()
    {
        rewardViewAction.displayMessage(rewardModel.confirmLeveledUp());
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestKeepItem()
    {
        rewardViewAction.displayMessage(rewardModel.takeOfferedItem());
        gameAction.requestGameUpdate();
        if(rewardListener != null)
            rewardListener.onChestDecision();
    }

    @Override
    public void requestDiscardItem()
    {
        rewardViewAction.displayMessage(rewardModel.discardOfferedItem());
        gameAction.requestGameUpdate();
        if(rewardListener != null)
            rewardListener.onChestDecision();
    }

    @Override
    public void requestOfferChestReward(Item item)
    {
        rewardViewAction.displayMessage(rewardModel.offerChestItem(item));
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestResetReward()
    {
        rewardModel.resetReward();
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
}