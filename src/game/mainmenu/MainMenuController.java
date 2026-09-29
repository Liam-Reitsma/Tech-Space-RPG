package game.mainmenu;

import game.gui.GUIAction;

public class MainMenuController implements MainMenuAction
{
    private final GUIAction guiAction;
    
    public MainMenuController(GUIAction actions)
    {
        guiAction = actions;
    }
    
    @Override
    public void requestNewGame()
    {
        guiAction.openGameScreen();
    }
    
    @Override
    public void requestLoadGame()
    {
        guiAction.openSavesScreen();
    }
    
    @Override
    public void requestOpenOptions()
    {
        guiAction.openOptionsScreen();
    }
    
    @Override
    public void requestExitGame()
    {
        guiAction.closeGame();
    }
}