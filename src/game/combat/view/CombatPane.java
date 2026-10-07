package game.combat.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;

public class CombatPane extends JPanel implements CombatViewAction
{
    private final CombatTextPane combatTextPane;
    private final CombatButtonPanel combatButtonPanel;

    public CombatPane(CombatButtonPanel cbPanel, CombatTextPane ctPane)
    {
        setLayout(new ScaledLayout());
        LayoutBox[] verticalBox = new LayoutBox().splitVertical(70, 30);
        combatTextPane = ctPane;
        add(combatTextPane, verticalBox[0]);
        combatButtonPanel = cbPanel;
        add(combatButtonPanel, verticalBox[1]);
    }

    @Override
    public void displayMessage(String message)
    {
        combatTextPane.update(message);
    }

    @Override
    public void setActionEnabled(boolean actionEnabled)
    {
        combatButtonPanel.setButtonEnabled(actionEnabled);
    }
}