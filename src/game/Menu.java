package game;

import game.persistence.SaveFileHandler;

public interface Menu
{
    public MainMenuResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH);
}
