package game.gui;

public interface GUIAction
{
    void openGameScreen();
    void openSavesScreen();
    void selectSave(String saveName);
    void closeSavesScreen();
    void openOptionsScreen();
    void changeResolution(DisplayResolution resolution);
    void closeOptionsScreen();
    void closeGame();
}