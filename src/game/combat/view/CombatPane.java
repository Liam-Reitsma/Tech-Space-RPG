package game.combat.view;

import game.entities.StatType;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.PotionType;
import game.reward.view.RewardStatPanel;
import game.reward.view.RewardViewAction;

import javax.swing.*;

public class CombatPane extends JPanel implements CombatViewAction, RewardViewAction
{
    private final CombatTextPane combatTextPane;
    private final CombatButtonPanel combatButtonPanel;
    private final RewardStatPanel rewardStatPanel;

    public CombatPane(CombatButtonPanel cbPanel, CombatTextPane ctPane, RewardStatPanel rsPanel)
    {
        setLayout(new ScaledLayout());
        LayoutBox[] verticalBox = new LayoutBox().splitVertical(70, 30);
        combatTextPane = ctPane;
        add(combatTextPane, verticalBox[0]);
        combatButtonPanel = cbPanel;
        add(combatButtonPanel, verticalBox[1]);
        rewardStatPanel = rsPanel;
        add(rewardStatPanel, verticalBox[1]);
        rewardStatPanel.setVisible(false);
    }

    @Override
    public void showAvailablePotions(PotionType[] potions)
    {
        combatButtonPanel.updatePotions(potions);
    }

    @Override
    public void displayMessage(String message)
    {
        combatTextPane.update(message);
    }

    @Override
    public void updateStat(StatType stat, int currentValue, int planned)
    {
        rewardStatPanel.updateStats(stat, currentValue, planned);
    }

    @Override
    public void updateRemaining(int remaining)
    {
        rewardStatPanel.updateRemaining(remaining);
    }

    @Override
    public void showStatPanel(boolean show)
    {
        rewardStatPanel.setVisible(show);
        combatButtonPanel.setVisible(!show);
    }

    @Override
    public void setActionEnabled(boolean actionEnabled)
    {
        combatButtonPanel.setControlEnabled(actionEnabled);
    }
}