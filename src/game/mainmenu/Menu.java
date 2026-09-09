package game.mainmenu;

import game.ConsoleHandler;
import game.persistence.PersistenceController;

public interface Menu
{
    public MainMenuResult execute(ConsoleHandler consoleH, PersistenceController saveFH);
}
