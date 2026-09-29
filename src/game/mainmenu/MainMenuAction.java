package game.mainmenu;

public interface MainMenuAction
{
    void requestNewGame();
    void requestLoadGame();
    void requestOpenOptions();
    void requestExitGame();
}