package game.player.view;

import game.frame.view.DragViewAction;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.EquipSlot;
import game.items.EquippableItem;
import game.player.controller.PlayerAction;
import game.player.model.ItemStack;
import game.player.model.StatEntry;

import javax.swing.*;
import java.util.List;

public class PlayerPanel extends JPanel implements PlayerViewAction
{
    private final PlayerEquippedPanel playerEquippedPanel;
    private final PlayerInventoryPanel playerInventoryPanel;
    private final PlayerStatsPanel playerStatsPanel;
    
    public PlayerPanel(PlayerAction playerAction, DragViewAction dvAction)
    {
        setLayout(new ScaledLayout());
        
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(60, 40);
        LayoutBox[] horizontalBoxes = verticalBoxes[0].splitHorizontal(40, 60);
        
        playerEquippedPanel = new PlayerEquippedPanel(playerAction, dvAction);
        add(playerEquippedPanel, horizontalBoxes[1]);
        
        playerInventoryPanel = new PlayerInventoryPanel(playerAction, dvAction);
        add(playerInventoryPanel, verticalBoxes[1]);
        
        playerStatsPanel = new PlayerStatsPanel();
        add(playerStatsPanel, horizontalBoxes[0]);
    }
    
    @Override
    public void updateEquippedItem(EquipSlot slot, EquippableItem item)
    {
        playerEquippedPanel.updateEquippedItem(slot, item);
    }
    
    @Override
    public void updateInventory(ItemStack[] itemStacks, boolean[] canEquips, String[] tooltips)
    {
        playerInventoryPanel.updateInventory(itemStacks, canEquips, tooltips);
    }
    
    @Override
    public void updatePlayerStats(List<StatEntry> statEntries)
    {
        playerStatsPanel.updatePlayerStats(statEntries);
    }

    /**
     * Flashes the incorrect equipment slot red and the correct slot green, if provided.
     *
     * @param incorrectSlot The slot flash as red.
     * @param correctSlot The valid equipment slot to flash green, or null if none.
     */
    @Override
    public void flashEquipmentSlot(EquipSlot incorrectSlot, EquipSlot correctSlot)
    {
        playerEquippedPanel.highlightSlots(incorrectSlot, SlotHighlight.INVALID);
        if(correctSlot != null && !correctSlot.equals(incorrectSlot))
            playerEquippedPanel.highlightSlots(correctSlot, SlotHighlight.VALID);
    }
}