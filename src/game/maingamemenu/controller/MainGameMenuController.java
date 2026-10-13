package game.maingamemenu.controller;

import game.frame.controller.DisplayTarget;
import game.frame.controller.FrameAction;
import game.maingame.model.GameModel;

public class MainGameMenuController implements MainGameMenuAction
{
    private final GameModel gameModel;
    
    private final FrameAction frameAction;
    
    public MainGameMenuController(GameModel gModel, FrameAction fAction)
    {
        gameModel = gModel;
        frameAction = fAction;
    }
    
    @Override
    public void requestSaveGame()
    {
        gameModel.fullSave();
    }
    
    @Override
    public void requestOpenDisplay(DisplayTarget displayTarget)
    {
        frameAction.requestDisplay(displayTarget);
    }
}