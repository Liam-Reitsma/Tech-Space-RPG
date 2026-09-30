package game.mainmenu;

public interface MainMenuViewAction
{
    void openNewGameScreen();
    void openLoadGameOverlay(String[] saveNames);
    void openOptionsOverlay();
    void closeGameFrame();
}