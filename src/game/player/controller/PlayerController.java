    package game.player.controller;
    
    import game.Updatable;
    import game.frame.controller.FrameAction;
    import game.items.EquipSlot;
    import game.player.model.PlayerModel;
    import game.player.view.ItemSlot;
    import game.player.view.PlayerViewAction;
    
    import javax.swing.*;
    import java.awt.event.MouseEvent;
    
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
        public void requestSlotPressed(int index)
        {
        
        }
        
        @Override
        public void requestSlotDragged(int index, MouseEvent e, ItemSlot dragSlot)
        {
            frameAction.requestDisplayDrag(e, dragSlot);
        }
        
        @Override
        public void requestSlotDragged(EquipSlot slot, MouseEvent e, ItemSlot dragSlot)
        {
            frameAction.requestDisplayDrag(e, dragSlot);
        }
    }