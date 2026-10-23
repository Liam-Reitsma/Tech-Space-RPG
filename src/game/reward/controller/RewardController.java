package game.reward.controller;

import game.Updatable;
import game.frame.controller.FrameAction;
import game.gui.popup.PopupError;
import game.maingame.controller.GameAction;
import game.reward.model.RewardResult;
import game.reward.view.RewardPanelType;
import game.entities.*;
import game.items.Item;
import game.reward.model.RewardModel;
import game.reward.view.RewardViewAction;

public class RewardController implements RewardAction, Updatable
{
    private final RewardModel rewardModel;

    private final FrameAction frameAction;
    
    private GameAction gameAction;
    
    private RewardViewAction rewardViewAction;
    private RewardListener rewardListener;

    public RewardController(RewardModel rModel, FrameAction fAction)
    {
        rewardModel = rModel;
        frameAction = fAction;
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
        RewardResult result = rewardModel.applyEnemyReward(enemy);
        rewardViewAction.displayMessage(result.message());
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
        RewardResult result = rewardModel.confirmLeveledUp();
        if(result.hasError())
            frameAction.requestDisplayError(PopupError.NO_STATS_INCREASED,
                    e-> frameAction.requestClosePopup(),
                    e-> frameAction.requestClosePopup());
        
        rewardViewAction.displayMessage(result.message());
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestKeepItem()
    {
        RewardResult result = rewardModel.takeOfferedItem();
        if(result.hasError())
            frameAction.requestDisplayError(PopupError.INVENTORY_FULL_ADDING,
                    e-> frameAction.requestClosePopup(),
                    e->
                    {
                        rewardModel.discardOfferedItem();
                        frameAction.requestClosePopup();
                        
                        if(rewardListener != null)
                            rewardListener.onChestDecision();
                        
                        gameAction.requestGameUpdate();
                    });
        else if(!rewardModel.hasOfferedItem() && rewardListener != null)
            rewardListener.onChestDecision();
        
        rewardViewAction.displayMessage(result.message());
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestDiscardItem()
    {
        RewardResult result = rewardModel.discardOfferedItem();
        rewardViewAction.displayMessage(result.message());
        
        if(rewardListener != null)
            rewardListener.onChestDecision();
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestOfferChestReward(Item item)
    {
        RewardResult result = rewardModel.offerChestItem(item);
        
        rewardViewAction.displayMessage(result.message());
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
            rewardViewAction.updateStat(statType, rewardModel.getCurrentStatPoint(statType), rewardModel.getPlannedStatPoint(statType));
        
        rewardViewAction.updateRemaining(rewardModel.getRemainingStatPoint());
        if(rewardModel.hasOfferedItem())
            rewardViewAction.showRewardPanel(RewardPanelType.CHEST);
        else if(rewardModel.hasPointsToAllocate())
            rewardViewAction.showRewardPanel(RewardPanelType.STAT_POINTS);
        else
            rewardViewAction.showRewardPanel(RewardPanelType.NONE);
    }
}