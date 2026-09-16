package game.gui;

public interface GUIAction
{
    public void openGameScreen();
    public void openSavesScreen();
    public void openOptionsScreen();
    public void closeGame();
    public void changeResolution(DisplayResolution resolution);
}