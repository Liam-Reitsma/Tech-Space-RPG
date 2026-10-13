package game.reward.view;

import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.*;
import game.reward.controller.RewardAction;

import javax.swing.*;
import java.awt.*;

public class ChestPanel extends JPanel
{
    private final RewardAction rewardAction;

    public ChestPanel(RewardAction rAction)
    {
        rewardAction = rAction;
        setLayout(new ScaledLayout());
        setOpaque(false);
        LayoutBox[] chestRewardBoxes = new LayoutBox().splitVertical(33, 33 ,33);
        JLabel questionLabel = new JLabel("What would you like to do?", SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        add(questionLabel, chestRewardBoxes[0]);

        PanelButton takeButton = new PanelButton("Take", e -> rewardAction.requestKeepItem());
        add(takeButton, chestRewardBoxes[1]);

        PanelButton discardButton = new PanelButton("Discard", e -> rewardAction.requestDiscardItem());
        add(discardButton, chestRewardBoxes[2]);
    }
}
