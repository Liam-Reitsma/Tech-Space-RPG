package game.combat.view;

import game.combat.controller.CombatAction;
import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.PotionType;

import javax.swing.*;
import java.awt.*;

public class CombatButtonPanel extends JPanel
{
    private final PanelButton fightButton;
    private final PanelButton healthButton;
    private final PanelButton runButton;
    private final JComboBox<PotionType> potionSelection;
    
    private final CombatAction combatAction;

    public CombatButtonPanel(CombatAction cAction)
    {
        combatAction = cAction;
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(33, 33, 33);
        setLayout(new ScaledLayout());
        setOpaque(false);

        potionSelection = new JComboBox<>();

        fightButton = new PanelButton("Fight", e -> combatAction.requestAttack());
        healthButton = new PanelButton("Heal",
                e ->combatAction.requestHeal(potionSelection.getItemAt(potionSelection.getSelectedIndex())));
        runButton = new PanelButton("Run", e ->combatAction.requestRun());

        setControlEnabled(false);

        LayoutBox[] healthBox = verticalBoxes[1].splitHorizontal(60, 40);

        add(fightButton, verticalBoxes[0]);
        add(healthButton, healthBox[0]);
        add(potionSelection, healthBox[1]);
        add(runButton, verticalBoxes[2]);
    }

    public void updatePotions(PotionType[] potionList)
    {
        PotionType potionType = (PotionType)potionSelection.getSelectedItem();
        potionSelection.removeAllItems();
        
        for(PotionType potion : potionList)
            potionSelection.addItem(potion);
        
        potionSelection.setSelectedItem(potionType);
    }

    /**
     * Enables or disables combat controls, only enables heal and potions selection when potions are available.
     *
     * @param enabled Whether combat controls should be enabled.
     */
    public void setControlEnabled(boolean enabled)
    {
        boolean potionPresent = potionSelection.getItemCount() > 0;
        fightButton.setEnabled(enabled);
        healthButton.setEnabled(enabled && potionPresent);
        potionSelection.setEnabled(enabled && potionPresent);
        runButton.setEnabled(enabled);
    }
}