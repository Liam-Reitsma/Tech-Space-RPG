package game.gui;

import game.persistence.SaveFile;

public interface GUIAction
{
    void openGameScreen();
    void openSavesScreen();
    void selectSave(SaveFile saveFile);
    void closeSavesScreen();
    void openOptionsScreen();
    void changeResolution(DisplayResolution resolution);
    void closeOptionsScreen();
    void closeGame();
}