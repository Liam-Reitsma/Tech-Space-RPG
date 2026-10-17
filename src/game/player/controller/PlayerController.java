    package game.player.controller;
    
    import game.Updatable;
    import game.frame.controller.FrameAction;
    import game.gui.popup.PopupMessage;
    import game.items.EquipSlot;
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
                requestDiscard(playerModel.getInventory().getItemStack(sources.inventorySlot()));
                return;
            }
            
            boolean isFromInventory = sources.equipSlot() == null;
            boolean isToEquip = destination.equipSlot() != null;
            
            if(isFromInventory)
            {
                if(isToEquip)
                    playerModel.getInventory().equipItem(sources.inventorySlot(), destination.equipSlot());
                else
                    playerModel.getInventory().swapItems(sources.inventorySlot(), destination.inventorySlot());
            }
            else
            {
                if(isToEquip)
                    return;
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
    }