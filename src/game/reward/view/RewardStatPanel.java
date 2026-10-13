package game.reward.view;

import game.entities.StatType;
import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.reward.controller.RewardAction;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RewardStatPanel extends JPanel
{
    private final RewardAction rewardAction;

    private final Map<StatType, JLabel> statLabels = new EnumMap<>(StatType.class);
    private final Map<StatType, JButton> minusButtons = new EnumMap<>(StatType.class);
    private final Map<StatType, JButton> plusButtons = new EnumMap<>(StatType.class);

    private final JLabel pointsToSpendLabel;

    public RewardStatPanel(RewardAction rAction)
    {
        rewardAction = rAction;
        setLayout(new ScaledLayout());
        setOpaque(false);

        LayoutBox[] statBoxes = new LayoutBox().splitVertical(22.5f, 22.5f, 22.5f, 22.5f, 10);
        pointsToSpendLabel = new JLabel("",  SwingConstants.CENTER);
        JButton confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e -> {rewardAction.requestConfirmStat();});

        pointsToSpendLabel.setForeground(Color.WHITE);
        add(pointsToSpendLabel, statBoxes[0]);

        StatType[] stats = StatType.values();
        for(int  i = 0; i < stats.length; i++)
        {
            StatType stat = stats[i];
            LayoutBox[] statBox = statBoxes[i + 1].splitHorizontal(20, 60, 20);

            PanelButton minusButton = new PanelButton("-", e -> rewardAction.requestTemporaryStatDecrease(stat));
            JLabel statTypeLabel =  new JLabel(stat.toString(), SwingConstants.CENTER);
            statTypeLabel.setForeground(Color.WHITE);
            PanelButton plusButton = new PanelButton("+", e -> rewardAction.requestTemporaryStatIncrease(stat));

            minusButtons.put(stat, minusButton);
            statLabels.put(stat, statTypeLabel);
            plusButtons.put(stat, plusButton);

            add(minusButton, statBox[0]);
            add(statTypeLabel, statBox[1]);
            add(plusButton, statBox[2]);
        }

        add(confirmButton, statBoxes[4]);
    }

    public void updateStats(StatType stat, int currentValue, int planned)
    {
        String labelText = stat + " " + currentValue;
        if(planned > 0) labelText += " (+" + planned + ")";
        minusButtons.get(stat).setEnabled(planned > 0);
        statLabels.get(stat).setText(labelText);
    }

    public void updateRemaining(int remaining)
    {
        pointsToSpendLabel.setText("Stat points to spend: " + remaining);
        for(JButton button : plusButtons.values())
        {
            button.setEnabled(remaining > 0);
        }
    }
}