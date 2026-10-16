    package game.player.controller;
    
    import game.Updatable;
    import game.frame.controller.FrameAction;
    import game.items.EquipSlot;
    import game.player.model.PlayerModel;
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
            playerViewAction.updateInventory(playerModel.getInventory());
        }
        
        @Override
        public void requestEquippedItemChange(EquipSlot slot)
        {
            playerViewAction.updateEquippedItem(slot, playerModel.getEquippedItem(slot));
        }
        
        @Override
        public void requestSlotDragged(int index)
        {
        
        }
        
        @Override
        public void requestSlotDragged(EquipSlot slot)
        {
        
        }
        
        @Override
        public void requestSlotPressed(int index)
        {
        
        }
        
        @Override
        public void requestSlotReleased(int index)
        {
        
        }
        
        @Override
        public void requestSlotReleased(EquipSlot slot)
        {
        
        }
    }