package game.gui;

public interface GUIAction
{
    void openGameScreen();
    void openSavesScreen();
    void openOptionsScreen();
    void closeOptionsScreen();
    void closeGame();
    void changeResolution(DisplayResolution resolution);
}