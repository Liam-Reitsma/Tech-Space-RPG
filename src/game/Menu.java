package game;

import game.persistence.SaveFileHandler;

public interface Menu
{
    public UserResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH);
}
