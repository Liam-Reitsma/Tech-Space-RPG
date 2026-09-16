package game.mainmenu;

import game.gui.GUIAction;

public class MainMenuController
{
    private final GUIAction guiAction;
    
    public MainMenuController(GUIAction actions)
    {
        guiAction = actions;
    }
    
    public void newGame()
    {
        guiAction.openGameScreen();
    }
    
    public void loadGame()
    {
        guiAction.openSavesScreen();
    }
    
    public void openOptions()
    {
        guiAction.openOptionsScreen();
    }
    
    public void exitGame()
    {
        guiAction.closeGame();
    }
}