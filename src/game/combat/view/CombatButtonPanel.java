package game.combat.view;

import game.combat.controller.CombatAction;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;

public class CombatButtonPanel extends JPanel
{
    private final CombatAction combatAction;
    private final JButton fightButton;
    private final JButton healthButton;
    private final JButton runButton;

    public CombatButtonPanel(CombatAction cAction)
    {
        combatAction = cAction;
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(33, 33, 33);
        setLayout(new ScaledLayout());
        fightButton = new JButton("Fight");
        healthButton = new JButton("Health");
        runButton = new JButton("Run");

        fightButton.addActionListener(e -> combatAction.requestAttack());

        healthButton.addActionListener(e -> combatAction.requestHeal());

        runButton.addActionListener(e -> combatAction.requestRun());

        setButtonEnabled(false);
        add(fightButton, verticalBoxes[0]);
        add(healthButton, verticalBoxes[1]);
        add(runButton, verticalBoxes[2]);
    }

    public void setButtonEnabled(boolean enabled)
    {
        fightButton.setEnabled(enabled);
        healthButton.setEnabled(enabled);
        runButton.setEnabled(enabled);
    }
}
