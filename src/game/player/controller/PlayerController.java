package game.player.controller;

import game.Updatable;
import game.frame.controller.FrameAction;
import game.gui.popup.PopupError;
import game.gui.popup.PopupMessage;
import game.items.EquipSlot;
import game.items.EquippableItem;
import game.player.model.InventoryResult;
import game.player.model.ItemStack;
import game.player.model.PlayerModel;
import game.player.model.SlotIdentity;
import game.player.view.PlayerViewAction;

import java.util.logging.Logger;

public class PlayerController implements PlayerAction, Updatable
{
    private static final Logger LOGGER = Logger.getLogger(PlayerController.class.getName());
    
    private final PlayerModel playerModel;
    
    private final FrameAction frameAction;
    
    private PlayerViewAction playerViewAction;
    
    public PlayerController(PlayerModel pModel, FrameAction fAction)
    {
        playerModel = pModel;
        frameAction = fAction;
    }
    
    public void setPlayerViewAction(PlayerViewAction pVAction) { playerViewAction = pVAction; }
    
    @Override
    public void requestUpdate()
    {
        for(EquipSlot slot : playerModel.getInventory().getAllEquippedSlots())
            requestEquippedItemChange(slot);
        playerViewAction.updatePlayerStats(playerModel.getPlayerStatEntries());
        playerViewAction.updateInventory(playerModel.getInventory().getInventorySlots());
    }
    
    private void requestEquippedItemChange(EquipSlot slot)
    {
        playerViewAction.updateEquippedItem(slot, playerModel.getInventory().getEquippedItem(slot));
    }

    /**
     * Handles drag-and-drop operations to equip, unequip, swap or discard items.
     * Requests confirmation before discarding and provides feedback for invalid operations.
     *
     * @param source The inventory or equip slot that the item originates from.
     * @param destination The slot where the item was released, or null if dropped outside a slot.
     */
    @Override
    public void requestSlotReleased(SlotIdentity source, SlotIdentity destination)
    {
        if(source == null || source.isInvalid() || (destination != null && destination.isInvalid()))
            return;
        
        if(destination == null)
        {
            if(source.inventorySlot() != null)
            {
                ItemStack itemStack = playerModel.getInventory().getItemStack(source.inventorySlot());
                
                if(itemStack.getItem() != null)
                    requestDiscard(itemStack);
            }
            else
            {
                EquippableItem equippableItem = playerModel.getInventory().getEquippedItem(source.equipSlot());
                
                if(equippableItem != null)
                    requestDiscard(equippableItem);
            }
            return;
        }
        
        boolean isFromInventory = source.equipSlot() == null;
        boolean isToEquip = destination.equipSlot() != null;
        
        if(isFromInventory)
        {
            if(isToEquip)
            {
                InventoryResult result = playerModel.getPlayer().equipItem(source.inventorySlot(), destination.equipSlot());
                switch(result)
                {
                    case SUCCESS -> {}
                    case INVALID_SLOT ->
                    {
                        if(playerModel.getInventory().getItemStack(source.inventorySlot()).getItem() instanceof EquippableItem item)
                            playerViewAction.flashEquipmentSlot(destination.equipSlot(), item.getEquipSlot());
                    }
                    case INVALID_ITEM ->
                    {
                        for(EquipSlot slot : playerModel.getInventory().getAllEquippedSlots())
                            playerViewAction.flashEquipmentSlot(slot, null);
                    }
                    case INVENTORY_FULL ->
                    {
                        frameAction.requestDisplayError(PopupError.INVENTORY_FULL_EQUIPPING,
                                e->
                                {
                                    requestUpdate();
                                    frameAction.requestClosePopup();
                                },
                                e ->
                                {
                                    playerModel.getPlayer().equipItemDiscardingEquipped(source.inventorySlot(), destination.equipSlot());
                                    requestUpdate();
                                    frameAction.requestClosePopup();
                                });
                    }
                    default ->
                    {
                        LOGGER.warning("Unexpected inventory result when equipping item: " + result);
                    }
                }
            }
            else
                playerModel.getPlayer().swapItems(source.inventorySlot(), destination.inventorySlot());
        }
        else
        {
            if(isToEquip)
            {
                playerViewAction.flashEquipmentSlot(source.equipSlot(), null);
                playerViewAction.flashEquipmentSlot(destination.equipSlot(), null );
            }
            else
            {
                InventoryResult result = playerModel.getPlayer().unequipItem(source.equipSlot(), destination.inventorySlot());
                switch(result)
                {
                    case SUCCESS -> {}
                    case INVENTORY_FULL ->
                    {
                        frameAction.requestDisplayError(PopupError.INVENTORY_FULL_UNEQUIPPING,
                                e->
                                {
                                    requestUpdate();
                                    frameAction.requestClosePopup();
                                },
                                e ->
                                {
                                    playerModel.getPlayer().unequipItemDiscardingInventory(source.equipSlot(), destination.inventorySlot());
                                    requestUpdate();
                                    frameAction.requestClosePopup();
                                });
                    }
                    default ->
                    {
                        LOGGER.warning("Unexpected inventory result when unequipping item: " + result);
                        playerViewAction.flashEquipmentSlot(source.equipSlot(), null);
                    }
                }
            }
        }
        
        requestUpdate();
    }
    
    private void requestDiscard(ItemStack itemStack)
    {
        PopupMessage popupMessage = new PopupMessage("Discarding " + itemStack.getItem().getName(),
                "You are about to discard " + itemStack.getQuantity() + " x " + itemStack.getItem().getName(),
                "Cancel", "Discard");
        frameAction.requestDisplayConfirmation(popupMessage,
                e->
                {
                    requestUpdate();
                    frameAction.requestClosePopup();
                },
                e->
                {
                    playerModel.getPlayer().removeFromInventory(itemStack.getItem(), true);
                    requestUpdate();
                    frameAction.requestClosePopup();
                });
    }
    
    private void requestDiscard(EquippableItem item)
    {
        PopupMessage popupMessage = new PopupMessage("Discarding " + item.getName(),
                "You are about to discard " + item.getName(),
                "Cancel", "Discard");
        frameAction.requestDisplayConfirmation(popupMessage,
                e->
                {
                    requestUpdate();
                    frameAction.requestClosePopup();
                },
                e->
                {
                    playerModel.getPlayer().removeFromEquipped(item);
                    requestUpdate();
                    frameAction.requestClosePopup();
                });
    }
}