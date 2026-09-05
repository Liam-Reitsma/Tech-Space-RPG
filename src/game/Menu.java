package game;

import game.persistence.SaveFileHandler;

public interface Menu
{
    public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH, GameController gameC);
}
