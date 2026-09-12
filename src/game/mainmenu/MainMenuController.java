package game.mainmenu;

import static game.mainmenu.MainMenuErrors.*;

import game.ConsoleHandler;
import game.Startable;

import java.util.Optional;

public class MainMenuController implements Startable<Void, Void, MainMenuResult>
{
    private final ConsoleHandler consoleH;
    
    public MainMenuController(ConsoleHandler cH)
    {
        consoleH = cH;
    }
    
    @Override
    public MainMenuResult start(Void input1, Void input2)
    {
        consoleH.printLine("Welcome to Tech Space.\nUse '~' whenever you wish to go back.");
        consoleH.printLine("Select an Option:");
        Optional<MainMenuStates> response = consoleH.confirmInputInteger("What would you like to do?", MainMenuStates.optionsToString(), MainMenuStates.class).getInput();
        if(response.isEmpty()) return new MainMenuResult(USER_WENT_BACK, MainMenuStates.EXIT_GAME);
        else return new MainMenuResult(SUCCESS, response.get());
    }
    
    public MainMenuResult start()
    {
        return start(null, null);
    }
}