package game.combat.view;

import game.combat.controller.CombatAction;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.PotionType;

import javax.swing.*;

public class CombatButtonPanel extends JPanel
{
    private final CombatAction combatAction;

    private final JButton fightButton;
    private final JButton healthButton;
    private final JButton runButton;

    private final JComboBox<PotionType> potionSelection;

    public CombatButtonPanel(CombatAction cAction)
    {
        combatAction = cAction;
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(33, 33, 33);
        setLayout(new ScaledLayout());

        fightButton = new JButton("Fight");
        healthButton = new JButton("Health");
        runButton = new JButton("Run");

        potionSelection = new JComboBox<>();

        fightButton.addActionListener(e -> combatAction.requestAttack());

        healthButton.addActionListener(e -> combatAction.requestHeal(potionSelection.getItemAt(potionSelection.getSelectedIndex())));

        runButton.addActionListener(e -> combatAction.requestRun());

        setControlEnabled(false);

        LayoutBox[] healthBox = verticalBoxes[1].splitHorizontal(60, 40);

        add(fightButton, verticalBoxes[0]);
        add(healthButton, healthBox[0]);
        add(potionSelection, healthBox[1]);
        add(runButton, verticalBoxes[2]);
    }

    public void updatePotions(PotionType[] potionList)
    {
        potionSelection.removeAllItems();
        for(PotionType potion : potionList)
        {
            potionSelection.addItem(potion);
        }
    }

    public void setControlEnabled(boolean enabled)
    {
        boolean potionPresent = potionSelection.getItemCount() > 0;
        fightButton.setEnabled(enabled);
        healthButton.setEnabled(enabled && potionPresent);
        potionSelection.setEnabled(enabled && potionPresent);
        runButton.setEnabled(enabled);
    }
}
