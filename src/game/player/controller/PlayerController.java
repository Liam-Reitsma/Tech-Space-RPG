    package game.player.controller;
    
    import game.Updatable;
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
            playerViewAction.updatePlayerStats(playerModel.getPlayerStatEntries());
        }
    }