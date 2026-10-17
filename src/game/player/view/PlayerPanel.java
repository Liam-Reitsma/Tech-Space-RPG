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
    private final PlayerStatsPanel playerStatsPanel;
    private final PlayerEquippedPanel playerEquippedPanel;
    private final PlayerInventoryPanel playerInventoryPanel;
    
    public PlayerPanel(PlayerAction playerAction, DragViewAction dvAction)
    {
        setLayout(new ScaledLayout());
        
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(60, 40);
        LayoutBox[] horizontalBoxes = verticalBoxes[0].splitHorizontal(40, 60);
        
        playerStatsPanel = new PlayerStatsPanel();
        add(playerStatsPanel, horizontalBoxes[0]);
        
        playerEquippedPanel = new PlayerEquippedPanel(playerAction, dvAction);
        add(playerEquippedPanel, horizontalBoxes[1]);
        
        playerInventoryPanel = new PlayerInventoryPanel(playerAction, dvAction);
        add(playerInventoryPanel, verticalBoxes[1]);
    }
    
    @Override
    public void updatePlayerStats(List<StatEntry> statEntries)
    {
        playerStatsPanel.updatePlayerStats(statEntries);
    }
    
    @Override
    public void updateEquippedItem(EquipSlot slot, EquippableItem item)
    {
        playerEquippedPanel.updateEquippedItem(slot, item);
    }
    
    @Override
    public void updateInventory(ItemStack[] itemStacks)
    {
        playerInventoryPanel.updateInventory(itemStacks);
    }
}