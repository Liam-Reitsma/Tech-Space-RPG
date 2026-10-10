package game.reward.view;

import game.gui.common.scaledlayout.*;
import game.reward.controller.RewardAction;

import javax.swing.*;

public class ChestPanel extends JPanel
{
    private final RewardAction rewardAction;

    public ChestPanel(RewardAction rAction)
    {
        rewardAction = rAction;
        setLayout(new ScaledLayout());
        LayoutBox[] chestRewardBoxes = new LayoutBox().splitVertical(33, 33 ,33);
        JLabel questionLabel = new JLabel("What would you like to do?", SwingConstants.CENTER);
        add(questionLabel, chestRewardBoxes[0]);

        JButton takeButton = new JButton("Take");
        takeButton.addActionListener(e -> rewardAction.requestKeepItem());
        add(takeButton, chestRewardBoxes[1]);

        JButton discardButton = new JButton("Discard");
        discardButton.addActionListener(e -> rewardAction.requestDiscardItem());
        add(discardButton, chestRewardBoxes[2]);
    }
}
