package game.mainmenu;

public class MainMenuController implements MainMenuAction
{
    private final MainMenuViewAction mmViewAction;
    
    public MainMenuController(MainMenuViewAction actions)
    {
        mmViewAction = actions;
    }
    
    @Override
    public void requestNewGame() { mmViewAction.openNewGameScreen(); }
    
    @Override
    public void requestLoadGame() { mmViewAction.openLoadGameScreen(); }
    
    @Override
    public void requestOpenOptions() { mmViewAction.openOptionsScreen(); }
    
    @Override
    public void requestExitGame() { mmViewAction.closeGameFrame(); }
}