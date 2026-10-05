package game.mainmenu.controller;

public interface MainMenuAction
{
    void requestOpenMainMenu();
    void requestOpenDisplay(MainMenuOption option);
    void requestExitGame();
}