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
    public void newGame()
    {
        guiAction.openGameScreen();
    }
    
    @Override
    public void loadGame()
    {
        guiAction.openSavesScreen();
    }
    
    @Override
    public void openOptions()
    {
        guiAction.openOptionsScreen();
    }
    
    @Override
    public void exitGame()
    {
        guiAction.closeGame();
    }
}