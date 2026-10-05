package game.mainmenu.controller;

import static game.frame.controller.DisplayTarget.MAIN_MENU;

import game.frame.controller.FrameAction;

public class MainMenuController implements MainMenuAction
{
    private final FrameAction frameAction;
    
    public MainMenuController(FrameAction fAction)
    {
        frameAction = fAction;
    }
    
    @Override
    public void requestOpenMainMenu()
    {
        frameAction.display(MAIN_MENU);
    }
    
    @Override
    public void requestOpenDisplay(MainMenuOption mainMenuOption)
    {
        frameAction.display(mainMenuOption.getDisplayTarget());
    }
    
    @Override
    public void requestExitGame()
    {
        frameAction.requestCloseGame();
    }
}