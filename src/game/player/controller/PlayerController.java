    package game.player.controller;
    
    import game.Updatable;
    import game.items.EquipSlot;
    import game.player.model.PlayerModel;
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
            playerViewAction.updateInventory(playerModel.getInventory());
        }
        
        @Override
        public void requestEquippedItemChange(EquipSlot slot)
        {
            playerViewAction.updateEquippedItem(slot, playerModel.getEquippedItem(slot));
        }
    }