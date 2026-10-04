package game.mainmenu.controller;

public interface MainMenuAction
{
    void requestOpenMainMenuScreen();
    void requestOpenNewGameScreen();
    void requestOpenSavesOverlay();
    void requestOpenOptionsOverlay();
    void requestCloseMainMenuScreen();
    void requestExitGame();
}