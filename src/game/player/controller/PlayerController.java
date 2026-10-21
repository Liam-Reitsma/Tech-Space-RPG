    package game.player.controller;
    
    import game.Updatable;
    import game.frame.controller.FrameAction;
    import game.gui.popup.PopupMessage;
    import game.items.EquipSlot;
    import game.items.EquippableItem;
    import game.player.model.ItemStack;
    import game.player.model.PlayerModel;
    import game.player.model.SlotIdentity;
    import game.player.view.PlayerViewAction;
    
    public class PlayerController implements PlayerAction, Updatable
    {
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
        
        @Override
        public void requestSlotReleased(SlotIdentity sources, SlotIdentity destination)
        {
            if(sources == null || sources.isInvalid() || (destination != null && destination.isInvalid()))
                return;
            
            if(destination == null)
            {
                if(sources.inventorySlot() != null)
                {
                    ItemStack itemStack = playerModel.getInventory().getItemStack(sources.inventorySlot());
                    
                    if(itemStack.getItem() != null)
                        requestDiscard(itemStack);
                }
                else
                {
                    EquippableItem equippableItem = playerModel.getInventory().getEquippedItem(sources.equipSlot());
                    
                    if(equippableItem != null)
                        requestDiscard(equippableItem);
                }
                return;
            }
            
            boolean isFromInventory = sources.equipSlot() == null;
            boolean isToEquip = destination.equipSlot() != null;
            
            if(isFromInventory)
            {
                if(isToEquip)
                {
                    if(!playerModel.getPlayer().equipItem(sources.inventorySlot(), destination.equipSlot()))
                    {
                        if(playerModel.getInventory().getItemStack(sources.inventorySlot()).getItem() instanceof EquippableItem item)
                        {
                            playerViewAction.flashEquipmentSlot(destination.equipSlot(), item.getEquipSlot());
                        }
                        else
                        {
                            for(EquipSlot slot : playerModel.getInventory().getAllEquippedSlots())
                                playerViewAction.flashEquipmentSlot(slot, null);
                        }
                    }
                }
                else
                    playerModel.getPlayer().swapItems(sources.inventorySlot(), destination.inventorySlot());
            }
            else
            {
                if(isToEquip)
                {
                    playerViewAction.flashEquipmentSlot(sources.equipSlot(), null);
                    playerViewAction.flashEquipmentSlot(destination.equipSlot(), null );
                }
                else if(!playerModel.getPlayer().unequipItem(sources.equipSlot(), destination.inventorySlot()))
                    playerViewAction.flashEquipmentSlot(sources.equipSlot(), null);
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