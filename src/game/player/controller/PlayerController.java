    package game.player.controller;
    
    import game.Updatable;
    import game.items.EquipSlot;
    import game.player.model.PlayerModel;
    import game.player.model.SlotIdentity;
    import game.player.view.PlayerViewAction;
    
    public class PlayerController implements PlayerAction, Updatable
    {
        private final PlayerModel playerModel;
        
        private PlayerViewAction playerViewAction;
        
        public PlayerController(PlayerModel pModel)
        {
            playerModel = pModel;
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
        public void requestSlotPressed(SlotIdentity source)
        {
        
        }
        
        @Override
        public void requestSlotReleased(SlotIdentity sources, SlotIdentity destination)
        {
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
    }