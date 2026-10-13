package game.mainmenu.controller;

import game.frame.controller.DisplayTarget;

public interface MainMenuAction
{
    void requestOpenMainMenu();
    void requestOpenDisplay(DisplayTarget displayTarget);
}