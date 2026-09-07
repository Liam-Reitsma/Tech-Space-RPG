package game.mainmenu;

import game.ConsoleHandler;
import game.persistence.SaveFileHandler;

public interface Menu
{
    public MainMenuResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH);
}
