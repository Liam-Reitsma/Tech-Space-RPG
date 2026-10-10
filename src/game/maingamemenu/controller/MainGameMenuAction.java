package game.maingamemenu.controller;

import game.frame.controller.DisplayTarget;

public interface MainGameMenuAction
{
    void requestSaveGame();
    void requestOpenDisplay(DisplayTarget displayTarget);
}