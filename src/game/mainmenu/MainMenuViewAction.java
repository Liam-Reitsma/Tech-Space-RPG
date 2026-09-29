package game.mainmenu;

public interface MainMenuViewAction
{
    void openNewGameScreen();
    void openLoadGameScreen(String[] saveNames);
    void openOptionsScreen();
    void closeGameFrame();
}