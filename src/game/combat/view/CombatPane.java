package game.combat.view;

import game.combat.controller.CombatAction;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;

public class CombatPane extends JPanel implements CombatViewAction
{
    public CombatPane(CombatAction cAction)
    {
        setLayout(new ScaledLayout());
        LayoutBox[] verticalBox = new LayoutBox().splitVertical(70, 30);

        add(new CombatTextPane(), verticalBox[0]);

        add(new CombatButtonPanel(cAction), verticalBox[1]);
    }

    @Override
    public void updateCombatTextPane(String actionPerformed)
    {

    }
}