package game.reward.view;

import game.entities.StatType;

public interface RewardViewAction
{
    void displayMessage(String message);
    void updateStat(StatType stat, int currentValue, int planned);
    void updateRemaining(int remaining);
    void showRewardPanel(RewardPanelType panelType);
}