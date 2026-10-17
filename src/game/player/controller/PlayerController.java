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
            for(EquipSlot slot : playerModel.getAllEquippedSlots())
                requestEquippedItemChange(slot);
            playerViewAction.updatePlayerStats(playerModel.getPlayerStatEntries());
            playerViewAction.updateInventory(playerModel.getInventorySlots());
        }
        
        @Override
        public void requestEquippedItemChange(EquipSlot slot)
        {
            playerViewAction.updateEquippedItem(slot, playerModel.getEquippedItem(slot));
        }
        
        @Override
        public void requestSlotReleased(SlotIdentity sources, SlotIdentity destination)
        {
            if(destination == null)
            {
                if(sources.inventorySlot() != null)
                    requestDiscard(playerModel.getInventory().getItemStack(sources.inventorySlot()));
                else
                    requestDiscard(playerModel.getInventory().getEquippedItem(sources.equipSlot()));
                return;
            }
            
            boolean isFromInventory = sources.equipSlot() == null;
            boolean isToEquip = destination.equipSlot() != null;
            
            if(isFromInventory)
            {
                if(isToEquip)
                {
                    if(!playerModel.getInventory().equipItem(sources.inventorySlot(), destination.equipSlot()))
                    {
                        if(playerModel.getInventory().getItemStack(sources.inventorySlot()).getItem() instanceof EquippableItem item)
                        {
                            playerViewAction.flashEquipmentSlot(destination.equipSlot(), item.getEquipSlot());
                        }
                        else
                        {
                            for(EquipSlot slot : playerModel.getInventory().getAllEquippedSlots())
                            {playerViewAction.flashEquipmentSlot(slot, null);}
                        }
                    }
                }
                else
                    playerModel.getInventory().swapItems(sources.inventorySlot(), destination.inventorySlot());
            }
            else
            {
                if(isToEquip)
                {
                    playerViewAction.flashEquipmentSlot(sources.equipSlot(), null);
                    playerViewAction.flashEquipmentSlot(destination.equipSlot(), null );
                }
                else
                    playerModel.getInventory().unequipItem(sources.equipSlot(), destination.inventorySlot());
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
                        playerModel.getInventory().removeFromInventory(itemStack.getItem(), true);
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
                        playerModel.getInventory().removeFromEquipped(item);
                        requestUpdate();
                        frameAction.requestClosePopup();
                    });
        }
    }