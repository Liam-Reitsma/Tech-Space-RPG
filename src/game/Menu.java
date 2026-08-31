package game;

import game.saves.SaveFileHandler;

public interface Menu
{
    public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH);
}
