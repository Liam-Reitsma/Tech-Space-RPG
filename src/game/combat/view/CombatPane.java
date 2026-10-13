package game.combat.view;

import game.entities.StatType;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.PotionType;
import game.reward.view.ChestPanel;
import game.reward.view.RewardPanelType;
import game.reward.view.RewardStatPanel;
import game.reward.view.RewardViewAction;

import javax.swing.*;
import java.awt.*;

public class CombatPane extends JPanel implements CombatViewAction, RewardViewAction
{
    private static final Image TEXT_PANE_BACKGROUND = new ImageIcon("resources/images/Options Content.png").getImage();

    private final CombatTextPane combatTextPane;
    private final CombatButtonPanel combatButtonPanel;
    private final RewardStatPanel rewardStatPanel;
    private final ChestPanel chestPanel;

    public CombatPane(CombatButtonPanel cbPanel, CombatTextPane ctPane, RewardStatPanel rsPanel, ChestPanel cpPanel)
    {
        setLayout(new ScaledLayout());
        LayoutBox[] verticalBox = new LayoutBox().splitVertical(70, 30);
        combatTextPane = ctPane;
        chestPanel = cpPanel;
        add(combatTextPane, verticalBox[0]);
        combatButtonPanel = cbPanel;
        add(combatButtonPanel, verticalBox[1]);
        rewardStatPanel = rsPanel;
        add(rewardStatPanel, verticalBox[1]);
        rewardStatPanel.setVisible(false);
        add(chestPanel, verticalBox[1]);
        chestPanel.setVisible(false);
    }

    @Override
    public void showAvailablePotions(PotionType[] potions)
    {
        combatButtonPanel.updatePotions(potions);
    }

    @Override
    public void displayMessage(String message)
    {
        combatTextPane.updateLog(message);
    }

    @Override
    public void clearMessage()
    {
        combatTextPane.clearLog();
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
    public void showRewardPanel(RewardPanelType panelType)
    {
        chestPanel.setVisible(panelType == RewardPanelType.CHEST);
        rewardStatPanel.setVisible(panelType == RewardPanelType.STAT_POINTS);
        combatButtonPanel.setVisible(panelType == RewardPanelType.NONE);
    }

    @Override
    public void setActionEnabled(boolean actionEnabled)
    {
        combatButtonPanel.setControlEnabled(actionEnabled);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.drawImage(TEXT_PANE_BACKGROUND, 0, 0, getWidth(), getHeight(), this);
    }
}